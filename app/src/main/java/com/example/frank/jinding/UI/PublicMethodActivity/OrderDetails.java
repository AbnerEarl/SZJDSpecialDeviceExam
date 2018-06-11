package com.example.frank.jinding.UI.PublicMethodActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.frank.jinding.Adapter.QAdapter;
import com.example.frank.jinding.Adapter.SpinnerAdapter;
import com.example.frank.jinding.Bean.OrderBean.CheckReference;
import com.example.frank.jinding.Bean.OrderBean.ConsignmentDetail;
import com.example.frank.jinding.Bean.OrderBean.DeviceType;
import com.example.frank.jinding.Bean.OrderBean.OrderDeviceDetail;
import com.example.frank.jinding.Interface.CallBack;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetails extends AppCompatActivity implements CallBack {


    private ListView lv_task;

    private ImageButton titleleft, titleright;
    private TextView title;
    public static String TAG = "OrderDetail";
    private static int REQUEST_CODE = 0x0d;
    private int ADD_OR_UPDATE=1;
    private MyAdapter adapter;
    private List<ConsignmentDetail> consignmentList=new ArrayList<>();
    private List<OrderDeviceDetail> deviceList;
    private List<CheckReference> checkReferenceList=new ArrayList<>();
    private List<DeviceType> deviceTypeList=new ArrayList<>();
    private static boolean update=false;
    private String orderId;
    private android.support.v7.app.AlertDialog processDialog;
    private List<String> checkReferenceCode=new ArrayList<>();
    private String mainCheckReference;
    private String checkReferenceStr="";
    QAdapter checkReferenceAdapter;
    SpinnerAdapter deviceTypeAdapter;
    private ListView checkReferenceLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.order_details_title);
        Intent intent=getIntent();
        update=intent.getBooleanExtra("update",false);
          orderId=intent.getStringExtra("orderId");
         init();
        getData();
         consignmentList=new ArrayList<>();
         adapter = new MyAdapter(this,consignmentList,this);//得到一个MyAdapter对象
         lv_task.setAdapter(adapter);//为ListView绑定Adapter

        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//
                Intent intent = new Intent(OrderDetails.this, TaskDetails.class);
                intent.putExtra("requestCode", REQUEST_CODE);
                intent.putExtra("ItemPosition", arg2);
                intent.putExtra("update",update);
                intent.putExtra("consignmentId",consignmentList.get(arg2).getConsignmentId());
                intent.putExtra("deviceType",consignmentList.get(arg2).getDeviceTypeId());
                startActivityForResult(intent,REQUEST_CODE);

            }
        });
        lv_task.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("长按",position+"");
                if (update) {
                 //   addOrUpdateConsignment(consignmentList.get(position));
                    return true;
                }else
                    return false;
            }
        });

      if (!update)
          titleright.setVisibility(titleright.INVISIBLE);
        titleright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    addOrUpdateConsignment(null);
            }
        });

        titleleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
   /* private void getCheckReference(String deviceTypeCode){
        Map<String,Object> map=new HashMap<>();
        Log.i(TAG,""+deviceTypeCode);
        map.put("deviceTypeCode",deviceTypeCode);
        checkReferenceList.clear();
        ApiService.GetString(this, "checkReference", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response!=null&&!TextUtils.isEmpty(response)){
                    JSONArray checkReferenceArray=JSONObject.parseArray(response);
                    for (Object checkReferenceObject : checkReferenceArray){
                        JSONObject checkJsonObject=(JSONObject)checkReferenceObject;
                        checkReferenceList.add(new CheckReference(checkJsonObject.getString("reference_id"),checkJsonObject.getString("reference_name"),checkJsonObject.getString("reference_code")));
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
    private void addOrUpdateConsignment(final ConsignmentDetail consignmentDetail){
        if (consignmentDetail==null){
            //添加协议
            ADD_OR_UPDATE=1;
        }else {
            //更新协议
            ADD_OR_UPDATE=2;
        }
        View dialogView=View.inflate(OrderDetails.this,R.layout.add_protocol_dialog,null);
        final Spinner deviceTypeSpinner=(Spinner)dialogView.findViewById(R.id.add_protocol_deviceType_spinner);
        deviceTypeAdapter=new SpinnerAdapter<DeviceType>(OrderDetails.this,R.layout.spinner_dropdown_item,deviceTypeList);
        deviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        deviceTypeSpinner.setAdapter(deviceTypeAdapter);
        deviceTypeSpinner.setSelection(deviceTypeList.size()-1,true);
        final TextView checkReferenceTv=(TextView) dialogView.findViewById(R.id.add_protocol_checkReference_et);
      //  final EditText checkPriceEt=(EditText)dialogView.findViewById(R.id.check_price);


        checkReferenceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((DeviceType)deviceTypeSpinner.getSelectedItem()).getDevice_type_code()==null){
                 Toast.makeText(OrderDetails.this,"请先选择设备类型",Toast.LENGTH_SHORT).show();
                }else {
                    getCheckReference(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_code());
                    checkReferenceLv.setAdapter(checkReferenceAdapter);

                    final AlertDialog dialog = new AlertDialog.Builder(OrderDetails.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ViewGroup) checkReferenceLv.getParent()).removeView(checkReferenceLv);
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkReferenceStr = "";

                            checkReferenceTv.setText(checkReferenceStr);
                            checkReferenceCode.add(mainCheckReference);
                            ((ViewGroup) checkReferenceLv.getParent()).removeView(checkReferenceLv);
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.setTitle("选择检验依据");
                    dialog.setView(checkReferenceLv);
                    dialog.show();
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(OrderDetails.this)
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
                                    Toast.makeText(OrderDetails.this,"请选择设备类型",Toast.LENGTH_SHORT).show();
                                else if(checkRepate(((DeviceType)deviceTypeSpinner.getSelectedItem()).getDevice_type_name())){
                                    Toast.makeText(OrderDetails.this,"请勿重复添加",Toast.LENGTH_SHORT).show();

                                }
                                else if (checkReferenceTv.getText()==null|| TextUtils.isEmpty(checkReferenceTv.getText().toString()))
                                    Toast.makeText(OrderDetails.this,"请选择检验依据",Toast.LENGTH_SHORT).show();
                                else {
                                    Float checkCharge=new Float(0);
                                       if (ADD_OR_UPDATE==1) {
                                           ConsignmentDetail consignmentDetail = new ConsignmentDetail(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id(), 0);
                                           consignmentDetail.setOrderId(orderId);
                                           consignmentDetail.setIsPassCheck("1");
                                           consignmentDetail.setConsignmentStatus("01");
                                           consignmentDetail.setMainCheckReference(mainCheckReference);
                                           consignmentDetail.setCheckCharge(checkCharge);
                                           Log.i(TAG, "修改协议");
                                           addOrUpdateConsignmentCommit(consignmentDetail);
                                       }else {
                                           consignmentDetail.setDeviceTypeId(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id());
                                           consignmentDetail.setMainCheckReference(mainCheckReference);
                                           addOrUpdateConsignmentCommit(consignmentDetail);
                                       }
                                }
                            }
                        }
                )
                .create();
        if (ADD_OR_UPDATE==1)
        alertDialog.setTitle("添加协议");
        else
            alertDialog.setTitle("修改协议");
        alertDialog.setView(dialogView);
        alertDialog.show();

    }
    public boolean checkRepate(String deviceTypeName){
        for(int i=0;i<consignmentList.size();i++){
            if(consignmentList.get(i).getDeviceTypeId().equals(deviceTypeName))
                return true;
        }
        return false;
    }
    private void addOrUpdateConsignmentCommit(final ConsignmentDetail consignment){
        ConsignmentDetail consignmentDetail=consignment;
        for (int i=0;i<deviceTypeList.size();i++){
            if (deviceTypeList.get(i).getDevice_type_name().equals(consignmentDetail.getDeviceTypeId()))
                consignmentDetail.setDeviceTypeId(deviceTypeList.get(i).getDevice_type_id());
        }
        consignmentDetail.setIsPassCheck("1");
        consignmentDetail.setConsignmentStatus("03");
        Map<String,Object> map=new HashMap<>();
        map.put("updatedConsignment",JSON.toJSONString(consignmentDetail));

        map.put("requestCode",ADD_OR_UPDATE);
        ApiService.GetString(this, "orderConsignmentUpdate", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")){
                    getData();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }*/
    private void getData(){
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        processDialog=new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String,Object> map=new HashMap<>();
        map.put("orderId",orderId);
        consignmentList.clear();
        ApiService.GetString(this,"orderConsignmentDetail",map, new RxStringCallback(){
            @Override
            public void onError(Object tag, Throwable e) {
                processDialog.dismiss();
             Toast.makeText(OrderDetails.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                Log.i(TAG,"获取协议成功"+response.toString());
                if (response!=null) {
                    List<ConsignmentDetail> list=JSON.parseArray(response, ConsignmentDetail.class);
                    for (int i=0;i<list.size();i++){
                        consignmentList.add(list.get(i));
                    }
                    Log.i(TAG,consignmentList.toString());
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, consignmentList.size() + "个");
                    //getDeviceType();
                }else {
                    new AlertDialog.Builder(OrderDetails.this).setTitle("委托协议为空").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }






        });

    }
    /*private void getDeviceType(){
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
                    checkReferenceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG,e.getMessage().toString());
                Toast.makeText(OrderDetails.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }
        });
        processDialog.dismiss();
    }*/
    private void init() {
        lv_task = (ListView) this.findViewById(R.id.lv_order_details);
        titleleft = (ImageButton) this.findViewById(R.id.titleleft);
        titleright = (ImageButton) this.findViewById(R.id.titleright);
        checkReferenceLv=new ListView(this);
        checkReferenceAdapter=new QAdapter(this);
        title = (TextView) this.findViewById(R.id.titlecenter);
        title.setText("委托协议");
    }

    @Override
    public void callBack(View view) {
     /*   final int position = (Integer) view.getTag();
        //打印Button的点击信息
        new AlertDialog.Builder(OrderDetails.this)
                .setTitle("系统提示")
                .setMessage("您确定要删除这条委托协议吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             if (REQUEST_CODE==AddOrder.ADDORDER_REQUEST_CODE){
                                consignmentList.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(OrderDetails.this, "委托协议删除成功", Toast.LENGTH_SHORT).show();}
                                else {
                                 deleteConsignment(consignmentList.get(position).getConsignmentId(),position);
                             }
                            }
                        }).show();
*/
    }
  /*  private void deleteConsignment(String consignmentId, final int position){
        Map<String,Object> map=new HashMap<>();
        map.put("consignmentId",consignmentId);
        ApiService.GetString(this, "orderConsignmentDelete", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")){
                   consignmentList.remove(position);
                   adapter.notifyDataSetChanged();
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
*/
    /*
    * 新建一个类继承BaseAdapter，实现视图与数据的绑定
    */
    private class MyAdapter extends BaseAdapter implements View.OnClickListener {
        private List<ConsignmentDetail> list;

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private CallBack callBack;

        /*构造函数*/
        public MyAdapter(Context context, List<ConsignmentDetail> consignmentList, CallBack callBack) {
            this.mInflater = LayoutInflater.from(context);
            this.list = consignmentList;
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
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.order_details, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                //xe
                holder.number = (TextView) convertView.findViewById(R.id.textView10);
                holder.stylee = (TextView) convertView.findViewById(R.id.textView7);

                holder.delete_Ibn = (ImageButton) convertView.findViewById(R.id.delete_divice);
                holder.checkReferenceTv=(TextView)convertView.findViewById(R.id.check_reference);
                holder.checkPrice=(TextView)convertView.findViewById(R.id.check_price);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
          /*  for (int i=0;i<deviceTypeList.size();i++){

                    holder.stylee.setTag(list.get(position).getDeviceTypeId());

            }*/
            holder.stylee.setText( list.get(position).getDeviceTypeId());
            holder.number.setText( list.get(position).getDeviceNum()+"");
            Log.i(TAG,holder.stylee.getText().toString());
            holder.checkReferenceTv.setText(list.get(position).getMainCheckReference());
            holder.checkPrice.setText(list.get(position).getCheckCharge()+"元");
            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

            holder.delete_Ibn.setOnClickListener(this);
            holder.delete_Ibn.setTag(position);
            if (!update){
                holder.delete_Ibn.setVisibility(View.INVISIBLE);
            }
       //     holder.delete_Ibn.setVisibility(holder.delete_Ibn.INVISIBLE);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            callBack.callBack(v);
        }

        class ViewHolder {
            public TextView stylee, number;
            public ImageButton delete_Ibn;
            public TextView checkReferenceTv;
            public TextView checkPrice;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"回调");
        if (requestCode == 0x0d) {if (update){
            int position=data.getIntExtra("ItemPosition",-1);
            Bundle bundle=data.getExtras();
            consignmentList.get(position).setDeviceNum(data.getIntExtra("DeviceNumber",0));
            consignmentList.get(position).setCheckCharge(data.getFloatExtra("checkCharge",new Float(0)));
            adapter.notifyDataSetChanged();
        }else {

        }
        }
    }
}
