package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.Bean.Instrument.InstrumentStatus;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpotSelect extends AppCompatActivity {

    private Button scanselect, inputnumber, btn_submit;
    private ImageView image_back;
    private ListView lv_tasksss;
    private QAdapter instrumentAdapter;
    private ArrayList<Map<String, String>> isSelectedList = new ArrayList<>();
    private int[] statusList = new int[40];
    private int index = 0;
    private Gson gson = new Gson();
    private Gson gsonContainTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_select);
        initData();
        initView();
        initListener();
    }

    private void initData() {

        if (getIntent().getStringExtra("selectList") != null) {
            ArrayList<Map<String, String>> list = gson.fromJson(getIntent().getStringExtra("selectList"), new TypeToken<ArrayList<Map<String, String>>>() {
            }.getType());
            for (Map<String, String> map : list) {
                isSelectedList.add(map);
            }
        }
        index = isSelectedList.size();
        for(int i=0;i<isSelectedList.size();i++){
            statusList[i]=1;
        }
    }

    private void initView() {
        image_back = (ImageView) this.findViewById(R.id.image_back);
        btn_submit = (Button) this.findViewById(R.id.btn_submit);

        scanselect = (Button) this.findViewById(R.id.scanselect);
        inputnumber = (Button) this.findViewById(R.id.inputnumber);

        lv_tasksss = (ListView) this.findViewById(R.id.lv_apply_instrment);
        instrumentAdapter = new QAdapter(this);
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            ArrayList<InstrumentStatus> list = new ArrayList<>();
            for (Map map : isSelectedList) {
                InstrumentStatus instrumentStatus = new InstrumentStatus(map.get("instrumentId").toString(),map.get("instrumentCode").toString(), map.get("instrumentType").toString(), sdf.parse(sdf.format(sdf1.parse(map.get("validateDate").toString()))), map.get("instrumentBoxCode").toString(), map.get("isUsing").toString(), map.get("isSubmitted").toString(), map.get("isBroken").toString());
                list.add(instrumentStatus);
            }
            instrumentAdapter.listItem = list;
        } catch (Exception e) {
            Toast.makeText(SpotSelect.this, "格式转换错误", Toast.LENGTH_LONG).show();
        }
        lv_tasksss.setAdapter(instrumentAdapter);
    }

    private void initListener() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectList", gson.toJson(isSelectedList));
                setResult(4, intent);
                finish();
            }
        });

        btn_submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectedList.size() > 0) {
                    refreshIsSelectList();
                    Intent intent = new Intent();
                    intent.putExtra("selectList", gson.toJson(isSelectedList));
                    setResult(4, intent);
                    finish();
                } else {
                    Toast.makeText(SpotSelect.this, "您还没有选取您要申领的仪器", Toast.LENGTH_LONG).show();
                }
            }
        });

        scanselect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpotSelect.this, com.example.frank.jinding.Zxing.activity.CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        inputnumber.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(SpotSelect.this);

                new AlertDialog.Builder(SpotSelect.this)
                        .setTitle("系统提示")
                        .setMessage("请输入仪器编号：")
                        .setView(editText)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("添加",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getInstrumentById(editText.getText().toString().trim());
                                    }
                                }).show();

            }
        });
    }

    private void getInstrumentById(String id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("instrument_id", id);
        ApiService.GetString(this, "getInstrumnetById", parameters, new RxStringCallback() {

            @Override
            public void onNext(Object tag, String response) {
                InstrumentStatus instrumentStatus;
                if (response.equals("notFound")) {
                    Toast.makeText(SpotSelect.this, "未找到该编号的仪器", Toast.LENGTH_LONG).show();
                } else {
                    instrumentStatus = gsonContainTime.fromJson(response, new TypeToken<InstrumentStatus>() {
                    }.getType());
                    Map<String, String> map = new HashMap<>();
                    map.put("Index", ++index + "");
                    map.put("instrumentId", instrumentStatus.getInstrumentId().toString());
                    map.put("instrumentCode", instrumentStatus.getInstrumentCode().toString());
                    map.put("instrumentType", instrumentStatus.getInstrumentType().toString());
                    map.put("validateDate", instrumentStatus.getValidateDate().toString());
                    map.put("instrumentBoxCode", instrumentStatus.getInstrumentBoxCode().toString());
                    map.put("isUsing", instrumentStatus.getIsUsing().toString());
                    map.put("isSubmitted", instrumentStatus.getIsSubmitted().toString());
                    map.put("isBroken", instrumentStatus.getIsBroken().toString());
                    boolean flag = false;
                    for (Map m : isSelectedList) {
                        if (m.get("instrumentCode").toString().equals(map.get("instrumentCode").toString())) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        instrumentAdapter.listItem.add(instrumentStatus);
                        instrumentAdapter.notifyDataSetChanged();
                        isSelectedList.add(map);
                        statusList[isSelectedList.size()]=1;
                        Toast.makeText(SpotSelect.this, "添加仪器成功", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(SpotSelect.this, "添加失败，该仪器已经选取了，不用再添加", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

        });
    }

    //去掉没有勾选的列表项
    private void refreshIsSelectList(){
        ArrayList<Map<String,String>> list_temp=new ArrayList<>();
        for(int i=0;i<isSelectedList.size();i++){
            if(statusList[i]==1){
                list_temp.add(isSelectedList.get(i));
            }
        }
        isSelectedList.clear();
        for(Map map:list_temp){
            isSelectedList.add(map);
        }
        list_temp.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            final String result = bundle.getString("result");
            new AlertDialog.Builder(SpotSelect.this)
                    .setTitle("扫描结果")
                    .setMessage("\n仪器编号：" + result)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(SpotSelect.this, "取消添加仪器", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setPositiveButton("添加",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getInstrumentById(result.trim());
                                }
                            }).show();

        }
    }

    public class QAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ArrayList<InstrumentStatus> listItem = new ArrayList<>();
        private ViewHolder holder = new ViewHolder();

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
                convertView = mInflater.inflate(R.layout.instrument_infomation_net, null);
                holder.instruNum = (TextView) convertView.findViewById(R.id.text_number_instru);
                holder.instruNam = (TextView) convertView.findViewById(R.id.text_name_instru);
                holder.selectinstu = (CheckBox) convertView.findViewById(R.id.checkBox_instru_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.instruNam.setText(listItem.get(position).getInstrumentType().toString());
            holder.instruNum.setText(listItem.get(position).getInstrumentCode().toString());

            holder.selectinstu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        statusList[position]=1;
                    }else{
                        statusList[position]=0;
                    }
                }
            });
            holder.selectinstu.setChecked(true);
            return convertView;
        }
    }

    public final class ViewHolder {
        public TextView instruNum, instruNam;
        public CheckBox selectinstu;
    }
}

