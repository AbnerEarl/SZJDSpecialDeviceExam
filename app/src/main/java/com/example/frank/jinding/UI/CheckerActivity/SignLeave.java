package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.frank.jinding.R;


public class SignLeave extends AppCompatActivity {

    private Button sign,leave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_leave);

        init();


        sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打印Button的点击信息
                new  AlertDialog.Builder(SignLeave.this)
                        .setTitle("系统提示")
                        .setMessage("签到成功！")
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


        leave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打印Button的点击信息
                new  AlertDialog.Builder(SignLeave.this)
                        .setTitle("系统提示")
                        .setMessage("离开成功！")
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



    }

    private void init(){
        sign=(Button)this.findViewById(R.id.button_sign);
        leave=(Button)this.findViewById(R.id.button_leave);
    }
}
