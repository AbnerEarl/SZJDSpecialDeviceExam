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
package com.example.frank.jinding.Upload.core.inf;

import android.support.annotation.NonNull;

import com.example.frank.jinding.Upload.core.queue.UploadTaskQueue;
import com.example.frank.jinding.Upload.core.upload.UploadEntity;
import com.example.frank.jinding.Upload.core.upload.UploadTask;
import com.example.frank.jinding.Upload.core.upload.UploadTaskEntity;
import com.example.frank.jinding.Upload.util.CheckUtil;

//import com.arialyy.aria.core.queue.UploadTaskQueue;
//import com.arialyy.aria.core.upload.UploadEntity;
//import com.arialyy.aria.core.upload.UploadTask;
//import com.arialyy.aria.core.upload.UploadTaskEntity;
//import com.arialyy.aria.util.CheckUtil;

/**
 * Created by AriaL on 2017/6/29.
 * 任务组超类
 */
public abstract class AbsUploadTarget<TARGET extends AbsUploadTarget, ENTITY extends UploadEntity, TASK_ENTITY extends UploadTaskEntity>
    extends AbsTarget<TARGET, ENTITY, TASK_ENTITY> {

  /**
   * 设置上传路径
   *
   * @param uploadUrl 上传路径
   */
  public TARGET setUploadUrl(@NonNull String uploadUrl) {
    CheckUtil.checkUrl(uploadUrl);
    if (mEntity.getUrl().equals(uploadUrl)) return (TARGET) this;
    mEntity.setUrl(uploadUrl);
    mEntity.update();
    return (TARGET) this;
  }

  /**
   * 下载任务是否存在
   */
  @Override
  public boolean taskExists() {
    return UploadTaskQueue.getInstance().getTask(mEntity.getFilePath()) != null;
  }

  /**
   * 是否在下载
   */
  public boolean isUploading() {
    UploadTask task = UploadTaskQueue.getInstance().getTask(mEntity.getKey());
    return task != null && task.isRunning();
  }
}
