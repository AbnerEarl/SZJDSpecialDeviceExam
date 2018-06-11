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
package com.example.frank.jinding.Upload.orm;

import android.text.TextUtils;

//import com.arialyy.aria.core.ErrorEntity;
//import com.arialyy.aria.core.download.DownloadEntity;
//import com.arialyy.aria.core.download.DownloadGroupEntity;
//import com.arialyy.aria.core.download.DownloadGroupTaskEntity;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.upload.UploadEntity;
//import com.arialyy.aria.core.upload.UploadTaskEntity;

import com.example.frank.jinding.Upload.core.ErrorEntity;
import com.example.frank.jinding.Upload.core.download.DownloadEntity;
import com.example.frank.jinding.Upload.core.download.DownloadGroupEntity;
import com.example.frank.jinding.Upload.core.download.DownloadGroupTaskEntity;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.upload.UploadEntity;
import com.example.frank.jinding.Upload.core.upload.UploadTaskEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyy on 2017/4/6.
 * 数据库配置信息
 */
class DBConfig {
  static Map<String, Class> mapping = new HashMap<>();
  static String DB_NAME;
  //static int VERSION = 16;
  static int VERSION = 17;

  static {
    if (TextUtils.isEmpty(DB_NAME)) {
      DB_NAME = "AriaLyyDb";
    }
    if (VERSION == -1) {
      VERSION = 1;
    }
  }

  static {
    mapping.put("DownloadEntity", DownloadEntity.class);
    mapping.put("DownloadGroupEntity", DownloadGroupEntity.class);
    mapping.put("DownloadTaskEntity", DownloadTaskEntity.class);
    mapping.put("DownloadGroupTaskEntity", DownloadGroupTaskEntity.class);
    mapping.put("UploadEntity", UploadEntity.class);
    mapping.put("UploadTaskEntity", UploadTaskEntity.class);
    mapping.put("ErrorEntity", ErrorEntity.class);
    //mapping.put("UrlMapping", UrlMapping.class);
  }
}
