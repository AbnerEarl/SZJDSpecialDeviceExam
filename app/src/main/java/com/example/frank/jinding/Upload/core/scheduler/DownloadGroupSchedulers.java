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

//import com.arialyy.aria.core.AriaManager;
//import com.arialyy.aria.core.download.DownloadGroupTask;
//import com.arialyy.aria.core.download.DownloadGroupTaskEntity;
//import com.arialyy.aria.core.queue.DownloadGroupTaskQueue;

import com.example.frank.jinding.Upload.core.AriaManager;
import com.example.frank.jinding.Upload.core.download.DownloadGroupTask;
import com.example.frank.jinding.Upload.core.download.DownloadGroupTaskEntity;
import com.example.frank.jinding.Upload.core.queue.DownloadGroupTaskQueue;

/**
 * Created by AriaL on 2017/7/2.
 * 任务组调度器
 */
public class DownloadGroupSchedulers extends
    AbsSchedulers<DownloadGroupTaskEntity, DownloadGroupTask, DownloadGroupTaskQueue> {
  private final String TAG = "DownloadGroupSchedulers";
  private static volatile DownloadGroupSchedulers INSTANCE = null;

  private DownloadGroupSchedulers() {
    mQueue = DownloadGroupTaskQueue.getInstance();
  }

  public static DownloadGroupSchedulers getInstance() {
    if (INSTANCE == null) {
      synchronized (AriaManager.LOCK) {
        INSTANCE = new DownloadGroupSchedulers();
      }
    }
    return INSTANCE;
  }

  @Override
  String getProxySuffix() {
    return "$$DownloadGroupListenerProxy";
  }
}
