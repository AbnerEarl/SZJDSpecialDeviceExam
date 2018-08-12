package com.example.frank.jinding.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frank.jinding.R;
import com.example.frank.jinding.UI.LoginActivity.Verification;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

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

        startLogin();
    }

    private void startLogin(){
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
    }
}
