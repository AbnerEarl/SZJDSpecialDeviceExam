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
package com.example.frank.jinding.Upload.core.upload;

import android.support.annotation.NonNull;
import android.text.TextUtils;

//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.AbsUploadTarget;
//import com.arialyy.aria.core.manager.TEManager;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CheckUtil;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.AbsUploadTarget;
import com.example.frank.jinding.Upload.core.manager.TEManager;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CheckUtil;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.io.File;

/**
 * Created by Aria.Lao on 2017/7/27.
 * ftp单任务上传
 */
public class FtpUploadTarget
    extends AbsUploadTarget<FtpUploadTarget, UploadEntity, UploadTaskEntity> {
  private final String TAG = "FtpUploadTarget";

  FtpUploadTarget(String filePath, String targetName) {
    this.mTargetName = targetName;
    initTask(filePath);
  }

  private void initTask(String filePath) {
    mTaskEntity = TEManager.getInstance().getTEntity(UploadTaskEntity.class, filePath);
    if (mTaskEntity == null) {
      mTaskEntity = TEManager.getInstance().createTEntity(UploadTaskEntity.class, filePath);
    }
    mEntity = mTaskEntity.entity;
    File file = new File(filePath);
    mEntity.setFileName(file.getName());
    mEntity.setFileSize(file.length());
    mTaskEntity.requestType = AbsTaskEntity.U_FTP;
  }

  /**
   * 设置上传路径
   *
   * @param uploadUrl 上传路径
   */
  public FtpUploadTarget setUploadUrl(@NonNull String uploadUrl) {
    CheckUtil.checkUrl(uploadUrl);
    mTaskEntity.urlEntity = CommonUtil.getFtpUrlInfo(uploadUrl);
    if (mEntity.getUrl().equals(uploadUrl)) return this;
    mEntity.setUrl(uploadUrl);
    mEntity.update();
    return this;
  }

  /**
   * ftp 用户登录信。
   * 设置登录信息需要在设置上传链接之后{@link #setUploadUrl(String)}
   *
   * @param userName ftp用户名
   * @param password ftp用户密码
   */
  public FtpUploadTarget login(String userName, String password) {
    return login(userName, password, null);
  }

  /**
   * ftp 用户登录信息
   * 设置登录信息需要在设置上传链接之后{@link #setUploadUrl(String)}
   *
   * @param userName ftp用户名
   * @param password ftp用户密码
   * @param account ftp账号
   */
  public FtpUploadTarget login(String userName, String password, String account) {
    if (TextUtils.isEmpty(userName)) {
      ALog.e(TAG, "用户名不能为null");
      return this;
    } else if (TextUtils.isEmpty(password)) {
      ALog.e(TAG, "密码不能为null");
      return this;
    }
    mTaskEntity.urlEntity.needLogin = true;
    mTaskEntity.urlEntity.user = userName;
    mTaskEntity.urlEntity.password = password;
    mTaskEntity.urlEntity.account = account;
    return this;
  }
}
