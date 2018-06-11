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

import android.text.TextUtils;

//import com.arialyy.aria.core.AriaManager;
//import com.arialyy.aria.core.common.QueueMod;
//import com.arialyy.aria.core.download.DownloadGroupTaskEntity;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.IEntity;
//import com.arialyy.aria.core.queue.DownloadGroupTaskQueue;
//import com.arialyy.aria.core.queue.DownloadTaskQueue;
//import com.arialyy.aria.core.queue.UploadTaskQueue;
//import com.arialyy.aria.core.upload.UploadTaskEntity;
//import com.arialyy.aria.orm.DbEntity;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CommonUtil;
//import com.arialyy.aria.util.NetUtils;

import com.example.frank.jinding.Upload.core.AriaManager;
import com.example.frank.jinding.Upload.core.common.QueueMod;
import com.example.frank.jinding.Upload.core.download.DownloadGroupTaskEntity;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.IEntity;
import com.example.frank.jinding.Upload.core.queue.DownloadGroupTaskQueue;
import com.example.frank.jinding.Upload.core.queue.DownloadTaskQueue;
import com.example.frank.jinding.Upload.core.queue.UploadTaskQueue;
import com.example.frank.jinding.Upload.core.upload.UploadTaskEntity;
import com.example.frank.jinding.Upload.orm.DbEntity;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CommonUtil;
import com.example.frank.jinding.Upload.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyy on 2016/8/22.
 * 开始命令
 * 队列模型{@link QueueMod#NOW}、{@link QueueMod#WAIT}
 */
class StartCmd<T extends AbsTaskEntity> extends AbsNormalCmd<T> {

  StartCmd(String targetName, T entity, int taskType) {
    super(targetName, entity, taskType);
  }

  @Override
  public void executeCmd() {
    if (!canExeCmd) return;
    if (!NetUtils.isConnected(AriaManager.APP)) {
      ALog.e(TAG, "启动任务失败，网络未连接");
      return;
    }
    String mod;
    int maxTaskNum = mQueue.getMaxTaskNum();
    AriaManager manager = AriaManager.getInstance(AriaManager.APP);
    if (isDownloadCmd) {
      mod = manager.getDownloadConfig().getQueueMod();
    } else {
      mod = manager.getUploadConfig().getQueueMod();
    }

    AbsTask task = getTask();
    if (task == null) {
      task = createTask();
      if (!TextUtils.isEmpty(mTargetName)) {
        task.setTargetName(mTargetName);
      }
      // 任务不存在时，根据配置不同，对任务执行操作
      if (mod.equals(QueueMod.NOW.getTag())) {
        startTask();
      } else if (mod.equals(QueueMod.WAIT.getTag())) {
        if (mQueue.getCurrentExePoolNum() < maxTaskNum
            || task.getState() == IEntity.STATE_STOP
            || task.getState() == IEntity.STATE_FAIL
            || task.getState() == IEntity.STATE_OTHER
            || task.getState() == IEntity.STATE_POST_PRE
            || task.getState() == IEntity.STATE_COMPLETE) {
          //startTask();
          resumeTask();
        }
      }
    } else {
      if (!task.isRunning()) {
        //startTask();
        resumeTask();
      }
    }
    if (mQueue.getCurrentCachePoolNum() == 0) {
      findAllWaitTask();
    }
  }

  /**
   * 当缓冲队列为null时，查找数据库中所有等待中的任务
   */
  private void findAllWaitTask() {
    new Thread(new WaitTaskThread()).start();
  }

  private class WaitTaskThread implements Runnable {

    @Override
    public void run() {
      if (isDownloadCmd) {
        handleTask(findWaitData(1));
        handleTask(findWaitData(2));
      } else {
        handleTask(findWaitData(3));
      }
    }

    private List<AbsTaskEntity> findWaitData(int type) {
      List<AbsTaskEntity> waitList = new ArrayList<>();
      switch (type) {
        case 1:
          List<DownloadTaskEntity> dEntity =
              DbEntity.findDatas(DownloadTaskEntity.class, "groupName=? and state=?", "", "3");
          if (dEntity != null && !dEntity.isEmpty()) {
            waitList.addAll(dEntity);
          }
          break;
        case 2:
          List<DownloadGroupTaskEntity> dgEntity =
              DbEntity.findDatas(DownloadGroupTaskEntity.class, "state=?", "3");
          if (dgEntity != null && !dgEntity.isEmpty()) {
            waitList.addAll(dgEntity);
          }
          break;
        case 3:
          List<UploadTaskEntity> uEntity =
              DbEntity.findDatas(UploadTaskEntity.class, "state=?", "3");
          if (uEntity != null && !uEntity.isEmpty()) {
            waitList.addAll(uEntity);
          }
          break;
      }
      return waitList;
    }

    private void handleTask(List<AbsTaskEntity> waitList) {
      for (AbsTaskEntity te : waitList) {
        if (te.getEntity() == null) continue;
        if (te instanceof DownloadTaskEntity) {
          if (te.requestType == AbsTaskEntity.D_FTP || te.requestType == AbsTaskEntity.U_FTP) {
            te.urlEntity = CommonUtil.getFtpUrlInfo(te.getEntity().getKey());
          }
          mQueue = DownloadTaskQueue.getInstance();
        } else if (te instanceof UploadTaskEntity) {
          mQueue = UploadTaskQueue.getInstance();
        } else if (te instanceof DownloadGroupTaskEntity) {
          mQueue = DownloadGroupTaskQueue.getInstance();
        }
        createTask(te);
      }
    }
  }
}