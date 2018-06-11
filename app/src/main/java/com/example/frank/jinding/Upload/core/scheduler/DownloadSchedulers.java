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
//import com.arialyy.aria.core.download.DownloadTask;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.queue.DownloadTaskQueue;

import com.example.frank.jinding.Upload.core.AriaManager;
import com.example.frank.jinding.Upload.core.download.DownloadTask;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.queue.DownloadTaskQueue;

/**
 * Created by lyy on 2016/8/16.
 * 任务下载器，提供抽象的方法供具体的实现类操作
 */
public class DownloadSchedulers
    extends AbsSchedulers<DownloadTaskEntity, DownloadTask, DownloadTaskQueue> {

  private final String TAG = "DownloadSchedulers";
  private static volatile DownloadSchedulers INSTANCE = null;

  private DownloadSchedulers() {
    mQueue = DownloadTaskQueue.getInstance();
  }

  public static DownloadSchedulers getInstance() {
    if (INSTANCE == null) {
      synchronized (AriaManager.LOCK) {
        INSTANCE = new DownloadSchedulers();
      }
    }
    return INSTANCE;
  }

  @Override
  String getProxySuffix() {
    return "$$DownloadListenerProxy";
  }
}
