package com.example.frank.jinding.Conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 2017/12/7.
 */

public class CheckInfo {
//    public static ArrayList<String> images[];
//    public static ArrayList<String> text[];
    public static ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

    //opinion
    public static ArrayList<HashMap<String, Object>> listpassed = new ArrayList<HashMap<String,     Object>>();
    public static ArrayList<HashMap<String, Object>> listnotpass = new ArrayList<HashMap<String,     Object>>();
    public static ArrayList<HashMap<String, Object>> listchecking = new ArrayList<HashMap<String,     Object>>();



    //task
    public static ArrayList<HashMap<String, Object>> waittask = new ArrayList<HashMap<String,     Object>>();
    public static ArrayList<HashMap<String, Object>> comfirmtask = new ArrayList<HashMap<String,     Object>>();
    public static ArrayList<HashMap<String, Object>> establishtask = new ArrayList<HashMap<String,     Object>>();
    public static ArrayList<HashMap<String, Object>> refusetask = new ArrayList<HashMap<String,     Object>>();


    //tecnical check opinion

    public static ArrayList<HashMap<String, Object>> waitreport = new ArrayList<HashMap<String,     Object>>();
    public static ArrayList<HashMap<String, Object>> confirmreport = new ArrayList<HashMap<String,     Object>>();


    //仪器选择信息
    //public static ArrayList<Map<String, String>> isSelectedList = new ArrayList<>();



}
