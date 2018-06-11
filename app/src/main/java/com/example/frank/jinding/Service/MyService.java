package com.example.frank.jinding.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {


    private Timer timer;
    Context context;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();

       /* Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 在此处添加执行的代码

                NewMessageNotification.notify(MyService.this,"hello!",1);
                handler.postDelayed(this, 3000);// 50ms后执行this，即runable
            }
        };
        handler.postDelayed(runnable, 3000);// 打开定时器，50ms后执行runnable操作*/


       /* TimerTask task = new TimerTask(){
            public void run(){
                // 在此处添加执行的代码

                NewMessageNotification.notify(MyService.this,"hello!",1);
            }
        };
        timer = new Timer();
        timer.schedule(task, 3000);//开启定时器，delay 1s后执行task*/



    }




























    //创建通知
    public void CreateInform() {

       /* //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //创建一个通知
        Notification notification = new Notification(R.drawable.example_picture, "巴拉巴拉~~", System.currentTimeMillis());
       // notification.setLatestEventInfo(context, "点击查看", "点击查看详细内容", pendingIntent);


        //用NotificationManager的notify方法通知用户生成标题栏消息通知
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(100, notification);//id是应用中通知的唯一标识
        //如果拥有相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。*/




        /*mHandler.post(new Runnable() {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                NewMessageNotification.notify(MyService.this,"hello!",1);
                mHandler.postDelayed(this, 3000);
            }
        });*/


//        NewMessageNotification.notify(MyService.this,"hello!",1);


        final Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 在此处添加执行的代码

                NewMessageNotification.notify(MyService.this,"hello!",1);

                handler.postDelayed(this, 3000);// 50ms后执行this，即runable
            }
        };
        handler.postDelayed(runnable, 3000);// 打开定时器，50ms后执行runnable操作


    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stubm
        super.onStart(intent, startId);
        CreateInform();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }



    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
