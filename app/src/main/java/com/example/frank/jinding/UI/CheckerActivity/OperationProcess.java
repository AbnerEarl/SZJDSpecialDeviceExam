package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.frank.jinding.Application.LocationApplication;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.Service.LocationService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;

public class OperationProcess extends AppCompatActivity {

    private Button sign, comfirm, start, leave;
    private ImageButton back;
    private TextView title;
    private TextView LocationResult;
    private LocationService locationService;
    private String locaterecorde="";
    private HashMap<String ,String> locate=new HashMap<>();
    private Button refresh;
    private static String submission_id="";
    String orderId;
    Boolean isMainChecker;
    public static Handler handler;
    public static Runnable runnable;
    private Boolean singtag=false,leavetag=false;
    private AlertDialog processDialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_process);
        Intent intent=getIntent();
        orderId=intent.getStringExtra("orderId");
        isMainChecker=intent.getBooleanExtra("isMainChecker",false);
        submission_id=intent.getStringExtra("submission_id");

        init();

        //标题栏设置
        title.setText("操作流程");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckControl.sign=false;
                CheckControl.comfirm=false;
                CheckControl.start=false;
                CheckControl.leave=false;
                finish();
            }
        });

        View processView = View.inflate(this, R.layout.simple_processbar, null);
        processDialog= new AlertDialog.Builder(this).create();
        processDialog.setView(processView);


         handler= new Handler();
         runnable= new Runnable() {
            public void run() {

                if (CheckControl.sign) {
                    sign.setBackgroundResource(R.drawable.button_shape);
                    comfirm.setBackgroundResource(R.drawable.button_com_shape);
                    comfirm.setEnabled(true);
                    start.setEnabled(false);
                    leave.setEnabled(false);
                }
                if (CheckControl.comfirm) {
                    sign.setBackgroundResource(R.drawable.button_shape);
                    comfirm.setBackgroundResource(R.drawable.button_shape);
                    start.setBackgroundResource(R.drawable.button_com_shape);
                    start.setEnabled(true);
                    leave.setEnabled(false);


                }
                if (CheckControl.start) {
                    sign.setBackground(getResources().getDrawable(R.drawable.button_shape));
                    comfirm.setBackground(getResources().getDrawable(R.drawable.button_shape));
                    start.setBackground(getResources().getDrawable(R.drawable.button_shape));
                    leave.setBackground(getResources().getDrawable(R.drawable.button_com_shape));
                    leave.setEnabled(true);


                }

               // handler.postDelayed(this, 1000);
            }
        };



        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               singtag=true;
                //waitprogress.setVisibility(waitprogress.VISIBLE);
                processDialog.show();
                try {
                    locationService.start();

                } catch (Exception ex) {
                    Log.e("签到：",ex.toString());

                }

            }
        });


        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isMainChecker){
                    Intent intent = new Intent(OperationProcess.this, OrderCheck.class);
                    intent.putExtra("submissionId",submission_id);
                    intent.putExtra("orderId",orderId);
                    //startActivity(intent);
                    startActivityForResult(intent,101);
                }else {
                    new  AlertDialog.Builder(OperationProcess.this)
                            .setTitle("系统提示")
                            .setMessage("\n请等待主检验员核对信息完成后开始检验！")
                            .setPositiveButton("确定",
                                    new  DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public  void  onClick(DialogInterface dialog, int  which)
                                        {

                                        }
                                    }).show();
                }



            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OperationProcess.this, CheckStartProtocl.class);
                intent.putExtra("submission_id",submission_id);
                intent.putExtra("order_id",orderId);
                intent.putExtra("isMainChecker",isMainChecker);
                //startActivity(intent);
                startActivityForResult(intent,101);


            }
        });


        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leavetag=true;
                //waitprogress.setVisibility(waitprogress.VISIBLE);
                processDialog.show();
                try {

                    locationService.start();
                } catch (Exception ex) {
                    Log.e("签到：",ex.toString());

                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OperationProcess.this,"操作成功",Toast.LENGTH_SHORT).show();
                startCheckStatus();
            }
        });


    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        sign = (Button) this.findViewById(R.id.button12);
        comfirm = (Button) this.findViewById(R.id.button13);
        start = (Button) this.findViewById(R.id.button62);
        leave = (Button) this.findViewById(R.id.button63);
        refresh=(Button)this.findViewById(R.id.start_check_refresh);
        back = (ImageButton) this.findViewById(R.id.titleback);
        title = (TextView) this.findViewById(R.id.titleplain);

        sign.setBackground(getResources().getDrawable(R.drawable.button_com_shape));
        comfirm.setEnabled(false);
        start.setEnabled(false);
        leave.setEnabled(false);

        getOrderStatu();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101){
            OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
        }
    }

    private void getOrderStatu(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", orderId);
                    ApiService.GetString(OperationProcess.this, "getOrderStatu", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (response.trim().equals("1")) {
                                flag =true;
                                CheckControl.sign=true;
                                CheckControl.comfirm=true;
                                CheckControl.start=true;
                                CheckControl.leave=true;
                                OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                            }else {
                                arriveInfomation();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(OperationProcess.this, "" + e, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(OperationProcess.this, "" + e, Toast.LENGTH_SHORT).show();

                        }


                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    private void arriveInfomation(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", submission_id);
                    ApiService.GetString(OperationProcess.this, "arriveInfomation", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (response.trim().equals("true")) {

                                CheckControl.sign=true;
                                OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(OperationProcess.this, "" + e, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(OperationProcess.this, "" + e, Toast.LENGTH_SHORT).show();

                        }


                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }




    private void startCheckStatus(){


        Map<String, Object> paremetes = new HashMap<>();
        paremetes.put("orderId", orderId);
        ApiService.GetString(OperationProcess.this, "startCheck", paremetes, new RxStringCallback() {
            boolean flag = false;

            @Override
            public void onNext(Object tag, String response) {

                if (response.trim().equals("ywc")){
                    CheckControl.sign=true;
                    CheckControl.comfirm=true;
                    CheckControl.start=true;
                    CheckControl.leave=true;
                    OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                }
                else if (response.trim().equals("yhd")&&CheckControl.sign) {
                    flag =true;
                    CheckControl.comfirm=true;
                    OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                }else {
                    CheckControl.comfirm=false;
                    OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(OperationProcess.this, "申请提交失败" + e, Toast.LENGTH_SHORT).show();
                flag=false;
                CheckControl.comfirm=false;
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                Toast.makeText(OperationProcess.this, "申请提交失败" + e, Toast.LENGTH_SHORT).show();
                flag=false;
                CheckControl.comfirm=false;
            }


        });

    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        final int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
    }


    public void logMsg(String str) {
        final String s = str;
        try {
            if (s != null&&!s.trim().equals("")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        title.post(new Runnable() {
                            @Override
                            public void run() {


                                //waitprogress.setVisibility(waitprogress.INVISIBLE);
                                processDialog.dismiss();


                                if (singtag){
                                    singtag=false;
                                    locaterecorde=submission_id+","+locaterecorde;
                                    locate.put("submission_id",submission_id);

                                    Map<String,Object> paremetes=new HashMap<>();
                                    paremetes.put("data", JSON.toJSONString(locate));

                                    ApiService.GetString(OperationProcess.this, "recordeSign", paremetes, new RxStringCallback() {
                                        @Override
                                        public void onNext(Object tag, String response) {
                                            locaterecorde="";
                                            if (response.equals("签到成功！")){
                                                locate.clear();
                                                CheckControl.sign=true;
                                                OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                                                new  AlertDialog.Builder(OperationProcess.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n签到成功！\n\n"+s)
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {


                                                                    }
                                                                }).show();
                                            }else if (response.equals("重复签到")){
                                                CheckControl.sign=true;
                                                OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                                                Toast.makeText(OperationProcess.this,"已经签到，您可以进行下一步操作了",Toast.LENGTH_SHORT).show();

                                            }
                                            else {
                                                CheckControl.sign=false;
                                                OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                                                new  AlertDialog.Builder(OperationProcess.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n签到失败！请检查网络是否连接正确，位置服务或GPS是否打开！\n\n"+s)
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {


                                                                    }
                                                                }).show();
                                            }


                                        }

                                        @Override
                                        public void onError(Object tag, Throwable e) {
                                            //CheckControl.sign=false;
                                            /*new  AlertDialog.Builder(OperationProcess.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n重复操作！\n\n"+e)
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {


                                                                }
                                                            }).show();*/
                                            Toast.makeText(OperationProcess.this,e.toString(),Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancel(Object tag, Throwable e) {
                                            /*CheckControl.sign=false;
                                            new  AlertDialog.Builder(OperationProcess.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n重复操作！\n\n"+e)
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {


                                                                }
                                                            }).show();*/
                                            Toast.makeText(OperationProcess.this,e.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }else if (leavetag){

                                    leavetag=false;
                                    //locaterecorde=submission_id+","+locaterecorde;
                                    locate.put("submission_id",submission_id);
                                    Map<String,Object> paremetes=new HashMap<>();
                                    paremetes.put("data", JSON.toJSONString(locate));
                                    //paremetes.put("data",locaterecorde);

                                    ApiService.GetString(OperationProcess.this, "recordeLeave", paremetes, new RxStringCallback() {
                                        @Override
                                        public void onNext(Object tag, String response) {
                                            locaterecorde="";
                                            if (response.equals("离开成功！")){
                                                locate.clear();
                                                CheckControl.leave=true;
                                                OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);
                                                new  AlertDialog.Builder(OperationProcess.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n离开成功！\n\n"+s+
                                                                "\n\n恭喜您，所有检测已经全部完成，是否现在离开现场？")
                                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                            }
                                                        })
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {
                                                                        CheckControl.sign=false;
                                                                        CheckControl.comfirm=false;
                                                                        CheckControl.start=false;
                                                                        CheckControl.leave=false;
                                                                        finish();

                                                                    }
                                                                }).show();
                                            }else {
                                                new  AlertDialog.Builder(OperationProcess.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n离开失败！请检查网络是否连接正确，位置服务或GPS是否打开！\n\n"+s)
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {


                                                                    }
                                                                }).show();
                                            }


                                        }

                                        @Override
                                        public void onError(Object tag, Throwable e) {
                                            Toast.makeText(OperationProcess.this,e.toString(),Toast.LENGTH_SHORT).show();
                                            /*new  AlertDialog.Builder(OperationProcess.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n重复操作！\n\n"+e)
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {


                                                                }
                                                            }).show();*/
                                        }

                                        @Override
                                        public void onCancel(Object tag, Throwable e) {
                                            /*new  AlertDialog.Builder(OperationProcess.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n重复操作！\n\n"+e)
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {


                                                                }
                                                            }).show();*/
                                            Toast.makeText(OperationProcess.this,e.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }else {
                                    /*new  AlertDialog.Builder(OperationProcess.this)
                                            .setTitle("系统提示")
                                            .setMessage("\n重复操作！")
                                            .setPositiveButton("确定",
                                                    new  DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public  void  onClick(DialogInterface dialog, int  which)
                                                        {

                                                        }
                                                    }).show();*/
                                    //Toast.makeText(OperationProcess.this,"重复操作！".toString(),Toast.LENGTH_SHORT).show();

                                }
                                    locationService.stop();


                            }
                        });




                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new  AlertDialog.Builder(OperationProcess.this)
                    .setTitle("系统提示")
                    .setMessage("\n请求失败！\n\n请检查网络环境是否连接正确，手机是否有信号！"+e)
                    .setPositiveButton("确定",
                            new  DialogInterface.OnClickListener()
                            {
                                @Override
                                public  void  onClick(DialogInterface dialog, int  which)
                                {
                                }
                            }).show();
        }
    }



    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location ) {//&& location.getLocType() != BDLocation.TypeServerError
                StringBuffer sb = new StringBuffer(256);
                locaterecorde="";
                locate.clear();
                //sb.append("时间 : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                //sb.append("\nlocType : ");// 定位类型
                //sb.append(location.getLocType());
                //sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                // sb.append(location.getLocTypeDescription());
                sb.append("\n\n经度 : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\n纬度 : ");// 纬度
                sb.append(location.getLatitude());
                locaterecorde=location.getLongitude()+","+location.getLatitude()+","+location.getAddrStr();
                locate.put("longitude",location.getLongitude()+"");
                locate.put("latitude",location.getLatitude()+"");
                locate.put("addstr",location.getAddrStr());
                //sb.append("\nradius : ");// 半径
                //sb.append(location.getRadius());
                //sb.append("\nCountryCode : ");// 国家码
                //sb.append(location.getCountryCode());
                //sb.append("\nCountry : ");// 国家名称
                //sb.append(location.getCountry());
                //sb.append("\ncitycode : ");// 城市编码
                //sb.append(location.getCityCode());
                //sb.append("\ncity : ");// 城市
                //sb.append(location.getCity());
                //sb.append("\nDistrict : ");// 区
                //sb.append(location.getDistrict());
                //sb.append("\nStreet : ");// 街道
                //sb.append(location.getStreet());
                //sb.append("\naddr : ");// 地址信息
                //sb.append("到达\n");
                sb.append("\n\n");
                sb.append(location.getAddrStr());
                //sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                //sb.append(location.getUserIndoorState());
                //sb.append("\nDirection(not all devices have value): ");
                //sb.append(location.getDirection());// 方向
                //sb.append("\nlocationdescribe: ");
                //sb.append(location.getLocationDescribe());// 位置语义化信息
                //sb.append("\n\n");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    int select=0;
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        select=i;
                    }
                    Poi poi = (Poi) location.getPoiList().get(select);
                    sb.append(poi.getName() + ";");

                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    //sb.append("\noperationers : ");// 运营商信息
                    //sb.append(location.getOperators());
                    //sb.append("\ndescribe : ");
                    //sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //result=sb.toString();
                logMsg(sb.toString());
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
            //getlocation();
        }
    };






    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK &&!CheckControl.leave&&CheckControl.sign&&CheckControl.start&&CheckControl.comfirm)
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("\n您还没有进行离开打卡，如果不打卡离开，可能会受到处罚！点击“取消”，进行离开打卡；点击“确定”，退出！");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }else if (keyCode == KeyEvent.KEYCODE_BACK &&CheckControl.sign&&CheckControl.leave&&CheckControl.start&&CheckControl.comfirm){
            CheckControl.sign=false;
            CheckControl.comfirm=false;
            CheckControl.start=false;
            CheckControl.leave=false;
            finish();
        }else {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("\n检验还没有完成，您确定要退出检验吗？");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }

        return false;

    }
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                {

                    CheckControl.sign=false;
                    CheckControl.comfirm=false;
                    CheckControl.start=false;
                    CheckControl.leave=false;

                    onStop();
                    finish();
                }
                break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };





}