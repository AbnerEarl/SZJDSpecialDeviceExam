package com.example.frank.jinding.UI.PublicMethodActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.frank.jinding.Bean.OrderBean.CheckType;
import com.example.frank.jinding.Bean.OrderBean.ConsignmentDetail;
import com.example.frank.jinding.Bean.OrderBean.OrderDeviceDetail;
import com.example.frank.jinding.Interface.CallBack;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.CheckerActivity.OrderCheck;
import com.example.frank.jinding.UI.SalesmanActivity.AddOrder;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDetails extends AppCompatActivity implements View.OnClickListener, CallBack {

    private String content;

    private ImageButton titleleft, titleright;
    private TextView title;
    private ListView device_lv;
    private static int REQUEST_CODE;
    private static int ITEM_POSITION;
    private List<OrderDeviceDetail> deviceList;
    private MyAdapter adapter;
    private String consignmentId;
    private String consignmentType;
    private static boolean permissioned;
    private OrderDeviceDetail device;
    private ConsignmentDetail consignmentDetail;
    private ListView checkTypeLv;
    private List<CheckType> checkTypeList = new ArrayList<>();
    private Boolean showMonitor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Intent intent = getIntent();
        REQUEST_CODE = intent.getIntExtra("requestCode", -1);
        ITEM_POSITION = intent.getIntExtra("ItemPosition", -1);
        permissioned = intent.getBooleanExtra("update", false);
        if (REQUEST_CODE == AddOrder.ADDORDER_REQUEST_CODE) {
            Bundle bundle = intent.getExtras();
            consignmentDetail = (ConsignmentDetail) bundle.getSerializable("consignment");
            consignmentType = consignmentDetail.getDeviceTypeId();
        } else {
            consignmentId = intent.getStringExtra("consignmentId");
            consignmentType = intent.getStringExtra("deviceType");
        }
        if (consignmentType.equals("塔式起重机") || consignmentType.equals("门式起重机") || consignmentType.equals("桥式起重机")) {
            showMonitor = true;
        }
        initView();
        if (!permissioned)
            titleright.setVisibility(titleright.INVISIBLE);
        else {
            checkType();
        }
    }

    private void getDeviceList(String consignmentId) {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("consignmentId", consignmentId);
        ApiService.GetString(this, "orderDeviceDetail", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                Log.i(TAG, "获取设备成功");
                List<OrderDeviceDetail> list = new ArrayList<>();
                if (response != null) {

                    list = (List<OrderDeviceDetail>) JSON.parseArray(response, OrderDeviceDetail.class);
                    for (int i = 0; i < list.size(); i++) {
                        deviceList.add(list.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

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
                    checkTypeLv = new ListView(TaskDetails.this);
                    ArrayAdapter adapter = new ArrayAdapter(TaskDetails.this, android.R.layout.simple_list_item_1, checkTypeList);
                    checkTypeLv.setAdapter(adapter);

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

    protected void initView() {
        titleleft = (ImageButton) this.findViewById(R.id.titleleft);
        titleright = (ImageButton) this.findViewById(R.id.titleright);
        title = (TextView) this.findViewById(R.id.titlecenter);
        device_lv = (ListView) this.findViewById(R.id.lv_order_details);
//标题栏设置
        title.setText(consignmentType);
        titleleft.setOnClickListener(this);
        titleright.setOnClickListener(this);
        deviceList = new ArrayList<>();

        adapter = new MyAdapter(this, this, deviceList);
        if (REQUEST_CODE == AddOrder.ADDORDER_REQUEST_CODE) {
            if (consignmentDetail.getDeviceNum() != 0) {
                for (int i = 0; i < consignmentDetail.getOrderDeviceDetailList().size(); i++)
                    deviceList.add(consignmentDetail.getOrderDeviceDetailList().get(i));
                adapter.notifyDataSetChanged();
            }
        } else {
            getDeviceList(consignmentId);
        }
        device_lv.setAdapter(adapter);
        device_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (permissioned) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.add_divice, (ViewGroup) findViewById(R.id.add_device));

                    final EditText et1 = (EditText) layout.findViewById(R.id.divice_number);
                    final EditText et2 = (EditText) layout.findViewById(R.id.device_type);
                    final EditText et3 = (EditText) layout.findViewById(R.id.check_type);
                    final EditText et4 = (EditText) layout.findViewById(R.id.height);
                    final EditText et5 = (EditText) layout.findViewById(R.id.auto_number);
                    final EditText et6 = (EditText) layout.findViewById(R.id.prize);
                    final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.add_device_rg);
                    final LinearLayout monitorLayout = (LinearLayout) layout.findViewById(R.id.monitor_layout);
                    final RadioButton monitorTrueRb = (RadioButton) layout.findViewById(R.id.monitor_true);
                    final RadioButton monitorFalseRb = (RadioButton) layout.findViewById(R.id.monitor_false);
                    monitorLayout.setVisibility(View.GONE);
                    if (showMonitor) {
                        monitorLayout.setVisibility(View.VISIBLE);
                        if (deviceList.get(position).getMonitorStatus() == null || deviceList.get(position).getMonitorStatus().equals("0")) {
                            monitorFalseRb.setChecked(true);
                            monitorTrueRb.setChecked(false);
                        } else {
                            monitorFalseRb.setChecked(false);
                            monitorTrueRb.setChecked(true);
                        }

                    }
                    et1.setText(deviceList.get(position).getDeviceManufacCode());
                    et2.setText(deviceList.get(position).getTypeSpecification());
                    et3.setText(deviceList.get(position).getCheckTypeId());
                    et4.setText(deviceList.get(position).getInstallHeight() + "");
                    et5.setText(deviceList.get(position).getSelfCode());
                    et6.setText(deviceList.get(position).getDeviceCharge() + "");

                    new AlertDialog.Builder(TaskDetails.this).setTitle("修改设备信息").setView(layout)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (et1.getText() == null || TextUtils.isEmpty(et1.getText()) || et2.getText() == null || TextUtils.isEmpty(et2.getText()) || et3.getText() == null || TextUtils.isEmpty(et3.getText()) || et4.getText() == null || TextUtils.isEmpty(et4.getText()) || et5.getText() == null || TextUtils.isEmpty(et5.getText())) {
                                        Toast.makeText(TaskDetails.this, "请输入完整设备信息", Toast.LENGTH_SHORT).show();
                                    } else if (checkLegal(et1.getText().toString(), et5.getText().toString(), position)) {
                                        Toast.makeText(TaskDetails.this, "设备出厂编号或自编号与其他设备相同，请重新输入", Toast.LENGTH_SHORT).show();
                                    } else {
                                        device = deviceList.get(position);
                                        Log.i("deviceCheckType", device.getCheckTypeId());
                                        device.setDeviceManufacCode(et1.getText().toString());
                                        device.setTypeSpecification(et2.getText().toString());
                                        device.setCheckTypeId(et3.getText().toString());
                                        device.setInstallHeight(Float.parseFloat(et4.getText().toString()));
                                        device.setSelfCode(et5.getText().toString());
                                        device.setDeviceCharge(Float.valueOf(et6.getText().toString()));
                                        if (showMonitor) {
                                            if (radioGroup.getCheckedRadioButtonId() == R.id.monitor_true) {
                                                device.setMonitorStatus("1");
                                            } else {
                                                device.setMonitorStatus("0");
                                            }
                                        }
                                        Log.i("deviceCheckTypeId", device.getCheckTypeId());
                                        if (REQUEST_CODE == AddOrder.ADDORDER_REQUEST_CODE) {
                                            deviceList.set(position, device);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(TaskDetails.this, "添加成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            deviceList.set(position, device);
                                            updateDevice(position);
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    et3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog dialog = new AlertDialog.Builder(TaskDetails.this)
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((ViewGroup) checkTypeLv.getParent()).removeView(checkTypeLv);
                                            dialog.dismiss();
                                        }
                                    }).setView(checkTypeLv).create();
                            dialog.setTitle("选择检验类别");
                            checkTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    et3.setText(checkTypeList.get(position).getCheck_type_name());
                                    ((ViewGroup) checkTypeLv.getParent()).removeView(checkTypeLv);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }
                    });
                }
            }
        });

    }

    private boolean checkLegal(String manufacCode, String selfCode, int position) {
        for (int i = 0; i < deviceList.size(); i++) {
            if (position != i && (deviceList.get(i).getDeviceManufacCode().equals(manufacCode) || deviceList.get(i).getSelfCode().equals(selfCode))) {
                //设备出厂编号和自编号与其他设备相同
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleright:
                addDivice();
                break;
            case R.id.titleleft:
                onBackPressed();
                finish();
        }
    }

    private void addDivice() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.add_divice, (ViewGroup) findViewById(R.id.add_device));

        final EditText et1 = (EditText) layout.findViewById(R.id.divice_number);
        final EditText et2 = (EditText) layout.findViewById(R.id.device_type);
        final EditText et3 = (EditText) layout.findViewById(R.id.check_type);
        final EditText et4 = (EditText) layout.findViewById(R.id.height);
        final EditText et5 = (EditText) layout.findViewById(R.id.auto_number);
        final EditText et6 = (EditText) layout.findViewById(R.id.prize);
        final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.add_device_rg);
        final LinearLayout monitorLayout = (LinearLayout) layout.findViewById(R.id.monitor_layout);
        monitorLayout.setVisibility(View.GONE);
        if (showMonitor)
            monitorLayout.setVisibility(View.VISIBLE);
        new AlertDialog.Builder(TaskDetails.this).setTitle("添加设备").setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (et1.getText() == null || TextUtils.isEmpty(et1.getText()) || et2.getText() == null || TextUtils.isEmpty(et2.getText()) || et3.getText() == null || TextUtils.isEmpty(et3.getText()) || et4.getText() == null || TextUtils.isEmpty(et4.getText()) || et5.getText() == null || TextUtils.isEmpty(et5.getText()) || et6.getText() == null || TextUtils.isEmpty(et6.getText())) {
                            Toast.makeText(TaskDetails.this, "请输入完整设备信息", Toast.LENGTH_SHORT).show();
                        } else if (checkLegal(et1.getText().toString(), et5.getText().toString(), -1)) {
                            Toast.makeText(TaskDetails.this, "设备出厂编号或自编号与其他设备相同，请重新输入", Toast.LENGTH_SHORT).show();
                        } else if (Float.parseFloat(et4.getText().toString().trim()) > 1000) {
                            Toast.makeText(TaskDetails.this, "安装高度不能大于1000", Toast.LENGTH_SHORT).show();
                            et4.setText("");
                        } else {
                            Log.i("添加设备", "" + Log.i("id", et1.getText().toString() + "2" + et2.getText().toString() + "3" + et3.getText().toString() + "4" + et4.getText().toString() + "5" + et5.getText().toString()));
                            OrderDeviceDetail orderDeviceDetail = new OrderDeviceDetail(et1.getText().toString(), et2.getText().toString(), et3.getText().toString(), Float.parseFloat(et4.getText().toString()), et5.getText().toString());
                            orderDeviceDetail.setStatusCode("01");
                            orderDeviceDetail.setDeviceCharge(Float.valueOf(et6.getText().toString()));
                            if (showMonitor) {
                                if (radioGroup.getCheckedRadioButtonId() == R.id.monitor_true) {
                                    orderDeviceDetail.setMonitorStatus("1");
                                } else {
                                    orderDeviceDetail.setMonitorStatus("0");
                                }
                            }
                            if (REQUEST_CODE == AddOrder.ADDORDER_REQUEST_CODE) {
                                deviceList.add(orderDeviceDetail);
                                Log.i("addDevice", "添加成功");
                                adapter.notifyDataSetChanged();
                                Toast.makeText(TaskDetails.this, "添加成功", Toast.LENGTH_SHORT).show();

                            } else {
                                orderDeviceDetail.setConsignmentId(consignmentId);
                                addDevice(orderDeviceDetail);
                            }
                            Log.i("更新item", "更新成功");

                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        et3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(TaskDetails.this)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ViewGroup) checkTypeLv.getParent()).removeView(checkTypeLv);
                                dialog.dismiss();
                            }
                        }).setView(checkTypeLv).create();
                dialog.setTitle("选择设备类型");
                checkTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et3.setText(checkTypeList.get(position).getCheck_type_name());
                        ((ViewGroup) checkTypeLv.getParent()).removeView(checkTypeLv);
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }

    @Override
    public void callBack(final View view) {
        //打印Button的点击信息
        new AlertDialog.Builder(TaskDetails.this)
                .setTitle("系统提示")
                .setMessage("您确定要删除这个设备吗？")
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
                                int position = (Integer) view.getTag();
                                if (REQUEST_CODE == AddOrder.ADDORDER_REQUEST_CODE) {

                                    deviceList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(TaskDetails.this, "设备删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    deleteDevice(position);
                                }
                            }
                        }).show();


    }

    private void deleteDevice(final int position) {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("orderDeviceDetail", JSON.toJSONString(deviceList.get(position)));
        ApiService.GetString(this, "orderDeviceDelete", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                if (response.equals("success")) {
                    Log.i(TAG, "删除成功");
                    deviceList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(TaskDetails.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "删除失败");
                    Toast.makeText(TaskDetails.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG, "删除失败");
                Toast.makeText(TaskDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    private void updateDevice(final int position) {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        OrderDeviceDetail orderDeviceDetail = deviceList.get(position);

        Log.i("device", device.getCheckTypeId());
        Map<String, Object> map = new HashMap<>();
        map.put("orderDeviceDetail", JSON.toJSONString(orderDeviceDetail));
        map.put("requestCode", 2);
        ApiService.GetString(this, "orderDeviceUpdate", map, new RxStringCallback() {

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(TaskDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                Log.i(TAG, "获取成功");
                if (response.equals("success")) {
                    Log.i(TAG, "更新成功");
                    Toast.makeText(TaskDetails.this, "修改成功", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, response);
                }
            }
        });
    }

    private void addDevice(final OrderDeviceDetail orderDeviceDetail) {
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        OrderDeviceDetail orderdeviceDetail = orderDeviceDetail;
        if (REQUEST_CODE == OrderCheck.REQUEST_CODE)
            orderdeviceDetail.setStatusCode("06");
        if (REQUEST_CODE == OrderSearch.UPDATE_REQUEST_CODE)
            orderdeviceDetail.setStatusCode("01");
        Map<String, Object> map = new HashMap<>();
        map.put("orderDeviceDetail", JSON.toJSONString(orderdeviceDetail));
        map.put("requestCode", 1);
        ApiService.GetString(this, "orderDeviceUpdate", map, new RxStringCallback() {
                    @Override
                    public void onError(Object tag, Throwable e) {
                        Toast.makeText(TaskDetails.this, "添加失败" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {

                    }

                    @Override
                    public void onNext(Object tag, String response) {
                        processDialog.dismiss();
                        if (response.equals("success")) {
                            deviceList.add(orderDeviceDetail);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(TaskDetails.this, "设备添加成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );

    }


    private class MyAdapter extends BaseAdapter implements View.OnClickListener {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private CallBack callBack;
        private List<OrderDeviceDetail> list;

        /*构造函数*/
        public MyAdapter(Context context, CallBack callBack, List<OrderDeviceDetail> devices) {
            this.mInflater = LayoutInflater.from(context);
            this.list = devices;
            this.callBack = callBack;
        }

        @Override
        public int getCount() {
            return list.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*书中详细解释该方法*/
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_order_divice, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                //xe
                holder.monitorLayout = (LinearLayout) convertView.findViewById(R.id.monitor_show);
                holder.number = (TextView) convertView.findViewById(R.id.device_number);
                holder.numberType = (TextView) convertView.findViewById(R.id.number_type);
                holder.height = (TextView) convertView.findViewById(R.id.height);
                holder.prize = (TextView) convertView.findViewById(R.id.prize);
                holder.monitorTv = (TextView) convertView.findViewById(R.id.monitor_status);
                holder.checkType = (TextView) convertView.findViewById(R.id.check_type);
                holder.autoNumber = (TextView) convertView.findViewById(R.id.auto_number);
                holder.delete_img = (ImageView) convertView.findViewById(R.id.delete_agree);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            if (showMonitor) {
                holder.monitorLayout.setVisibility(View.VISIBLE);
                if (list.get(position).getMonitorStatus() != null) {
                    if (list.get(position).getMonitorStatus().equals("0"))
                        holder.monitorTv.setText("无监控");
                    else
                        holder.monitorTv.setText("有监控");
                }
            } else {
                holder.monitorLayout.setVisibility(View.GONE);
            }

            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.number.setText(list.get(position).getDeviceManufacCode());
            holder.numberType.setText(list.get(position).getTypeSpecification());
            holder.checkType.setText(list.get(position).getCheckTypeId());
            holder.height.setText(list.get(position).getInstallHeight() + "");
            holder.autoNumber.setText(list.get(position).getSelfCode());
            holder.prize.setText(list.get(position).getDeviceCharge() + "元");
            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/


            if (!permissioned)
                holder.delete_img.setVisibility(holder.delete_img.INVISIBLE);
            holder.delete_img.setOnClickListener(this);
            holder.delete_img.setTag(position);

            return convertView;
        }

        @Override
        public void onClick(View v) {
            callBack.callBack(v);
        }

        /*存放控件*/
        class ViewHolder {
            public TextView number, numberType, checkType, height, prize, autoNumber, monitorTv;
            public ImageView delete_img;
            public LinearLayout monitorLayout;
        }
    }


    @Override
    public void onBackPressed() {
        if (permissioned) {
            Intent intent = new Intent();
            intent.putExtra("ItemPosition", ITEM_POSITION);
            if (deviceList == null || deviceList.size() == 0) {
                intent.putExtra("DeviceNumber", 0);
                intent.putExtra("checkCharge", new Float(0));
            } else {
                intent.putExtra("DeviceNumber", deviceList.size());
                Float checkCharge = new Float(0);
                for (OrderDeviceDetail orderDeviceDetail : deviceList)
                    if (orderDeviceDetail.getDeviceCharge() != null)
                        checkCharge += orderDeviceDetail.getDeviceCharge();
                intent.putExtra("checkCharge", checkCharge);
            }
            if (REQUEST_CODE == AddOrder.ADDORDER_REQUEST_CODE) {
                Bundle bundle = new Bundle();
                consignmentDetail.setOrderDeviceDetailList(deviceList);
                Log.i("TaskDetail", "deviceList" + deviceList.size());
                bundle.putSerializable("consignment", consignmentDetail);
                intent.putExtras(bundle);
            }
            setResult(REQUEST_CODE, intent);
        }
        finish();
    }
}
