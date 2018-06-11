/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.frank.jinding.Upload.core;

import java.net.InetAddress;

/**
 * Created by Aria.Lao on 2017/10/24.
 * ftp url 信息链接实体
 */
public class FtpUrlEntity implements Cloneable {

  public String remotePath;

  public String account;

  /**
   * 原始url
   */
  public String url;

  /**
   * ftp协议
   */
  public String protocol;

  /**
   * 用户
   */
  public String user;
  /**
   * 密码
   */
  public String password;

  /**
   * 端口
   */
  public String port;

  /**
   * 主机域名
   */
  public String hostName;

  /**
   * 是否需要登录
   */
  public boolean needLogin = false;

  /**
   * 有效的ip地址
   */
  public InetAddress validAddr;

  @Override
  public FtpUrlEntity clone() {
    FtpUrlEntity entity = null;
    try {
      entity = (FtpUrlEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return entity;
  }
}
