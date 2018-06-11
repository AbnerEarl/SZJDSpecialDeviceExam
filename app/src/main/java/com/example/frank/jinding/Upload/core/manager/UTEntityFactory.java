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

//import com.arialyy.aria.core.upload.UploadEntity;
//import com.arialyy.aria.core.upload.UploadTaskEntity;
//import com.arialyy.aria.orm.DbEntity;
//import com.arialyy.aria.util.Regular;

import com.example.frank.jinding.Upload.core.upload.UploadEntity;
import com.example.frank.jinding.Upload.core.upload.UploadTaskEntity;
import com.example.frank.jinding.Upload.orm.DbEntity;
import com.example.frank.jinding.Upload.util.Regular;

import java.util.regex.Pattern;

/**
 * Created by Aria.Lao on 2017/11/1.
 * 任务实体工厂
 */
class UTEntityFactory implements ITEntityFactory<UploadEntity, UploadTaskEntity> {
  private static final String TAG = "DTEntityFactory";
  private static volatile UTEntityFactory INSTANCE = null;

  private UTEntityFactory() {
  }

  public static UTEntityFactory getInstance() {
    if (INSTANCE == null) {
      synchronized (UTEntityFactory.class) {
        INSTANCE = new UTEntityFactory();
      }
    }
    return INSTANCE;
  }

  @Override
  public UploadTaskEntity create(UploadEntity entity) {
    UploadTaskEntity uTaskEntity =
        DbEntity.findFirst(UploadTaskEntity.class, "key=?", entity.getFilePath());
    if (uTaskEntity == null) {
      uTaskEntity = new UploadTaskEntity();
      uTaskEntity.entity = entity;
    }
    if (uTaskEntity.entity == null) {
      uTaskEntity.entity = entity;
    }
    return uTaskEntity;
  }

  @Override
  public UploadTaskEntity create(String key) {
    return create(getUploadEntity(key));
  }

  /**
   * 从数据中读取上传实体，如果数据库查不到，则新创建一个上传实体
   *
   * @param filePath 上传文件的文件路径
   */
  private UploadEntity getUploadEntity(String filePath) {
    UploadEntity entity = UploadEntity.findFirst(UploadEntity.class, "filePath=?", filePath);
    if (entity == null) {
      entity = new UploadEntity();
      //String regex = "[/|\\\\|//]";
      String regex = Regular.REG_FILE_NAME;
      Pattern p = Pattern.compile(regex);
      String[] strs = p.split(filePath);
      String fileName = strs[strs.length - 1];
      entity.setFileName(fileName);
      entity.setFilePath(filePath);
      entity.insert();
    }
    return entity;
  }
}
