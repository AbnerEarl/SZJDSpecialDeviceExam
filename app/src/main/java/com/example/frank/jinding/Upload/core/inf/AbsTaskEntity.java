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
package com.example.frank.jinding.Upload.core.inf;

//import com.arialyy.aria.core.FtpUrlEntity;
//import com.arialyy.aria.core.common.RequestEnum;
//import com.arialyy.aria.orm.DbEntity;
//import com.arialyy.aria.orm.Ignore;
//import com.arialyy.aria.orm.Primary;

import com.example.frank.jinding.Upload.core.FtpUrlEntity;
import com.example.frank.jinding.Upload.core.common.RequestEnum;
import com.example.frank.jinding.Upload.orm.DbEntity;
import com.example.frank.jinding.Upload.orm.Ignore;
import com.example.frank.jinding.Upload.orm.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyy on 2017/2/23.
 * 所有任务实体的父类
 */
public abstract class AbsTaskEntity<ENTITY extends AbsEntity> extends DbEntity {
  /**
   * HTTP单任务载
   */
  public static final int D_HTTP = 0x11;
  /**
   * HTTP任务组下载
   */
  public static final int DG_HTTP = 0x12;
  /**
   * HTTP单文件上传
   */
  public static final int U_HTTP = 0xA1;

  /**
   * FTP单文件下载
   */
  public static final int D_FTP = 0x13;
  /**
   * FTP文件夹下载，为避免登录过多，子任务由单线程进行处理
   */
  public static final int D_FTP_DIR = 0x14;
  /**
   * FTP单文件上传
   */
  public static final int U_FTP = 0xA2;

  /**
   * Task实体对应的key
   */
  @Primary
  public String key = "";

  /**
   * 账号和密码
   */
  @Ignore
  public FtpUrlEntity urlEntity;

  /**
   * 刷新信息 {@code true} 重新刷新下载信息
   */
  @Ignore public boolean refreshInfo = false;

  /**
   * 是否是新任务，{@code true} 新任务
   */
  @Ignore public boolean isNewTask = false;

  /**
   * 任务状态，和Entity的state同步
   */
  public int state = IEntity.STATE_WAIT;

  /**
   * 请求类型
   * {@link AbsTaskEntity#D_HTTP}、{@link AbsTaskEntity#D_FTP}、{@link AbsTaskEntity#D_FTP_DIR}。。。
   */
  public int requestType = D_HTTP;

  /**
   * http 请求头
   */
  public Map<String, String> headers = new HashMap<>();

  /**
   * 字符编码，默认为"utf-8"
   */
  public String charSet = "utf-8";

  /**
   * 网络请求类型
   */
  public RequestEnum requestEnum = RequestEnum.GET;

  /**
   * 从header中含有的文件md5码信息所需要的key
   */
  public String md5Key = "Content-MD5";

  /**
   * 从header中获取文件描述信息所需要的key
   */
  public String dispositionKey = "Content-Disposition";

  /**
   * 重定向后，从header中获取新url所需要的key
   */
  public String redirectUrlKey = "location";

  /**
   * 从Disposition获取的文件名说需要的key
   */
  public String dispositionFileKey = "attachment;filename";

  /**
   * 从header中含有的文件长度信息所需要的key
   */
  public String contentLength = "Content-Length";

  /**
   * 重定向链接
   */
  public String redirectUrl = "";

  /**
   * {@code true}  删除任务数据库记录，并且删除已经下载完成的文件
   * {@code false} 如果任务已经完成，只删除任务数据库记录
   */
  @Ignore public boolean removeFile = false;

  /**
   * 是否支持断点, {@code true} 为支持断点
   */
  public boolean isSupportBP = true;

  /**
   * 状态码
   */
  public int code;

  public abstract ENTITY getEntity();

  /**
   * 获取任务下载状态
   *
   * @return {@link IEntity}
   */
  public int getState() {
    return getEntity().getState();
  }

  @Override
  public void deleteData() {
    if (getEntity() != null) {
      getEntity().deleteData();
    }
    super.deleteData();
  }

  @Override
  public void update() {
    if (getEntity() != null) {
      getEntity().update();
    }
    super.update();
  }
}
