package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.frank.jinding.R;

public class Check_Order_Details extends AppCompatActivity {

   private String content;

    private TextView textshow;
    private ImageButton back;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_order_details);


        Intent intent=getIntent();
        content=intent.getStringExtra("content");

        textshow=(TextView)this.findViewById(R.id.textView4);
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
//标题栏设置
        title.setText("订单详情");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textshow.setText("正在查询订单："+content);

    }
}
