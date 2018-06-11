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
package com.example.frank.jinding.Upload.core.manager;

//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Aria.Lao on 2017/9/1.
 * 任务管理器
 */
class TaskManager {
  private static final String TAG = "TaskManager";
  private static volatile TaskManager INSTANCE = null;
  private Map<String, AbsTask> map = new ConcurrentHashMap<>();

  public static TaskManager getInstance() {
    if (INSTANCE == null) {
      synchronized (TaskManager.class) {
        INSTANCE = new TaskManager();
      }
    }
    return INSTANCE;
  }

  private TaskManager() {

  }

  /**
   * 管理器添加任务
   *
   * @param key 任务的key，下载为保存路径，任务组为任务组名，上传为文件上传路径
   * @param task 任务
   * @return {@code true}添加成功
   */
  public <T extends AbsTask> boolean addTask(String key, Class<T> clazz, T task) {
    String hash = CommonUtil.keyToHashKey(key);
    if (map.keySet().contains(hash)) {
      ALog.e(TAG, "任务【" + key + "】已存在");
      return false;
    }
    map.put(CommonUtil.keyToHashKey(key), task);
    return true;
  }

  /**
   * 移除任务
   *
   * @param key 任务的key，下载为保存路径，任务组为任务组名，上传为文件上传路径
   */
  public void removeTask(String key) {
    String hash = CommonUtil.keyToHashKey(key);
    for (Iterator<Map.Entry<String, AbsTask>> iter = map.entrySet().iterator(); iter.hasNext(); ) {
      Map.Entry<String, AbsTask> entry = iter.next();
      if (entry.getKey().equals(hash)) iter.remove();
    }
  }

  /**
   * 通过key获取任务
   *
   * @return 如果找不到任务，返回null，否则返回key对应的任务
   */
  public AbsTask getTask(String key) {
    return map.get(CommonUtil.keyToHashKey(key));
  }
}
