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

//import com.arialyy.aria.core.manager.TEManager;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CheckUtil;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.manager.TEManager;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CheckUtil;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.util.List;

/**
 * Created by AriaL on 2017/6/29.
 * 下载任务组
 */
public class DownloadGroupTarget
    extends BaseGroupTarget<DownloadGroupTarget, DownloadGroupTaskEntity> {
  private final String TAG = "DownloadGroupTarget";

  DownloadGroupTarget(DownloadGroupEntity groupEntity, String targetName) {
    this.mTargetName = targetName;
    if (groupEntity.getUrls() != null && !groupEntity.getUrls().isEmpty()) {
      this.mUrls.addAll(groupEntity.getUrls());
    }
    mGroupName = CommonUtil.getMd5Code(groupEntity.getUrls());
    mTaskEntity = TEManager.getInstance()
        .getTEntity(DownloadGroupTaskEntity.class, mGroupName);
    if (mTaskEntity == null) {
      mTaskEntity =
          TEManager.getInstance().createTEntity(DownloadGroupTaskEntity.class, groupEntity);
    }
    mEntity = mTaskEntity.entity;
  }

  DownloadGroupTarget(List<String> urls, String targetName) {
    this.mTargetName = targetName;
    this.mUrls = urls;
    mGroupName = CommonUtil.getMd5Code(urls);
    mTaskEntity = TEManager.getInstance().getTEntity(DownloadGroupTaskEntity.class, mGroupName);
    if (mTaskEntity == null) {
      mTaskEntity = TEManager.getInstance().createGTEntity(DownloadGroupTaskEntity.class, mUrls);
    }
    mEntity = mTaskEntity.entity;
  }

  /**
   * 任务组总任务大小，任务组是一个抽象的概念，没有真实的数据实体，任务组的大小是Aria动态获取子任务大小相加而得到的，
   * 如果你知道当前任务组总大小，你也可以调用该方法给任务组设置大小
   *
   * 为了更好的用户体验，建议直接设置任务组文件大小
   *
   * @param fileSize 任务组总大小
   */
  public DownloadGroupTarget setFileSize(long fileSize) {
    if (fileSize <= 0) {
      ALog.e(TAG, "文件大小不能小于 0");
      return this;
    }
    if (mEntity.getFileSize() <= 1 || mEntity.getFileSize() != fileSize) {
      mEntity.setFileSize(fileSize);
      mEntity.update();
    }
    return this;
  }

  /**
   * 如果你是使用{@link DownloadReceiver#load(DownloadGroupEntity)}进行下载操作，那么你需要设置任务组的下载地址
   */
  public DownloadGroupTarget setGroupUrl(List<String> urls) {
    CheckUtil.checkDownloadUrls(urls);
    mUrls.clear();
    mUrls.addAll(urls);
    mEntity.setGroupName(CommonUtil.getMd5Code(urls));
    mEntity.update();
    return this;
  }
}
