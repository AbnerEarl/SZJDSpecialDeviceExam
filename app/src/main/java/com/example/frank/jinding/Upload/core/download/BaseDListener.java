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

//import com.arialyy.aria.core.AriaManager;
//import com.arialyy.aria.core.inf.AbsEntity;
//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.IDownloadListener;
//import com.arialyy.aria.core.inf.IEntity;
//import com.arialyy.aria.core.scheduler.ISchedulers;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.AriaManager;
import com.example.frank.jinding.Upload.core.inf.AbsEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.IDownloadListener;
import com.example.frank.jinding.Upload.core.inf.IEntity;
import com.example.frank.jinding.Upload.core.scheduler.ISchedulers;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.lang.ref.WeakReference;

/**
 * 下载监听类
 */
class BaseDListener<ENTITY extends AbsEntity, TASK_ENTITY extends AbsTaskEntity<ENTITY>, TASK extends AbsTask<TASK_ENTITY>>
    implements IDownloadListener {
  protected WeakReference<Handler> outHandler;
  private int RUN_SAVE_INTERVAL = 5 * 1000;  //5s保存一次下载中的进度
  private long mLastLen = 0;   //上一次发送长度
  private boolean isFirst = true;
  protected ENTITY mEntity;
  protected TASK_ENTITY mTaskEntity;
  protected TASK mTask;
  private boolean isConvertSpeed = false;
  boolean isWait = false;
  private long mLastSaveTime;

  BaseDListener(TASK task, Handler outHandler) {
    this.outHandler = new WeakReference<>(outHandler);
    this.mTask = new WeakReference<>(task).get();
    this.mEntity = mTask.getTaskEntity().getEntity();
    this.mTaskEntity = mTask.getTaskEntity();
    final AriaManager manager = AriaManager.getInstance(AriaManager.APP);
    isConvertSpeed = manager.getDownloadConfig().isConvertSpeed();
    mLastLen = mEntity.getCurrentProgress();
    mLastSaveTime = System.currentTimeMillis();
  }

  @Override
  public void onPre() {
    saveData(IEntity.STATE_PRE, -1);
    sendInState2Target(ISchedulers.PRE);
  }

  @Override
  public void onPostPre(long fileSize) {
    mEntity.setFileSize(fileSize);
    mEntity.setConvertFileSize(CommonUtil.formatFileSize(fileSize));
    saveData(IEntity.STATE_POST_PRE, -1);
    sendInState2Target(ISchedulers.POST_PRE);
  }

  @Override
  public void supportBreakpoint(boolean support) {

  }

  @Override
  public void onStart(long startLocation) {
    saveData(IEntity.STATE_RUNNING, startLocation);
    sendInState2Target(ISchedulers.START);
  }

  @Override
  public void onResume(long resumeLocation) {
    saveData(IEntity.STATE_RUNNING, resumeLocation);
    sendInState2Target(ISchedulers.RESUME);
  }

  @Override
  public void onProgress(long currentLocation) {
    mEntity.setCurrentProgress(currentLocation);
    long speed = currentLocation - mLastLen;
    if (isFirst) {
      speed = 0;
      isFirst = false;
    }
    handleSpeed(speed);
    sendInState2Target(ISchedulers.RUNNING);
    if (System.currentTimeMillis() - mLastSaveTime >= RUN_SAVE_INTERVAL) {
      saveData(IEntity.STATE_RUNNING, currentLocation);
      mLastSaveTime = System.currentTimeMillis();
    }

    mLastLen = currentLocation;
  }

  @Override
  public void onStop(long stopLocation) {
    saveData(isWait ? IEntity.STATE_WAIT : IEntity.STATE_STOP, stopLocation);
    handleSpeed(0);
    sendInState2Target(ISchedulers.STOP);
  }

  @Override
  public void onCancel() {
    saveData(IEntity.STATE_CANCEL, -1);
    handleSpeed(0);
    sendInState2Target(ISchedulers.CANCEL);
  }

  @Override
  public void onComplete() {
    saveData(IEntity.STATE_COMPLETE, mEntity.getFileSize());
    handleSpeed(0);
    sendInState2Target(ISchedulers.COMPLETE);
  }

  @Override
  public void onFail(boolean needRetry) {
    mEntity.setFailNum(mEntity.getFailNum() + 1);
    saveData(IEntity.STATE_FAIL, mEntity.getCurrentProgress());
    handleSpeed(0);
    mTask.needRetry = needRetry;
    sendInState2Target(ISchedulers.FAIL);
  }

  private void handleSpeed(long speed) {
    if (isConvertSpeed) {
      mEntity.setConvertSpeed(CommonUtil.formatFileSize(speed < 0 ? 0 : speed) + "/s");
    } else {
      mEntity.setSpeed(speed < 0 ? 0 : speed);
    }

    mEntity.setPercent((int) (mEntity.getFileSize() <= 0 ? 0
        : mEntity.getCurrentProgress() * 100 / mEntity.getFileSize()));
  }

  /**
   * 将任务状态发送给下载器
   *
   * @param state {@link ISchedulers#START}
   */
  private void sendInState2Target(int state) {
    if (outHandler.get() != null) {
      outHandler.get().obtainMessage(state, mTask).sendToTarget();
    }
  }

  private void saveData(int state, long location) {
    mTaskEntity.state = state;
    mEntity.setState(state);
    mEntity.setComplete(state == IEntity.STATE_COMPLETE);
    if (state == IEntity.STATE_CANCEL) {
      mTaskEntity.deleteData();
      return;
    } else if (mEntity.isComplete()) {
      mEntity.setCompleteTime(System.currentTimeMillis());
      mEntity.setCurrentProgress(mEntity.getFileSize());
    } else if (location > 0) {
      mEntity.setCurrentProgress(location);
    }
    mTaskEntity.update();
  }
}