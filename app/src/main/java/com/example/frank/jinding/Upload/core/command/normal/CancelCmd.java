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

package com.example.frank.jinding.Upload.core.command.normal;

import android.text.TextUtils;

import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
//
//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;

/**
 * Created by lyy on 2016/9/20.
 * 取消命令
 */
public class CancelCmd<T extends AbsTaskEntity> extends AbsNormalCmd<T> {
  /**
   * removeFile {@code true} 删除已经下载完成的任务，不仅删除下载记录，还会删除已经下载完成的文件，{@code false}
   * 如果文件已经下载完成，只删除下载记录
   */
  public boolean removeFile = false;

  CancelCmd(String targetName, T entity, int taskType) {
    super(targetName, entity, taskType);
  }

  @Override
  public void executeCmd() {
    if (!canExeCmd) return;
    AbsTask task = getTask();
    if (task == null) {
      task = createTask();
    }
    if (task != null) {
      mTaskEntity.removeFile = removeFile;
      if (!TextUtils.isEmpty(mTargetName)) {
        task.setTargetName(mTargetName);
      }
      removeTask();
    }
  }
}