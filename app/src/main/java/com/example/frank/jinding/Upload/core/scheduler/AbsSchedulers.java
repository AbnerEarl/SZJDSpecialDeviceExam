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
package com.example.frank.jinding.Upload.core.scheduler;

import android.os.CountDownTimer;
import android.os.Message;

//import com.arialyy.aria.core.AriaManager;
//import com.arialyy.aria.core.download.DownloadTask;
//import com.arialyy.aria.core.inf.AbsEntity;
//import com.arialyy.aria.core.inf.AbsNormalEntity;
//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.GroupSendParams;
//import com.arialyy.aria.core.inf.IEntity;
//import com.arialyy.aria.core.manager.TEManager;
//import com.arialyy.aria.core.queue.ITaskQueue;
//import com.arialyy.aria.core.upload.UploadTask;
//import com.arialyy.aria.util.ALog;

import com.example.frank.jinding.Upload.core.AriaManager;
import com.example.frank.jinding.Upload.core.download.DownloadTask;
import com.example.frank.jinding.Upload.core.inf.AbsEntity;
import com.example.frank.jinding.Upload.core.inf.AbsNormalEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.GroupSendParams;
import com.example.frank.jinding.Upload.core.inf.IEntity;
import com.example.frank.jinding.Upload.core.manager.TEManager;
import com.example.frank.jinding.Upload.core.queue.ITaskQueue;
import com.example.frank.jinding.Upload.core.upload.UploadTask;
import com.example.frank.jinding.Upload.util.ALog;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lyy on 2017/6/4.
 * 事件调度器，用于处理任务状态的调度
 */
