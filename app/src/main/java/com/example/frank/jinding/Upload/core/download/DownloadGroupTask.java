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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.frank.jinding.Upload.core.AriaManager;
import com.example.frank.jinding.Upload.core.download.downloader.DownloadGroupUtil;
import com.example.frank.jinding.Upload.core.download.downloader.FtpDirDownloadUtil;
import com.example.frank.jinding.Upload.core.inf.AbsGroupTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.scheduler.ISchedulers;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CheckUtil;

//import com.arialyy.aria.core.AriaManager;
//import com.arialyy.aria.core.download.downloader.DownloadGroupUtil;
//import com.arialyy.aria.core.download.downloader.FtpDirDownloadUtil;
//import com.arialyy.aria.core.inf.AbsGroupTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.scheduler.ISchedulers;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CheckUtil;

/**
 * Created by AriaL on 2017/6/27.
 * 任务组任务
 */
public class DownloadGroupTask extends AbsGroupTask<DownloadGroupTaskEntity> {
  private final String TAG = "DownloadGroupTask";
  private DownloadGroupListener mListener;

  private DownloadGroupTask(DownloadGroupTaskEntity taskEntity, Handler outHandler) {
    mTaskEntity = taskEntity;
    mOutHandler = outHandler;
    mContext = AriaManager.APP;
    mListener = new DownloadGroupListener(this, mOutHandler);
    switch (taskEntity.requestType) {
      case AbsTaskEntity.D_HTTP:
        mUtil = new DownloadGroupUtil(mListener, mTaskEntity);
        break;
      case AbsTaskEntity.D_FTP_DIR:
        mUtil = new FtpDirDownloadUtil(mListener, mTaskEntity);
        break;
    }
    Log.d(TAG, "FTP_TASK_MD5:" + mTaskEntity.hashCode());
  }

  @Override
  public boolean isRunning() {
    return mUtil.isRunning();
  }

  public DownloadGroupEntity getEntity() {
    return mTaskEntity.getEntity();
  }

  @Override
  public void start() {
    if (mUtil.isRunning()) {
      ALog.d(TAG, "任务正在下载");
    } else {
      mUtil.start();
    }
  }

  @Override
  public void stop() {
    if (!mUtil.isRunning()) {
      mListener.onStop(getCurrentProgress());
    }
    mUtil.stop();
  }

  @Override
  public void cancel() {
    if (!mUtil.isRunning()) {
      mListener.onCancel();
    }
    mUtil.cancel();
  }

  public static class Builder {
    DownloadGroupTaskEntity taskEntity;
    Handler outHandler;
    String targetName;

    public Builder(String targetName, DownloadGroupTaskEntity taskEntity) {
      CheckUtil.checkTaskEntity(taskEntity);
      this.targetName = targetName;
      this.taskEntity = taskEntity;
    }

    /**
     * 设置自定义Handler处理下载状态时间
     *
     * @param schedulers {@link ISchedulers}
     */
    public DownloadGroupTask.Builder setOutHandler(ISchedulers schedulers) {
      try {
        outHandler = new Handler(schedulers);
      } catch (Exception e) {
        e.printStackTrace();
        outHandler = new Handler(Looper.getMainLooper(), schedulers);
      }
      return this;
    }

    public DownloadGroupTask build() {
      DownloadGroupTask task = new DownloadGroupTask(taskEntity, outHandler);
      task.setTargetName(targetName);
      taskEntity.save();
      return task;
    }
  }
}
