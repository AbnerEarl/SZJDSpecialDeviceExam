package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.frank.jinding.Bean.Instrument.InstrumentStatus;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.Utils.ClickUtils;
import com.example.frank.jinding.Utils.JsonTimeUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApplyInstrument extends AppCompatActivity {

    private ImageView image_back;
    private Button btn_submit, btn_title;
    private ListView listview_instrument;
    private Adapter_instrument adapter_instrument;
    private ArrayList<Map<String, String>> isSelectedList = new ArrayList<>();
    private int[] statusList = new int[40];
    private Gson gson = new Gson();
    private Gson gsonContainTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Button netselect, spotselect;
    private ArrayList<String> selectOrderId = new ArrayList<>();
    //表示此次提交是否为    修改之前申请的仪器，若是则为true
    private boolean isRevising;
    private boolean isNotModify;
    //记录此订单是否为复检
    private HashMap<String,String> orderStatusMap=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_instrument);
        initData();
        initView();
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            isSelectedList = gson.fromJson(data.getStringExtra("selectList"), new TypeToken<ArrayList<Map<String, String>>>() {
            }.getType());
            refreshDataIndex();
            adapter_instrument.listitem = isSelectedList;
            adapter_instrument.notifyDataSetChanged();
            for (int i = 0; i < isSelectedList.size(); i++) {
                statusList[i] = 1;
            }
        }
        if (requestCode == 3 && resultCode == 4) {
            isSelectedList = gson.fromJson(data.getStringExtra("selectList"), new TypeToken<ArrayList<Map<String, String>>>() {
            }.getType());
            refreshDataIndex();
            adapter_instrument.listitem = isSelectedList;
            adapter_instrument.notifyDataSetChanged();
            for (int i = 0; i < isSelectedList.size(); i++) {
                statusList[i] = 1;
            }
        }
    }

    private void initData() {
        Bundle b = this.getIntent().getExtras();
        selectOrderId = b.getStringArrayList("orderIdList");
        //Toast.makeText(ApplyInstrument.this, selectOrderId.toString(), Toast.LENGTH_LONG).show();
        adapter_instrument = new Adapter_instrument(this);
        if (getIntent().getStringExtra("isNotModify").equals("false")){
            isNotModify=false;
        }else {
            isNotModify=true;
        }

        orderStatusMap=gson.fromJson(getIntent().getStringExtra("isRecheck"),new TypeToken<HashMap<String,String>>(){}.getType());
        if (getIntent().getStringExtra("type").equals("已申领仪器订单")) {
            isRevising = true;
        }else{
            isRevising=false;
        }
    }

    private void initView() {
        image_back = (ImageView) this.findViewById(R.id.image_back);
        btn_submit = (Button) this.findViewById(R.id.btn_submit);
        btn_title = (Button) this.findViewById(R.id.title_button);
        netselect = (Button) this.findViewById(R.id.net_select);
        spotselect = (Button) this.findViewById(R.id.spot_select);
        listview_instrument = (ListView) this.findViewById(R.id.listview_instrument);

        btn_submit.setEnabled(isNotModify);
        netselect.setEnabled(isNotModify);
        spotselect.setEnabled(isNotModify);


        if (isRevising) {
            Map<String, Object> params = new HashMap<>();
            params.put("orderIdList", gson.toJson(selectOrderId));
            ApiService.GetString(this, "getInstrumentListByOrderIdList", params, new RxStringCallback() {
                @Override
                public void onNext(Object tag, String response) {
                    String[] list = null;
                    Map<String, Object> params = new HashMap<>();
                    if (response.contains(",")) {
                        list = response.split(",");
                    } else {
                        String[] temp = {response};
                        list = temp;
                    }
                    params.put("list_instrument_id", gson.toJson(Arrays.asList(list)));
                    ApiService.GetString(ApplyInstrument.this, "getInstrumnetByIdList", params, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                            List<InstrumentStatus> list = gsonContainTime.fromJson(response, new TypeToken<List<InstrumentStatus>>() {
                            }.getType());
                            int index_temp = 0;
                            for (InstrumentStatus instrumentStatus : list) {
                                Map<String, String> map = new HashMap<>();
                                map.put("Index", "" + index_temp++);
                                map.put("instrumentId", instrumentStatus.getInstrumentId());
                                map.put("instrumentCode", instrumentStatus.getInstrumentCode());
                                map.put("instrumentType", instrumentStatus.getInstrumentType());
                                map.put("validateDate", instrumentStatus.getValidateDate().toString());
                                map.put("instrumentBoxCode", instrumentStatus.getInstrumentBoxCode());
                                map.put("isUsing", instrumentStatus.getIsUsing());
                                map.put("isSubmitted", instrumentStatus.getIsSubmitted());
                                map.put("isBroken", instrumentStatus.getIsBroken());
                                isSelectedList.add(map);
                            }
                            adapter_instrument.listitem = isSelectedList;
                            for (int i = 0; i < isSelectedList.size(); i++) {
                                statusList[i] = 1;
                            }
                            adapter_instrument.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {

                        }
                    });
                }

                @Override
                public void onError(Object tag, Throwable e) {

                }

                @Override
                public void onCancel(Object tag, Throwable e) {

                }
            });

        }
        listview_instrument.setAdapter(adapter_instrument);

        //更改标题栏内容
        if (isRevising) {
            btn_title.setText("选取仪器修改");
            btn_submit.setText("修改");
        }
    }

    private void initListener() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSelectedList.size() > 0&&!ClickUtils.isFastClick()) {
                    new AlertDialog.Builder(ApplyInstrument.this)
                            .setTitle("系统提示")
                            .setMessage("您是否确认提交所选仪器？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            refreshDataIndex();
                                            List<InstrumentStatus> list = new ArrayList<>();
                                            for (int i = 0; i < isSelectedList.size(); i++) {
                                                Log.i("selkect sadasdasdasdas",String.valueOf(statusList[i]));
                                                if (statusList[i] == 1) {
                                                    String instrumentId=isSelectedList.get(i).get("instrumentId");
                                                    String instrumentCode = isSelectedList.get(i).get("instrumentCode");
                                                    String instrumentType = isSelectedList.get(i).get("instrumentType");
                                                    Date validateDate = new Date();
                                                    try {
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                        validateDate = sdf.parse((isSelectedList.get(i).get("validateDate")));
                                                    } catch (Exception e) {

                                                    }
                                                    String instrumentBoxCode = isSelectedList.get(i).get("instrumentBoxCode");
                                                    String isUsing = isSelectedList.get(i).get("isUsing");
                                                    String isSubmitted = isSelectedList.get(i).get("isSubmitted");
                                                    String isBroken = isSelectedList.get(i).get("isBroken");
                                                    String deleteFlag = isSelectedList.get(i).get("deleteFlag");
                                                    InstrumentStatus instrumentStatus = new InstrumentStatus(instrumentId,instrumentCode, instrumentType, validateDate, instrumentBoxCode, isUsing, isSubmitted, isBroken,deleteFlag,true);
                                                    list.add(instrumentStatus);
                                                }
                                            }
                                            submit(list);
                                        }
                                    })
                            .show();
                } else {
                    Toast.makeText(ApplyInstrument.this, "您要申请的仪器列表为空，提交失败", Toast.LENGTH_LONG).show();
                }
            }
        });
        netselect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyInstrument.this, NetSelect.class);
                refreshDataIndex();
                ArrayList<Map<String, String>> isSelectedList_temp = new ArrayList<>();
                for (int i = 0; i < isSelectedList.size(); i++) {
                    if (statusList[i] == 1) {
                        isSelectedList_temp.add(isSelectedList.get(i));
                    }
                }
                isSelectedList.clear();
                for (Map map : isSelectedList_temp) {
                    isSelectedList.add(map);
                }
                isSelectedList_temp.clear();
                if (isSelectedList.size() > 0) {
                    intent.putExtra("selectList", gson.toJson(isSelectedList));
                }
                startActivityForResult(intent, 1);
            }
        });
        spotselect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ApplyInstrument.this, SpotSelect.class);
                refreshDataIndex();
                ArrayList<Map<String, String>> isSelectedList_temp = new ArrayList<>();
                for (int i = 0; i < isSelectedList.size(); i++) {
                    if (statusList[i] == 1) {
                        isSelectedList_temp.add(isSelectedList.get(i));
                    }
                }
                isSelectedList.clear();
                for (Map map : isSelectedList_temp) {
                    isSelectedList.add(map);
                }
                isSelectedList_temp.clear();
                if (isSelectedList.size() > 0) {
                    intent.putExtra("selectList", gson.toJson(isSelectedList));
                }
                startActivityForResult(intent, 3);
            }
        });
    }


    private void submit(List<InstrumentStatus> list) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("instrumentStatusList", JsonTimeUtil.toJSON(list));
        parameters.put("selectOrderId", JSON.toJSONString(selectOrderId));
        parameters.put("isRevising", isRevising);

        //获取选择的订单是复检还是初检
        //parameters.put("isRecheck", gson.toJson(orderStatusMap));

        ApiService.GetString(this, "addInstrumentApplication", parameters, new RxStringCallback() {

            @Override
            public void onNext(Object tag, String response) {
                if(response.equals("success")){
                    Toast.makeText(ApplyInstrument.this, response, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ApplyInstrument.this, OrderSelectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(ApplyInstrument.this, response, Toast.LENGTH_LONG).show();
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

    private void refreshDataIndex() {
        int size = isSelectedList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                isSelectedList.get(i).remove("Index");
                isSelectedList.get(i).put("Index", i + 1 + "");
            }
        }
    }

    public class Adapter_instrument extends BaseAdapter {

        private LayoutInflater mInflater;
        private ViewHolder_instrument holder;
        private ArrayList<Map<String, String>> listitem = new ArrayList<>();

        /*构造函数*/
        public Adapter_instrument(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listitem.size();
        }

        @Override
        public Object getItem(int position) {
            return listitem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.instrument_select, null);
                holder = new ViewHolder_instrument();
                holder.textview_index = (TextView) convertView.findViewById(R.id.instrument_index);
                holder.textview_box = (TextView) convertView.findViewById(R.id.instrument_box);
                holder.textview_number = (TextView) convertView.findViewById(R.id.instrument_number);
                holder.textview_name = (TextView) convertView.findViewById(R.id.instrument_name);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.instrument_checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_instrument) convertView.getTag();
            }

            holder.textview_index.setText(listitem.get(position).get("Index").toString());
            holder.textview_box.setText(listitem.get(position).get("instrumentBoxCode").toString());
            holder.textview_number.setText(listitem.get(position).get("instrumentCode").toString());
            holder.textview_name.setText(listitem.get(position).get("instrumentType").toString());
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (statusList[position] ==0) {
                        statusList[position] = 1;
                    } else {
                        statusList[position] = 0;
                    }

                }
            });
            if(statusList[position]==0)
            holder.checkBox.setChecked(false);
            else holder.checkBox.setChecked(true);
            return convertView;
        }

    }

    public final class ViewHolder_instrument {
        public TextView textview_index;
        public TextView textview_box;
        public TextView textview_number;
        public TextView textview_name;
        private CheckBox checkBox;
    }
}