abstract class AbsSchedulers<TASK_ENTITY extends AbsTaskEntity, TASK extends AbsTask<TASK_ENTITY>, QUEUE extends ITaskQueue<TASK, TASK_ENTITY>>
    implements ISchedulers<TASK> {
  private final String TAG = "AbsSchedulers";

  protected QUEUE mQueue;

  private Map<String, AbsSchedulerListener<TASK, AbsNormalEntity>> mObservers =
      new ConcurrentHashMap<>();

  /**
   * 设置代理类后缀名
   */
  abstract String getProxySuffix();

  @Override
  public void register(Object obj) {
    String targetName = obj.getClass().getName();
    AbsSchedulerListener<TASK, AbsNormalEntity> listener = mObservers.get(targetName);
    if (listener == null) {
      listener = createListener(targetName);
      if (listener != null) {
        listener.setListener(obj);
        mObservers.put(targetName, listener);
      } else {
        ALog.e(TAG, "注册错误，没有【" + targetName + "】观察者");
      }
    }
  }

  @Override
  public void unRegister(Object obj) {
    if (!mObservers.containsKey(obj.getClass().getName())) {
      return;
    }
    for (Iterator<Map.Entry<String, AbsSchedulerListener<TASK, AbsNormalEntity>>> iter =
         mObservers.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry<String, AbsSchedulerListener<TASK, AbsNormalEntity>> entry = iter.next();
      if (entry.getKey().equals(obj.getClass().getName())) {
        iter.remove();
      }
    }
  }

  /**
   * 创建代理类
   *
   * @param targetName 通过观察者创建对应的Aria事件代理
   */
  private AbsSchedulerListener<TASK, AbsNormalEntity> createListener(String targetName) {
    AbsSchedulerListener<TASK, AbsNormalEntity> listener = null;
    try {
      Class clazz = Class.forName(targetName + getProxySuffix());
      listener = (AbsSchedulerListener<TASK, AbsNormalEntity>) clazz.newInstance();
    } catch (ClassNotFoundException e) {
      ALog.e(TAG, targetName + "，没有Aria的Download或Upload注解方法");
    } catch (InstantiationException e) {
      ALog.e(TAG, e.getMessage());
    } catch (IllegalAccessException e) {
      ALog.e(TAG, e.getMessage());
    }
    return listener;
  }

  @Override
  public boolean handleMessage(Message msg) {
    if (msg.arg1 == IS_SUB_TASK) {
      return handleSubEvent(msg);
    }

    TASK task = (TASK) msg.obj;
    if (task == null) {
      ALog.e(TAG, "请传入下载任务");
      return true;
    }
    handleNormalEvent(task, msg.what);
    return true;
  }

  /**
   * 处理任务组子任务事件
   */
  private boolean handleSubEvent(Message msg) {
    GroupSendParams params = (GroupSendParams) msg.obj;
    if (mObservers.size() > 0) {
      Set<String> keys = mObservers.keySet();
      for (String key : keys) {
        AbsSchedulerListener<TASK, AbsNormalEntity> listener = mObservers.get(key);
        switch (msg.what) {
          case SUB_PRE:
            listener.onSubTaskPre((TASK) params.groupTask, params.entity);
            break;
          case SUB_START:
            listener.onSubTaskStart((TASK) params.groupTask, params.entity);
            break;
          case SUB_STOP:
            listener.onSubTaskStop((TASK) params.groupTask, params.entity);
            break;
          case SUB_FAIL:
            listener.onSubTaskFail((TASK) params.groupTask, params.entity);
            break;
          case SUB_RUNNING:
            listener.onSubTaskRunning((TASK) params.groupTask, params.entity);
            break;
          case SUB_CANCEL:
            listener.onSubTaskCancel((TASK) params.groupTask, params.entity);
            break;
          case SUB_COMPLETE:
            listener.onSubTaskComplete((TASK) params.groupTask, params.entity);
            break;
        }
      }
    }
    return true;
  }

  /**
   * 处理普通任务事件
   */
  private void handleNormalEvent(TASK task, int what) {
    switch (what) {
      case STOP:
        if (task.getState() == IEntity.STATE_WAIT) {
          break;
        }
      case CANCEL:
        mQueue.removeTaskFormQueue(task.getKey());
        if (mQueue.getCurrentExePoolNum() < mQueue.getMaxTaskNum()) {
          startNextTask();
        }
        break;
      case COMPLETE:
        mQueue.removeTaskFormQueue(task.getKey());
        startNextTask();
        break;
      case FAIL:
        handleFailTask(task);
        break;
    }
    if (what == CANCEL || what == COMPLETE) {
      TEManager.getInstance().removeTEntity(task.getKey());
    }
    callback(what, task);
  }

  /**
   * 回调
   *
   * @param state 状态
   */
  private void callback(int state, TASK task) {
    if (mObservers.size() > 0) {
      Set<String> keys = mObservers.keySet();
      for (String key : keys) {
        callback(state, task, mObservers.get(key));
      }
    }
  }

  private void callback(int state, TASK task,
      AbsSchedulerListener<TASK, AbsNormalEntity> listener) {
    if (listener != null) {
      if (task == null) {
        ALog.e(TAG, "TASK 为null，回调失败");
        return;
      }
      switch (state) {
        case PRE:
          listener.onPre(task);
          break;
        case POST_PRE:
          listener.onTaskPre(task);
          break;
        case RUNNING:
          listener.onTaskRunning(task);
          break;
        case START:
          listener.onTaskStart(task);
          break;
        case STOP:
          listener.onTaskStop(task);
          break;
        case RESUME:
          listener.onTaskResume(task);
          break;
        case CANCEL:
          listener.onTaskCancel(task);
          break;
        case COMPLETE:
          listener.onTaskComplete(task);
          break;
        case FAIL:
          listener.onTaskFail(task);
          break;
        case SUPPORT_BREAK_POINT:
          listener.onNoSupportBreakPoint(task);
          break;
      }
    }
  }

  /**
   * 处理下载任务下载失败的情形
   *
   * @param task 下载任务
   */
  private void handleFailTask(final TASK task) {
    if (!task.needRetry) {
      mQueue.removeTaskFormQueue(task.getKey());
      startNextTask();
      return;
    }
    long interval = 2000;
    int num = 10;
    if (task instanceof DownloadTask) {
      interval = AriaManager.getInstance(AriaManager.APP).getDownloadConfig().getReTryInterval();
      num = AriaManager.getInstance(AriaManager.APP).getDownloadConfig().getReTryNum();
    } else if (task instanceof UploadTask) {
      interval = AriaManager.getInstance(AriaManager.APP).getUploadConfig().getReTryInterval();
      num = AriaManager.getInstance(AriaManager.APP).getUploadConfig().getReTryNum();
    }

    final int reTryNum = num;
    CountDownTimer timer = new CountDownTimer(interval, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {

      }

      @Override
      public void onFinish() {
        AbsEntity entity = task.getTaskEntity().getEntity();
        if (entity.getFailNum() < reTryNum) {
          TASK task = mQueue.getTask(entity.getKey());
          mQueue.reTryStart(task);
        } else {
          mQueue.removeTaskFormQueue(task.getKey());
          startNextTask();
          TEManager.getInstance().removeTEntity(task.getKey());
        }
      }
    };
    timer.start();
  }

  /**
   * 启动下一个任务，条件：任务停止，取消下载，任务完成
   */
  private void startNextTask() {
    TASK newTask = mQueue.getNextTask();
    if (newTask == null) {
      ALog.i(TAG, "没有下一任务");
      return;
    }
    if (newTask.getState() == IEntity.STATE_WAIT) {
      mQueue.startTask(newTask);
    }
  }
}
