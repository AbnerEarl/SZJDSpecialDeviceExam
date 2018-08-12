package com.example.frank.jinding.UI.LoginActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.AuthorActivity.AuthorizedPersonsActivity;
import com.example.frank.jinding.UI.CheckerActivity.CheckersActivityNew;
import com.example.frank.jinding.UI.DepartorActivity.DepartmentHeadersActivityNew;
import com.example.frank.jinding.UI.SalesmanActivity.SalesmansActivity;
import com.example.frank.jinding.UI.SurveyorsActivity.SurveyorsActivity;
import com.example.frank.jinding.UI.TechnicorActivity.TechnicalDirectorsActivity;
import com.example.frank.jinding.Utils.MenuMessage;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class RolePermission extends AppCompatActivity {

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private List<Map<String, Object>> role_list;
    private SimpleAdapter sim_adapter;
    private  List<String> roles=new ArrayList<String>();
    private  String role="";
    private int[] icon = { R.mipmap.jianyanyuan, R.mipmap.jianyanshi,R.mipmap.jishufuzeren,
            R.mipmap.yewuyuan, R.mipmap.shouquanqianziren, R.mipmap.bumenfuzeren};
    private String[] iconName = { "检验员", "检验师", "技术负责人", "业务员", "授权签字人", "部门负责人"};
    private int is_init;
    private SharedPreferences sharedPreferences;
    private  Set<String> tags=new HashSet<>();
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_permission);

        role=getIntent().getStringExtra("role");
        init();

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map =role_list.get(i);
                if (map.get("text").toString().trim().equals("部门负责人")){
                    Intent intent=new Intent(RolePermission.this,DepartmentHeadersActivityNew.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 6666);
                }else if (map.get("text").toString().trim().equals("检验员")){
                    System.out.print("点击事件结果："+i);
                    Intent intent=new Intent(RolePermission.this,CheckersActivityNew.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 6666);
                }else if (map.get("text").toString().trim().equals("检验师")){
                    Intent intent=new Intent(RolePermission.this,SurveyorsActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 6666);
                }else if (map.get("text").toString().trim().equals("技术负责人")){
                    Intent intent=new Intent(RolePermission.this,TechnicalDirectorsActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 6666);
                }else if (map.get("text").toString().trim().equals("业务员")){
                    Intent intent=new Intent(RolePermission.this,SalesmansActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 6666);
                }else if (map.get("text").toString().trim().equals("授权签字人")){
                    Intent intent=new Intent(RolePermission.this,AuthorizedPersonsActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, 6666);
                }
            }
        });


        initMessageAndVersionUpdate();

    }

