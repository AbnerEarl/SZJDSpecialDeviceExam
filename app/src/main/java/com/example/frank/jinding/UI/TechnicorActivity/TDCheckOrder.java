package com.example.frank.jinding.UI.TechnicorActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.Adapter.OrderAdapter;
import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TDCheckOrder extends AppCompatActivity {
    @BindView(R.id.titleback)
    ImageButton titleback;
    @BindView(R.id.titleplain)
    TextView titleplain;
    @BindView(R.id.lv_task)
    ListView lvTask;
    @BindView(R.id.start_date)
    TextView startdate;
    @BindView(R.id.end_date)
    TextView enddate;
    @BindView(R.id.order_search_org)
    EditText orderOrgEt;
    @BindView(R.id.linearLayout5)
    LinearLayout linearLayout5;
    List<CheckOrder>orderList=new ArrayList<>();
    private OrderAdapter mAdapter;
    private int requestCode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdcheck_order);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        if (intent!=null){
            if ((requestCode=intent.getIntExtra("requestCode",0))!=0){
                titleplain.setText("已审核订单");
            }
        }else{
            titleplain.setText("待审核订单");
        }
        initView();
        search();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        orderList.clear();
    }

    private void initView(){
        mAdapter=new OrderAdapter(this);
        mAdapter.listItem=orderList;
        lvTask.setAdapter(mAdapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(TDCheckOrder.this,TDProtocolCheck.class);
                intent.putExtra("checkOrder",orderList.get(position));
                intent.putExtra("requestCode",requestCode);
                intent.putExtra("orderId",orderList.get(position).getOrderId());
                startActivity(intent);
            }
        });
}
    @OnClick({R.id.titleback, R.id.start_date, R.id.end_date, R.id.search_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleback:
                finish();
                break;
            case R.id.start_date:
                chooseDate(1);
                break;
            case R.id.end_date:
                chooseDate(2);
                break;
            case R.id.search_history:
                search();
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

    private void search() {
        String url=null;
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final AlertDialog processDialog=new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String, Object> map = new HashMap<>();
        if (startdate.getText() != null)
            map.put("startDate", startdate.getText().toString());
        if (enddate.getText() != null)
            map.put("endDate", enddate.getText().toString());
        if (orderOrgEt.getText()!=null&&!TextUtils.isEmpty(orderOrgEt.getText().toString()))
            map.put("orderOrg",orderOrgEt.getText().toString());
        if (requestCode==0) {
            url="orderSearch";
            map.put("orderStatus", "01");
            map.put("endStatus", "02");
        }
        else if (requestCode==0x12){
            //已审核订单历史
            url="orderCheckHistory";
        }
        ApiService.GetString(this, url, map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                if (response != null && !TextUtils.isEmpty(response)) {
                    orderList.clear();
                    List<CheckOrder> list= JSON.parseArray(response,CheckOrder.class);
                    if (list!=null){
                        for (int i=0;i<list.size();i++){
                            orderList.add(list.get(i));
                        }
                        list.clear();
                    }
                    Log.i("获得订单",orderList.size()+"");
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                processDialog.dismiss();
                Toast.makeText(TDCheckOrder.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }
}
