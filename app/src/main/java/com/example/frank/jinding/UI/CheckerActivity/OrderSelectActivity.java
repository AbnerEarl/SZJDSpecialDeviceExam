package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Bean.Order;
import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.Log.L;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
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

    private int ISnull=1;//判断返回键
    private List<String> list_spinner_data = new ArrayList<>();
    private ArrayAdapter<String> adapter_spinner;
    //private HashMap<String,String> orderStatusMap=new HashMap<>();
    private Gson gson = new Gson();
    private static String submission_id="";
    private static int orderStatusTag=0;
    private List<String> seqList=new ArrayList<>();
    private NiceSpinner spinner_order;
    private List<String>NiceSpinner;
    private String Type;

    private Gson gsonContainTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_select);
        init();
        initView();
        initListener();
    }

    private void initView() {
        orderStatusTag=0;
        submissionOrderList.clear();
        seqList.clear();
        getNotApplyOrder();
        selectOrderId.clear();
        btn_submit.setVisibility(View.VISIBLE);
    }

    @Override       //这里实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        init();
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
        //adapter_spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_spinner_data);
        NiceSpinner = new LinkedList<String>(list_spinner_data);
        //adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        establishAdapter = new OrderAdapter(OrderSelectActivity.this);

        image_back = (ImageView) this.findViewById(R.id.image_back);
        btn_submit = (Button) this.findViewById(R.id.btn_submit);
        listView_order = (ListView) this.findViewById(R.id.listview_order);
        spinner_order = (NiceSpinner) this.findViewById(R.id.spinner_order);
        spinner_order.attachDataSource(NiceSpinner);
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
                if(orderIdList.size()==0)
                    ISnull=0;

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
        ISnull=0;
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


                establishAdapter.listItem = formateHadApplyOrder(submissionOrderList);
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


    private ArrayList<JSONObject>  formateHadApplyOrder(ArrayList<JSONObject> submissionOrderList){
        ArrayList<JSONObject> resultOrderList = new ArrayList<>();
        if (orderStatusTag==0){

            return submissionOrderList;

        }else {
            for (int i=0;i<submissionOrderList.size();i++) {
                boolean flag = false;
                int hadJoinOrderIndex=0;
                String seq = submissionOrderList.get(i).get("application_seq").toString();
                for (String ss : seqList) {
                    if (ss.equals(seq)) {
                        flag = true;
                        break;
                    }
                    hadJoinOrderIndex++;
                }
                if (!flag) {
                    seqList.add(seq);
                    JSONObject object=submissionOrderList.get(i);
                    object.put("orderNumTag","1");
                    resultOrderList.add(object);

                }else {
                    JSONObject object=resultOrderList.get(hadJoinOrderIndex);
                    int numTag=Integer.parseInt(object.getString("orderNumTag"))+1;
                   // object.put("orderNumTag",numTag+"");
                    resultOrderList.get(hadJoinOrderIndex).put("orderNumTag",numTag+"");
                }
            }

        }
        return resultOrderList;
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

        listView_order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (orderStatusTag==1) {
                    LinearLayout linearLayout = new LinearLayout(OrderSelectActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    String seq = establishAdapter.listItem.get(position).getString("application_seq");
                    for (int k = 0; k < submissionOrderList.size(); k++) {
                        JSONObject object = submissionOrderList.get(k);
                        if (object.get("application_seq").equals(seq)) {
                            TextView projectName = new TextView(OrderSelectActivity.this);
                            projectName.setText("工程名称：" + object.get("projectName").toString());
                            TextView actualDate = new TextView(OrderSelectActivity.this);
                            actualDate.setText("检验时间：" + object.get("actualDate").toString());
                            TextView orderOrg = new TextView(OrderSelectActivity.this);
                            orderOrg.setText("委托单位：" + object.get("orderOrg").toString());
                            String address = "";
                            if (object.get("projectAddress") != null && !object.get("projectAddress").equals("")) {
                                address = object.get("province").toString() +
                                        object.get("city").toString() +
                                        object.get("area").toString() +
                                        object.get("projectAddress").toString();
                            } else {
                                address = object.get("province").toString() +
                                        object.get("city").toString() +
                                        object.get("area").toString();
                            }
                            TextView projectAddress = new TextView(OrderSelectActivity.this);
                            projectAddress.setText("工程地址：" + address);
                            TextView blank = new TextView(OrderSelectActivity.this);


                            linearLayout.addView(projectName);
                            linearLayout.addView(actualDate);
                            linearLayout.addView(orderOrg);
                            linearLayout.addView(projectAddress);
                            linearLayout.addView(blank);
                        }
                    }

                    new AlertDialog.Builder(OrderSelectActivity.this).setTitle("订单信息").setView(linearLayout).setPositiveButton("确定", null).show();
                }
                return true;
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
                if (orderStatusTag==1){
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",selectOrderId.get(0));
                    ApiService.GetString(OrderSelectActivity.this, "isMainChecker", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().equals("true")) {

                                Intent intent = new Intent(OrderSelectActivity.this, ApplyInstrument.class);
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("orderIdList", selectOrderId);
                                bundle.putString("type", Type.toString());
                                //bundle.putString("isRecheck", gson.toJson(orderStatusMap));
                                intent.putExtra("isNotModify",establishAdapter.listItem.get(position).get("isNotModify").toString());
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
                }
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
                                bundle.putString("type", Type.toString());
                                //bundle.putString("isRecheck", gson.toJson(orderStatusMap));
                                intent.putExtra("isNotModify","true");
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
                if ("已申领仪器订单".equals(NiceSpinner.get(position).toString())) {
                    Type=NiceSpinner.get(position);
                    Toast.makeText(OrderSelectActivity.this,NiceSpinner.get(position).toString(),Toast.LENGTH_SHORT).show();
                    orderStatusTag=1;
                    submissionOrderList.clear();
                    getHasApplyOrder();
                    selectOrderId.clear();
                    btn_submit.setVisibility(View.INVISIBLE);

                } else if ("未申领仪器订单".equals(NiceSpinner.get(position).toString())) {
                    Type=NiceSpinner.get(position);
                    Toast.makeText(OrderSelectActivity.this,NiceSpinner.get(position).toString(),Toast.LENGTH_SHORT).show();
                    orderStatusTag=0;
                    submissionOrderList.clear();
                    seqList.clear();
                    getNotApplyOrder();
                    selectOrderId.clear();
                    btn_submit.setVisibility(View.VISIBLE);

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
                holder.orderStatus = (TextView) convertView.findViewById(R.id.order_check_status_app);
                holder.taskIcon=(ImageView)convertView.findViewById(R.id.task_icon);
                holder.tip=(TextView)convertView.findViewById(R.id.order_apply_tip);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Order) convertView.getTag();
            }


            if (listItem.get(position).get("projectName")!=null&&!listItem.get(position).get("projectName").equals("")){
                holder.projectName.setText(listItem.get(position).get("projectName").toString());
            }else {
                holder.projectName.setText("暂无");
            }
            String address="";
            if (listItem.get(position).get("projectAddress")!=null&&!listItem.get(position).get("projectAddress").equals("")){
                address=listItem.get(position).get("province").toString()+
                        listItem.get(position).get("city").toString()+
                        listItem.get(position).get("area").toString()+
                        listItem.get(position).get("projectAddress").toString();
            }else {
                address=listItem.get(position).get("province").toString()+
                        listItem.get(position).get("city").toString()+
                        listItem.get(position).get("area").toString();
            }

            if (orderStatusTag==1){
                L.e("已经申领订单标志"+orderStatusTag);
                String tag=listItem.get(position).get("orderNumTag").toString();
                if (Integer.parseInt(tag)>1){
                    holder.orderStatus.setText("多个订单");
                }else {
                    holder.orderStatus.setText("单个订单");
                }
                holder.orderStatus.setVisibility(View.VISIBLE);
                holder.select.setVisibility(View.INVISIBLE);
                holder.tip.setVisibility(View.VISIBLE);
            }else {
                L.e("没有申领订单标志"+orderStatusTag);
                holder.orderStatus.setVisibility(View.INVISIBLE);
                holder.select.setVisibility(View.VISIBLE);
                holder.tip.setVisibility(View.INVISIBLE);
            }

            holder.place.setText(address);
            holder.actualTime.setText(listItem.get(position).get("actualDate").toString());
            holder.title.setText(listItem.get(position).get("orderOrg").toString());

            if(listItem.get(position).get("projectName").toString().indexOf("(复检)")!=-1)
                holder.taskIcon.setImageResource(R.drawable.third_order);
            else
                holder.taskIcon.setImageResource(R.drawable.first_order);

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
        public TextView tip;
        public TextView place;
        public TextView actualTime;
        public TextView projectName;
        public TextView orderStatus;
        public ImageView taskIcon;
    }



}
