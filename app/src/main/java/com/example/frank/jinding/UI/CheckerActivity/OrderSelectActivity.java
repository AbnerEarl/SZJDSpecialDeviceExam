package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄庆 on 2017/12/28.
 */

public class OrderSelectActivity extends AppCompatActivity {

    private ImageView image_back;
    private Button btn_submit;
    private ListView listView_order;
    private OrderAdapter establishAdapter;
    private ArrayList<JSONObject> submissionOrderList = new ArrayList<>();
    private ArrayList<String> selectOrderId = new ArrayList<>();
    private Spinner spinner_order;
    private List<String> list_spinner_data = new ArrayList<>();
    private ArrayAdapter<String> adapter_spinner;
    //private HashMap<String,String> orderStatusMap=new HashMap<>();
    private Gson gson = new Gson();
    private String submission_id="";
    private Gson gsonContainTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select);
        init();
        initListener();
    }

    private void init() {
        if (getIntent().getStringExtra("flag") != null) {
            if (getIntent().getStringExtra("flag").equals("fromApplyInstrument")) {
                selectOrderId.clear();
            }
        }

        list_spinner_data.clear();
        list_spinner_data.add("未申领仪器订单");
        list_spinner_data.add("已申领仪器订单");
        adapter_spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_spinner_data);
        adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        establishAdapter = new OrderAdapter(OrderSelectActivity.this);

        image_back = (ImageView) this.findViewById(R.id.image_back);
        btn_submit = (Button) this.findViewById(R.id.btn_submit);
        listView_order = (ListView) this.findViewById(R.id.listview_order);
        spinner_order = (Spinner) this.findViewById(R.id.spinner_order);
        spinner_order.setAdapter(adapter_spinner);
    }

    private void getNotApplyOrder() {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final AlertDialog processDialog = new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String, Object> parameters = new HashMap<>();
        ApiService.GetString(this, "getNotApplyOrder", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                if (response != null) {
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        submissionOrderList.add(jsonObject);
                    }
                }
                List<String> orderIdList = new ArrayList<>();
                for (int i = 0; i < submissionOrderList.size(); i++) {
                    orderIdList.add(submissionOrderList.get(i).get("orderId").toString());
                    //orderStatusMap.put(submissionOrderList.get(i).get("submissionId").toString(),submissionOrderList.get(i).get("recheck_status").toString());
                }
                establishAdapter.listItem = submissionOrderList;
                listView_order.setAdapter(establishAdapter);

            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i("失败", "" + e.getMessage());
                processDialog.dismiss();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                processDialog.dismiss();
            }
        });
    }

    private void getHasApplyOrder() {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final AlertDialog processDialog = new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String, Object> parameters = new HashMap<>();
        ApiService.GetString(this, "getHasApplyOrder", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                if (response != null) {
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        submissionOrderList.add(jsonObject);
                    }
                }
                List<String> orderIdList = new ArrayList<>();
                for (int i = 0; i < submissionOrderList.size(); i++) {
                    orderIdList.add(submissionOrderList.get(i).get("orderId").toString());
                    //orderStatusMap.put(submissionOrderList.get(i).get("submissionId").toString(),submissionOrderList.get(i).get("recheck_status").toString());
                }
                establishAdapter.listItem = submissionOrderList;
                listView_order.setAdapter(establishAdapter);

            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i("失败", "" + e.getMessage());
                processDialog.dismiss();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                processDialog.dismiss();

            }
        });
    }

    private void initListener() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("flag") != null) {
                    if (getIntent().getStringExtra("flag").equals("fromApplyInstrument")) {
                        Intent intent = new Intent(OrderSelectActivity.this, CheckersActivity.class);
                        startActivity(intent);
                    }
                } else {
                    finish();
                }
            }
        });
        listView_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = establishAdapter.getView(position, view, parent).findViewById(R.id.dispatching_checkBox);
                if (checkBox.isChecked()) {

                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                establishAdapter.notifyDataSetChanged();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(OrderSelectActivity.this,selectOrderId.toString(),Toast.LENGTH_LONG).show();
                if (selectOrderId.size() > 0) {

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",selectOrderId.get(0));
                    ApiService.GetString(OrderSelectActivity.this, "isMainChecker", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().equals("true")) {

                                Intent intent = new Intent(OrderSelectActivity.this, ApplyInstrument.class);
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("orderIdList", selectOrderId);
                                bundle.putString("type", spinner_order.getSelectedItem().toString());
                                //bundle.putString("isRecheck", gson.toJson(orderStatusMap));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }else if (response.trim().equals("false")){
                                Toast.makeText(OrderSelectActivity.this,"请等待主检验员申领仪器，您没有权限进行此项操作",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(OrderSelectActivity.this,"您的登录凭证已过期，需要退出系统后重新登录",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(OrderSelectActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(OrderSelectActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });


                } else {
                    Toast.makeText(OrderSelectActivity.this, "请先勾选您的订单再进行下一步", Toast.LENGTH_LONG).show();
                }
            }
        });
        spinner_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("已申领仪器订单".equals(spinner_order.getSelectedItem().toString().trim())) {
                    submissionOrderList.clear();
                    getHasApplyOrder();
                    selectOrderId.clear();

                } else if ("未申领仪器订单".equals(spinner_order.getSelectedItem().toString().trim())) {
                    submissionOrderList.clear();
                    getNotApplyOrder();
                    selectOrderId.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class OrderAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ViewHolder_Order holder;
        private ArrayList<JSONObject> listItem = new ArrayList<>();

        public OrderAdapter(Context context) {
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
                convertView = mInflater.inflate(R.layout.item_listview_instrument_order, null);
                holder = new ViewHolder_Order();
                holder.place = (TextView) convertView.findViewById(R.id.sendPlace);
                holder.actualTime = (TextView) convertView.findViewById(R.id.actual_time);
                holder.select = (CheckBox) convertView.findViewById(R.id.dispatching_checkBox);
                holder.title = (TextView) convertView.findViewById(R.id.dispatching_unit);
                holder.projectName = (TextView) convertView.findViewById(R.id.dispatching_projectName);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.order_check_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Order) convertView.getTag();
            }

            holder.projectName.setText(listItem.get(position).get("projectName").toString());
            String address=listItem.get(position).get("province").toString()+
                    listItem.get(position).get("city").toString()+
                    listItem.get(position).get("area").toString()+
                    listItem.get(position).get("projectAddress").toString();
            holder.place.setText(address);
            holder.actualTime.setText(listItem.get(position).get("actualDate").toString());
            holder.title.setText(listItem.get(position).get("orderOrg").toString());
            /*if(orderStatusMap.get(listItem.get(position).get("submissionId").toString()).equals("0")){
                holder.orderStatus.setText("初检");
            }else{
                holder.orderStatus.setText("复检");
            }*/

            holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckOrder checkOrder = null;
                    if ((submissionOrderList != null) || (submissionOrderList.size() > 0)) {
                        checkOrder = JSON.parseObject(submissionOrderList.get(position).toJSONString(), CheckOrder.class);
                    }
                    if (isChecked) {
                        if (checkOrder != null) {
                            selectOrderId.add(checkOrder.getOrderId());
                        }
                    } else {
                        if (checkOrder != null) {
                            selectOrderId.remove(checkOrder.getOrderId());
                        }
                    }
                }
            });
            return convertView;
        }
    }

    public final class ViewHolder_Order {
        public CheckBox select;
        public TextView title;
        public TextView place;
        public TextView actualTime;
        public TextView projectName;
        public TextView orderStatus;
    }

}
