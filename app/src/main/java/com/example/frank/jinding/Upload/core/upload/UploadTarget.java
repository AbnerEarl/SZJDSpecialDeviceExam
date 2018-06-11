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
package com.example.frank.jinding.Upload.core.upload;

import android.support.annotation.NonNull;

//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.AbsUploadTarget;
//import com.arialyy.aria.core.manager.TEManager;

import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.AbsUploadTarget;
import com.example.frank.jinding.Upload.core.manager.TEManager;

import java.io.File;

/**
 * Created by lyy on 2017/2/28.
 * http 当文件上传
 */
public class UploadTarget extends AbsUploadTarget<UploadTarget, UploadEntity, UploadTaskEntity> {

  UploadTarget(String filePath, String targetName) {
    this.mTargetName = targetName;
    initTask(filePath);
  }

  private void initTask(String filePath) {
    mTaskEntity = TEManager.getInstance().getTEntity(UploadTaskEntity.class, filePath);
    if (mTaskEntity == null) {
      mTaskEntity = TEManager.getInstance().createTEntity(UploadTaskEntity.class, filePath);
    }
    mEntity = mTaskEntity.entity;
    File file = new File(filePath);
    mEntity.setFileName(file.getName());
    mEntity.setFileSize(file.length());
    //http暂时不支持断点上传
    mTaskEntity.isSupportBP = false;
    mTaskEntity.requestType = AbsTaskEntity.U_HTTP;
  }

  /**
   * 设置userAgent
   */
  public UploadTarget setUserAngent(@NonNull String userAgent) {
    mTaskEntity.userAgent = userAgent;
    return this;
  }

  /**
   * 设置服务器需要的附件key
   *
   * @param attachment 附件key
   */
  public UploadTarget setAttachment(@NonNull String attachment) {
    mTaskEntity.attachment = attachment;
    return this;
  }

  /**
   * 设置上传文件类型
   *
   * @param contentType tip：multipart/form-data
   */
  public UploadTarget setContentType(String contentType) {
    mTaskEntity.contentType = contentType;
    return this;
  }
}
