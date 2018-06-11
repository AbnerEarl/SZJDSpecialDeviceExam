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
package com.example.frank.jinding.Upload.core;

//import com.arialyy.aria.orm.DbEntity;

import com.example.frank.jinding.Upload.orm.DbEntity;

/**
 * Created by Aria.Lao on 2017/8/29.
 * 错误实体
 */
public class ErrorEntity extends DbEntity {

  /**
   * 插入时间
   */
  public long insertTime;

  /**
   * 错误信息
   */
  public String err;

  /**
   * 任务名
   */
  public String taskName;

  /**
   *任务类型
   */
  public String taskType;

  /**
   * 提示
   */
  public String msg;

  /**
   * 任务key
   */
  public String key;

  @Override
  public String toString() {
    return "ErrorEntity{"
        + "insertTime="
        + insertTime
        + ", err='"
        + err
        + '\''
        + ", taskName='"
        + taskName
        + '\''
        + ", taskType='"
        + taskType
        + '\''
        + ", msg='"
        + msg
        + '\''
        + ", key='"
        + key
        + '\''
        + '}';
  }
}
