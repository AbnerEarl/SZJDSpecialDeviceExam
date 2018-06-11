package com.example.frank.jinding.UI.DepartorActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
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

public class Dispatching extends AppCompatActivity implements View.OnClickListener{

    private Button add, clear, comfirm,addTimeBtn;
    private TextView otherem;
    private Spinner  mCheckTypeSpinner;
    private TextView mainCheckerTv;
    private TextView checkTime;
    private ListView  tasklistView;
    //private ArrayAdapter<String> adapter, checkTypeAdapter,mainWorkerAdapter;
    private ImageButton back;
    private TextView titleplain;
    List<String> orderIds;
    ArrayList<String> checkerList=new ArrayList<>();
    ArrayList<String> checkerIdList=new ArrayList<>();
    private String mainCheckerId;
    //private String checkType;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0x11&&data.getBooleanExtra("isCommited",false)){
            mainCheckerTv.setText(null);
            otherem.setText(null);
            checkerIdList=data.getStringArrayListExtra("checkerId");
            mainCheckerId=data.getStringExtra("mainCheckerId");
            otherem.setText(data.getStringExtra("checkerName"));
            mainCheckerTv.setText(data.getStringExtra("mainCheckerName"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching);
        final Intent intent = getIntent();
        orderIds = intent.getStringArrayListExtra("dispatchingNumber");
        Log.i("orderIds", orderIds.toString());
        init();
        checkerList = new ArrayList<>();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Dispatching.this, ChooseChecker.class);
               intent1.putExtra("requestCode",0x11);
                startActivityForResult(intent1, 0x11);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherem.setText(null);
                checkerList.clear();
                checkerIdList.clear();
                mainCheckerId = "";
                mainCheckerTv.setText(null);
            }
        });


        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Dispatching.this)
                        .setTitle("系统提示")
                        .setMessage("您确定要提交此次派工信息吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        submit();
                                    }
                                }).show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleplain.setText("业务派工");

       /* final List<String> checkTypeList = new ArrayList<>();
        checkTypeList.add("定期检验");
        checkTypeList.add("安装改造（大修）验收检验");
        checkTypeList.add("复检");
        checkTypeList.add("其他检验");
        checkTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, checkTypeList);
        checkTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCheckTypeSpinner.setAdapter(checkTypeAdapter);
        mCheckTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            checkType=checkTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }*/
   /* private void getWorkerDetail(String workerId,final int arg2){
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final AlertDialog processDialog=new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
    workerTaskList=new ArrayList<>();
    Map<String,Object> map=new HashMap<>();
    map.put("workerId",workerId);
    ApiService.GetString(this, "getWorkerDetail", map, new RxStringCallback() {
        @Override
        public void onNext(Object tag, String response) {
            Log.i(TAG,"获取工人任务数据成功"+response);
            if (response!=null&& TextUtils.isEmpty(response)) {
            }else {
                JSONArray jsonArray=JSON.parseArray(response);
                for (Object object:jsonArray){
                    JSONObject jsonObject=(JSONObject) object;
                    Map<String,Object> map=new HashMap<>();
                    map.put("orderOrg",jsonObject.get("orderOrg").toString());
                    map.put("projectAddress",jsonObject.get("projectAddress").toString());
                    map.put("actrualDate",jsonObject.get("actrualDate").toString());
                    map.put("status",jsonObject.get("status").toString());
                    workerTaskList.add(map);
                }
                processDialog.dismiss();
             if (workerTaskList.size()==0){
                    new AlertDialog.Builder(Dispatching.this).setTitle("该检验员暂时没有任务").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
             }else {
                 taskAdapter=new MyAdapter(Dispatching.this,workerTaskList);
                 tasklistView.setAdapter(taskAdapter);
                 final AlertDialog dialog = new AlertDialog.Builder(Dispatching.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         ((ViewGroup)tasklistView.getParent()).removeView(tasklistView);
                         dialog.dismiss();
                     }
                 }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         ((ViewGroup)tasklistView.getParent()).removeView(tasklistView);
                         if (checkerNum == 3) {
                             Toast.makeText(Dispatching.this, "检验员人数已满", Toast.LENGTH_SHORT).show();
                             alertDialog.dismiss();
                             dialog.dismiss();

                         } else {
                             checkerList.add(workerNameList.get(arg2));
                             checkerIdList.add(workerIdList.get(arg2));
                             if (otherem.getText().toString().trim().equals("")) {
                                 otherem.setText(workerNameList.get(arg2));
                             } else {
                                 otherem.setText(otherem.getText() + "," + workerNameList.get(arg2));
                             }
                             workerNameList.remove(arg2);
                             workerIdList.remove(arg2);
                             dialog.dismiss();
                             adapter.notifyDataSetChanged();
                             checkerNum++;
                             if (checkerNum==3){
                                 alertDialog.dismiss();
                             }
                         }
                     }
                 }).create();
                 dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
                 dialog.setTitle("该检验员近期任务");
                 dialog.setIcon(R.mipmap.order);
                 dialog.setView(tasklistView);
                 dialog.show();

             }
            }

        }

        @Override
        public void onError(Object tag, Throwable e) {
            processDialog.dismiss();
            Toast.makeText(Dispatching.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(Object tag, Throwable e) {

        }
    });
    }*/
    }
    private void submit(){
        if (checkTime.getText()==null||mainCheckerTv.getText()==null||otherem.getText()==null||checkTime.getText().toString().isEmpty()||mainCheckerTv.getText().toString().isEmpty()||otherem.getText().toString().isEmpty()){
            Toast.makeText(Dispatching.this,"请完善派工信息",Toast.LENGTH_LONG).show();
        }else {
            ArrayList worker = new ArrayList();
            Map<String, Object> data = new HashMap<>();
            data.put("org", JSON.toJSONString(orderIds));
            data.put("mainWorker", mainCheckerId);
            for (int i = 0; i < checkerIdList.size(); i++) {
                if (!mainCheckerId.equals(checkerIdList.get(i))) {
                    worker.add(checkerIdList.get(i));
                }
            }
            data.put("worker",JSON.toJSONString(worker));
            Log.i("提交数据","5");
           // data.put("submissionType", checkType);
            data.put("date", checkTime.getText().toString());

            Map<String, Object> map = new HashMap<>();
            map.put("data", JSON.toJSONString(data));
            Log.i("添加派工数据", map.get("data").toString());
            ApiService.GetString(this, "setSubmission", map, new RxStringCallback() {
                @Override
                public void onNext(Object tag, String response) {
                    Log.i("提交成功",response);
                    if (response.equals("success")) {
                        Toast.makeText(Dispatching.this, "派工成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("isCommit", true);

                        intent.putStringArrayListExtra("dispatchingNumber", (ArrayList<String>) orderIds);
                        setResult(0x01, intent);
                        finish();
                    }else {
                        Toast.makeText(Dispatching.this,"提交失败", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(Dispatching.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Object tag, Throwable e) {

                }
            });
        }
    }

  /*  private void getWorkList() {
    workerIdList=new ArrayList<>();
    workerNameList=new ArrayList<>();
    checkerIdList=new ArrayList<>();
    Map<String,Object> map=new HashMap<>();
        ApiService.GetString(this, "getWorkerList", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Log.i(TAG,"获取检验员成功");
                JSONArray jsonArray= JSON.parseArray(response);
                for (Object object:jsonArray){
                    JSONObject jsonObject=(JSONObject)object;
                    workerIdList.add(jsonObject.get("userId").toString());
                    workerNameList.add(jsonObject.get("userName").toString());
                }
                adapter=new ArrayAdapter<String>(Dispatching.this,android.R.layout.simple_list_item_1,workerNameList);
                showMyDialog();
            }

            @Override
            public void onError(Object tag, Throwable e) {
              //Toast.makeText(Dispatching.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }*/

    private void init() {
        add = (Button) this.findViewById(R.id.button49);
        clear = (Button) this.findViewById(R.id.button48);
        comfirm = (Button) this.findViewById(R.id.button50);
        addTimeBtn=(Button)this.findViewById(R.id.btn_add_time);
        otherem = (TextView) this.findViewById(R.id.textView27);
        checkTime=(TextView)this.findViewById(R.id.textview_showtime);
        mainCheckerTv = (TextView) findViewById(R.id.spinner2);
        addTimeBtn.setVisibility(View.INVISIBLE);
      //  mCheckTypeSpinner = (Spinner) findViewById(R.id.check_type_spinner);
        back = (ImageButton) findViewById(R.id.titleback);
        titleplain = (TextView) findViewById(R.id.titleplain);
        checkTime.setOnClickListener(this);
         tasklistView= new ListView(Dispatching.this);
    }


   /* private void showMyDialog() {

         listView=new ListView(this);

        listView.setAdapter((ListAdapter) adapter);

         alertDialog = new AlertDialog.Builder(this).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        alertDialog.setTitle("请选择副检验员");
        alertDialog.setIcon(R.mipmap.second_checker);
        alertDialog.setView(listView);
        alertDialog.show();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                {
                    // TODO Auto-generated method stub
                    final TextView tv = (TextView) arg1.findViewById(R.id.textView28);//取得每条item中的textview控件

                    getWorkerDetail(workerIdList.get(arg2),arg2);

                }
            }
        });
    }
*/
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("isCommit", false);
        setResult(0x01, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview_showtime:
                chooseDate();
                break;
                default:
                    break;
        }
    }
    protected void chooseDate() {
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


}
