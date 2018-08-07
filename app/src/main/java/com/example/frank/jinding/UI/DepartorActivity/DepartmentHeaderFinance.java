package com.example.frank.jinding.UI.DepartorActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Adapter.FinanceAdapter;
import com.example.frank.jinding.Log.L;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DepartmentHeaderFinance extends AppCompatActivity {

    @BindView(R.id.search_content_spinner)
    Spinner searchContentSpinner;
    @BindView(R.id.applicant_spinner)
    Spinner applicantSpinner;
    @BindView(R.id.search_history)
    ImageButton searchHistory;
    @BindView(R.id.lv_tasksss)
    ListView lvTasksss;
    FinanceAdapter mAdapter;
    ArrayAdapter mSearchContentAdapter;
    ArrayAdapter mApplicantAdapter;
    List<String>  mSearchContentList;
    List<Map<String, Object>> mapList;
    List<Applicant>mApplicantList;
    @BindView(R.id.titleplain)
    TextView titleplain;
    private int mode = 3;
    private boolean initFirst = true;
    private int checkItemPosition=-1;
    AlertDialog checkDetailDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_header_finance);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        titleplain.setText("财务异动");
        searchContentSpinner.setAdapter(mSearchContentAdapter);
        applicantSpinner.setAdapter(mApplicantAdapter);
        searchContentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (searchContentSpinner.getSelectedItem().toString().equals("待审核财务异动申请")) {
                    mode = 3;
                    mAdapter = new FinanceAdapter(DepartmentHeaderFinance.this, mapList, mode);
                    lvTasksss.setAdapter(mAdapter);
                    loadData();
                    mAdapter.setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("check","onClick");
                            checkItemPosition=(Integer) v.getTag();
                            checkFinance();
                        }
                    });
                    lvTasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            checkItemPosition=position;
                            //checkFinance();
                            checkDetail();
                        }
                    });
                } else if (searchContentSpinner.getSelectedItem().toString().equals("已审核财务异动历史")){
                    mode = 4;
                    mAdapter = new FinanceAdapter(DepartmentHeaderFinance.this, mapList, 4);
                    lvTasksss.setAdapter(mAdapter);
                    mAdapter.setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkItemPosition=(Integer) v.getTag();
                            Log.i("checkDetail","onClick");
                            checkDetail();

                        }
                    });
                    lvTasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            checkItemPosition=position;
                            checkDetail();
                        }
                    });
                    loadData();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        mSearchContentList = new ArrayList<>();
        mSearchContentList.add("待审核财务异动申请");
        mSearchContentList.add("已审核财务异动历史");
        mSearchContentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mSearchContentList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        mSearchContentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mApplicantList = new ArrayList<>();

        mapList = new ArrayList<>();

    }
    private void checkFinance(){
        TextView textView=new TextView(this);
        if (mapList.get(checkItemPosition).get("remark")!=null)
            textView.setText(mapList.get(checkItemPosition).get("remark").toString());
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("财务异动审核")
                .setView(textView)
                .setPositiveButton("通过", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkChange(1);
                    }
                }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkChange(0);
                    }
                }).create();
        dialog.show();
    }
    private void checkDetail(){
        View checkView=View.inflate(this,R.layout.finance_check_detail,null);
        initCheckDetailView(checkView);
        checkDetailDialog=new AlertDialog.Builder(this).setView(checkView).create();
        checkDetailDialog.show();
    }
    private void checkChange(int option) {
        EditText et=new EditText(this);
        String title="";
        if (option==0){
            et.setHint("输入拒绝理由");
            title="拒绝财务异动";
        }else{
            et.setHint("输入批准理由");
            title="批准财务异动";
        }

        AlertDialog dialog=new AlertDialog.Builder(this).setTitle(title).setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason=null;
                if (et.getText()!=null&&!et.getText().toString().equals("")){
                    reason=et.getText().toString();
                }
                commitCheckResult(option,reason);
                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }
    public void initCheckDetailView(View view){
        ImageButton exit=view.findViewById(R.id.exit_check_detail);
        TextView applyPerson=view.findViewById(R.id.app_person);
        TextView checkPerson=view.findViewById(R.id.check_person);
        TextView applyTime=view.findViewById(R.id.apply_time);
        TextView checkTime=view.findViewById(R.id.finance_check_time);
        TextView result=view.findViewById(R.id.check_result);
        TextView rejectReason=view.findViewById(R.id.reject_reason);
        LinearLayout rejectLayout=view.findViewById(R.id.reject_layout);
        TextView applyReason=view.findViewById(R.id.apply_reason);
        applyPerson.setText(mapList.get(checkItemPosition).get("applicantId").toString());
        applyTime.setText(mapList.get(checkItemPosition).get("applicantTime").toString());
        if (mapList.get(checkItemPosition).get("approvalId")!=null)
            checkPerson.setText(mapList.get(checkItemPosition).get("approvalId").toString());
        checkTime.setText(mapList.get(checkItemPosition).get("approvalTime").toString());
        result.setText(mapList.get(checkItemPosition).get("applicationStatus").toString());
        if (mapList.get(checkItemPosition).get("applicationRemark")!=null){
            applyReason.setText(mapList.get(checkItemPosition).get("applicationRemark").toString());
        }
        if (mapList.get(checkItemPosition).get("rejectReason")!=null&&!mapList.get(checkItemPosition).get("rejectReason").toString().equals("")){
            rejectReason.setText(mapList.get(checkItemPosition).get("rejectReason").toString());
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
    private void commitCheckResult(int result,String reason){
        Map<String,Object> map=new HashMap<>();
        map.put("exceptionId",mapList.get(checkItemPosition).get("exceptionId"));
        map.put("deviceDetailID",mapList.get(checkItemPosition).get("deviceDetailID"));
        map.put("applicationStatus",result+"");
        map.put("rejectReason",reason);
        Map<String,Object> p=new HashMap<>();
        p.put("map", JSON.toJSONString(map));
        ApiService.GetString(this, "checkException", p, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response!=null&&!response.equals("")){
                    try {
                        if (response.equals("审核成功")) {
                            mapList.remove(checkItemPosition);
                            checkItemPosition = -1;
                            mAdapter.notifyDataSetChanged();
                        }
                        showToastShort(response);
                    }
                    catch (JSONException e) {
                        Log.i(TAG, "onNext: "+e.getMessage());
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadData() {
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final AlertDialog processDialog=new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();

        mapList.clear();
        Map<String, Object> para = new HashMap<>();
        para.put("option", mode-3);
        if (applicantSpinner.getSelectedItem() != null && !applicantSpinner.getSelectedItem().equals("")) {
            Applicant applicant=mApplicantList.stream().filter((item)->item.getName().equals(applicantSpinner.getSelectedItem().toString())).findFirst().orElse(null);
            para.put("applicant_id", applicant.getId());
        }
        ApiService.GetString(this, "searchReviewExceptionResult", para, new RxStringCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(Object tag, String response) {
                if (response != null && !response.equals("")) {

                    try {
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        if (response.contains("financialList")){
                            List<Map<String, Object>> financialTmp = (List<Map<String, Object>>) jsonObject.get("financialList");
                            if (financialTmp != null&&financialTmp.size()>0) {
                                for (Map<String, Object> item : financialTmp)
                                    mapList.add(item);
                            } else {
                                showToastShort("暂无待审核财务异动");
                            }
                        }
                        if (initFirst&&response.contains("manList")) {
                            L.e("执行了业务员查询方法");
                            initFirst = false;
                            List<Map<String,Object>> salesmanTmp = (List<Map<String,Object>>)jsonObject.get("manList");
                            L.e("执行了业务员查询大小："+salesmanTmp.size());
                            mApplicantList.add(new Applicant("",""));
                            for (int i=0;i<salesmanTmp.size();i++) {
                                Map<String,Object> item=salesmanTmp.get(i);
                                if (item!=null&&!item.equals("")&&!item.equals("null")&&item.get("name")!=null) {
                                    L.e("获得检验员信息："+item.toString());
                                    mApplicantList.add(new Applicant(item.get("name").toString(), item.get("id").toString()));
                                }
                                else
                                    continue;
                            }
                            mApplicantAdapter = new ArrayAdapter<String>(DepartmentHeaderFinance.this, android.R.layout.simple_spinner_item, mApplicantList.stream().map((item)->item.getName()).collect(Collectors.toList()));
                            //第三步：为适配器设置下拉列表下拉时的菜单样式。
                            mApplicantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            applicantSpinner.setAdapter(mApplicantAdapter);
                            mApplicantAdapter.notifyDataSetChanged();
                        }

                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.i(TAG, "onNext: "+e.getMessage());
                        showToastShort(response);
                    }

                }else {
                    showToastShort("暂无待审核财务异动");
                }
                processDialog.dismiss();
            }

            @Override
            public void onError(Object tag, Throwable e) {
                showToastShort(e.getMessage());
                processDialog.dismiss();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                processDialog.dismiss();
            }
        });
    }


    public void showToastShort(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.titleback, R.id.search_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleback:
                finish();
                break;
            case R.id.search_history:
                loadData();
                break;
        }
    }
    private class Applicant{

        String name;
        String id;


        public Applicant() {
        }

        public Applicant(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
