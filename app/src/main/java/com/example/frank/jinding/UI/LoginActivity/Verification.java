package com.example.frank.jinding.UI.LoginActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frank.jinding.Conf.Token;
import com.example.frank.jinding.Conf.URLConfig;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.LoginService;
import com.example.frank.jinding.UI.WebBrowser.ApplicationLoad;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class Verification extends AppCompatActivity {

    private Button login,company,introduce,btnserver;

    private EditText et_username,et_password;

    private static final String TAG="Verification";
    private String token=null;
    private String role="";
    //private String token="";
    private String usernameq,passwordq;
    private String logintag="";
    //private String role="";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private ProgressBar llp;

    //请求存储权限
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the u
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verifyStoragePermissions(this);
        //请求存储权限
        init();
        llp.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Verification.this,ApplicationLoad.class);
                intent.putExtra("url","http://www.szsafety.com/portal.php");
                startActivity(intent);
            }
        });

        introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new  AlertDialog.Builder(Verification.this)
                        .setTitle("系统提示")
                        .setMessage("暂时没有使用介绍！")
                        .setPositiveButton("确定",
                                new  DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public  void  onClick(DialogInterface dialog, int  which)
                                    {


                                    }
                                }).show();

            }
        });


        btnserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText ttt=new EditText(Verification.this);
                new  AlertDialog.Builder(Verification.this)
                        .setView(ttt)
                        .setMessage("请输入服务器地址：")
                        .setPositiveButton("确定",
                                new  DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public  void  onClick(DialogInterface dialog, int  which)
                                    {

                                        URLConfig.ServiceURL=ttt.getText().toString().trim();
                                        Toast.makeText(Verification.this,URLConfig.ServiceURL,Toast.LENGTH_SHORT).show();

                                    }
                                }).show();

            }
        });



        /*
        角色编码规则：前两位为“01”代表客户，其中后三位为“001”代表机构客户，
        “002”代表个人客户；前两位为“02”代表检验业务提供者，其中后三位为：
        000--检验业务提供公司，001—分管负责人，002—授权签字人，003—部门负责人，004—技术负责人，
        005—检验师，006—检验员，007—设备管理员，008—财务人员，009—报告发放员，010—档案管理员。
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });


        token= Token.getTOKEN(this);

        Map<String, String> map;
        map = LoginService.getSaveIfo(this);
        if(map!=null) {
            usernameq=map.get("用户名");
            passwordq=map.get("密码");
        }


       if (logintag.trim().equals("100")) {
           logintag="";
           new Thread(new Runnable() {

               @Override
               public void run() {
                   llp.setVisibility(View.VISIBLE);
                   login.setVisibility(View.INVISIBLE);
                   final String result = LoginService.LoginByHttpClientPost(usernameq, passwordq, token);
                   JSONObject resJosn = null;
                   logintag="";
                   try {
                       //文件测试
                    /*FtpUpload ff=new FtpUpload();
                    ff.upload(MainActivity.this,"aaa", Environment.getExternalStorageDirectory() + "/Luban/image/33.jpg");
*/
                       resJosn = new JSONObject(result);
                       if (resJosn != null && "token".equals(resJosn.getString("message")))
                           LoginService.saveToken(Verification.this, resJosn.getString("data"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           login.setVisibility(View.VISIBLE);
                           llp.setVisibility(View.INVISIBLE);
                           if (result != null && result.contains("token") || result.contains("msg1")) {

                               String rr[] = result.split(":");
                               role = rr[rr.length - 1];
                               logintag="";
                               //Toast.makeText(Verification.this, result, Toast.LENGTH_SHORT).show();
                               Toast.makeText(Verification.this, "登录成功", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(Verification.this, RolePermission.class);
                               intent.putExtra("role", role);
                               startActivityForResult(intent,5201);
                               //startActivity(intent);
                               //finish();
                           }else {
                               llp.setVisibility(View.INVISIBLE);
                               login.setVisibility(View.VISIBLE);
                           }

                       }
                   });

               }
           }).start();
       }


    }

    private void init(){

        logintag=getIntent().getStringExtra("tag");

        llp=(ProgressBar)this.findViewById(R.id.progressBar2_login);
        login=(Button)this.findViewById(R.id.button42);
        company=(Button)findViewById(R.id.button4);
        introduce=(Button)findViewById(R.id.button38);
        btnserver=(Button)this.findViewById(R.id.serverbtn);

        et_username=(EditText)this.findViewById(R.id.editText2);
        et_password=(EditText)this.findViewById(R.id.editText3);


        //检查是否存在用户名密码
        Map<String, String> map;
        map = LoginService.getSaveIfo(this);

        if(map!=null) {
            et_username.setText(map.get("用户名"));
            et_password.setText(map.get("密码"));
        }
        /*Map<String, String> map1=LoginService.getSaveToken(this);
        if (map1!=null){
            token=map1.get("token");
        }*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5201){
            logintag="";
        }
    }

    /**
     * 登录
     */
    public void login() {
        final String username=et_username.getText().toString().trim();
        final String password=et_password.getText().toString().trim();
        token=null;
        logintag="";

//		Intent intent=new Intent(MainActivity.this,MainActivity2.class);
//		startActivity(intent);


        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password))
        {
            Toast.makeText(Verification.this, "用户账号和密码不能为空", Toast.LENGTH_SHORT).show();

        }
        else
        {
            llp.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
            login.setEnabled(false);
                //保存密码
                Log.i(TAG, "需要保存密码");
                boolean flag;
                flag=LoginService.saveInfo(this,username, password);

            //发送消息给服务器 服务器判断是否存在用户名和密码是否正确
            new Thread(new Runnable() {

                @Override
                public void run() {


                    final String result=LoginService.LoginByHttpClientPost(username, password,token);
                    JSONObject resJosn= null;
                    logintag="";

                   /* try {
                        resJosn=new JSONObject(result);
                        if (resJosn!=null&&"token".equals(resJosn.getString("message")))
                            LoginService.saveToken(Verification.this,resJosn.getString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result!=null&&result.contains("token")||result.contains("msg1")){
                                String rr[]=result.split(":");
                                String role=rr[rr.length-1];
                                Toast.makeText(Verification.this,result, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Verification.this,Login.class);
                                intent.putExtra("role",role);
                                startActivity(intent);
                                finish();

                            }
                            else if (result!=null){
                                Toast.makeText(Verification.this, "失败结果："+result, Toast.LENGTH_SHORT).show();
                            }*/


                    try {
                        resJosn=new JSONObject(result);
                        if (resJosn!=null&&"token".equals(resJosn.getString("message")))
                            LoginService.saveToken(Verification.this,resJosn.getString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(result!=null&&result.contains("token")||result.contains("msg1")){
                                String rr[]=result.split(":");
                                role=rr[rr.length-1];
                                logintag="";
                                //Toast.makeText(Verification.this,result, Toast.LENGTH_SHORT).show();
                                Toast.makeText(Verification.this,"登录成功", Toast.LENGTH_SHORT).show();
                                Log.i("result:",result.toString());
                                Intent intent=new Intent(Verification.this,RolePermission.class);
                                intent.putExtra("role",role);
                                startActivityForResult(intent,5201);
                                //startActivity(intent);
                                //finish();

                                llp.setVisibility(View.INVISIBLE);
                                login.setVisibility(View.VISIBLE);
                                login.setEnabled(true);
                            }
                            else{
                                llp.setVisibility(View.INVISIBLE);
                                login.setVisibility(View.VISIBLE);
                                login.setEnabled(true);
                                Toast.makeText(Verification.this, "错误结果："+result, Toast.LENGTH_LONG).show();
                            }



                        }
                    });

                }
            }).start();


        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
            System.exit(0);
        }
        return false;
    }
}
