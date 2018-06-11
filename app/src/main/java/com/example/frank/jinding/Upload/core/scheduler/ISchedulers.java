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

import android.os.Handler;

import com.example.frank.jinding.Upload.core.inf.AbsTask;

//import com.arialyy.aria.core.inf.AbsTask;

/**
 * Created by lyy on 2016/11/2.
 * 调度器功能接口
 */
public interface ISchedulers<Task extends AbsTask> extends Handler.Callback {
  /**
   * 为任务组任务
   */
  int IS_SUB_TASK = 0xd1;

  /**
   * 断点支持
   */
  int SUPPORT_BREAK_POINT = 9;
  /**
   * 任务预加载
   */
  int PRE = 0;
  /**
   * 任务预加载完成
   */
  int POST_PRE = 1;

  /**
   * 任务开始
   */
  int START = 2;
  /**
   * 任务停止
   */
  int STOP = 3;
  /**
   * 任务失败
   */
  int FAIL = 4;
  /**
   * 任务取消
   */
  int CANCEL = 5;
  /**
   * 任务完成
   */
  int COMPLETE = 6;
  /**
   * 任务处理中
   */
  int RUNNING = 7;
  /**
   * 恢复任务
   */
  int RESUME = 8;

  /**
   * 任务组子任务预处理
   */
  int SUB_PRE = 0xa1;

  /**
   * 任务组子任务开始
   */
  int SUB_START = 0xa2;

  /**
   * 任务组子任务停止
   */
  int SUB_STOP = 0xa3;

  /**
   * 任务组子任务取消
   */
  int SUB_CANCEL = 0xa4;

  /**
   * 任务组子任务失败
   */
  int SUB_FAIL = 0xa5;

  /**
   * 任务组子任务执行执行中
   */
  int SUB_RUNNING = 0xa6;

  /**
   * 任务组子任务完成
   */
  int SUB_COMPLETE = 0xa7;

  /**
   * 将当前类注册到Aria
   *
   * @param obj 观察者类
   */
  void register(Object obj);

  /**
   * 移除注册
   *
   * @param obj 观察者类
   */
  void unRegister(Object obj);
}