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
package com.example.frank.jinding.Upload.core.download.downloader;

//import com.arialyy.aria.core.common.AbsFtpThreadTask;
//import com.arialyy.aria.core.common.StateConstance;
//import com.arialyy.aria.core.common.SubThreadConfig;
//import com.arialyy.aria.core.download.DownloadEntity;
//import com.arialyy.aria.core.download.DownloadTaskEntity;
//import com.arialyy.aria.core.inf.IDownloadListener;
//import com.arialyy.aria.util.ALog;
//import com.arialyy.aria.util.BufferedRandomAccessFile;
//
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPReply;

import com.example.frank.jinding.Upload.core.common.AbsFtpThreadTask;
import com.example.frank.jinding.Upload.core.common.StateConstance;
import com.example.frank.jinding.Upload.core.common.SubThreadConfig;
import com.example.frank.jinding.Upload.core.download.DownloadEntity;
import com.example.frank.jinding.Upload.core.download.DownloadTaskEntity;
import com.example.frank.jinding.Upload.core.inf.IDownloadListener;
import com.example.frank.jinding.Upload.net.ftp.FTPClient;
import com.example.frank.jinding.Upload.net.ftp.FTPReply;
import com.example.frank.jinding.Upload.util.ALog;
import com.example.frank.jinding.Upload.util.BufferedRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aria.Lao on 2017/7/24.
 * Ftp下载任务
 */
class FtpThreadTask extends AbsFtpThreadTask<DownloadEntity, DownloadTaskEntity> {
  private final String TAG = "FtpThreadTask";

  FtpThreadTask(StateConstance constance, IDownloadListener listener,
                SubThreadConfig<DownloadTaskEntity> downloadInfo) {
    super(constance, listener, downloadInfo);
  }

  @Override
  public void run() {
    //当前子线程的下载位置
    mChildCurrentLocation = mConfig.START_LOCATION;
    FTPClient client = null;
    InputStream is = null;
    BufferedRandomAccessFile file = null;
    try {
      ALog.d(TAG, "任务【"
          + mConfig.TEMP_FILE.getName()
          + "】线程__"
          + mConfig.THREAD_ID
          + "__开始下载【开始位置 : "
          + mConfig.START_LOCATION
          + "，结束位置："
          + mConfig.END_LOCATION
          + "】");
      client = createClient();
      if (client == null) return;
      client.setRestartOffset(mConfig.START_LOCATION);
      String remotePath =
          new String(mTaskEntity.urlEntity.remotePath.getBytes(charSet), SERVER_CHARSET);
      is = client.retrieveFileStream(remotePath);
      //发送第二次指令时，还需要再做一次判断
      int reply = client.getReplyCode();
      if (!FTPReply.isPositivePreliminary(reply)) {
        client.disconnect();
        fail(mChildCurrentLocation, "获取文件信息错误，错误码为：" + reply, null);
        return;
      }
      file = new BufferedRandomAccessFile(mConfig.TEMP_FILE, "rwd", mBufSize);
      file.seek(mConfig.START_LOCATION);
      byte[] buffer = new byte[mBufSize];
      int len;

      while ((len = is.read(buffer)) != -1) {
        if (STATE.isCancel || STATE.isStop) {
          break;
        }
        if (mSleepTime > 0) Thread.sleep(mSleepTime);
        if (mChildCurrentLocation + len >= mConfig.END_LOCATION) {
          len = (int) (mConfig.END_LOCATION - mChildCurrentLocation);
          file.write(buffer, 0, len);
          progress(len);
          break;
        } else {
          file.write(buffer, 0, len);
          progress(len);
        }
      }
      if (STATE.isCancel || STATE.isStop) return;
      ALog.i(TAG, "任务【" + mConfig.TEMP_FILE.getName() + "】线程__" + mConfig.THREAD_ID + "__下载完毕");
      writeConfig(true, 1);
      STATE.COMPLETE_THREAD_NUM++;
      if (STATE.isComplete()) {
        File configFile = new File(mConfigFPath);
        if (configFile.exists()) {
          configFile.delete();
        }
        STATE.isRunning = false;
        mListener.onComplete();
      }
      if (STATE.isFail()) {
        STATE.isRunning = false;
        mListener.onFail(false);
      }
    } catch (IOException e) {
      fail(mChildCurrentLocation, "下载失败【" + mConfig.URL + "】", e);
    } catch (Exception e) {
      fail(mChildCurrentLocation, "获取流失败", e);
    } finally {
      try {
        if (file != null) {
          file.close();
        }
        if (is != null) {
          is.close();
        }
        if (client != null && client.isConnected()) {
          client.disconnect();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected String getTaskType() {
    return "FTP_DOWNLOAD";
  }
}
