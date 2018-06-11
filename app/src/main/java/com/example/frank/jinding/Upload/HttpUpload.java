package com.example.frank.jinding.Upload;

import android.content.Context;
import android.content.pm.ActivityInfo;

import com.example.frank.jinding.Upload.core.Aria;

/**
 * Created by Frank on 2017/11/28.
 */

public class HttpUpload {


    public static final String FILE_PATH = "/sdcard/large.rar";


    public void upload(String path, ActivityInfo context) {
        Aria.upload(this).register();
        Aria.upload(context)
                .load(path)
                .setUploadUrl("http://192.168.191.1:8020/")
                .setAttachment("file")
                .start();
    }

}
