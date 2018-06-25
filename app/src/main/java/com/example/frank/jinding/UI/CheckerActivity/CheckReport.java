package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.Adapter.ReportAdapter;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckReport extends AppCompatActivity {

    @BindView(R.id.titleback)
    ImageButton titleback;
    @BindView(R.id.titleplain)
    TextView titleplain;
    @BindView(R.id.start_date)
    TextView startdate;
    @BindView(R.id.end_date)
    TextView enddate;
    @BindView(R.id.search)
    ImageButton search;
    private SwipeRefreshLayout refreshLayout;
    private ListView lv_tasksss;
    private int option;
    private ReportAdapter mAdapter;
    private List<HashMap<String, Object>> mapList;

    public static boolean isOperate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_report);
        ButterKnife.bind(this);
        option = getIntent().getIntExtra("option", 1);
        init();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isOperate=false;
                refreshLayout.setRefreshing(true);
                getReportData();
            }
        });

        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            mAdapter.initReportView(arg2);
            }
        });


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                handler.postDelayed(this, 1000);
                if (isOperate) {
                    isOperate=false;
                    refreshLayout.setRefreshing(true);
                    getReportData();
                }

            }
        };
        handler.postDelayed(runnable, 1000);

    }


    private void init() {
        titleplain.setText("检验报告管理");
        refreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_report_list);
        lv_tasksss = (ListView) this.findViewById(R.id.lv_check_report_checker);
        mapList = new ArrayList<>();
        mAdapter = new ReportAdapter(this,CheckReport.this, option, mapList);
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter


        getReportData();
    }


    private void getReportData() {
        refreshLayout.setRefreshing(false);
        Map<String, Object> paremetes = new HashMap<>();
        paremetes.put("option", option);
        if (startdate.getText()!=null){
            paremetes.put("startDate",startdate.getText().toString());
        }
        if (enddate.getText()!=null){
            paremetes.put("endDate",enddate.getText().toString());
        }
        ApiService.GetString(CheckReport.this, "getReportList", paremetes, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {

                if (!response.trim().equals("") && response != null) {
                    Log.i("report",response);
                    mapList.clear();
                    JSONArray jsonArray= JSONObject.parseArray(response);
                    Iterator iterator=jsonArray.iterator();
                    while (iterator.hasNext()){
                        JSONObject jsonObject=(JSONObject) iterator.next();
                       Iterator iterator1=jsonObject.keySet().iterator();
                       HashMap<String,Object> item=new HashMap<>();
                        while (iterator1.hasNext()){
                            String key=(String)iterator1.next();
                            if (key.equals("auditPeople")){
                                System.out.print("报告数据取得审核人："+jsonObject.get(key));
                            }
                            item.put(key,jsonObject.get(key));
                        }
                        mapList.add(item);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CheckReport.this, "暂时没有报告，请稍后再查看", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(CheckReport.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                         /*   Toast.makeText(CheckReport.this, "获取失败！" + e, Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);*/
            }
        });


    }


    @OnClick({R.id.titleback, R.id.titleplain, R.id.search,R.id.start_date,R.id.end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleback:
                finish();
                break;
            case R.id.search:
                getReportData();
                break;
        }
    }
    protected void chooseDate(final int code) {

//控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        java.util.Calendar selectedDate = java.util.Calendar.getInstance();
        final java.util.Calendar startDate = java.util.Calendar.getInstance();
        startDate.set(2013, 0, 23);
        final java.util.Calendar endDate = java.util.Calendar.getInstance();
        endDate.set(2100, 11, 28);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                if (code==1)
                    startdate.setText(getTime(date));
                else
                    enddate.setText(getTime(date));

            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime.show();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
