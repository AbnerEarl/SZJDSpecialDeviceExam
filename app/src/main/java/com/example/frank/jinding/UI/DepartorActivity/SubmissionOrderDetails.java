package com.example.frank.jinding.UI.DepartorActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubmissionOrderDetails extends AppCompatActivity {

    @BindView(R.id.comapany_name)
    TextView companyName;
    @BindView(R.id.check_time)
    TextView checkTime;
    @BindView(R.id.mian_checker)
    TextView mainChecker;
    @BindView(R.id.checkers)
    TextView checkers;
    @BindView(R.id.submitor)
    TextView submitor;
    @BindView(R.id.submit_time)
    TextView submitTime;
    @BindView(R.id.confirm_checkers)
    TextView confirmCheckers;
    @BindView(R.id.reject_checkers)
    TextView rejectCheckers;
    @BindView(R.id.wait_confirm_checkers)
    TextView waitConfirmCheckers;
    private final static String TAG = "SubmissionOrderDetails";
    List<String> workerIdList;
    List<String> workerNameList;
    ArrayList<String> checkerList;
    ArrayList<String> checkerIdList;
    @BindView(R.id.update_check_time)
    ImageButton updateCheckTime;
    @BindView(R.id.update_checkers)
    ImageButton updateCheckers;
    @BindView(R.id.update_main_checker)
    ImageButton updateMainChecker;
    @BindView(R.id.update_submission)
    Button updateSubmission;
    private int checkerNum = 0;
    private boolean VISIBLE=false;
    private String mainCheckerId;
    private String submissionId;
    private ListView listView;
    private boolean isVISIBLE=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_order_details);
        ButterKnife.bind(this);
        //tasklistView=new ListView(this);
        initView();
        initData();
    }

    private void initView() {

    updateCheckers.setVisibility(View.INVISIBLE);
    updateMainChecker.setVisibility(View.INVISIBLE);
    updateCheckTime.setVisibility(View.INVISIBLE);
    updateSubmission=(Button)findViewById(R.id.update_submission);

    updateSubmission.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isVISIBLE==false)
                Toast.makeText(SubmissionOrderDetails.this, "此派工单不可修改",Toast.LENGTH_SHORT).show();
            if (VISIBLE){
                submitUpdate();

            }else {
                VISIBLE=true;
                updateCheckTime.setVisibility(View.VISIBLE);
                updateSubmission.setText("立即提交");
                Intent intent=new Intent(SubmissionOrderDetails.this,ChooseChecker.class);
                startActivityForResult(intent,0x11);

            }
        }
    });
    }

    private void initData() {
        Intent intent = getIntent();
        Map<String, Object> submissionDetails = (Map<String, Object>) intent.getSerializableExtra("submission");
        submissionId=submissionDetails.get("submissionId").toString();
        companyName.setText(submissionDetails.get("orderOrg").toString());
        checkTime.setText(submissionDetails.get("actrualDate").toString());
        mainChecker.setText(submissionDetails.get("mainChecker").toString());
        JSONArray checkerArray=JSON.parseArray(submissionDetails.get("checkers").toString());
        StringBuilder checkerStr=new StringBuilder();
        for (Object item:checkerArray){
            checkerStr.append(item+" ");
        }
        checkers.setText(checkerStr);
        //检验员id
        Log.i(TAG, submissionDetails.get("checkerIds").toString());
        submitor.setText(submissionDetails.get("submitor").toString());
        submitTime.setText(submissionDetails.get("submitTime").toString());
        if (submissionDetails.get("confirmedPerson") != null)
            confirmCheckers.setText(submissionDetails.get("confirmedPerson").toString());
        if (submissionDetails.get("rejectPerson") != null) {
            rejectCheckers.setText(submissionDetails.get("rejectPerson").toString());
            }
        if(submissionDetails.get("confirmedPerson").toString().trim().length()>1||submissionDetails.get("rejectPerson").toString().trim().length()>1){
            isVISIBLE=false;

        }
        updateSubmission.setClickable(isVISIBLE);
        Log.i("isVisble",String.valueOf(isVISIBLE));



        if (submissionDetails.get("waitingPerson") != null) {
            waitConfirmCheckers.setText(submissionDetails.get("waitingPerson").toString());

        }
        checkerList=new ArrayList<>();
    }

    protected void chooseDate() {
//控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        final Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        final Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 11, 28);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                checkTime.setText(getTime(date));
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

    @OnClick({R.id.update_check_time, R.id.update_checkers, R.id.update_main_checker,R.id.update_submission,R.id.image_back})

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_check_time:
                chooseDate();
                break;
            case R.id.update_checkers:
                break;
            case R.id.update_main_checker:
                break;
                case R.id.image_back:
                finish();
                default:
                    break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0x11&&data.getBooleanExtra("isCommited",false)){
            checkerIdList=data.getStringArrayListExtra("checkerId");
            mainCheckerId=data.getStringExtra("mainCheckerId");
            checkers.setText(data.getStringExtra("checkerName"));
            mainChecker.setText(data.getStringExtra("mainCheckerName"));
        }
    }
    private void submitUpdate() {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();

        if (checkers.getText() == null || TextUtils.isEmpty(checkers.getText())) {
            Toast.makeText(SubmissionOrderDetails.this, "请选择检验员", Toast.LENGTH_SHORT).show();

        } else if (mainChecker.getText() == null || TextUtils.isEmpty(mainChecker.getText().toString())) {
            Toast.makeText(SubmissionOrderDetails.this, "请选择检验组长", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList worker = new ArrayList();
            Map<String, Object> data = new HashMap<>();
            data.put("submissionId", submissionId);
            data.put("mainWorker", mainCheckerId);
            for (int i = 0; i < checkerIdList.size(); i++) {
                if (!mainCheckerId.equals(checkerIdList.get(i))) {
                    worker.add(checkerIdList.get(i));
                }
            }
            Log.i(TAG, "提交主检验员id" + mainCheckerId);
            Log.i(TAG, "提交检验员id" + worker.toString());
            data.put("worker", JSON.toJSONString(worker));
            data.put("date", checkTime.getText().toString());

            Map<String, Object> map = new HashMap<>();
            map.put("data", JSON.toJSONString(data));
            ApiService.GetString(this, "setSubmissionById", map, new RxStringCallback() {
                @Override
                public void onNext(Object tag, String response) {
                    if (response != null && response.equals("success")) {
                        processDialog.dismiss();
                        Toast.makeText(SubmissionOrderDetails.this, "修改成功", Toast.LENGTH_SHORT).show();
                        initView();
                        updateSubmission.setText("修改派工");
                        VISIBLE = false;
                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(SubmissionOrderDetails.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Object tag, Throwable e) {

                }
            });
        }


    }
}
