package com.example.frank.jinding.Upload;

import android.app.Activity;
import android.os.Environment;

import com.example.frank.jinding.Upload.core.Aria;
import com.example.frank.jinding.Upload.core.Aria;
import com.example.frank.jinding.Upload.core.download.DownloadEntity;
import com.example.frank.jinding.Upload.core.upload.UploadEntity;
import com.example.frank.jinding.Upload.util.CommonUtil;

/**
 * Created by Frank on 2017/11/28. final String rootPath = "/opt/tomcat/webapps/ExamSpotRecordStorage/SZJD/";
 */

public class FtpUpload {


   // private final String FILE_PATH = Environment.getExternalStorageDirectory() + "/Luban/image/printtest.doc";
    public static final String URL = "ftp://119.23.243.96:21/";
    //public static final String URL = "ftp://119.29.133.185:21/";



    private static  final String Patt=Environment.getExternalStorageDirectory() + "/Luban/image/";
    //private  String path = Environment.getExternalStorageDirectory() + "/Luban/image/";


    public  void upload(Activity aa,String folder,String file) {
        Aria.upload(aa).register();
        UploadEntity entity = Aria.upload(aa).getUploadEntity(file);
        Aria.upload(aa).loadFtp(file).setUploadUrl(URL+folder).login("myftp", "myftp").start();
    }


    public void download(Activity aa,String folder,String filename){
        Aria.download(aa).register();
        DownloadEntity entity = Aria.download(aa).getDownloadEntity(URL+folder+filename);
        Aria.download(aa).loadFtp(URL+folder+filename, true).charSet("gbk").login("myftp", "myftp").setDownloadPath(Patt).start();

    }



}
