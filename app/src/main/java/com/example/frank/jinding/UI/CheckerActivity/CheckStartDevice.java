package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.widget.PullRefreshLayout;
import com.example.frank.jinding.Bean.OrderBean.CheckType;
import com.example.frank.jinding.Bean.OrderBean.OrderDeviceDetail;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckStartDevice extends AppCompatActivity {
    private ImageButton back;
    private TextView title;
    private ListView lv_task;
    private MyAdapter waitAdapter;
    private String consignmentId;
    private String consignmentType, isMainChecker = "", orderId = "", submission_id = "";
    private List<OrderDeviceDetail> deviceList;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner mSpinner;
    //private SwipeRefreshLayout refreshLayout;
    private PullRefreshLayout pullRefreshLayout;
    private List<CheckType> checkTypeList = new ArrayList<>();
    private String refreshcode = "23";
    private boolean showMonitor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_start_device);

        Intent intent = getIntent();
        consignmentId = intent.getStringExtra("consignmentId");
        consignmentType = intent.getStringExtra("deviceType");
        isMainChecker = Boolean.toString(intent.getBooleanExtra("isMainChecker", false));
        orderId = intent.getStringExtra("orderId");
        submission_id = intent.getStringExtra("submission_id");
        if (consignmentType.equals("塔式起重机") || consignmentType.equals("门式起重机") || consignmentType.equals("桥式起重机")) {
            showMonitor = true;
        }
        init();

        title.setText("设备检验");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String map = consignmentType + "-" + waitAdapter.listItem.get(arg2).getDeviceManufacCode().toString();

                Intent intent = new Intent(CheckStartDevice.this, SelectEquipment.class);
                intent.putExtra("device", map);
                intent.putExtra("orderId", orderId);
                intent.putExtra("submission_id", submission_id);
                intent.putExtra("isMainChecker", isMainChecker);
                intent.putExtra("consignmentId", consignmentId);
                intent.putExtra("deviceId", waitAdapter.listItem.get(arg2).getDeviceDetailId().toString());//设备id有待确认
                //startActivity(intent);
                startActivityForResult(intent,123);

            }
        });

       /* refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getDeviceList(consignmentId, refreshcode);

            }
        });*/

       pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               getDeviceList(consignmentId, refreshcode);
           }
       });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==123&& CheckControl.protocol_finish){
            CheckControl.protocol_finish=false;
            finish();
        }
        else if (requestCode ==123) {
            pullRefreshLayout.setRefreshing(true);
            getDeviceList(consignmentId, refreshcode);
        }
    }

    private void init() {

        back = (ImageButton) this.findViewById(R.id.titleback);
        title = (TextView) this.findViewById(R.id.titleplain);
        mSpinner = (Spinner) this.findViewById(R.id.check_start_device_spinner);
        lv_task = (ListView) this.findViewById(R.id.lv_check_start_device_info);
        waitAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        deviceList = new ArrayList<>();
        //refreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_check_start_device);
        pullRefreshLayout=(PullRefreshLayout)this.findViewById(R.id.refreshCheckStartDevice);
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add("待检验设备");
        spinnerList.add("已检验设备");
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //  TODO  Auto-generated  method  stub
                /*  将所选mySpinner  的值带入myTextView  中*/
                if (mSpinner.getSelectedItem().equals("待检验设备")) {
                    waitAdapter.listItem = deviceList;
                    lv_task.setAdapter(waitAdapter);//为ListView绑定Adapter
                    getDeviceList(consignmentId, "23");
                    refreshcode = "23";
                } else if (mSpinner.getSelectedItem().equals("已检验设备")) {

                    waitAdapter.listItem = deviceList;
                    lv_task.setAdapter(waitAdapter);//为ListView绑定Adapter
                    getDeviceList(consignmentId, "07");
                    refreshcode = "07";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //  TODO  Auto-generated  method  stub
                //myTextView.setText("NONE");
            }

        });



    }

    //如果是复检，去掉不是本次复检的设备
    private void getDeviceList(String consignmentId, final String statusCode) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("consignmentId", consignmentId);
        parameters.put("statusCode", statusCode);
        parameters.put("orderId", orderId);
        ApiService.GetString(this, "orderDeviceDetail", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Log.i(TAG, "获取设备成功");
                List<OrderDeviceDetail> list = new ArrayList<>();
                if (response != null) {

                    deviceList.clear();
                    list = (List<OrderDeviceDetail>) JSON.parseArray(response, OrderDeviceDetail.class);
                    for (int i = 0; i < list.size(); i++) {
                        deviceList.add(list.get(i));
                    }
                    waitAdapter.notifyDataSetChanged();
                } else {
                    String toastStr = null;
                    if (statusCode.equals("23"))
                        toastStr = "设备全部检验完成";
                    else if (statusCode.equals("07"))
                        toastStr = "还没有已检验完成的设备";
                    new AlertDialog.Builder(CheckStartDevice.this).setTitle(toastStr).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
                pullRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG, e.getMessage());
                Toast.makeText(CheckStartDevice.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                pullRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                pullRefreshLayout.setRefreshing(false);
            }

        });
    }

    private void checkType() {
        Map<String, Object> map = new HashMap<>();
        ApiService.GetString(this, "checkType", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response != null) {
                    Log.i("response", "" + response);
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        checkTypeList.add(new CheckType(jsonObject.getString("check_type_id"), jsonObject.getString("check_type_code"), jsonObject.getString("check_type_name")));
                    }

                }
                Log.i(TAG, "检验类型" + checkTypeList.get(0).getCheck_type_name());
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private List<OrderDeviceDetail> listItem;

        /*构造函数*/
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public int getCount() {

            return listItem.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /*书中详细解释该方法*/
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CheckStartDevice.ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.check_start_device_info, null);
                holder = new CheckStartDevice.ViewHolder();
                /*得到各个控件的对象*/
                holder.monitorLayout = (LinearLayout) convertView.findViewById(R.id.monitor_show);
                holder.monitorTv = (TextView) convertView.findViewById(R.id.monitor_status);
                holder.dnumber = (TextView) convertView.findViewById(R.id.csdi_num);
                holder.dmodel = (TextView) convertView.findViewById(R.id.csdi_model);
                holder.dstyle = (TextView) convertView.findViewById(R.id.csdi_style);
                holder.dheigh = (TextView) convertView.findViewById(R.id.csdi_heigh);
                holder.dno = (TextView) convertView.findViewById(R.id.csdi_no);
                holder.deviceCharge = (TextView) convertView.findViewById(R.id.device_charge);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (CheckStartDevice.ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            if (showMonitor) {
                holder.monitorLayout.setVisibility(View.VISIBLE);
                if (listItem.get(position).getMonitorStatus() != null) {
                    if (listItem.get(position).getMonitorStatus().equals("0"))
                        holder.monitorTv.setText("无监控");
                    else
                        holder.monitorTv.setText("有监控");
                }
            } else {
                holder.monitorLayout.setVisibility(View.GONE);
            }


            holder.dnumber.setText(listItem.get(position).getDeviceManufacCode());
            holder.dmodel.setText(listItem.get(position).getTypeSpecification());
            holder.dstyle.setText(listItem.get(position).getCheckTypeId());
            holder.dheigh.setText(listItem.get(position).getInstallHeight() + "");
            holder.dno.setText(listItem.get(position).getSelfCode());
            holder.deviceCharge.setText(listItem.get(position).getDeviceCharge()+"元");
            return convertView;
        }

    }

    /*存放控件*/
    public final class ViewHolder {
        public TextView dnumber, dmodel, dstyle, dheigh, dno, monitorTv, deviceCharge;
        public LinearLayout monitorLayout;
    }


}
