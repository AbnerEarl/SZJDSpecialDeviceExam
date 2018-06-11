package com.example.frank.jinding.UI.PersonAboutActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.Conf.DeviceNumber;
import com.example.frank.jinding.Conf.OrderInfoCheck;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.LoginActivity.Verification;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;


public class PersonInformation extends AppCompatActivity {

    private ImageButton titleleft,titleright;
    private TextView title,piloginname,pipass,piusername,pisex,piphone,pifax,piemail,piaddress;
    private Button modifyInfo;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        sharedPreferences = getSharedPreferences("is_init", MODE_PRIVATE);
        init();
        //标题栏设置
        title.setText("个人信息");

        titleright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("is_init",1);
                editor.commit();

                OrderInfoCheck.tag=false;
                DeviceNumber.num=0;

                CheckControl.order="";
                CheckControl.sign=false;
                CheckControl.comfirm=false;
                CheckControl.start=false;
                CheckControl.leave=false;



                Intent intent=new Intent(PersonInformation.this,Verification.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity

                intent.putExtra("tag","101");
                startActivity(intent);
                finish();
            }
        });

        titleleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        modifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (piloginname.getText().toString().trim().equals("") || piloginname.getText().toString().trim().length() < 2) {
                    new AlertDialog.Builder(PersonInformation.this)
                            .setTitle("系统提示")
                            .setMessage("\n个人信息尚未加载完成，您暂时不能修改个人信息，请等待个人信息加载完成！")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    }).show();
                    getPersonInfo();
                } else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.pi_modify_person_infomation, (ViewGroup) findViewById(R.id.pi_modify_person_infomation));
                    final EditText lpiloginpass = (EditText) layout.findViewById(R.id.lpi_login_pass);
                    final EditText lpiusername = (EditText) layout.findViewById(R.id.lpi_username);
                    final EditText lpiphone = (EditText) layout.findViewById(R.id.lpi_phone);
                    final EditText lpifax = (EditText) layout.findViewById(R.id.lpi_fax);
                    final EditText lpiemail = (EditText) layout.findViewById(R.id.lpi_email);
                    final EditText lpiaddress = (EditText) layout.findViewById(R.id.lpi_address);
                    final RadioButton lpi_nv = (RadioButton) layout.findViewById(R.id.lpi_sex_nv);
                    final RadioButton lpi_nan = (RadioButton) layout.findViewById(R.id.lpi_sex_nan);


                    if (pisex.getText().toString().trim().equals("男")) {
                        lpi_nan.setChecked(true);
                    } else {
                        lpi_nv.setChecked(true);
                    }
                    lpiloginpass.setText(pipass.getText());
                    lpiusername.setText(piusername.getText());
                    lpiphone.setText(piphone.getText());
                    lpifax.setText(pifax.getText());
                    lpiemail.setText(piemail.getText());
                    lpiaddress.setText(piaddress.getText());


                    new AlertDialog.Builder(PersonInformation.this)
                            .setTitle("修改个人信息：")
                            .setView(layout)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            String sex="";
                                            if (lpi_nan.isChecked()){
                                                sex="男";
                                            }else {
                                                sex="女";
                                            }
                                            String datas=lpiusername.getText().toString().trim()+"##"+sex+"##"+lpiphone.getText().toString().trim()+
                                                    "##"+lpifax.getText().toString().trim()+"##"+lpiemail.getText().toString().trim()+"##"+lpiaddress.getText().toString().trim();


                                            if (!pipass.getText().equals(lpiloginpass.getText())){

                                                datas=lpiloginpass.getText().toString().trim()+"##"+datas;
                                                datas="101##"+datas;
                                                setPersonInfo(datas);

                                            }else {
                                                datas="102##"+datas;
                                                setPersonInfo(datas);
                                            }

                                        }
                                    }).show();


                }
            }
        });



    }


    private void init(){
        titleleft=(ImageButton)this.findViewById(R.id.pileft);
        titleright=(ImageButton)this.findViewById(R.id.piright);
        title=(TextView)this.findViewById(R.id.picenter);

        piloginname=(TextView)this.findViewById(R.id.pi_login_name);
        pipass=(TextView)this.findViewById(R.id.pi_login_pass);
        piusername=(TextView)this.findViewById(R.id.pi_user_name);
        pisex=(TextView)this.findViewById(R.id.pi_sex);
        piphone=(TextView)this.findViewById(R.id.pi_phone);
        pifax=(TextView)this.findViewById(R.id.pi_fax);
        piemail=(TextView)this.findViewById(R.id.pi_email);
        piaddress=(TextView)this.findViewById(R.id.pi_address);

        modifyInfo=(Button)this.findViewById(R.id.pi_modify);

        getPersonInfo();
    }


    private void getPersonInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data","pp");
                    ApiService.GetString(PersonInformation.this, "getPersonInfo", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().split(":")[0].equals("true")) {
                                String info[]=(response.trim().split(":")[1]).split("##");
                                piloginname.setText(info[0]);
                                //pipass.setText(info[1]);
                                pipass.setText("********");
                                piusername.setText(info[2]);
                                pisex.setText(info[3]);
                                piphone.setText(info[4]);
                                pifax.setText(info[5]);
                                piemail.setText(info[6]);
                                piaddress.setText(info[7]);
                            }else {
                                Toast.makeText(PersonInformation.this,"需要重新登录",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                           // Toast.makeText(PersonInformation.this, "获取失败！" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                           // Toast.makeText(PersonInformation.this, "获取失败！" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private void setPersonInfo(final String dd){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",dd);
                    ApiService.GetString(PersonInformation.this, "setPersonInfo", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().equals("true")) {
                                Toast.makeText(PersonInformation.this,"资料修改成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(PersonInformation.this,"需要重新登录",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(PersonInformation.this, "修改失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(PersonInformation.this, "修改失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




}
