package com.example.frank.jinding.UI.DepartorActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Adapter.OrderMapAdapter;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.SalesmanActivity.AddOrderInformation;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseChecker extends AppCompatActivity {
    List<Map<String, Object>> workerTaskList;
    List<String> orderIds;
    List<String> workerIdList;
    List<String> workerNameList;
    ArrayList<String> checkerList;
    ArrayList<String> checkerIdList;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.title_button)
    Button titleButton;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.icon_second_checker)
    ImageView iconSecondChecker;
    @BindView(R.id.checker_lv)
    ListView checkerLv;
    @BindView(R.id.recent_submission_lv)
    ListView recentSubmissionLv;
    @BindView(R.id.task_layout)
    LinearLayout taskLayout;
    List<Boolean> isSelected;
    QAdapter adapter;
    String mainCheckerId = null;
    String mainCheckerName = null;
    String checkerName = "";
    private  int REQUEST_CODE=0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_checker);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        initData(intent);
        isSelected = new ArrayList<>();
        workerIdList = new ArrayList<>();
        workerNameList = new ArrayList<>();
        checkerIdList = new ArrayList<>();
        adapter = new QAdapter(this);
        adapter.listItem = workerNameList;
        checkerLv.setAdapter(adapter);
        getWorkList();
        checkerLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if (REQUEST_CODE==0x11){
                //派工时显示近期任务
                  Log.i("点击检验员", position + "");
                getWorkerDetail(workerIdList.get(position));}

            }
        });
    }

    private void initData(Intent intent) {
     if(intent.getIntExtra("requestCode",0x11)== AddOrderInformation.REQUEST_CODE){
         REQUEST_CODE=intent.getIntExtra("requestCode",0x11);
         taskLayout.setVisibility(View.GONE);
         checkerLv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
     }
    }

    private void getWorkerDetail(String workerId) {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final AlertDialog processDialog = new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        workerTaskList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("workerId", workerId);
        ApiService.GetString(this, "getWorkerDetail", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Log.i(TAG, "获取工人任务数据成功" + response);
                if (response != null && TextUtils.isEmpty(response)) {

                } else {
                    JSONArray jsonArray = JSON.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderOrg", jsonObject.get("orderOrg").toString());
                        map.put("projectAddress", jsonObject.get("projectAddress").toString());
                        map.put("actrualDate", jsonObject.get("actrualDate").toString());
                        map.put("status", jsonObject.get("status").toString());
                        workerTaskList.add(map);
                    }
                    processDialog.dismiss();
                    if (workerTaskList.size() == 0) {
                        new AlertDialog.Builder(ChooseChecker.this).setTitle("该检验员暂时没有任务").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    } else {
                        OrderMapAdapter adapter = new OrderMapAdapter(ChooseChecker.this, workerTaskList);
                        recentSubmissionLv.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onError(Object tag, Throwable e) {
                processDialog.dismiss();
                Toast.makeText(ChooseChecker.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    private void getWorkList() {

        Map<String, Object> map = new HashMap<>();
        ApiService.GetString(this, "getWorkerList", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response != null && !TextUtils.isEmpty(response)) {
                    Log.i(TAG, "获取检验员成功");
                    JSONArray jsonArray = JSON.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        workerIdList.add(jsonObject.get("userId").toString());
                        workerNameList.add(jsonObject.get("userName").toString());
                        isSelected.add(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                //Toast.makeText(ChooseChecker.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("isCommited", false);
        setResult(REQUEST_CODE , intent);
        finish();
    }

    private void submit() {
        checkerList = new ArrayList<>();
        checkerName="";
         boolean firstChecker=true;
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i)) {
                checkerIdList.add(workerIdList.get(i));
                checkerList.add(workerNameList.get(i));
                if (!firstChecker){
                checkerName +=",";
                }
                checkerName+=workerNameList.get(i) ;
                firstChecker=false;
            }

        }
        if (checkerList.size() != 0) {
            if (REQUEST_CODE==0x11) {
                //派工选择检验员
                Log.i("chooseChecker",checkerList.size()+"");
                final AlertDialog dialog = new AlertDialog.Builder(ChooseChecker.this).create();
                final RadioGroup radioGroup = new RadioGroup(ChooseChecker.this);
                radioGroup.setOrientation(LinearLayout.VERTICAL);
                for (int i = 0; i < checkerList.size(); i++) {
                    RadioButton radioButton = new RadioButton(ChooseChecker.this);
                    radioButton.setText(checkerList.get(i));
                    radioButton.setHeight(100);
                    radioButton.setWidth(500);
                    radioButton.setId(i);
                    radioGroup.addView(radioButton);
                }
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("选择检验组长");
                dialog.setView(radioGroup);
                dialog.setIcon(R.mipmap.main_checker);
                radioGroup.setGravity(Gravity.CENTER);
                radioGroup.setPadding(5, 0, 0, 0);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.i("radioGroup", "选择" + checkedId);
                        mainCheckerName = checkerList.get(group.getCheckedRadioButtonId());
                        mainCheckerId = checkerIdList.get(group.getCheckedRadioButtonId());
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("isCommited", true);
                        intent.putStringArrayListExtra("checkerId", checkerIdList);
                        intent.putExtra("checkerName", checkerName);
                        intent.putExtra("mainCheckerId", mainCheckerId);
                        intent.putExtra("mainCheckerName", mainCheckerName);
                        setResult(REQUEST_CODE, intent);
                        finish();
                    }
                });
                dialog.show();
            }else{
                //添加订单选择期望检验员
                Intent intent = new Intent();
                intent.putExtra("isCommited", true);
                intent.putExtra("checkerName", checkerName);
                setResult(REQUEST_CODE, intent);
                finish();
            }
        }

    }

    public class QAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<String> listItem = new ArrayList<>();
        private ChooseChecker.ViewHolder holder;

        public QAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listItem.size();
        }

        @Override
        public Object getItem(int position) {
            return listItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.checker_item, null);
                holder = new ChooseChecker.ViewHolder();
                holder.checkerNam = (TextView) convertView.findViewById(R.id.text_checker_name);
                holder.selectinstu = (CheckBox) convertView.findViewById(R.id.checkBox_checker_select);
                convertView.setTag(holder);
            } else {
                holder = (ChooseChecker.ViewHolder) convertView.getTag();
            }
            holder.checkerNam.setText(listItem.get(position));

            holder.selectinstu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isSelected.set(position, isChecked);

                }
            });
            holder.selectinstu.setChecked(isSelected.get(position));

            return convertView;
        }
    }

    public final class ViewHolder {
        public TextView checkerNam;
        public CheckBox selectinstu;
    }

}
