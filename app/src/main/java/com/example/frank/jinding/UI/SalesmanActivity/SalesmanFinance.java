package com.example.frank.jinding.UI.SalesmanActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.Adapter.FinanceAdapter;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesmanFinance extends AppCompatActivity {


    @BindView(R.id.titleback)
    ImageButton titleback;
    @BindView(R.id.titleplain)
    TextView titleplain;
    @BindView(R.id.start_date)
    TextView mStartDate;
    @BindView(R.id.end_date)
    TextView mEndDate;
    @BindView(R.id.order_search_org)
    EditText orderSearchOrg;
    @BindView(R.id.spinner3)
    NiceSpinner spinner;
    @BindView(R.id.search_history)
    ImageButton searchHistory;
    @BindView(R.id.lv_tasksss)
    ListView lvTasksss;
    @BindView(R.id.apply_change)
    Button applyChange;
    FinanceAdapter mAdapter;
    List<Map<String, Object>> mapList = new ArrayList<>();
    List<String> spinnerList;
    List<String> applyList=new ArrayList<>();
    //private ArrayAdapter<String> spinnerAdapter;
    private int clickedItem=-1;
    private AlertDialog checkDetailDialog;
    private List<String>NiceSpinner;

    private int mode = 0;
    String remark=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesman_finance);
        ButterKnife.bind(this);
        initView();
        mode = 0;
        applyChange.setVisibility(View.VISIBLE);
        mAdapter = new FinanceAdapter(SalesmanFinance.this, mapList, mode);
        lvTasksss.setAdapter(mAdapter);
        loadData();
        //    loadData();
    }

    private void initView() {
        titleplain.setText("财务异动");
        spinnerList = new ArrayList<>();
        spinnerList.add("可申请财务异动");
        spinnerList.add("已申请财务异动");
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
       // spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
       // spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        //spinner.setAdapter(spinnerAdapter);
        NiceSpinner = new LinkedList<String>(spinnerList);
        spinner.attachDataSource(NiceSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (NiceSpinner.get(position).equals("可申请财务异动")) {
                    mode = 0;
                    applyChange.setVisibility(View.VISIBLE);
                    mAdapter = new FinanceAdapter(SalesmanFinance.this, mapList, mode);
                    lvTasksss.setAdapter(mAdapter);
                    loadData();
                } else {
                    mode = 1;
                     applyChange.setVisibility(View.GONE);
                    mAdapter = new FinanceAdapter(SalesmanFinance.this, mapList, mode);
                    lvTasksss.setAdapter(mAdapter);
                    loadData();
                    mAdapter.setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickedItem=(Integer) v.getTag();
                            View checkView=View.inflate(SalesmanFinance.this,R.layout.finance_check_detail,null);
                            initCheckDetailView(checkView);
                            checkDetailDialog=new AlertDialog.Builder(SalesmanFinance.this).setView(checkView).create();
                            checkDetailDialog.show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadData() {
        mapList.clear();
        final Map<String, Object> map = new HashMap<>();
        map.put("option", String.valueOf(mode));
        if (mStartDate.getText() != null) {
            map.put("startDate", mStartDate.getText().toString());
        }
        if (mEndDate.getText() != null) {
            map.put("endDate", mEndDate.getText().toString());
        }
        if (orderSearchOrg.getText() != null && !orderSearchOrg.getText().toString().equals("")) {
            map.put("orderOrg", orderSearchOrg.getText().toString());
        }
        ApiService.GetString(this, "searchFinancialException", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response != null) {
                    try {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) JSON.parse(response);
                        if (list != null) {
                            for (Map<String, Object> item : list) {
                                mapList.add(item);
                            }
                            Log.i("financeMap", mapList.toString());
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        showToastShort(response);
                    }
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                showToastShort(e.getMessage());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });

    }

    @OnClick({R.id.titleback, R.id.start_date, R.id.end_date, R.id.search_history,R.id.apply_change})
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
                loadData();
                break;
            case R.id.apply_change:
                applyChange();
                break;
        }
    }

    protected void applyChange() {
         remark=null;
        final HashMap<Integer,Boolean> selectedMap=mAdapter.getIsSelected();
        if (!selectedMap.containsValue(true)||selectedMap.isEmpty()){
            showToastShort("未选择可申请财务异动");
        }else{
            EditText et=new EditText(SalesmanFinance.this);
            et.setHint("输入申请财务异动理由");
            AlertDialog alertDialog=new AlertDialog.Builder(this).setTitle("申请理由").setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (et.getText()!=null){
                        remark=et.getText().toString();
                    }

                    commitFinanceChange(selectedMap);
                    dialog.dismiss();
                }
            }).create();
            alertDialog.show();

        }

    }
    private void commitFinanceChange(HashMap<Integer,Boolean> selectedMap){
         List<String> applyDeviceIdList=new ArrayList<>();
        Iterator iterator=selectedMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,Boolean> entry=(Map.Entry<Integer,Boolean>)iterator.next();
           Log.i("entry",entry.getKey()+entry.getValue().toString());
            if (entry.getValue()){
                Log.i("添加",entry.getValue()+mapList.get(entry.getKey()).get("deviceDetailID").toString());
                applyDeviceIdList.add(mapList.get(entry.getKey()).get("deviceDetailID").toString());
            }
        }
        Map<String,Object> p=new HashMap<>();
        p.put("list",JSON.toJSONString(applyDeviceIdList));
        p.put("remark",remark);
        ApiService.GetString(this, "applyException", p, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("申请成功")||response.equals("所选设备均已提交财务异动申请，请等待审核结果")){
                    for (String applyId:applyDeviceIdList){
                        for (Map<String,Object> item:mapList){
                            if (applyId.equals(item.get("deviceDetailID").toString())){
                                mapList.remove(item);
                            }
                        }
                    }
                    mAdapter.setIsSelected(new HashMap<Integer, Boolean>());

                    mAdapter.notifyDataSetChanged();
                }
                showToastShort(response);
            }
            @Override
            public void onError(Object tag, Throwable e) {

                showToastShort(e.getMessage());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });


    }
    public void initCheckDetailView(View view){
        ImageButton exit=view.findViewById(R.id.exit_check_detail);
        TextView applyPerson=view.findViewById(R.id.app_person);
        TextView checkPerson=view.findViewById(R.id.check_person);
        TextView applyTime=view.findViewById(R.id.apply_time);
        TextView checkTime=view.findViewById(R.id.finance_check_time);
        TextView result=view.findViewById(R.id.check_result);
        TextView rejectReason=view.findViewById(R.id.reject_reason);
        TextView applyReason=view.findViewById(R.id.apply_reason);
        LinearLayout rejectLayout=view.findViewById(R.id.reject_layout);
        applyPerson.setText(mapList.get(clickedItem).get("applicantId").toString());
        applyTime.setText(mapList.get(clickedItem).get("applicantTime").toString());
        if (mapList.get(clickedItem).get("approvalId")!=null)
            checkPerson.setText(mapList.get(clickedItem).get("approvalId").toString());
        checkTime.setText(mapList.get(clickedItem).get("approvalTime").toString());
        result.setText(mapList.get(clickedItem).get("applicationStatus").toString());
        if (mapList.get(clickedItem).get("applicationRemark")!=null){
            applyReason.setText(mapList.get(clickedItem).get("applicationRemark").toString());
        }
        if (mapList.get(clickedItem).get("rejectReason")!=null&&!mapList.get(clickedItem).get("rejectReason").toString().equals("")){
            rejectReason.setText(mapList.get(clickedItem).get("rejectReason").toString());
        }else{
            rejectLayout.setVisibility(View.GONE);
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDetailDialog.dismiss();
            }
        });
    }
    protected void showToastShort(String mStr) {
        Toast.makeText(this, mStr, Toast.LENGTH_SHORT).show();
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
                if (code == 1)
                    mStartDate.setText(getTime(date));
                else
                    mEndDate.setText(getTime(date));

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
