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

import android.text.TextUtils;

//import com.arialyy.aria.core.download.DownloadGroupEntity;
//import com.arialyy.aria.core.download.DownloadGroupTaskEntity;
//import com.arialyy.aria.orm.DbEntity;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.download.DownloadGroupEntity;
import com.example.frank.jinding.Upload.core.download.DownloadGroupTaskEntity;
import com.example.frank.jinding.Upload.orm.DbEntity;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.util.List;

/**
 * Created by Aria.Lao on 2017/11/1.
 * 任务实体工厂
 */
class DGTEntityFactory implements ITEntityFactory<DownloadGroupEntity, DownloadGroupTaskEntity>,
    IGTEntityFactory<DownloadGroupEntity, DownloadGroupTaskEntity> {
  private static final String TAG = "DTEntityFactory";
  private static volatile DGTEntityFactory INSTANCE = null;

  private DGTEntityFactory() {
  }

  public static DGTEntityFactory getInstance() {
    if (INSTANCE == null) {
      synchronized (DGTEntityFactory.class) {
        INSTANCE = new DGTEntityFactory();
      }
    }
    return INSTANCE;
  }

  /**
   * 通过下载实体创建任务实体
   */
  @Override
  public DownloadGroupTaskEntity create(DownloadGroupEntity entity) {
    DownloadGroupTaskEntity dgTaskEntity =
        DbEntity.findFirst(DownloadGroupTaskEntity.class, "key=?", entity.getGroupName());
    if (dgTaskEntity == null) {
      dgTaskEntity = new DownloadGroupTaskEntity();
      dgTaskEntity.save(entity);
    }
    if (dgTaskEntity.entity == null || TextUtils.isEmpty(dgTaskEntity.entity.getKey())) {
      dgTaskEntity.save(entity);
    }
    return dgTaskEntity;
  }

  /**
   * 对于任务组，不能使用这个，可用于FTP文件夹下载
   *
   * @deprecated {@link #create(String, List)}
   */
  @Override
  @Deprecated
  public DownloadGroupTaskEntity create(String key) {
    DownloadGroupTaskEntity dgTaskEntity =
        DbEntity.findFirst(DownloadGroupTaskEntity.class, "key=?", key);
    if (dgTaskEntity == null) {
      dgTaskEntity = new DownloadGroupTaskEntity();
      dgTaskEntity.save(getDownloadGroupEntity(key, null));
    }
    if (dgTaskEntity.entity == null || TextUtils.isEmpty(dgTaskEntity.entity.getKey())) {
      dgTaskEntity.save(getDownloadGroupEntity(key, null));
    }
    dgTaskEntity.urlEntity = CommonUtil.getFtpUrlInfo(key);
    return dgTaskEntity;
  }

  @Override
  public DownloadGroupTaskEntity create(String groupName, List<String> urls) {
    return create(getDownloadGroupEntity(groupName, urls));
  }

  /**
   * 查询任务组实体，如果数据库不存在该实体，则新创建一个新的任务组实体
   */
  private DownloadGroupEntity getDownloadGroupEntity(String groupName, List<String> urls) {
    DownloadGroupEntity entity =
        DbEntity.findFirst(DownloadGroupEntity.class, "groupName=?", groupName);
    if (entity == null) {
      entity = new DownloadGroupEntity();
      entity.setGroupName(groupName);
      entity.setUrls(urls);
    }
    return entity;
  }
}
