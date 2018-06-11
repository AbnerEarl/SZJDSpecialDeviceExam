/*
 * Copyright (C) 2016 AriaLyy(https://github.com/AriaLyy/Aria)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.frank.jinding.Upload.core.command.normal;
//
//import com.arialyy.aria.core.command.AbsCmd;
//import com.arialyy.aria.core.command.ICmd;
//import com.arialyy.aria.core.download.DownloadGroupTaskEntity;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.inf.AbsEntity;
//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.manager.TEManager;
//import com.arialyy.aria.core.queue.DownloadGroupTaskQueue;
//import com.arialyy.aria.core.queue.DownloadTaskQueue;
//import com.arialyy.aria.core.queue.UploadTaskQueue;
//import com.arialyy.aria.core.upload.UploadTaskEntity;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.command.AbsCmd;
import com.example.frank.jinding.Upload.core.command.ICmd;
import com.example.frank.jinding.Upload.core.download.DownloadGroupTaskEntity;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.inf.AbsEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.manager.TEManager;
import com.example.frank.jinding.Upload.core.queue.DownloadGroupTaskQueue;
import com.example.frank.jinding.Upload.core.queue.DownloadTaskQueue;
import com.example.frank.jinding.Upload.core.queue.UploadTaskQueue;
import com.example.frank.jinding.Upload.core.upload.UploadTaskEntity;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CommonUtil;

/**
 * Created by lyy on 2016/8/22.
 * 下载命令
 */
public abstract class AbsNormalCmd<T extends AbsTaskEntity> extends AbsCmd<T> {
  /**
   * 能否执行命令
   */
  boolean canExeCmd = true;

  private AbsTask tempTask = null;
  int taskType;

  /**
   * @param targetName 产生任务的对象名
   * @param taskType 下载任务类型{@link ICmd#TASK_TYPE_DOWNLOAD}、{@link ICmd#TASK_TYPE_DOWNLOAD_GROUP}、{@link
   * ICmd#TASK_TYPE_UPLOAD}
   */
  AbsNormalCmd(String targetName, T entity, int taskType) {
    this.taskType = taskType;
    mTargetName = targetName;
    mTaskEntity = entity;
    TAG = CommonUtil.getClassName(this);
    if (taskType == ICmd.TASK_TYPE_DOWNLOAD) {
      if (!(entity instanceof DownloadTaskEntity)) {
        ALog.e(TAG, "任务类型错误，任务类型应该为ICM.TASK_TYPE_DOWNLOAD");
        return;
      }
      mQueue = DownloadTaskQueue.getInstance();
    } else if (taskType == ICmd.TASK_TYPE_DOWNLOAD_GROUP) {
      if (!(entity instanceof DownloadGroupTaskEntity)) {
        ALog.e(TAG, "任务类型错误，任务类型应该为ICM.TASK_TYPE_DOWNLOAD_GROUP");
        return;
      }
      mQueue = DownloadGroupTaskQueue.getInstance();
    } else if (taskType == ICmd.TASK_TYPE_UPLOAD) {
      if (!(entity instanceof UploadTaskEntity)) {
        ALog.e(TAG, "任务类型错误，任务类型应该为ICM.TASK_TYPE_UPLOAD");
        return;
      }
      mQueue = UploadTaskQueue.getInstance();
    } else {
      ALog.e(TAG, "任务类型错误，任务类型应该为ICM.TASK_TYPE_DOWNLOAD、TASK_TYPE_DOWNLOAD_GROUP、TASK_TYPE_UPLOAD");
      return;
    }
    isDownloadCmd = taskType < ICmd.TASK_TYPE_UPLOAD;
  }

  /**
   * 删除所有任务
   */
  void removeAll() {
    mQueue.removeAllTask();
  }

  /**
   * 停止所有任务
   */
  void stopAll() {
    mQueue.stopAllTask();
  }

  /**
   * 停止任务
   */
  void stopTask() {
    if (tempTask == null) createTask();
    mQueue.stopTask(tempTask);
  }

  /**
   * 删除任务
   */
  void removeTask() {
    if (tempTask == null) createTask();
    mQueue.cancelTask(tempTask);
  }

  /**
   * 启动任务
   */
  void startTask() {
    mQueue.startTask(tempTask);
  }

  /**
   * 恢复任务
   */
  void resumeTask() {
    mQueue.resumeTask(tempTask);
  }

  /**
   * 启动指定任务
   *
   * @param task 指定任务
   */
  void startTask(AbsTask task) {
    mQueue.startTask(task);
  }

  /**
   * 从队列中获取任务
   *
   * @return 执行任务
   */
  AbsTask getTask() {
    tempTask = mQueue.getTask(mTaskEntity.getEntity().getKey());
    return tempTask;
  }

  /**
   * 从队列中获取任务
   *
   * @return 执行任务
   */
  AbsTask getTask(AbsEntity entity) {
    tempTask = mQueue.getTask(entity.getKey());
    return tempTask;
  }

  /**
   * 创建任务
   *
   * @return 创建的任务
   */
  AbsTask createTask() {
    tempTask = mQueue.createTask(mTargetName, mTaskEntity);
    return tempTask;
  }

  /**
   * 创建指定实体的任务
   *
   * @param taskEntity 特定的任务实体
   * @return 创建的任务
   */
  AbsTask createTask(AbsTaskEntity taskEntity) {
    TEManager.getInstance().addTEntity(taskEntity);
    return mQueue.createTask(mTargetName, taskEntity);
  }
}