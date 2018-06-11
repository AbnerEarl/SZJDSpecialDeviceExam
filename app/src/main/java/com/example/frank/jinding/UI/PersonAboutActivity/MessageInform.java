package com.example.frank.jinding.UI.PersonAboutActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.frank.jinding.Print.PrintActivity;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;


public class MessageInform extends AppCompatActivity {

    private Button print_file, btn_evaluate, btn_message;
    private ImageView image_back;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inform);

        initView();
        initListener();
    }

    private void initView() {
        image_back = (ImageView) this.findViewById(R.id.image_back);
        print_file = (Button) this.findViewById(R.id.print_file);
        btn_evaluate = (Button) this.findViewById(R.id.btn_evaluate);
        btn_message = (Button) this.findViewById(R.id.button5);
        alertDialog = new AlertDialog.Builder(this);
    }

    private void initListener() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageInform.this, EvaluateActivity.class);
                startActivity(intent);
            }
        });
        print_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageInform.this, PrintActivity.class);
                startActivity(intent);
            }
        });
        //Map<String, Object> params = new HashMap<>();  getMessagePush
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                ApiService.GetString(MessageInform.this, "pushMessageToSingleObject",params, new RxStringCallback() {
                    @Override
                    public void onNext(Object tag, String response) {
                        Toast.makeText(MessageInform.this,response,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object tag, Throwable e) {

                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {

                    }
                });
            }
        });
    }
}
