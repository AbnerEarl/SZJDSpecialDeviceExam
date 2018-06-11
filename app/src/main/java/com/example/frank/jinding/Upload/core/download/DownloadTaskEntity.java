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
package com.example.frank.jinding.Upload.core.download;

//import com.arialyy.aria.core.inf.AbsNormalTaskEntity;
//import com.arialyy.aria.orm.NoNull;
//import com.arialyy.aria.orm.OneToOne;

import com.example.frank.jinding.Upload.core.inf.AbsNormalTaskEntity;
import com.example.frank.jinding.Upload.orm.NoNull;
import com.example.frank.jinding.Upload.orm.OneToOne;

/**
 * Created by lyy on 2017/1/23.
 * 下载任务实体
 */
public class DownloadTaskEntity extends AbsNormalTaskEntity<DownloadEntity> {

  @OneToOne(table = DownloadEntity.class, key = "downloadPath") public DownloadEntity entity;

  /**
   * 任务的url
   */
  @NoNull
  public String url = "";

  /**
   * 所属的任务组组名，如果不属于任务组，则为null
   */
  public String groupName = "";

  /**
   * 该任务是否属于任务组
   */
  public boolean isGroupTask = false;

  public DownloadTaskEntity() {
  }

  @Override
  public DownloadEntity getEntity() {
    return entity;
  }

  public void save(DownloadEntity entity) {
    this.entity = entity;
    if (entity != null) {
      url = entity.getUrl();
      key = entity.getDownloadPath();
      entity.save();
    }
    save();
  }
}
