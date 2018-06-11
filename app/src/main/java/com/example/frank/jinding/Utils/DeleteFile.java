package com.example.frank.jinding.Utils;

import java.io.File;

/**
 * Created by Frank on 2017/12/7.
 */

public class DeleteFile {
    public static boolean deleteFile(String picpath){

        try{
            //    File file = new File("/sdcard/1spray/1.png");
            File file = new File(picpath);
            if(file.exists()){
                file.delete();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
