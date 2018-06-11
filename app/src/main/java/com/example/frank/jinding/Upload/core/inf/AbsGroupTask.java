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

//import com.arialyy.aria.core.download.downloader.AbsGroupUtil;

import com.example.frank.jinding.Upload.core.download.downloader.AbsGroupUtil;

/**
 * Created by AriaL on 2017/6/29.
 * 任务组任务抽象类
 */
public abstract class AbsGroupTask<TASK_ENTITY extends AbsGroupTaskEntity>
    extends AbsTask<TASK_ENTITY> {

  protected AbsGroupUtil mUtil;

  @Override
  public String getKey() {
    return mTaskEntity.getEntity().getGroupName();
  }

  /**
   * 启动任务组中的子任务
   *
   * @param url 子任务下载地址
   */
  public void startSubTask(String url) {
    if (mUtil != null) {
      mUtil.startSubTask(url);
    }
  }

  /**
   * 停止任务组中的子任务
   *
   * @param url 子任务下载地址
   */
  public void stopSubTask(String url) {
    if (mUtil != null) {
      mUtil.stopSubTask(url);
    }
  }

  /**
   * 删除子任务组中的子任务
   *
   * @param url 子任务下载地址
   */
  public void cancelSubTask(String url) {
    if (mUtil != null) {
      mUtil.cancelSubTask(url);
    }
  }
}
