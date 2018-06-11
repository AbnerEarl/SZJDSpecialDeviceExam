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

//import com.arialyy.aria.core.download.DownloadGroupTaskEntity;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.upload.UploadTaskEntity;
//import com.arialyy.aria.orm.DbEntity;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.download.DownloadGroupTaskEntity;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.upload.UploadTaskEntity;
import com.example.frank.jinding.Upload.orm.DbEntity;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.util.List;

/**
 * Created by AriaL on 2017/6/27.
 * 删除所有任务，并且删除所有回掉
 */
public class CancelAllCmd<T extends AbsTaskEntity> extends AbsNormalCmd<T> {
  /**
   * removeFile {@code true} 删除已经下载完成的任务，不仅删除下载记录，还会删除已经下载完成的文件，{@code false}
   * 如果文件已经下载完成，只删除下载记录
   */
  public boolean removeFile = false;

  /**
   * @param targetName 产生任务的对象名
   */
  CancelAllCmd(String targetName, T entity, int taskType) {
    super(targetName, entity, taskType);
  }

  @Override
  public void executeCmd() {
    removeAll();
    if (mTaskEntity instanceof DownloadTaskEntity
        || mTaskEntity instanceof DownloadGroupTaskEntity) {
      handleDownloadRemove();
      handleDownloadGroupRemove();
    } else if (mTaskEntity instanceof UploadTaskEntity) {
      handleUploadRemove();
      handleUploadRemove();
    }
  }

  /**
   * 处理下载任务组的删除操作
   */
  private void handleDownloadGroupRemove() {
    List<DownloadGroupTaskEntity> allEntity = DbEntity.findAllData(DownloadGroupTaskEntity.class);
    if (allEntity == null || allEntity.size() == 0) return;
    for (DownloadGroupTaskEntity entity : allEntity) {
      CommonUtil.delDownloadGroupTaskConfig(removeFile, entity);
    }
  }

  /**
   * 处理上传的删除
   */
  private void handleUploadRemove() {
    List<UploadTaskEntity> allEntity = DbEntity.findAllData(UploadTaskEntity.class);
    if (allEntity == null || allEntity.size() == 0) return;
    for (UploadTaskEntity entity : allEntity) {
      CommonUtil.delUploadTaskConfig(removeFile, entity);
    }
  }

  /**
   * 处理下载的删除
   */
  private void handleDownloadRemove() {
    List<DownloadTaskEntity> allEntity = DbEntity.findAllData(DownloadTaskEntity.class);
    if (allEntity == null || allEntity.size() == 0) return;
    for (DownloadTaskEntity entity : allEntity) {
      CommonUtil.delDownloadTaskConfig(removeFile, entity);
    }
  }
}
