package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Adapter.ProtocolAdapter;
import com.example.frank.jinding.Adapter.QAdapter;
import com.example.frank.jinding.Adapter.SpinnerAdapter;
import com.example.frank.jinding.Bean.OrderBean.CheckReference;
import com.example.frank.jinding.Bean.OrderBean.ConsignmentDetail;
import com.example.frank.jinding.Bean.OrderBean.DeviceType;
import com.example.frank.jinding.Interface.CallBack;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PublicMethodActivity.TaskDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderCheck extends AppCompatActivity implements CallBack{

    private ImageButton titleleft,titleright,titlerightadd;
    private TextView title;

    private ListView lv_task;
    private ProtocolAdapter mAdapter;
    private List<ConsignmentDetail> consignmentList;
    String orderId,submissionId;
    private List<DeviceType>deviceTypeList;
    private List<CheckReference> checkReferenceList;
    public static int REQUEST_CODE=1001;
    private int checkReferencePosition=0;
    private boolean GOT_DEVICE_TYPE=false;
    private android.support.v7.app.AlertDialog processDialog;
    private List<String> checkReferenceCode=new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String checkReferenceStr="";
    private int ADD_OR_UPDATE=1;
    QAdapter checkReferenceAdapter;
    SpinnerAdapter deviceTypeAdapter;
    private ListView checkReferenceLv;
    private boolean NOT_CONFIRMED=true;
    private String mainCheckReference;
    private String deviceTypeName;
    private int updatePosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check);
        orderId=getIntent().getStringExtra("orderId");
        submissionId=getIntent().getStringExtra("submissionId");
        deviceTypeList=new ArrayList<>();
        checkReferenceList=new ArrayList<>();
        checkReferenceLv=new ListView(this);
        init();
        title.setText("订单信息核对");
        titleleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        titlerightadd.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                           addAndUpdateConsignment(null);
                                             }
                                         });



        titleright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new  AlertDialog.Builder(OrderCheck.this)
                        .setTitle("系统提示")
                        .setMessage("\n您确定订单信息无误吗？如有疑问请返回修改，提交后将开始检验！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定",
                                new  DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public  void  onClick(DialogInterface dialog, int  which)
                                    {
                                        //OrderInfoCheck.tag=true;

                                        /*Intent intent=new Intent(OrderCheck.this,SelectEquipment.class);
                                        startActivity(intent);*/
                                        submit();
                                    }
                                }).show();

            }
        });





/*为ListView添加点击事件*/

swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getData();
    }
});
        lv_task.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem==0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode==REQUEST_CODE){
        int position=data.getIntExtra("ItemPosition",-1);
        Bundle bundle=data.getExtras();
        consignmentList.get(position).setDeviceNum(data.getIntExtra("DeviceNumber",0));
        consignmentList.get(position).setCheckCharge(data.getFloatExtra("checkCharge",0));
        mAdapter.notifyDataSetChanged();
    }
    }

    private void addAndUpdateConsignment(final ConsignmentDetail consignmentDetail)
    {
        if (consignmentDetail==null){
      //添加协议
       ADD_OR_UPDATE=1;
    }else {
            //更新协议
            ADD_OR_UPDATE=2;
        }
        View dialogView=View.inflate(OrderCheck.this,R.layout.add_protocol_dialog,null);
        final Spinner deviceTypeSpinner=(Spinner)dialogView.findViewById(R.id.add_protocol_deviceType_spinner);
        deviceTypeAdapter=new SpinnerAdapter<DeviceType>(OrderCheck.this,R.layout.spinner_dropdown_item,deviceTypeList);
        deviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        deviceTypeSpinner.setAdapter(deviceTypeAdapter);
        deviceTypeSpinner.setSelection(deviceTypeList.size()-1,true);
        final TextView checkReferenceTv=(TextView) dialogView.findViewById(R.id.add_protocol_checkReference_et);
       // final EditText checkPriceEt=(EditText)dialogView.findViewById(R.id.check_price);

        checkReferenceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_code() == null) {
                    Toast.makeText(OrderCheck.this, "请先选择设备类型", Toast.LENGTH_SHORT).show();
                } else {
                    deviceTypeName=((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_name();
                    checkReferenceAdapter = new QAdapter(OrderCheck.this);
                    getCheckReference(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id());
                    checkReferenceLv.setAdapter(checkReferenceAdapter);

                    final AlertDialog dialog = new AlertDialog.Builder(OrderCheck.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ViewGroup) checkReferenceLv.getParent()).removeView(checkReferenceLv);
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkReferenceStr = "";
                            if (checkReferenceAdapter.isSelected.contains(true)){
                                int position=checkReferenceAdapter.isSelected.indexOf(true);

                                checkReferenceCode.add(checkReferenceList.get(position).getReference_code());
                                checkReferenceStr =checkReferenceList.get(position).getReference_name();
                                mainCheckReference=checkReferenceList.get(position).getReference_code();
                            }
                            checkReferenceTv.setText(checkReferenceStr);
                            ((ViewGroup) checkReferenceLv.getParent()).removeView(checkReferenceLv);
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.setTitle("选择检验依据");
                    dialog.setView(checkReferenceLv);
                    dialog.show();
                }
                }
            }
        );

        AlertDialog alertDialog = new AlertDialog.Builder(OrderCheck.this)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (((DeviceType)deviceTypeSpinner.getSelectedItem()).getDevice_type_id()==null)
                                    Toast.makeText(OrderCheck.this,"请选择设备类型",Toast.LENGTH_SHORT).show();
                                else if(checkRepate(((DeviceType)deviceTypeSpinner.getSelectedItem()).getDevice_type_name())){
                                    Toast.makeText(OrderCheck.this,"请勿重复添加",Toast.LENGTH_SHORT).show();
                                }
                                else if (checkReferenceTv.getText()==null|| TextUtils.isEmpty(checkReferenceTv.getText().toString()))
                                    Toast.makeText(OrderCheck.this,"请选择检验依据",Toast.LENGTH_SHORT).show();
                                else{
                                    Float checkCharge=new Float(0);
                                   if (ADD_OR_UPDATE==1) {
                                       ConsignmentDetail consignmentDetail = new ConsignmentDetail(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id(), 0);
                                       consignmentDetail.setOrderId(orderId);
                                       consignmentDetail.setIsPassCheck("2");
                                       consignmentDetail.setConsignmentStatus("06");
                                      // consignmentDetail.setReferenceCodes(checkArray.toJSONString());
                                       consignmentDetail.setCheckCharge(checkCharge);
                                       consignmentDetail.setMainCheckReference(mainCheckReference);
                                       //   Log.i(TAG,"修改协议");
                                       updateConsignmentCommit(consignmentDetail);
                                   }else {
                                      // consignmentDetail.setCheckCharge(checkCharge);
                                       consignmentDetail.setIsPassCheck("2");
                                       consignmentDetail.setConsignmentStatus("06");
                                       consignmentDetail.setDeviceTypeId(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id());
                                      // consignmentDetail.setReferenceCodes(checkArray.toJSONString());
                                       consignmentDetail.setMainCheckReference(mainCheckReference);
                                        updateConsignmentCommit(consignmentDetail);
                                   }
                                }
                            }
                        }
                )
                .create();
        if (ADD_OR_UPDATE==0)
        alertDialog.setTitle("添加协议");
        else
            alertDialog.setTitle("修改协议");
        alertDialog.setView(dialogView);
        alertDialog.show();
    }
    private void getCheckReference(String deviceTypeCode){
        Map<String,Object> map=new HashMap<>();
       // Log.i(TAG,""+deviceTypeCode);
        map.put("deviceTypeCode",deviceTypeCode);
        checkReferenceList.clear();
        ApiService.GetString(this, "checkReference", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response!=null&&!TextUtils.isEmpty(response)){
                    JSONArray checkReferenceArray=JSONObject.parseArray(response);
                    for (Object checkReferenceObject : checkReferenceArray){
                        JSONObject checkJsonObject=(JSONObject)checkReferenceObject;
                        checkReferenceList.add(new CheckReference(checkJsonObject.getString("referenceId"),checkJsonObject.getString("referencenName"),checkJsonObject.getString("referenceId")));
                        checkReferenceAdapter.isSelected.add(false);
                    }
                    Log.i(TAG,"检验类别"+checkReferenceList.size());
                    checkReferenceAdapter.listItem=checkReferenceList;
                    checkReferenceAdapter.notifyDataSetChanged();
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
    public boolean checkRepate(String deviceTypeName){
        for(int i=0;i<consignmentList.size();i++){
            if(consignmentList.get(i).getDeviceTypeId().equals(deviceTypeName))
                return true;
        }
        return false;
    }
    private void updateConsignmentCommit(final ConsignmentDetail consignment){
        ConsignmentDetail consignmentDetail=consignment;
        for (int i=0;i<deviceTypeList.size();i++){
            if (deviceTypeList.get(i).getDevice_type_name().equals(consignmentDetail.getDeviceTypeId()))
                consignmentDetail.setDeviceTypeId(deviceTypeList.get(i).getDevice_type_id());
        }
        consignmentDetail.setIsPassCheck("1");
        consignmentDetail.setConsignmentStatus("06");
        Map<String,Object> map=new HashMap<>();
        map.put("updatedConsignment",JSON.toJSONString(consignmentDetail));

        map.put("requestCode",ADD_OR_UPDATE);
        ApiService.GetString(this, "orderConsignmentUpdate", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")){
                    consignmentDetail.setMainCheckReference(checkReferenceStr);
                    consignmentDetail.setDeviceTypeId(deviceTypeName);
                    if (ADD_OR_UPDATE==1) {
                        Toast.makeText(OrderCheck.this, "添加成功", Toast.LENGTH_SHORT).show();
                        consignmentList.add(consignmentDetail);
                    }else{
                        Toast.makeText(OrderCheck.this,"修改成功",Toast.LENGTH_SHORT).show();
                        consignmentList.set(updatePosition,consignmentDetail);
                    }
                    mAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(OrderCheck.this,"更新失败",Toast.LENGTH_SHORT).show();
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
    private void submit(){
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("statusCode","23");
        map.put("submissionId",submissionId);
        ApiService.GetString(this, "orderCheck", map, new RxStringCallback() {
        @Override
        public void onNext(Object tag, String response) {
            if (response!=null&&response.equals("true")){

                Toast.makeText(OrderCheck.this,"信息核对完成",Toast.LENGTH_SHORT).show();
             finish();
            }else {
                Toast.makeText(OrderCheck.this,response,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Object tag, Throwable e) {
             Toast.makeText(OrderCheck.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(Object tag, Throwable e) {

        }
    });
    }

    private void getData(){
       View processView=View.inflate(this,R.layout.simple_processbar,null);
        processDialog=new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        consignmentList.clear();
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",orderId);

        ApiService.GetString(this,"orderConsignmentDetail",map, new RxStringCallback(){
            @Override
            public void onError(Object tag, Throwable e) {
              //  processDialog.dismiss();
             Toast.makeText(OrderCheck.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                swipeRefreshLayout.setRefreshing(false);
                //processDialog.dismiss();

                Log.i(TAG,"获取协议成功"+response.toString());
                if (response!=null&&!TextUtils.isEmpty(response)) {
                    List<ConsignmentDetail> list= JSON.parseArray(response, ConsignmentDetail.class);
                    for (int i=0;i<list.size();i++){
                        consignmentList.add(list.get(i));
                    }
                    Log.i(TAG,consignmentList.toString());
                  //  mAdapter.notifyDataSetChanged();
                    Log.i(TAG, consignmentList.size() + "个"+consignmentList.get(0).getConsignmentStatus());

                    if(consignmentList.get(0).getConsignmentStatus().equals("检验现场信息核对完成")){
                        //已经核对过
                       // mAdapter = new TDProtocolCheck.ProtocolAdapter(OrderCheck.this,consignmentList,OrderCheck.this,false,false,false);//得到一个MyAdapter对象
                     //为ListView绑定Adapter
                         NOT_CONFIRMED=false;
                         mAdapter.update=false;
                         mAdapter.notifyDataSetChanged();
                         titleright.setVisibility(View.INVISIBLE);
                        titlerightadd.setVisibility(View.INVISIBLE);
                    }else {
                     mAdapter.update=true;
                     mAdapter.notifyDataSetChanged();
                    }

                }
                getDeviceType();
            }
        });

    }
    private void getDeviceType(){
        Map<String,Object> map=new HashMap<>();
        ApiService.GetString(this, "deviceType", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response!=null) {
                    Log.i("获取deviceType结果成功",response.toString());
                    JSONObject object=JSON.parseObject(response);
                    com.alibaba.fastjson.JSONArray deviceArray= JSONObject.parseArray(object.getString("deviceType"));
                    for (Object objectDevicetype : deviceArray){
                        JSONObject deviceType=(JSONObject)objectDevicetype;
                        deviceTypeList.add(new DeviceType(deviceType.getString("device_type_id"),deviceType.getString("device_type_code"),deviceType.getString("device_type_name")));
                    }
                    DeviceType spinnerHint=new DeviceType();
                    spinnerHint.setDevice_type_name("请选择设备类型");
                    deviceTypeList.add(spinnerHint);
                    //checkReferenceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG,e.getMessage().toString());
                Toast.makeText(OrderCheck.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }
        });
        processDialog.dismiss();
    }




    private void init(){

        titleleft=(ImageButton)this.findViewById(R.id.titleleftch);
        titleright=(ImageButton)this.findViewById(R.id.titlerightch);
        title=(TextView)this.findViewById(R.id.titlecenterch);
        swipeRefreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.refresh_order_check);
        titlerightadd=(ImageButton)this.findViewById(R.id.titlerightadd);

        lv_task=(ListView)this.findViewById(R.id.lv_order_check);

        consignmentList=new ArrayList<>();

        getData();
        mAdapter = new ProtocolAdapter(OrderCheck.this,consignmentList,OrderCheck.this,true,false,false);//得到一个MyAdapter对象

        lv_task.setAdapter(mAdapter);
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
Log.i("OrderCheck","点击列表");
                Intent intent=new Intent(OrderCheck.this,TaskDetails.class);
                intent.putExtra("ItemPosition",arg2);
                intent.putExtra("update",NOT_CONFIRMED);
                intent.putExtra("requestCode",REQUEST_CODE);
                intent.putExtra("deviceType",consignmentList.get(arg2).getDeviceTypeId());
                intent.putExtra("consignmentId",consignmentList.get(arg2).getConsignmentId());
                if (NOT_CONFIRMED)
                startActivityForResult(intent,REQUEST_CODE);
                else
                    startActivity(intent);
            }

        });
        lv_task.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
       Log.i("长按",position+"");
        if (NOT_CONFIRMED) {
            updatePosition=position;
            addAndUpdateConsignment(consignmentList.get(position));
            return true;
        }else
            return false;
            }
        });
        }
    private void deleteConsignment(String consignmentId, final int position){
        Map<String,Object> map=new HashMap<>();
        map.put("consignmentId",consignmentId);
        ApiService.GetString(this, "orderConsignmentDelete", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")){
                    mAdapter.list.remove(position);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(OrderCheck.this,"委托协议删除成功",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG,e.getMessage().toString());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }


    @Override
    public void callBack(View view) {
        switch (view.getId()){
            case R.id.delete_divice:
                final int position=Integer.parseInt((view.getTag()).toString());
                 new AlertDialog.Builder(OrderCheck.this).setTitle("删除委托协议").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         deleteConsignment(consignmentList.get(position).getConsignmentId(),position);

                     }
                 }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 }).create().show();
        break;

        }
    }
}
