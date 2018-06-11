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

import android.support.annotation.NonNull;
import android.text.TextUtils;

//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.CommonUtil;

import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.CommonUtil;

import java.io.File;

/**
 * Created by lyy on 2016/12/5.
 * https://github.com/AriaLyy/Aria
 */
public class FtpDownloadTarget extends DownloadTarget {
  private final String TAG = "FtpDownloadTarget";

  FtpDownloadTarget(DownloadEntity entity, String targetName, boolean refreshInfo) {
    super(entity, targetName, refreshInfo);
    init(refreshInfo);
  }

  FtpDownloadTarget(String url, String targetName) {
    this(url, targetName, false);
  }

  FtpDownloadTarget(String url, String targetName, boolean refreshInfo) {
    super(url, targetName);
    init(refreshInfo);
  }

  private void init(boolean refreshInfo) {
    int lastIndex = url.lastIndexOf("/");
    mEntity.setFileName(url.substring(lastIndex + 1, url.length()));
    mTaskEntity.urlEntity = CommonUtil.getFtpUrlInfo(url);
    mTaskEntity.refreshInfo = refreshInfo;
    mTaskEntity.requestType = AbsTaskEntity.D_FTP;
  }

  /**
   * 设置文件保存文件夹路径
   * 关于文件名：
   * 1、如果保存路径是该文件的保存路径，如：/mnt/sdcard/file.zip，则使用路径中的文件名file.zip
   * 2、如果保存路径是文件夹路径，如：/mnt/sdcard/，则使用FTP服务器该文件的文件名
   *
   * @param downloadPath 路径必须为文件路径，不能为文件夹路径
   */
  @Override
  public FtpDownloadTarget setDownloadPath(@NonNull String downloadPath) {
    if (TextUtils.isEmpty(downloadPath)) {
      throw new IllegalArgumentException("文件保持路径不能为null");
    }
    File file = new File(downloadPath);
    if (file.isDirectory()) {
      downloadPath += mEntity.getFileName();
    }
    if (!downloadPath.equals(mEntity.getDownloadPath())) {
      File oldFile = new File(mEntity.getDownloadPath());
      File newFile = new File(downloadPath);
      if (TextUtils.isEmpty(mEntity.getDownloadPath()) || oldFile.renameTo(newFile)) {
        mEntity.setDownloadPath(downloadPath);
        mEntity.setFileName(newFile.getName());
        mTaskEntity.key = downloadPath;
        mEntity.update();
        mTaskEntity.update();
        CommonUtil.renameDownloadConfig(oldFile.getName(), newFile.getName());
      }
    }
    return this;
  }

  /**
   * 设置字符编码
   */
  public FtpDownloadTarget charSet(String charSet) {
    if (TextUtils.isEmpty(charSet)) return this;
    mTaskEntity.charSet = charSet;
    return this;
  }

  /**
   * ftp 用户登录信息
   *
   * @param userName ftp用户名
   * @param password ftp用户密码
   */
  public FtpDownloadTarget login(String userName, String password) {
    return login(userName, password, null);
  }

  /**
   * ftp 用户登录信息
   *
   * @param userName ftp用户名
   * @param password ftp用户密码
   * @param account ftp账号
   */
  public FtpDownloadTarget login(String userName, String password, String account) {
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
