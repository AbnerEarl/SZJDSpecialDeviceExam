package com.example.frank.jinding.ExtraPermission;

import android.app.Activity;
import android.content.Intent;

import com.example.frank.jinding.UI.SalesmanActivity.AddOrder;
import com.example.frank.jinding.UI.DepartorActivity.DispatchRecord;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderSearch;
import com.example.frank.jinding.UI.CheckerActivity.OrderSelectActivity;
import com.example.frank.jinding.UI.TechnicorActivity.TDCheckOrder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * File description.
 *
 * @author Frank
 * @date 2018/1/20
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class Permission {
    public static ArrayList<HashMap<String, Object>> getPermissions(String permiss){
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        String menus[]=permiss.split("#");
       String extra_menus[]={"订单查看/提交","订单录入/修改","委托协议审核","订单派工","派工查看","派工确认","仪器申领",
               "检验意见审核","校核检验报告","审核检验报告","审批检验报告"};
       for (int i=0;i<menus.length;i++){
           switch (menus[i]){
               case "订单查看/提交":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "订单录入/修改":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "委托协议审核":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "订单派工":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "派工查看":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "派工确认":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "仪器申领":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "检验意见审核":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }case "校核检验报告":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "审核检验报告":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               case "审批检验报告":{
                   HashMap<String, Object> map=new HashMap<>();
                   map.put("ItemText",menus[i]);
                   listItem.add(map);
                   break;

               }
               default:{

               }


           }
       }
       return listItem;
    }
    public static void excutePermissions(String permiss,Activity activity){
        switch (permiss){
            case "订单查看/提交":{
                Intent intent=new Intent(activity,OrderSearch.class);
                intent.putExtra("requestCode",0x01);
                activity.startActivity(intent);
                break;
            }
            case "订单录入/修改":{

                Intent intent=new Intent(activity,AddOrder.class);
                activity.startActivity(intent);
                break;
            }
            case "委托协议审核":{
                Intent intent=new Intent(activity, TDCheckOrder.class);
                activity.startActivity(intent);
                break;
            }
            case "订单派工":{
                /*Intent intent=new Intent(activity,);
                activity.startActivity(intent);*/
                break;
            }
            case "派工查看":{
                Intent intent=new Intent(activity, DispatchRecord.class);
                activity.startActivity(intent);
                break;
            }
            case "派工确认":{
                /*Intent intent=new Intent(activity,);
                activity.startActivity(intent);*/
                break;
            }
            case "仪器申领":{
                Intent intent=new Intent(activity,OrderSelectActivity.class);
                activity.startActivity(intent);
                break;
            }case "检验意见审核":{
                /*Intent intent=new Intent(activity,);
                activity.startActivity(intent);*/
                break;
            }
            case "校核检验报告":{
                /*Intent intent=new Intent(activity,);
                activity.startActivity(intent);*/
                break;
            }
            case "审核检验报告":{
                /*Intent intent=new Intent(activity,);
                activity.startActivity(intent);*/
                break;
            }
            case "审批检验报告":{
                /*Intent intent=new Intent(activity,);
                activity.startActivity(intent);*/
                break;
            }
            default:{

            }


        }

    }
}
