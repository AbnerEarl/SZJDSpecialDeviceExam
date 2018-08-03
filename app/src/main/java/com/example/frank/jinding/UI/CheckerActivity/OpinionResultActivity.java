package com.example.frank.jinding.UI.CheckerActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;

public class OpinionResultActivity extends AppCompatActivity {

    private ImageButton back;
    private TextView title;

    private TextView opinion_result_suggestion,opinion_result_suggestion_text,opinion_result_state_text,opinion_result_opinion_text,opinion_result_conclue_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_result);



        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        opinion_result_suggestion=(TextView)this.findViewById(R.id.opinion_result_suggestion);
        opinion_result_suggestion_text=(TextView)this.findViewById(R.id.opinion_result_suggestion_text);
        opinion_result_state_text=(TextView)this.findViewById(R.id.opinion_result_state_text);
        opinion_result_opinion_text=(TextView)this.findViewById(R.id.opinion_result_opinion_text);
        opinion_result_conclue_text=(TextView)this.findViewById(R.id.opinion_result_conclue_text);

        title.setText("设备检验");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }






    private void getData(final String statuscode){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    //String data=statuscode+"#"+device_id;
                    HashMap<String,String> map_data=new HashMap<>();
                    map_data.put("statuscode",statuscode);
                    map_data.put("device_id",device_id);

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", JSON.toJSONString(map_data));
                    ApiService.GetString(ResultOpinion.this, "lookCheckOpinionResult", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            HashMap<String,String> map_data=JSON.parseObject(response.trim(),new TypeReference<HashMap<String,String>>(){});
                            if (map_data!=null&&map_data.get("result").equals("true")) {

                                if (map_data.get("statusCode").trim().equals("0")) {

                                    checkingAdapter.listItem.clear();
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    if (map_data.get("examResult").trim().equals("0")) {
                                        map.put("ItemResult", "不合格");
                                    } else if (map_data.get("examResult").trim().equals("1")) {
                                        map.put("ItemResult", "合格");
                                    } else if (map_data.get("examResult").trim().equals("2")) {
                                        map.put("ItemResult", "需复检（待确认）");
                                    }
                                    map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                    map.put("ItemStatus", "检测意见正在审核中，请稍后查看！");

                                    checkingAdapter.listItem.add(map);
                                    checkingAdapter.notifyDataSetChanged();

                                } else if (map_data.get("statusCode").trim().equals("1")) {
                                    passAdapter.listItem.clear();
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    if (map_data.get("examResult").trim().equals("0")) {
                                        map.put("ItemResult", "不合格");
                                    } else if (map_data.get("examResult").trim().equals("1")) {
                                        map.put("ItemResult", "合格");
                                    } else if (map_data.get("examResult").trim().equals("2")) {
                                        map.put("ItemResult", "需复检（待确认）");
                                    }
                                    map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                    map.put("ItemStatus", "检测意见已通过审核，请注意查看！");

                                    passAdapter.listItem.add(map);
                                    passAdapter.notifyDataSetChanged();
                                } else if (map_data.get("statusCode").trim().equals("2")) {
                                    notAdapter.listItem.clear();
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    if (map_data.get("examResult").trim().equals("0")) {
                                        map.put("ItemResult", "不合格");
                                    } else if (map_data.get("examResult").trim().equals("1")) {
                                        map.put("ItemResult", "合格");
                                    } else if (map_data.get("examResult").trim().equals("2")) {
                                        map.put("ItemResult", "需复检（待确认）");
                                    }
                                    map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                    map.put("ItemStatus", "检测意见未通过审核，请修改意见！");
                                    map.put("ItemSuggestion", map_data.get("changeSuggestion"));

                                    notAdapter.listItem.add(map);
                                    notAdapter.notifyDataSetChanged();
                                }



                            }else {
                                Toast.makeText(ResultOpinion.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(ResultOpinion.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(ResultOpinion.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }






}
