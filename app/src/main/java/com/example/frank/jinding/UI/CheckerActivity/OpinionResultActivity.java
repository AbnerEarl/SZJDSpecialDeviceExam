package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.platform.comapi.map.B;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpinionResultActivity extends AppCompatActivity {

    private ImageButton back;
    private TextView title;
    private Button modifyOpinion;
    private String submission_id="",device_id="",orderId="";
    private TextView opinion_result_suggestion,opinion_result_suggestion_text,opinion_result_state_text,opinion_result_opinion_text,opinion_result_conclue_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_result);


        Intent intent=getIntent();
        submission_id=intent.getStringExtra("submission_id");
        orderId=intent.getStringExtra("orderId");
        device_id=intent.getStringExtra("deviceId");

        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        modifyOpinion=(Button)this.findViewById(R.id.modify_opinion_result);
        opinion_result_suggestion=(TextView)this.findViewById(R.id.opinion_result_suggestion);
        opinion_result_suggestion_text=(TextView)this.findViewById(R.id.opinion_result_suggestion_text);
        opinion_result_state_text=(TextView)this.findViewById(R.id.opinion_result_state_text);
        opinion_result_opinion_text=(TextView)this.findViewById(R.id.opinion_result_opinion_text);
        opinion_result_conclue_text=(TextView)this.findViewById(R.id.opinion_result_conclue_text);

        title.setText("检验意见审核结果");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        modifyOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.modify_check_opion, (ViewGroup) findViewById(R.id.modify_check_opion));
                List<String> list = new ArrayList<String>();
                ArrayAdapter<String> spadapter;
                final EditText etcontent = (EditText) layout.findViewById(R.id.editText_check_opinion);
                final Spinner resultsp = (Spinner) layout.findViewById(R.id.spinner_check_opinion);

                list.add("请选择检验结论：");
                list.add("不合格");
                list.add("合格");
                list.add("需复检（待确认）");

                //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
                spadapter  =  new ArrayAdapter<String>(OpinionResultActivity.this,android.R.layout.simple_spinner_item,  list);
                //第三步：为适配器设置下拉列表下拉时的菜单样式。
                spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //第四步：将适配器添加到下拉列表上
                resultsp.setAdapter(spadapter);

                new AlertDialog.Builder(OpinionResultActivity.this)
                        .setTitle("修改报告")
                        .setView(layout)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("提交",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (resultsp.getSelectedItemId()!=0&&etcontent.getText().toString().trim().length()>2){

                                            new  AlertDialog.Builder(OpinionResultActivity.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n请核对检测意见！\n\n检测结论："+resultsp.getSelectedItem()+"\n\n检测意见："+etcontent.getText()+"\n\n\n\n确认无误后，点击“确定”进行提交，点击“取消”返回修改！")
                                                    .setNegativeButton("取消",null)
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {
                                                                    HashMap<String ,String> map_data=new HashMap<>();

                                                                    String exam_result=(resultsp.getSelectedItemId()-1)+"";
                                                                    String problem_suggestion=etcontent.getText().toString();
                                                                    //String data=submission_id+"#"+device_id+"#"+instrment_codes+"#"+exam_result+"#"+problem_suggestion;
                                                                    map_data.put("exam_result",exam_result);
                                                                    map_data.put("problem_suggestion",problem_suggestion);
                                                                    map_data.put("submission_id",submission_id);
                                                                    map_data.put("device_id",device_id);
                                                                    map_data.put("orderId",orderId);
                                                                    Map<String, Object> paremetes = new HashMap<>();
                                                                    paremetes.put("data", JSON.toJSONString(map_data));
                                                                    ApiService.GetString(OpinionResultActivity.this, "addCheckOpinionResult", paremetes, new RxStringCallback() {
                                                                        boolean flag = false;

                                                                        @Override
                                                                        public void onNext(Object tag, String response) {

                                                                            if (response.trim().equals("重复提交")){
                                                                                //Toast.makeText(ResultOpinion.this,"该台设备已经提交过检测意见，请查看审核结果",Toast.LENGTH_SHORT).show();
                                                                                Toast.makeText(OpinionResultActivity.this,"该台设备的检测意见修改成功",Toast.LENGTH_SHORT).show();
                                                                                finish();

                                                                            }else if (response.trim().equals("拒绝修改")){
                                                                                Toast.makeText(OpinionResultActivity.this, "该台设备的检测意见已经审核通过，拒绝再次修改", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            else if (response.trim().equals("提交成功！")&& CheckControl.sign) {
                                                                                etcontent.setText("");
                                                                                CheckControl.start=true;

                                                                                Toast.makeText(OpinionResultActivity.this,"检测意见提交成功，请等待审核结果",Toast.LENGTH_SHORT).show();

                                                                            }else {
                                                                                Toast.makeText(OpinionResultActivity.this, "提交失败" , Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onError(Object tag, Throwable e) {
                                                                            Toast.makeText(OpinionResultActivity.this, "提交失败" + e, Toast.LENGTH_SHORT).show();


                                                                        }

                                                                        @Override
                                                                        public void onCancel(Object tag, Throwable e) {
                                                                            Toast.makeText(OpinionResultActivity.this, "提交失败" + e, Toast.LENGTH_SHORT).show();

                                                                        }


                                                                    });


                                                                }
                                                            }).show();
                                        }else if (etcontent.getText().toString().trim().length()<3){
                                            new  AlertDialog.Builder(OpinionResultActivity.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n请填写不少于3个字符的检测意见！")
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {

                                                                }
                                                            }).show();
                                        }else {
                                            new  AlertDialog.Builder(OpinionResultActivity.this)
                                                    .setTitle("系统提示")
                                                    .setMessage("\n请选择检验结论！")
                                                    .setPositiveButton("确定",
                                                            new  DialogInterface.OnClickListener()
                                                            {
                                                                @Override
                                                                public  void  onClick(DialogInterface dialog, int  which)
                                                                {

                                                                }
                                                            }).show();
                                        }
                                           /* HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("ItemName", holder.pname.getText());
                                            checkingAdapter.listItem.add(map);
                                            checkingAdapter.notifyDataSetChanged();
                                            //mySpinner.setSelected(true);
                                            notAdapter.listItem.remove(position);
                                            notAdapter.notifyDataSetChanged();
                                            Toast.makeText(ResultOpinion.this,"提交成功！",Toast.LENGTH_SHORT).show();*/
                                    }
                                }).show();
            }
        });


        getData();

    }






    private void getData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String,String> map_data=new HashMap<>();
                    map_data.put("device_id",device_id);
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", JSON.toJSONString(map_data));
                    ApiService.GetString(OpinionResultActivity.this, "lookCheckOpinionResult", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            HashMap<String,String> map_data=JSON.parseObject(response.trim(),new TypeReference<HashMap<String,String>>(){});
                            if (map_data!=null&&map_data.get("result").equals("true")) {

                                if (map_data.get("statusCode").equals("0")){
                                    opinion_result_suggestion.setVisibility(View.INVISIBLE);
                                    opinion_result_suggestion_text.setVisibility(View.INVISIBLE);
                                    modifyOpinion.setVisibility(View.INVISIBLE);
                                    opinion_result_state_text.setText("正在审核中");
                                }else if (map_data.get("statusCode").equals("1")){
                                    opinion_result_suggestion.setVisibility(View.INVISIBLE);
                                    opinion_result_suggestion_text.setVisibility(View.INVISIBLE);
                                    modifyOpinion.setVisibility(View.INVISIBLE);
                                    opinion_result_state_text.setText("已通过审核");
                                }
                                else if (map_data.get("statusCode").equals("2")) {
                                    opinion_result_suggestion.setVisibility(View.VISIBLE);
                                    opinion_result_suggestion_text.setVisibility(View.VISIBLE);
                                    modifyOpinion.setVisibility(View.VISIBLE);
                                    opinion_result_state_text.setText("未通过审核，请根据修改意见修改检验结论或检验意见");
                                }
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    if (map_data.get("examResult").trim().equals("0")) {
                                        //map.put("ItemResult", "不合格");
                                        opinion_result_conclue_text.setText("不合格");
                                    } else if (map_data.get("examResult").trim().equals("1")) {
                                        //map.put("ItemResult", "合格");
                                        opinion_result_conclue_text.setText("不合格");
                                    } else if (map_data.get("examResult").trim().equals("2")) {
                                        //map.put("ItemResult", "需复检（待确认）");
                                        opinion_result_conclue_text.setText("不合格");
                                    }
                                    //map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                opinion_result_opinion_text.setText(map_data.get("problemSuggestion"));
                                   // map.put("ItemSuggestion", map_data.get("changeSuggestion"));
                                opinion_result_suggestion_text.setText(map_data.get("changeSuggestion"));





                            }else {
                                Toast.makeText(OpinionResultActivity.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();
                                opinion_result_suggestion.setVisibility(View.INVISIBLE);
                                opinion_result_suggestion_text.setVisibility(View.INVISIBLE);
                                modifyOpinion.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(OpinionResultActivity.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();
                            opinion_result_suggestion.setVisibility(View.INVISIBLE);
                            opinion_result_suggestion_text.setVisibility(View.INVISIBLE);
                            modifyOpinion.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(OpinionResultActivity.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();
                            opinion_result_suggestion.setVisibility(View.INVISIBLE);
                            opinion_result_suggestion_text.setVisibility(View.INVISIBLE);
                            modifyOpinion.setVisibility(View.INVISIBLE);
                        }
                    });



                } catch (Exception e) {
                    opinion_result_suggestion.setVisibility(View.INVISIBLE);
                    opinion_result_suggestion_text.setVisibility(View.INVISIBLE);
                    modifyOpinion.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }).start();


    }






}
