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

import android.os.Handler;
import android.os.Looper;

import com.example.frank.jinding.Upload.core.inf.AbsNormalTask;
import com.example.frank.jinding.Upload.core.scheduler.ISchedulers;
import com.example.frank.jinding.Upload.core.upload.uploader.SimpleUploadUtil;
import com.example.frank.jinding.Upload.util.ALog;

//import com.arialyy.aria.core.inf.AbsNormalTask;
//import com.arialyy.aria.core.scheduler.ISchedulers;
//import com.arialyy.aria.core.upload.uploader.SimpleUploadUtil;
//import com.arialyy.aria.util.ALog;

/**
 * Created by lyy on 2017/2/23.
 * 上传任务
 */
public class UploadTask extends AbsNormalTask<UploadTaskEntity> {
  private static final String TAG = "UploadTask";

  private SimpleUploadUtil mUtil;
  private BaseUListener<UploadEntity, UploadTaskEntity, UploadTask> mListener;

  private UploadTask(UploadTaskEntity taskEntity, Handler outHandler) {
    mTaskEntity = taskEntity;
    mOutHandler = outHandler;
    mListener = new BaseUListener<>(this, mOutHandler);
    mUtil = new SimpleUploadUtil(taskEntity, mListener);
  }

  @Override
  public String getKey() {
    return mTaskEntity.getEntity().getFilePath();
  }

  @Override
  public boolean isRunning() {
    return mUtil.isRunning();
  }

  public UploadEntity getEntity() {
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
    if (mUtil.isRunning()) {
      mUtil.stop();
    } else {
      mListener.onStop(getCurrentProgress());
    }
  }

  @Override
  public void cancel() {
    if (!mUtil.isRunning()) {
      mListener.onCancel();
    }
    mUtil.cancel();
  }

  public static class Builder {
    private Handler mOutHandler;
    private UploadTaskEntity mTaskEntity;
    private String mTargetName;

    public void setOutHandler(ISchedulers outHandler) {
      try {
        mOutHandler = new Handler(outHandler);
      } catch (Exception e) {
        e.printStackTrace();
        mOutHandler = new Handler(Looper.getMainLooper(), outHandler);
      }
    }

    public void setUploadTaskEntity(UploadTaskEntity taskEntity) {
      mTaskEntity = taskEntity;
    }

    public void setTargetName(String targetName) {
      mTargetName = targetName;
    }

    public Builder() {

    }

    public UploadTask build() {
      UploadTask task = new UploadTask(mTaskEntity, mOutHandler);
      task.setTargetName(mTargetName);
      return task;
    }
  }
}
