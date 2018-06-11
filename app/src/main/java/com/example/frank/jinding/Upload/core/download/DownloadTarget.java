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
package com.example.frank.jinding.Upload.core.download;

import android.support.annotation.NonNull;
import android.text.TextUtils;

//import com.arialyy.aria.core.inf.AbsDownloadTarget;
//import com.arialyy.aria.core.manager.TEManager;
//import com.arialyy.aria.core.queue.DownloadTaskQueue;
//import com.arialyy.aria.orm.DbEntity;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.inf.AbsDownloadTarget;
import com.example.frank.jinding.Upload.core.manager.TEManager;
import com.example.frank.jinding.Upload.core.queue.DownloadTaskQueue;
import com.example.frank.jinding.Upload.orm.DbEntity;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.io.File;

/**
 * Created by lyy on 2016/12/5.
 * https://github.com/AriaLyy/Aria
 */
public class DownloadTarget
    extends AbsDownloadTarget<DownloadTarget, DownloadEntity, DownloadTaskEntity> {
  protected String url;

  DownloadTarget(DownloadEntity entity, String targetName) {
    this(entity, targetName, false);
  }

  DownloadTarget(DownloadEntity entity, String targetName, boolean refreshInfo) {
    this.url = entity.getUrl();
    mTargetName = targetName;
    mTaskEntity = TEManager.getInstance().getTEntity(DownloadTaskEntity.class, url);
    if (mTaskEntity == null) {
      mTaskEntity = TEManager.getInstance().createTEntity(DownloadTaskEntity.class, entity);
    }
    mEntity = mTaskEntity.entity;
    mTaskEntity.refreshInfo = refreshInfo;
  }

  DownloadTarget(String url, String targetName) {
    this(url, targetName, false);
  }

  DownloadTarget(String url, String targetName, boolean refreshInfo) {
    this.url = url;
    mTargetName = targetName;
    mTaskEntity = TEManager.getInstance().getTEntity(DownloadTaskEntity.class, url);
    if (mTaskEntity == null) {
      mTaskEntity = TEManager.getInstance().createTEntity(DownloadTaskEntity.class, url);
    }
    mEntity = mTaskEntity.entity;
    mTaskEntity.refreshInfo = refreshInfo;
  }

  /**
   * 将任务设置为最高优先级任务，最高优先级任务有以下特点：
   * 1、在下载队列中，有且只有一个最高优先级任务
   * 2、最高优先级任务会一直存在，直到用户手动暂停或任务完成
   * 3、任务调度器不会暂停最高优先级任务
   * 4、用户手动暂停或任务完成后，第二次重新执行该任务，该命令将失效
   * 5、如果下载队列中已经满了，则会停止队尾的任务，当高优先级任务完成后，该队尾任务将自动执行
   * 6、把任务设置为最高优先级任务后，将自动执行任务，不需要重新调用start()启动任务
   */
  @Override
  public void setHighestPriority() {
    super.setHighestPriority();
  }

  /**
   * 下载任务是否存在
   *
   * @return {@code true}任务存在
   */
  @Override
  public boolean taskExists() {
    return DownloadTaskQueue.getInstance().getTask(mEntity.getUrl()) != null;
  }

  /**
   * 设置文件存储路径，如果需要修改新的文件名，修改路径便可。
   * 如：原文件路径 /mnt/sdcard/test.zip
   * 如果需要将test.zip改为game.zip，只需要重新设置文件路径为：/mnt/sdcard/game.zip
   *
   * @param downloadPath 路径必须为文件路径，不能为文件夹路径
   */
  public DownloadTarget setDownloadPath(@NonNull String downloadPath) {
    if (TextUtils.isEmpty(downloadPath)) {
      throw new IllegalArgumentException("文件保持路径不能为null");
    }
    File file = new File(downloadPath);
    if (file.isDirectory()) {
      throw new IllegalArgumentException("保存路径不能为文件夹，路径需要是完整的文件路径，如：/mnt/sdcard/game.zip");
    }
    if (!downloadPath.equals(mEntity.getDownloadPath())) {
      if (!mTaskEntity.refreshInfo && DbEntity.checkDataExist(DownloadEntity.class,
          "downloadPath=?", downloadPath)) {
        throw new IllegalArgumentException("保存路径【" + downloadPath + "】已经被其它任务占用，请设置其它保存路径");
      }
      File oldFile = new File(mEntity.getDownloadPath());
      File newFile = new File(downloadPath);
      if (TextUtils.isEmpty(mEntity.getDownloadPath()) || oldFile.renameTo(newFile)) {
        mEntity.setDownloadPath(downloadPath);
        mEntity.setFileName(newFile.getName());
        mTaskEntity.key = downloadPath;
        mTaskEntity.update();
        CommonUtil.renameDownloadConfig(oldFile.getName(), newFile.getName());
      }
    }
    return this;
  }

  public DownloadEntity getDownloadEntity() {
    return mEntity;
  }

  /**
   * 从header中获取文件描述信息
   */
  public String getContentDisposition() {
    return mEntity.getDisposition();
  }

  /**
   * 是否在下载
   */
  public boolean isDownloading() {
    DownloadTask task = DownloadTaskQueue.getInstance().getTask(mEntity.getKey());
    return task != null && task.isRunning();
  }
}
