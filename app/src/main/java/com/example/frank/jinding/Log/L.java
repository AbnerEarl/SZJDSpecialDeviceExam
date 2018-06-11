package com.example.frank.jinding.Log;

import android.util.Log;

/**
 * PROJECT_NAME:SZJDSpecialDeviceExam
 * PACKAGE_NAME:com.example.frank.jinding.Log
 * USER:Frank
 * DATE:2018/1/10
 * TIME:4:25
 * DAY_NAME_FULL:星期日
 * DESCRIPTION:On the description and function of the document
 **/
public class L {

    public static final String TAG="SZJDAndroid";
    public static boolean debug=true;
    public static void e(String msg){
        if (debug){
            Log.e(TAG,msg);
        }
    }
}
