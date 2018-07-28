package com.example.frank.jinding.UI.DepartorActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.Adapter.OrderMapAdapter;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DispatchRecord extends AppCompatActivity {
    @BindView(R.id.submission_history_start_date)
    TextView submissionHistoryStartDate;
    @BindView(R.id.submission_history_org)
    EditText submissionHistoryOrg;
    @BindView(R.id.titleback)
    ImageButton titleback;
    @BindView(R.id.titleplain)
    TextView titleplain;
    @BindView(R.id.submission_history_end_date)
    TextView submissionHistoryEndDate;
    private ImageButton back;
    private TextView title;
    private RadioButton task, hadtask;
    private ListView lv_tasksss;
    private OrderMapAdapter mAdapter;
    private List<Map<String, Object>> submissionList;
    private  int startIndex=0;
    private  int numberShow=10;
    private  int firstVisibleItemTag=0;
    private static boolean requestFlag=false;
    private int totalItemFlag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_record);
        ButterKnife.bind(this);

        init();
        //标题栏设置
        title.setText("派工查看");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter = new OrderMapAdapter(this,submissionList);
        //得到一个MyAdapter对象
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter
        //判断订单是被拒绝还是接受

        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {



                Intent intent = new Intent(DispatchRecord.this,SubmissionOrderDetails.class);
                intent.putExtra("submission", (Serializable) submissionList.get(arg2));
                startActivity(intent);
            }
        });
        //长按删除
        lv_tasksss.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog deleteDialog = new AlertDialog.Builder(DispatchRecord.this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      deleteSubmission(position);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("确认删除派工单？").create();
                deleteDialog.show();
                return true;
            }
        });
        lv_tasksss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://停止滑动
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://正在滑动
                        break;
                    case SCROLL_STATE_FLING://滑动ListView离开后，由于惯性继续滑动

                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //用于底部加载更多数据的判断逻辑,在这个地方调用自己的方法请求网络数据，一次性请求10条或者15条等
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&totalItemCount>totalItemFlag) {
                    totalItemFlag=totalItemCount;
                    if (requestFlag){
                        requestFlag=false;
                        search(startIndex,numberShow);
                    }

                }

                //判断ListView的滑动方向
                if (firstVisibleItemTag == firstVisibleItem) {
                    Log.e("滑动分页：", "未发生滑动");
                } else if (firstVisibleItemTag > firstVisibleItem) {
                    Log.e("滑动分页：", "发生下滑");
                } else {
                    Log.e("滑动分页：", "发生上滑");
                }
                firstVisibleItemTag = firstVisibleItem;


            }
        });
    }
    private void deleteSubmission(final int  position){
        Map<String,Object> map=new HashMap<>();
        map.put("submissionId",submissionList.get(position).get("submissionId").toString());
        ApiService.GetString(this, "deleteSubmission", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response!=null&&response.equals("success")){
                Toast.makeText(DispatchRecord.this,"撤回成功",Toast.LENGTH_SHORT).show();
                    submissionList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Object tag, Throwable e) {
             Toast.makeText(DispatchRecord.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }


    private void init() {

        back = (ImageButton) this.findViewById(R.id.titleback);
        title = (TextView) this.findViewById(R.id.titleplain);

        lv_tasksss = (ListView) this.findViewById(R.id.lv_order_recorde);
        submissionList = new ArrayList<>();

    }

    @OnClick({R.id.submission_history_start_date, R.id.submission_history_end_date, R.id.submission_history_org, R.id.search_submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submission_history_start_date:
                chooseDate(1);
                break;
            case R.id.submission_history_end_date:
                chooseDate(2);
                break;
            case R.id.submission_history_org:
                break;
            case R.id.search_submission:
                search(startIndex,numberShow);
                break;

        }
    }
    protected void search(int startIndexPos,int numberShowSum){
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final AlertDialog processDialog=new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String,Object> data=new HashMap<>();
        if (submissionHistoryStartDate.getText()!=null)
            data.put("ActualStart",submissionHistoryStartDate.getText().toString());
        if (submissionHistoryEndDate.getText()!=null)
            data.put("ActualEnd",submissionHistoryEndDate.getText().toString());
        if (submissionHistoryOrg.getText()!=null)
            data.put("orderOrg",submissionHistoryOrg.getText().toString());
        data.put("startIndex",startIndexPos);
        data.put("number",numberShowSum);
            Map<String,Object> map=new HashMap<>();
            map.put("data", JSON.toJSONString(data));

            ApiService.GetString(this, "getSubmissionOrder", map, new RxStringCallback() {
                @Override
                public void onNext(Object tag, String response) {

                    //submissionList.clear();
                    processDialog.dismiss();
                    if (response!=null&&!response.equals("")){
                        startIndex=startIndex+numberShow;
                        requestFlag=true;
                        JSONArray jsonArray=JSON.parseArray(response);
                        for (Object object:jsonArray){
                            JSONObject jsonObject=(JSONObject)object;
                            Map<String,Object>submission=new HashMap<>();
                            submission.put("status",jsonObject.get("sub_status").toString());
                            submission.put("submissionId",jsonObject.get("submissionId").toString());
                            submission.put("confirmedPerson",jsonObject.get("confirmedPerson"));
                            submission.put("rejectPerson",jsonObject.get("rejectPerson"));
                            submission.put("waitingPerson",jsonObject.get("waitingPerson"));
                            submission.put("submitor",jsonObject.get("submitor"));
                            submission.put("submitTime",jsonObject.getString("submitTime"));
                            submission.put("orderOrg",jsonObject.getString("orderOrg"));
                            submission.put("mainChecker",jsonObject.getString("mainChecker"));
                            submission.put("checkerIds",jsonObject.getString("checkerIds"));
                            submission.put("checkers",jsonObject.getString("checkers"));
                            submission.put("actrualDate",jsonObject.getString("dispatchTime"));
                            Log.i("派工时间",submission.get("actrualDate").toString());
                            submission.put("projectAddress",jsonObject.get("projectAddress"));
                            submissionList.add(submission);
                            mAdapter.notifyDataSetChanged();
                        }


                    }else {
                        processDialog.dismiss();
                        Toast.makeText(DispatchRecord.this,"派工信息为空",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    processDialog.dismiss();
                 Toast.makeText(DispatchRecord.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Object tag, Throwable e) {

                }
            });
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
                    submissionHistoryStartDate.setText(getTime(date));
                else
                    submissionHistoryEndDate.setText(getTime(date));

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
