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
package com.example.frank.jinding.Upload.core.download.downloader;

//import com.arialyy.aria.core.common.AbsFtpInfoThread;
//import com.arialyy.aria.core.common.OnFileInfoCallback;
//import com.arialyy.aria.core.download.DownloadEntity;
//import com.arialyy.aria.core.download.DownloadTaskEntity;

import com.example.frank.jinding.Upload.core.common.AbsFtpInfoThread;
import com.example.frank.jinding.Upload.core.common.OnFileInfoCallback;
import com.example.frank.jinding.Upload.core.download.DownloadEntity;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;

/**
 * Created by Aria.Lao on 2017/7/25.
 * 获取ftp文件信息
 */
class FtpFileInfoThread extends AbsFtpInfoThread<DownloadEntity, DownloadTaskEntity> {

  FtpFileInfoThread(DownloadTaskEntity taskEntity, OnFileInfoCallback callback) {
    super(taskEntity, callback);
  }

  @Override
  protected String setRemotePath() {
    return mTaskEntity.urlEntity.remotePath;
  }

  @Override
  protected void onPreComplete(int code) {
    super.onPreComplete(code);
    if (mSize != mTaskEntity.getEntity().getFileSize()) {
      mTaskEntity.isNewTask = true;
    }
    mEntity.setFileSize(mSize);
    mCallback.onComplete(mEntity.getUrl(), code);
  }
}
