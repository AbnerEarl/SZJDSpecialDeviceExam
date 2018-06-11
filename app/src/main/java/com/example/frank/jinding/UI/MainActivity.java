package com.example.frank.jinding.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frank.jinding.R;
import com.example.frank.jinding.UI.LoginActivity.Verification;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView tag;

    private String token="";
    private String username,password;

    private String role="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo=(ImageView)this.findViewById(R.id.imageView3);
        tag=(TextView)this.findViewById(R.id.textView5);

        /*token= Token.getTOKEN(this);

        Map<String, String> map;
        map = LoginService.getSaveIfo(this);
        if(map!=null) {
            username=map.get("用户名");
            password=map.get("密码");
        }


       */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent=new Intent(MainActivity.this,Verification.class);
                intent.putExtra("tag","100");
                startActivity(intent);
                finish();
            }
        }).start();


        /*
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String result=LoginService.LoginByHttpClientPost(username, password,token);
                JSONObject resJosn= null;
                try {
                    //文件测试
                    *//*FtpUpload ff=new FtpUpload();
                    ff.upload(MainActivity.this,"aaa", Environment.getExternalStorageDirectory() + "/Luban/image/33.jpg");
*//*
                    resJosn=new JSONObject(result);
                    if (resJosn!=null&&"token".equals(resJosn.getString("message")))
                        LoginService.saveToken(MainActivity.this,resJosn.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result!=null&&result.contains("token")||result.contains("msg1")){
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String rr[]=result.split(":");
                            role=rr[rr.length-1];
                            Toast.makeText(MainActivity.this,result, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,RolePermission.class);
                            intent.putExtra("role",role);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this, "错误结果："+result, Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(MainActivity.this,Verification.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

            }
        }).start();*/



    }
}
