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

//import com.arialyy.aria.core.common.IUtil;
//import com.arialyy.aria.core.common.OnFileInfoCallback;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.IDownloadListener;
//import com.arialyy.aria.util.ErrorHelp;

import com.example.frank.jinding.Upload.core.common.IUtil;
import com.example.frank.jinding.Upload.core.common.OnFileInfoCallback;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.IDownloadListener;
import com.example.frank.jinding.Upload.util.ErrorHelp;

/**
 * Created by lyy on 2015/8/25.
 * D_HTTP\FTP单任务下载工具
 */
public class SimpleDownloadUtil implements IUtil, Runnable {
  private static final String TAG = "SimpleDownloadUtil";
  private IDownloadListener mListener;
  private Downloader mDownloader;
  private DownloadTaskEntity mTaskEntity;

  public SimpleDownloadUtil(DownloadTaskEntity entity, IDownloadListener downloadListener) {
    mTaskEntity = entity;
    mListener = downloadListener;
    mDownloader = new Downloader(downloadListener, entity);
  }

  @Override
  public long getFileSize() {
    return mDownloader.getFileSize();
  }

  /**
   * 获取当前下载位置
   */
  @Override
  public long getCurrentLocation() {
    return mDownloader.getCurrentLocation();
  }

  @Override
  public boolean isRunning() {
    return mDownloader.isRunning();
  }

  /**
   * 取消下载
   */
  @Override
  public void cancel() {
    mDownloader.cancel();
  }

  /**
   * 停止下载
   */
  @Override
  public void stop() {
    mDownloader.stop();
  }

  /**
   * 多线程断点续传下载文件，开始下载
   */
  @Override
  public void start() {
    new Thread(this).start();
  }

  @Override
  public void resume() {
    start();
  }

  public void setMaxSpeed(double maxSpeed) {
    mDownloader.setMaxSpeed(maxSpeed);
  }

  private void failDownload(String msg, boolean needRetry) {
    mListener.onFail(needRetry);
    ErrorHelp.saveError("HTTP_DOWNLOAD", mTaskEntity.getEntity(), msg, "");
  }

  @Override
  public void run() {
    mListener.onPre();
    if (mTaskEntity.getEntity().getFileSize() <= 1
        || mTaskEntity.refreshInfo
        || mTaskEntity.requestType == AbsTaskEntity.D_FTP) {
      new Thread(createInfoThread()).start();
    } else {
      mDownloader.start();
    }
  }

  /**
   * 通过链接类型创建不同的获取文件信息的线程
   */
  private Runnable createInfoThread() {
    switch (mTaskEntity.requestType) {
      case AbsTaskEntity.D_FTP:
        return new FtpFileInfoThread(mTaskEntity, new OnFileInfoCallback() {
          @Override
          public void onComplete(String url, int code) {
            mDownloader.start();
          }

          @Override
          public void onFail(String url, String errorMsg, boolean needRetry) {
            failDownload(errorMsg, needRetry);
          }
        });
      case AbsTaskEntity.D_HTTP:
        return new HttpFileInfoThread(mTaskEntity, new OnFileInfoCallback() {
          @Override
          public void onComplete(String url, int code) {
            mDownloader.start();
          }

          @Override
          public void onFail(String url, String errorMsg, boolean needRetry) {
            failDownload(errorMsg, needRetry);
          }
        });
    }
    return null;
  }
}