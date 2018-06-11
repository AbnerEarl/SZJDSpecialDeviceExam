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

//import com.arialyy.aria.core.inf.AbsTask;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.IEntity;
//import com.arialyy.aria.util.ALog;

import com.example.frank.jinding.Upload.core.inf.AbsTask;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.IEntity;
import com.example.frank.jinding.Upload.util.ALog;

/**
 * Created by lyy on 2016/8/22.
 * 添加任务的命令
 */
class AddCmd<T extends AbsTaskEntity> extends AbsNormalCmd<T> {

  AddCmd(String targetName, T entity, int taskType) {
    super(targetName, entity, taskType);
  }

  @Override
  public void executeCmd() {
    if (!canExeCmd) return;
    AbsTask task = getTask();
    if (task == null) {
      mTaskEntity.getEntity().setState(IEntity.STATE_WAIT);
      createTask();
    } else {
      ALog.w(TAG, "添加命令执行失败，【该任务已经存在】");
    }
  }
}