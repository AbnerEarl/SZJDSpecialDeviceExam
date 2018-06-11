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
package com.example.frank.jinding.Upload.core.command.group;

//import com.arialyy.aria.core.inf.AbsGroupTaskEntity;

import com.example.frank.jinding.Upload.core.inf.AbsGroupTaskEntity;

/**
 * Created by AriaL on 2017/6/29.
 * 任务组开始命令，该命令负责处理任务组子任务的开始\恢复等工作
 */
class GroupStartCmd<T extends AbsGroupTaskEntity> extends AbsGroupCmd<T> {
  /**
   * @param targetName 创建任务的对象名
   */
  GroupStartCmd(String targetName, T entity) {
    super(targetName, entity);
  }

  @Override
  public void executeCmd() {
    if (checkTask()) {
      tempTask.startSubTask(childUrl);
    }
  }
}
