package com.example.frank.jinding.UI.LoginActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.frank.jinding.Conf.Token;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.AddOrder;
import com.example.frank.jinding.UI.AuthorActivity.AuthorizedPersonsActivity;
import com.example.frank.jinding.UI.CheckerActivity.CheckersActivity;
import com.example.frank.jinding.UI.DepartorActivity.DepartmentHeadersActivity;
import com.example.frank.jinding.UI.SalesmanActivity.SalesmansActivity;
import com.example.frank.jinding.UI.SurveyorsActivity.SurveyorsActivity;


import java.util.Random;


public class Login extends AppCompatActivity {

    private LinearLayout linearLayout_return;
    private Button jianyanyuan,jianyanshi,jishu,bumen,yewuyuan,shouquanren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        init();

        linearLayout_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Verification.class);
                startActivity(intent);
            }
        });
        jianyanyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,CheckersActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        jianyanshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SurveyorsActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        jishu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(Login.this,TechnicalDirectorsActivity.class);
                //startActivity(intent);
                //finish();

                /*String result= AddOrder.LoginByHttpClientPost("yangyi123123", Token.getTOKEN(Login.this));
                Toast.makeText(Login.this,result,Toast.LENGTH_LONG).show();*/
                final Random random=new Random();
                String result="";
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String result= AddOrder.LoginByHttpClientPost("yangyiyangyi123123"+random.nextInt(100) , Token.getTOKEN(Login.this));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                Toast.makeText(Login.this,result,Toast.LENGTH_LONG).show();

            }
        });

        bumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,DepartmentHeadersActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        yewuyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SalesmansActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        shouquanren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,AuthorizedPersonsActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void init(){

        linearLayout_return=(LinearLayout)this.findViewById(R.id.linear_return);
        jianyanyuan=(Button)this.findViewById(R.id.button8);
        jianyanshi=(Button)this.findViewById(R.id.button14);
        jishu=(Button)this.findViewById(R.id.button15);
        bumen=(Button)this.findViewById(R.id.button16);
        yewuyuan=(Button)this.findViewById(R.id.button17);
        shouquanren=(Button)this.findViewById(R.id.button43);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
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
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                {
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
