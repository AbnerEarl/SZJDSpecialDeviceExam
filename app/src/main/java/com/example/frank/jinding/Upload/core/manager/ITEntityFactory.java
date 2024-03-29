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

//import com.arialyy.aria.core.inf.AbsEntity;
//import com.arialyy.aria.core.inf.AbsTaskEntity;

import com.example.frank.jinding.Upload.core.inf.AbsEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;

/**
 * Created by Aria.Lao on 2017/11/1.
 */
interface ITEntityFactory<ENTITY extends AbsEntity, TASK_ENTITY extends AbsTaskEntity<ENTITY>> {
  /**
   * 通过信息实体创建任务实体
   */
  TASK_ENTITY create(ENTITY entity);

  /**
   * 通过key创建任务，只适应于单任务
   */
  TASK_ENTITY create(String key);
}
