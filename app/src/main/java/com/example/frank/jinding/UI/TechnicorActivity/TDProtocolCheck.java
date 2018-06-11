package com.example.frank.jinding.UI.TechnicorActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.frank.jinding.Adapter.ProtocolAdapter;
import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.Bean.OrderBean.ConsignmentDetail;
import com.example.frank.jinding.Interface.CallBack;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.SalesmanActivity.AddOrderInformation;
import com.example.frank.jinding.UI.PublicMethodActivity.TaskDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TDProtocolCheck extends AppCompatActivity implements CallBack {

    @BindView(R.id.titleback)
    ImageButton titleback;
    @BindView(R.id.titleplain)
    TextView titleplain;
    @BindView(R.id.protocol_lv)
    ListView protocolLv;
    @BindView(R.id.user_info)
    TextView user_information;
    private CheckOrder checkOrder;
    private String orderId;
    private ProtocolAdapter mAdapter;
    private List<ConsignmentDetail> consignmentDetails;
    private String consignmentId;
    private int requestCode=0;
    AlertDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdprotocol_check);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        requestCode=intent.getIntExtra("requestCode",0);
        checkOrder=(CheckOrder) intent.getSerializableExtra("checkOrder");
        user_information.setText("所在地区：" + checkOrder.getProvince() + checkOrder.getCity() + checkOrder.getArea() + "\n委托单位：" + checkOrder.getOrderOrg() + "\n工程名称：" + checkOrder.getProjectName() + "\n工程地址：" + checkOrder.getProjectAddress());
        user_information.setTextSize(16);
        consignmentDetails = new ArrayList<>();
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        processDialog = new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        initView();
        initData();


    }

    private void initView() {
        if (requestCode==0)
        titleplain.setText("协议评审");
        else if (requestCode==0x12){
            titleplain.setText("评审历史");
        }
        mAdapter = new ProtocolAdapter(this, consignmentDetails, this, false, true, true);
        protocolLv.setAdapter(mAdapter);
        protocolLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TDProtocolCheck.this, TaskDetails.class);
                intent.putExtra("consignmentId", consignmentDetails.get(position).getConsignmentId());
                intent.putExtra("deviceType", consignmentDetails.get(position).getDeviceTypeId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        consignmentDetails = null;
    }

    private void initData() {

        getData();
    }

    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("requestCode", 1 + "");
        //有待斟酌
        ApiService.GetString(this, "orderConsignmentDetail", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                consignmentDetails.clear();
                if (response != null && !TextUtils.isEmpty(response)) {
                    List<ConsignmentDetail> list = JSON.parseArray(response, ConsignmentDetail.class);
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            consignmentDetails.add(list.get(i));
                        }
                        list.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(TDProtocolCheck.this, "协议为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                processDialog.dismiss();
                Toast.makeText(TDProtocolCheck.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }
        });

    }

    @OnClick({R.id.titleback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleback:
                finish();
                break;
        }
    }

    @Override
    public void callBack(View view) {
        switch (view.getId()) {
            case R.id.protocol_check: {
                consignmentId = consignmentDetails.get(Integer.parseInt(view.getTag().toString())).getConsignmentId();
                if (consignmentDetails.get(Integer.parseInt(view.getTag().toString())).getIsPassCheck().equals("2")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("通过", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            passProtocol();
                        }
                    }).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final EditText editText = new EditText(TDProtocolCheck.this);
                            editText.setHint("请输入拒绝理由");
                            AlertDialog alertDialog1 = new AlertDialog.Builder(TDProtocolCheck.this).setTitle("协议审核").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (editText.getText() != null) {
                                        rejectProtocol(editText.getText().toString());
                                    } else
                                        Toast.makeText(TDProtocolCheck.this, "请输入拒绝理由", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).create();
                            alertDialog1.show();
                        }
                    }).setTitle("协议审核").create();
                    alertDialog.show();
                } else if (consignmentDetails.get(Integer.parseInt(view.getTag().toString())).getIsPassCheck().equals("0")) {
                    Log.i("拒绝理由", consignmentDetails.get(Integer.parseInt(view.getTag().toString())).getRefuseReason());
                    TextView textView = new TextView(TDProtocolCheck.this);
                    textView.setText(consignmentDetails.get(Integer.parseInt(view.getTag().toString())).getRefuseReason());
                    textView.setPadding(50, 0, 0, 0);
                    textView.setTextColor(getResources().getColor(R.color.__picker_black_40));
                    AlertDialog alertDialog = new AlertDialog.Builder(TDProtocolCheck.this).setTitle("协议已拒绝")
                            .setView(textView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }
            }
            break;
            default:
                break;
        }

    }

    private void passProtocol() {
        Map<String, Object> map = new HashMap<>();
        map.put("consignmentId", consignmentId);
        ApiService.GetString(this, "passConsignment", map, new RxStringCallback() {
            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(TDProtocolCheck.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                if (response != null && response.equals("success")) {
                    Toast.makeText(TDProtocolCheck.this, "审核成功", Toast.LENGTH_SHORT).show();
                    getData();
                } else {
                    Toast.makeText(TDProtocolCheck.this, "审核失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void rejectProtocol(final String reason) {
        Map<String, Object> map = new HashMap<>();
        map.put("consignmentId", consignmentId);
        map.put("reason", reason);
        ApiService.GetString(this, "rejectConsignment", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response != null && response.equals("success")) {
                    Toast.makeText(TDProtocolCheck.this, "拒绝成功", Toast.LENGTH_SHORT).show();
                    getData();
                } else {
                    Toast.makeText(TDProtocolCheck.this, "拒绝失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(TDProtocolCheck.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    @OnClick(R.id.user_info)
    public void onViewClicked() {
       Intent intent=new Intent(this,AddOrderInformation.class);
       intent.putExtra("checkOrder",checkOrder);
        startActivity(intent);
    }
}
