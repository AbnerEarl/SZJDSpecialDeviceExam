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
package com.example.frank.jinding.Upload.core.common;

import android.text.TextUtils;

//import com.arialyy.aria.core.FtpUrlEntity;
//import com.arialyy.aria.core.inf.AbsNormalEntity;
//import com.arialyy.aria.core.inf.AbsTaskEntity;
//import com.arialyy.aria.core.inf.IEventListener;
//import com.arialyy.aria.util.ALog;
//
//import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPReply;

import com.example.frank.jinding.Upload.core.FtpUrlEntity;
import com.example.frank.jinding.Upload.core.inf.AbsNormalEntity;
import com.example.frank.jinding.Upload.core.inf.AbsTaskEntity;
import com.example.frank.jinding.Upload.core.inf.IEventListener;
import com.example.frank.jinding.Upload.net.ftp.FTP;
import com.example.frank.jinding.Upload.net.ftp.FTPClient;
import com.example.frank.jinding.Upload.net.ftp.FTPReply;
import com.example.frank.jinding.Upload.util.ALog;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lyy on 2017/9/26.
 * FTP单任务父类
 */
public abstract class AbsFtpThreadTask<ENTITY extends AbsNormalEntity, TASK_ENTITY extends AbsTaskEntity<ENTITY>>
    extends AbsThreadTask<ENTITY, TASK_ENTITY> {
  private final String TAG = "AbsFtpThreadTask";
  protected String charSet, port;
  /**
   * D_FTP 服务器编码
   */
  public static String SERVER_CHARSET = "ISO-8859-1";

  protected AbsFtpThreadTask(StateConstance constance, IEventListener listener,
      SubThreadConfig<TASK_ENTITY> info) {
    super(constance, listener, info);
  }

  /**
   * 构建FTP客户端
   */
  protected FTPClient createClient() {
    FTPClient client = null;
    final FtpUrlEntity urlEntity = mTaskEntity.urlEntity;
    if (urlEntity.validAddr == null) {
      try {
        InetAddress[] ips = InetAddress.getAllByName(urlEntity.hostName);
        client = connect(new FTPClient(), ips, 0, Integer.parseInt(urlEntity.port));
        if (client == null) {
          return null;
        }
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    } else {
      client = new FTPClient();
      try {
        client.connect(urlEntity.validAddr, Integer.parseInt(urlEntity.port));
      } catch (IOException e) {
        ALog.e(TAG, ALog.getExceptionString(e));
        return null;
      }
    }

    if (client == null){
      return null;
    }

    try {
      if (urlEntity.needLogin) {
        if (TextUtils.isEmpty(urlEntity.account)) {
          client.login(urlEntity.user, urlEntity.password);
        } else {
          client.login(urlEntity.user, urlEntity.password, urlEntity.account);
        }
      }
      int reply = client.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        client.disconnect();
        fail(STATE.CURRENT_LOCATION, "无法连接到ftp服务器，错误码为：" + reply, null);
        return null;
      }
      // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码
      charSet = "UTF-8";
      if (!TextUtils.isEmpty(mTaskEntity.charSet) || !FTPReply.isPositiveCompletion(
          client.sendCommand("OPTS UTF8", "ON"))) {
        charSet = mTaskEntity.charSet;
      }
      client.setControlEncoding(charSet);
      client.setDataTimeout(10 * 1000);
      client.enterLocalPassiveMode();
      client.setFileType(FTP.BINARY_FILE_TYPE);
      client.setControlKeepAliveTimeout(5);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return client;
  }

  /**
   * 连接到ftp服务器
   */
  private FTPClient connect(FTPClient client, InetAddress[] ips, int index, int port) {
    try {
      client.connect(ips[index], port);
      mTaskEntity.urlEntity.validAddr = ips[index];
      return client;
    } catch (IOException e) {
      try {
        if (client.isConnected()) {
          client.disconnect();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      if (index + 1 >= ips.length) {
        ALog.w(TAG, "遇到[ECONNREFUSED-连接被服务器拒绝]错误，已没有其他地址，链接失败");
        return null;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
      ALog.w(TAG, "遇到[ECONNREFUSED-连接被服务器拒绝]错误，正在尝试下一个地址");
      return connect(new FTPClient(), ips, index + 1, port);
    }
  }
}
