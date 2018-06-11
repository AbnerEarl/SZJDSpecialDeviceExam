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
package com.example.frank.jinding.Upload.util;

//import com.arialyy.aria.core.ErrorEntity;
//import com.arialyy.aria.core.download.DownloadEntity;
//import com.arialyy.aria.core.download.DownloadGroupEntity;
//import com.arialyy.aria.core.inf.AbsEntity;
//import com.arialyy.aria.core.upload.UploadEntity;

import com.example.frank.jinding.Upload.core.ErrorEntity;
import com.example.frank.jinding.Upload.core.download.DownloadEntity;
import com.example.frank.jinding.Upload.core.download.DownloadGroupEntity;
import com.example.frank.jinding.Upload.core.inf.AbsEntity;
import com.example.frank.jinding.Upload.core.upload.UploadEntity;

/**
 * Created by Aria.Lao on 2017/8/29.
 * 错误帮助类
 */
public class ErrorHelp {

  /**
   * 保存错误信息
   *
   * @param taskType 任务类型
   * @param entity 任务实体
   * @param msg 错误提示
   * @param ex 异常
   */
  public static void saveError(String taskType, AbsEntity entity, String msg, String ex) {
    ErrorEntity errorEntity = new ErrorEntity();
    errorEntity.insertTime = System.currentTimeMillis();
    errorEntity.err = ex;
    errorEntity.msg = msg;
    errorEntity.taskType = taskType;
    String name = "";
    String key = entity.getKey();
    if (entity instanceof DownloadEntity) {
      name = ((DownloadEntity) entity).getFileName();
    } else if (entity instanceof DownloadGroupEntity) {
      name = ((DownloadGroupEntity) entity).getGroupName();
    } else if (entity instanceof UploadEntity) {
      name = ((UploadEntity) entity).getFileName();
    }

    errorEntity.taskName = name;
    errorEntity.key = key;
    errorEntity.insert();
  }
}