/*
    @Override
    protected void onResume() {
        super.onResume();


    }
*/




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==6666) {

            for (int k=0;k<role_list.size();k++){

                View view=((ViewGroup)gview.getChildAt(k)).getChildAt(2);

                view.setVisibility(View.INVISIBLE);
            }

            getMessageInfo();
        }
    }



    private void init(){
        gview = (GridView) findViewById(R.id.role_gridview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        role_list = new ArrayList<Map<String, Object>>();
        //获取数据

        formatData(role);
        getData();

        for (int n=0;n<roles.size();n++){
            String tem=roles.get(n).trim();
            getRole(tem.substring(1,tem.length()-1));
        }

        //新建适配器
        String [] from ={"image","text","messageradio"};
        int [] to = {R.id.login_role_image,R.id.login_role_name,R.id.radio_message};
        sim_adapter = new SimpleAdapter(this, role_list, R.layout.item_role_login, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

        sharedPreferences = getSharedPreferences("is_init", MODE_PRIVATE);
        is_init=sharedPreferences.getInt("is_init",1);

        getMessageInfo();
    }

    private void initMessageAndVersionUpdate(){

       /* new Thread(new Runnable() {
            @Override
            public void run() {*/

                //初始化消息通知，为登陆的用户设置别名和标签
                if(is_init==1){//如果还没有初始化
                    Map<String,Object> params=new HashMap<>();
                    ApiService.GetString(RolePermission.this, "getUserId", params, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            JPushInterface.setAlias(RolePermission.this,is_init,response);
                            Set<String> tags=new HashSet<>();
                            for(Map map:role_list){
                                tags.add(map.get("text").toString());
                            }
                            JPushInterface.setTags(RolePermission.this,1,tags);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putInt("is_init",2);
                            editor.commit();
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {

                        }
                    });
                }

                PgyCrashManager.register();
                //动态请求权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSIONS);
                        requestPermissions(
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                }

                Handler handler=new Handler();
                Runnable runnable=new Runnable() {
                    public void run() {
                        new PgyUpdateManager.Builder()
                                .setForced(false)                //设置是否强制更新
                                .setUserCanRetry(false)         //失败后是否提示重新下载
                                .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk
                                .register();
                    }
                };
                handler.postDelayed(runnable, 1000);
            /*}
        }).start();*/

    }

    private void formatData(String origin){
        Log.i("origin:", origin.toString());
        String r1[]=origin.split("Role");

        if (r1.length>1){
            for (int i=1;i<r1.length;i++){

                String r2[]=r1[i].split(",");
                if (r2.length>1){
                        String r3[]=r2[1].split("=");
                       if (r3.length>1){
                           roles.add(r3[1]);
                       }
                }

            }

        }
    }




    private void getMessageInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    String data="查询待处理事务";
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(RolePermission.this, "getMessageInfo", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (!response.trim().equals("获取失败！")&&response.trim().length()>3) {

                                System.out.println("===========待处理信息=============="+response);
                                String data[]=response.split("##");
                                for (int i=0;i+1<data.length;i++){

                                    String dd[]=data[i].split("#");
                                    String match_role="";
                                    if (dd[2]!=null&&!dd[2].trim().equals("")&&Integer.parseInt(dd[2].trim())>0){

                                        if (MenuMessage.checker.contains(dd[1].trim())){
                                            match_role="检验员";
                                        }else if (MenuMessage.salesmans.contains(dd[1].trim())){
                                            match_role="业务员";
                                        }else if (MenuMessage.departheader.contains(dd[1].trim())){
                                            match_role="部门负责人";
                                        }else if (MenuMessage.technicaldirectors.contains(dd[1].trim())){
                                            match_role="技术负责人";
                                        }else if (MenuMessage.surveyors.contains(dd[1].trim())){
                                            match_role="检验师";
                                        }else if (MenuMessage.authorizer.contains(dd[1].trim())){
                                            match_role="授权签字人";
                                        }
                                    }
                                    int tagrole=100;
                                    for (int k=0;k<role_list.size();k++){
                                        Map<String, Object> map =role_list.get(k);
                                        if (map.get("text").toString().trim().equals(match_role)){
                                            tagrole=k;
                                            break;
                                        }

                                    }

                                  //  System.out.println("匹配结果："+tagrole);

                                    if (tagrole!=100){
                                        /*其他点击事件获取控件
                                        View view=gridview.getChildAt（position）;
                                        TextView text=(TextView)view.findviewbyid(R.id.xxx);*/
                                        //RadioButton radioButton = (RadioButton)((ViewGroup)gview.getChildAt(tagrole)).getChildAt(2);

                                        View view=((ViewGroup)gview.getChildAt(tagrole)).getChildAt(2);
                                        //View view1=((ViewGroup)gview.getChildAt(tagrole)).getChildAt(1);
                                        System.out.println("id获取结果："+view.getId()+"===="+view);
                                        /*RadioButton radioButton = (RadioButton)view.findViewById(R.id.message_inform);
                                        radioButton.setVisibility(View.VISIBLE);*/
                                        view.setVisibility(View.VISIBLE);
                                        //view1.setVisibility(View.INVISIBLE);
                                    }



                                }



                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(RolePermission.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(RolePermission.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    private void getRole(String role){

        if (role.equals("系统管理员")){

            role_list=data_list;

        }else if (role.equals("检验员")){
            Map<String, Object> map =data_list.get(0);
            role_list.add(map);

        }else if (role.equals("检验师")){

            Map<String, Object> map =data_list.get(1);
            role_list.add(map);

        }else if (role.equals("业务员")){

            Map<String, Object> map =data_list.get(3);
            role_list.add(map);

        }else if (role.equals("授权签字人")){
            Map<String, Object> map =data_list.get(4);
            role_list.add(map);

        }else if (role.equals("技术负责人")){
            Map<String, Object> map =data_list.get(2);
            role_list.add(map);

        }else if (role.equals("部门负责人")){

            Map<String, Object> map =data_list.get(5);
            role_list.add(map);
        }

    }

    private void getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            android.app.AlertDialog isExit = new android.app.AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("\n您确定要退出系统吗？");
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
                case android.app.AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                {
                    /*finish();
                    android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
                    System.exit(0);*/

                    //ExitAPP.AppExit(RolePermission.this);

                    /*ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);
                    am.restartPackage(getPackageName());*/

                    /*Intent intent =new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    finish();
                    //android.os.Process.killProcess(android.os.Process.myPid());
                    //System.exit(0);




                }
                break;
                case android.app.AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
}
