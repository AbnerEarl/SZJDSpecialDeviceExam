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

import android.os.Parcel;
import android.os.Parcelable;

//import com.arialyy.aria.core.inf.AbsGroupEntity;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.orm.OneToMany;

import com.example.frank.jinding.Upload.core.inf.AbsGroupEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.orm.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AriaL on 2017/6/29.
 * 下载任务组实体
 */
public class DownloadGroupEntity extends AbsGroupEntity {

  @OneToMany(table = DownloadEntity.class, key = "groupName") private List<DownloadEntity> subtask =
      new ArrayList<>();

  /**
   * 任务组下载文件的文件夹地址
   *
   * @see DownloadGroupTarget#setDownloadDirPath(String)
   */
  private String dirPath = "";

  public List<DownloadEntity> getSubTask() {
    return subtask;
  }

  public void setSubTasks(List<DownloadEntity> subTasks) {
    this.subtask = subTasks;
  }

  public String getDirPath() {
    return dirPath;
  }

  public void setDirPath(String dirPath) {
    this.dirPath = dirPath;
  }

  public void setGroupName(String key) {
    this.groupName = key;
  }

  @Override
  public int getTaskType() {
    return getKey().startsWith("ftp") ? AbsTaskEntity.D_FTP_DIR : AbsTaskEntity.DG_HTTP;
  }

  public DownloadGroupEntity() {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeTypedList(this.subtask);
    dest.writeString(this.dirPath);
  }

  protected DownloadGroupEntity(Parcel in) {
    super(in);
    this.subtask = in.createTypedArrayList(DownloadEntity.CREATOR);
    this.dirPath = in.readString();
  }

  public static final Parcelable.Creator<DownloadGroupEntity> CREATOR = new Parcelable.Creator<DownloadGroupEntity>() {
    @Override
    public DownloadGroupEntity createFromParcel(Parcel source) {
      return new DownloadGroupEntity(source);
    }

    @Override
    public DownloadGroupEntity[] newArray(int size) {
      return new DownloadGroupEntity[size];
    }
  };
}
