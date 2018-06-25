package com.example.frank.jinding.UI.SalesmanActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Adapter.SpinnerAdapter;
import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.Bean.OrderBean.CheckReference;
import com.example.frank.jinding.Bean.OrderBean.ConsignmentDetail;
import com.example.frank.jinding.Bean.OrderBean.DeviceType;
import com.example.frank.jinding.Bean.OrderBean.OrderDeviceDetail;
import com.example.frank.jinding.Interface.CallBack;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderSearch;
import com.example.frank.jinding.UI.PublicMethodActivity.TaskDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrder extends AppCompatActivity implements View.OnClickListener,CallBack {

    private ImageView image_back;
    private Button  adduser, addevice,titleBtn;
    private TextView commit,user_information,protocol_info;
    private ListView listView;
    private ListView checkReferenceLv;
    private MyAdapter adapter;
    private List<ConsignmentDetail> consignmentList;
    private List<CheckReference> checkReferenceList=new ArrayList<>();
    private List<DeviceType> deviceTypeList=new ArrayList<>();
    public static String TAG="AddOrder";
    public static int ADDORDER_REQUEST_CODE=0x02;
    private CheckOrder checkOrder;
    private int requestCode=0x02;
    private List<Boolean> isSelected=new ArrayList<>();
    private List<String> checkReferenceCode=new ArrayList<>();
    private String checkReferenceStr="";
    private String mainCheckReference;
    private int ADD_OR_UPDATE=1;
    QAdapter checkReferenceAdapter;
    SpinnerAdapter deviceTypeAdapter;
     boolean submitClick=true;
     private boolean update=true;
     private String deviceTypeName;
     private int updatePosition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        init();
        initData();
    }

    private void init() {
        image_back=(ImageView)this.findViewById(R.id.image_back);
        user_information=(TextView) this.findViewById(R.id.user_info);
        protocol_info=(TextView)this.findViewById(R.id.protocol_info);
        commit = (TextView) this.findViewById(R.id.button54);
        adduser = (Button) this.findViewById(R.id.button52);
        addevice = (Button) this.findViewById(R.id.button53);
        titleBtn=(Button)this.findViewById(R.id.title_button);
        listView = (ListView) findViewById(R.id.lv_order_agreement);
        checkReferenceLv=new ListView(this);
        checkReferenceLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        checkReferenceAdapter=new QAdapter(AddOrder.this);
        commit.setOnClickListener(this);
        image_back.setOnClickListener(this);
        adduser.setOnClickListener(this);
        addevice.setOnClickListener(this);
        user_information.setOnClickListener(this);
        consignmentList=new ArrayList<>();
        adapter=new MyAdapter(this, consignmentList,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Log.i(TAG,"点击item"+position);
                 Intent intent=new Intent(AddOrder.this,TaskDetails.class);
              intent.putExtra("content","任务编号："+position);
              intent.putExtra("requestCode",requestCode);
              intent.putExtra("ItemPosition",position);
              Bundle bundle=new Bundle();
              bundle.putSerializable("consignment",consignmentList.get(position));
              intent.putExtras(bundle);
              if (requestCode!= OrderSearch.LOOK_REQUEST_CODE){
                  intent.putExtra("update",true);
              }
              if (requestCode!=ADDORDER_REQUEST_CODE){
                  intent.putExtra("consignmentId",consignmentList.get(position).getConsignmentId());
                  intent.putExtra("deviceType",consignmentList.get(position).getDeviceTypeId());
              }
              startActivityForResult(intent,requestCode);
          }
      });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (update){
                    addConsignment(consignmentList.get(position),position);
                    return true;
                }
                return false;
            }
        });

    }
    private void getData(){
        consignmentList.clear();

        Map<String,Object> map=new HashMap<>();
        map.put("orderId",checkOrder.getOrderId());
        //显示审核未通过协议
        map.put("requestCode",1+"");
        ApiService.GetString(this,"orderConsignmentDetail",map, new RxStringCallback(){
            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(AddOrder.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onNext(Object tag, String response) {
                Log.i(TAG,"获取协议成功"+response.toString());
                if (response!=null) {
                    List<ConsignmentDetail> list= JSON.parseArray(response, ConsignmentDetail.class);
                    if (list!=null){
                        if (list.size()!=0) {
                            for (int i = 0; i < list.size(); i++) {
                                consignmentList.add(list.get(i));
                            }
                            protocol_info.setVisibility(View.GONE);
                        }
                }else {
                    if (requestCode==OrderSearch.UPDATE_REQUEST_CODE)
                        addevice.setVisibility(View.VISIBLE);
                        protocol_info.setVisibility(View.VISIBLE);
                    }
                    Log.i(TAG,consignmentList.toString());
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, consignmentList.size() + "个");
                }
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
    private void initData(){
      Intent intent=getIntent();
      if (intent.getIntExtra("requestCode",0x02)!=0x02){
          checkOrder=(CheckOrder) intent.getSerializableExtra("checkOrder");
          requestCode=intent.getIntExtra("requestCode",0x02);
          adduser.setVisibility(View.GONE);
          if (requestCode==OrderSearch.LOOK_REQUEST_CODE){
              addevice.setVisibility(View.INVISIBLE);
              commit.setVisibility(View.INVISIBLE);
             titleBtn.setText("订单查看");
             update=false;
          }else {
              titleBtn.setText("订单修改");
          }
          user_information.setText("所在地区：" +checkOrder.getProvince()+checkOrder.getCity()+checkOrder.getArea() + "\n委托单位：" + checkOrder.getOrderOrg() + "\n工程名称：" + checkOrder.getProjectName() + "\n工程地址：" +checkOrder.getProjectAddress());
          user_information.setTextSize(16);
          adduser.setVisibility(View.INVISIBLE);

          getData();
      }

      if (requestCode!=OrderSearch.LOOK_REQUEST_CODE)
          getDeviceType();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("AddOrder", "回调");
        if (requestCode ==0x01&&data.getBooleanExtra("isSaved",false) ) {
            checkOrder=(CheckOrder) data.getSerializableExtra("checkOrder");
           if (checkOrder!=null) {
                user_information.setText("所在地区：" +checkOrder.getProvince()+checkOrder.getCity()+checkOrder.getArea() + "\n委托单位：" + checkOrder.getOrderOrg() + "\n工程名称：" + checkOrder.getProjectName() + "\n工程地址：" +checkOrder.getProjectAddress());
                user_information.setTextSize(16);
                adduser.setVisibility(View.INVISIBLE);
           }
        }else if (requestCode==ADDORDER_REQUEST_CODE||requestCode==OrderSearch.UPDATE_REQUEST_CODE){
            int position=data.getIntExtra("ItemPosition",-1);
            Bundle bundle=data.getExtras();
            ConsignmentDetail consignment=(ConsignmentDetail)bundle.getSerializable("consignment");
            consignmentList.get(position).setDeviceNum(data.getIntExtra("DeviceNumber",0));
            if(requestCode==ADDORDER_REQUEST_CODE)
            {
            if (consignment.getOrderDeviceDetailList()!=null) {
                Log.i(TAG,"设置设备list"+consignment.getOrderDeviceDetailList().size());
                consignmentList.get(position).setOrderDeviceDetailList(consignment.getOrderDeviceDetailList());

            }}
            consignmentList.get(position).setCheckCharge(data.getFloatExtra("checkCharge",0));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.button54:
                if (submitClick){
                submit();
                submitClick=false;
                }
                break;
            case R.id.button52:
                Intent intentUserDetail = new Intent(AddOrder.this,AddOrderInformation.class);
                intentUserDetail.putExtra("checkOrder",checkOrder);
                intentUserDetail.putExtra("requestCode",0x01);
                startActivityForResult(intentUserDetail, 0x01);
                break;
            case R.id.button53:
                if (user_information.getText()==null||TextUtils.isEmpty(user_information.getText())){
                    Toast.makeText(AddOrder.this,"请先添加订单信息",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddOrder.this,AddOrderInformation.class);
                    intent.putExtra("checkOrder",checkOrder);
                    intent.putExtra("requestCode",0x01);
                    startActivityForResult(intent, 0x01);
                }else {
                    addConsignment(null,-1);
                }
                break;
            case R.id.user_info:
                if (checkOrder!=null){
                Intent intent = new Intent(AddOrder.this,AddOrderInformation.class);
                intent.putExtra("checkOrder",checkOrder);
                if (requestCode!=OrderSearch.LOOK_REQUEST_CODE){
                    intent.putExtra("requestCode",0x01);
                startActivityForResult(intent, 0x01);
                }
                else {
                    intent.putExtra("requestCode",requestCode);
                    startActivity(intent);
                }
                }
                break;
            default:
                break;
        }


    }
    private void getCheckReference(String deviceTypeCode){
        Map<String,Object> map=new HashMap<>();
        Log.i(TAG,""+deviceTypeCode);
        map.put("deviceTypeCode",deviceTypeCode);
        checkReferenceList.clear();
        isSelected=new ArrayList<>();
        ApiService.GetString(this, "checkReference", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response!=null&&!TextUtils.isEmpty(response)){
                    JSONArray checkReferenceArray=JSONObject.parseArray(response);
                    for (Object checkReferenceObject : checkReferenceArray){
                        JSONObject checkJsonObject=(JSONObject)checkReferenceObject;
                        checkReferenceList.add(new CheckReference(checkJsonObject.getString("referenceId"),checkJsonObject.getString("referencenName"),checkJsonObject.getString("referenceId")));
                        isSelected.add(false);
                    }
                    Log.i(TAG,"检验类别"+checkReferenceList.size());
                    checkReferenceAdapter.listItem=checkReferenceList;
                    checkReferenceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
           Toast.makeText(AddOrder.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }
    private void addConsignment(final ConsignmentDetail consignmentDetail,final int position){
        if (consignmentDetail==null){
            ADD_OR_UPDATE=1;
        }else {
            ADD_OR_UPDATE=2;
        }
        View dialogView=View.inflate(AddOrder.this,R.layout.add_protocol_dialog,null);
        final Spinner deviceTypeSpinner=(Spinner)dialogView.findViewById(R.id.add_protocol_deviceType_spinner);
        deviceTypeAdapter=new SpinnerAdapter<DeviceType>(AddOrder.this,R.layout.spinner_dropdown_item,deviceTypeList);
        deviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        deviceTypeSpinner.setAdapter(deviceTypeAdapter);
        deviceTypeSpinner.setSelection(deviceTypeList.size()-1,true);
        final TextView checkReferenceTv=(TextView) dialogView.findViewById(R.id.add_protocol_checkReference_et);
        checkReferenceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((DeviceType)deviceTypeSpinner.getSelectedItem()).getDevice_type_code()==null){
                    Toast.makeText(AddOrder.this,"请先选择设备类型",Toast.LENGTH_SHORT).show();
                }else {
                    getCheckReference(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id());
                    checkReferenceLv.setAdapter(checkReferenceAdapter);
                    final AlertDialog dialog = new AlertDialog.Builder(AddOrder.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ViewGroup) checkReferenceLv.getParent()).removeView(checkReferenceLv);
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (isSelected.contains(true)){
                                int position=isSelected.indexOf(true);

                                checkReferenceCode.add(checkReferenceList.get(position).getReference_code());
                                checkReferenceStr =checkReferenceList.get(position).getReference_name();
                                mainCheckReference=checkReferenceList.get(position).getReference_code();
                            }
                            checkReferenceTv.setText(checkReferenceStr);

                            ((ViewGroup) checkReferenceLv.getParent()).removeView(checkReferenceLv);
                            // checkReferenceStr="";
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.setTitle("选择检验依据");
                    dialog.setView(checkReferenceLv);

                    dialog.show();
                }
            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(AddOrder.this)
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
                                    Toast.makeText(AddOrder.this,"请选择设备类型",Toast.LENGTH_SHORT).show();
                                else if(checkRepate(((DeviceType)deviceTypeSpinner.getSelectedItem()).getDevice_type_name())){
                                    Toast.makeText(AddOrder.this,"请勿重复添加",Toast.LENGTH_SHORT).show();
                                }
                                else if (checkReferenceTv.getText()==null|| TextUtils.isEmpty(checkReferenceTv.getText().toString()))
                                    Toast.makeText(AddOrder.this,"请选择检验依据",Toast.LENGTH_SHORT).show();
                                else {
                                    Float checkCharge=new Float(0);
                                    deviceTypeName=((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_name();
                                    if (requestCode==OrderSearch.UPDATE_REQUEST_CODE){
                                       if (ADD_OR_UPDATE==1) {
                                           ConsignmentDetail consignmentDetail = new ConsignmentDetail(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id(), 0);
                                           consignmentDetail.setOrderId(checkOrder.getOrderId());
                                           consignmentDetail.setIsPassCheck("2");
                                           consignmentDetail.setConsignmentStatus(checkOrder.getOrderStatus());
                                         //  consignmentDetail.setReferenceCodes((String) checkArray.get(0));
                                           consignmentDetail.setMainCheckReference(mainCheckReference);
                                           consignmentDetail.setCheckCharge(checkCharge);
                                           Log.i(TAG, "修改协议 ");
                                           addOrUpdateConsignmentCommit(consignmentDetail);
                                       }else if (ADD_OR_UPDATE==2){
                                          // consignmentDetail.setCheckCharge(checkCharge);
                                           consignmentDetail.setDeviceTypeId(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_id());
                                           //consignmentDetail.setReferenceCodes(mainCheckReference);
                                           consignmentDetail.setMainCheckReference(mainCheckReference);
                                           addOrUpdateConsignmentCommit(consignmentDetail);
                                           updatePosition=position;
                                        }
                                    }else {
                                        if (ADD_OR_UPDATE==1) {
                                            ConsignmentDetail consignmentDetail = new ConsignmentDetail(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_name(), 0);
                                            consignmentDetail.setIsPassCheck("2");
                                            consignmentDetail.setConsignmentStatus("01");
                                            consignmentDetail.setCheckCharge(checkCharge);
                                            consignmentDetail.setMainCheckReference(checkReferenceStr);
                                            consignmentList.add(consignmentDetail);
                                            adapter.notifyDataSetChanged();
                                            protocol_info.setVisibility(View.GONE);
                                            // Toast.makeText(AddOrder.this, "添加成功！", Toast.LENGTH_SHORT).show();
                                        }else if (ADD_OR_UPDATE==2){
                                          //  consignmentDetail.setCheckCharge(checkCharge);
                                            consignmentDetail.setDeviceTypeId(((DeviceType) deviceTypeSpinner.getSelectedItem()).getDevice_type_name());
                                            consignmentDetail.setMainCheckReference(checkReferenceStr);
                                            consignmentList.set(position,consignmentDetail);
                                            adapter.notifyDataSetChanged();
                                            }
                                    }
                                }
                            }
                        }
                )
                .create();
        if (ADD_OR_UPDATE==1)
        alertDialog.setTitle("添加协议");
        else {
            alertDialog.setTitle("修改协议");
        }
        alertDialog.setView(dialogView);
        alertDialog.show();
    }
    private void addOrUpdateConsignmentCommit(final ConsignmentDetail consignment){
        ConsignmentDetail consignmentDetail=consignment;
        Map<String,Object> map=new HashMap<>();
        map.put ("updatedConsignment",JSON.toJSONString(consignmentDetail));
        map.put("requestCode",ADD_OR_UPDATE);
        ApiService.GetString(this, "orderConsignmentUpdate", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")){
                    consignmentDetail.setMainCheckReference(checkReferenceStr);
                    consignmentDetail.setDeviceTypeId(deviceTypeName);
                    if (ADD_OR_UPDATE==1) {
                        Toast.makeText(AddOrder.this, "添加成功", Toast.LENGTH_SHORT).show();
                        consignmentList.add(consignmentDetail);
                    }else{
                        Toast.makeText(AddOrder.this,"修改成功",Toast.LENGTH_SHORT).show();
                        consignmentList.set(updatePosition,consignmentDetail);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(AddOrder.this,"添加失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(AddOrder.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

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
                    JSONArray deviceArray= JSONObject.parseArray(object.getString("deviceType"));
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
                Toast.makeText(AddOrder.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }
        });
    }

    @Override
    public void callBack(View view) {
      final  int position=(Integer)view.getTag();
        //打印Button的点击信息
        new AlertDialog.Builder(AddOrder.this)
                .setTitle("系统提示")
                .setMessage("您确定要删除这条委托协议吗？")
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
                                if (requestCode==0x02){
                                    checkReferenceCode.remove(position);
                                consignmentList.remove(position);
                                adapter.notifyDataSetChanged();  Toast.makeText(AddOrder.this, "委托协议删除成功", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    deleteConsignment(consignmentList.get(position).getConsignmentId(),position);
                                }

                            }
                        }).show();

    }
    private void deleteConsignment(String consignmentId, final int position){
        Map<String,Object> map=new HashMap<>();
        map.put("consignmentId",consignmentId);
        ApiService.GetString(this, "orderConsignmentDelete", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")){
                  consignmentList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(AddOrder.this,"委托协议删除成功",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e)
            {
                Log.i(TAG,e.getMessage().toString());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }
    public void checkPosition(int position) {
        for (int i = 0; i < isSelected.size(); i++) {
            if (position == i) {// 设置已选位置
                isSelected.set(i, true);
            } else {
                isSelected.set(i, false);
            }
        }
        checkReferenceAdapter.notifyDataSetChanged();
    }
    public  class QAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<CheckReference> listItem = new ArrayList<>();
        private ViewHolder holder;



        public QAdapter(Activity context) {
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
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.checker_item, null);
                holder.checkerNam= (TextView) convertView.findViewById(R.id.text_checker_name);
                holder.selectinstu = (CheckBox) convertView.findViewById(R.id.checkBox_checker_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkerNam.setText(listItem.get(position).getReference_name());

            holder.selectinstu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (isChecked){
                    checkPosition(position);
                }else{
                       isSelected.set(position,false);
                   }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {//item单击进行单选设置

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    checkPosition(position);
                }
            });
            holder.selectinstu.setChecked(isSelected.get(position));
            return convertView;
        }
        public final class ViewHolder {
            public TextView  checkerNam;
            public CheckBox selectinstu;
        }

    }

    /*

    * 新建一个类继承BaseAdapter，实现视图与数据的绑定
    */
    private class MyAdapter extends BaseAdapter implements View.OnClickListener{
        private List<ConsignmentDetail> list;
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private CallBack callBack;
        /*构造函数*/
        public MyAdapter(Context context, List<ConsignmentDetail> consignmentList, CallBack callBack) {
            this.mInflater = LayoutInflater.from(context);
            this.list=consignmentList;
            this.callBack=callBack;
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
                convertView = mInflater.inflate(R.layout.td_protiocol_check_item, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                //xe
                holder.number = (TextView) convertView.findViewById(R.id.textView10);
                holder.stylee = (TextView) convertView.findViewById(R.id.textView7);
                holder.protocolStatus = (TextView) convertView.findViewById(R.id.protocol_status);
                holder.protocolCheck = (TextView) convertView.findViewById(R.id.protocol_check);
                holder.delete_Ibn = (ImageButton) convertView.findViewById(R.id.delete_divice);
                holder.checkReferenceTv = (TextView) convertView.findViewById(R.id.check_reference);
                holder.checkPrice = (TextView) convertView.findViewById(R.id.check_price);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.protocolCheck.setVisibility(View.GONE);
            holder.stylee.setText(list.get(position).getDeviceTypeId());
            holder.number.setText(list.get(position).getDeviceNum()+"");
            holder.checkReferenceTv.setText(list.get(position).getMainCheckReference());
            holder.checkPrice.setText(list.get(position).getCheckCharge()+"元");
            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

           if (requestCode==OrderSearch.LOOK_REQUEST_CODE)
               holder.delete_Ibn.setVisibility(View.INVISIBLE);
           if (requestCode!=0x02){
               //非添加订单,显示协议状态
            if(list.get(position).getIsPassCheck().equals("0"))
                holder.protocolStatus.setText("已拒绝");
               else {
                   holder.protocolStatus.setText(list.get(position).getConsignmentStatus());
               }
           }
            holder.delete_Ibn.setOnClickListener(this);
            holder.delete_Ibn.setTag(position);
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
            public TextView protocolStatus;
            public TextView protocolCheck;
//        public Button bt_chakan;
//        public Button bt_beizhu;
        }
    }

    /*存放控件*/

 private void submit() {
     if (checkOrder == null) {
         submitClick=true;
         Toast.makeText(this, "请完善订单信息", Toast.LENGTH_SHORT).show();

     } else {
         submitClick=false;

         View processView = View.inflate(this, R.layout.simple_processbar, null);
         final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
         processDialog.setView(processView);
         processDialog.show();
         Map<String, Object> parameters = new HashMap<>();
         CheckOrder checkOrder = this.checkOrder;
         List<ConsignmentDetail> consignmentDetailList = this.consignmentList;
         checkOrder.setConsignmentList(null);
         checkOrder.setOrderStatus("01");
         List<OrderDeviceDetail> orderDeviceDetails = new ArrayList<>();
         parameters.put("checkOrderJson", JSON.toJSON(checkOrder));
         if (requestCode != OrderSearch.UPDATE_REQUEST_CODE) {
             for (int i = 0; i < consignmentList.size(); i++) {
                 consignmentList.get(i).setConsignmentId(i+"");
                 if (consignmentDetailList.get(i).getOrderDeviceDetailList() != null)
                     for (int j = 0; j < consignmentList.get(i).getOrderDeviceDetailList().size(); j++) {
                         consignmentList.get(i).getOrderDeviceDetailList().get(j).setConsignmentId(i+"");
                         orderDeviceDetails.add(consignmentList.get(i).getOrderDeviceDetailList().get(j));
                     }
                 consignmentList.get(i).setMainCheckReference(checkReferenceCode.get(i));
                 consignmentList.get(i).setOrderDeviceDetailList(null);
             }
             parameters.put("requestCode", 1);
             parameters.put("consignmentDetailJson", JSON.toJSONString(consignmentList));
             parameters.put("orderDeviceDetailJson", JSON.toJSONString(orderDeviceDetails));
         } else {
             parameters.put("requestCode", 2);
         }
         ApiService.GetString(this, "addOrUpdateOrder", parameters, new RxStringCallback() {
             @Override
             public void onNext(Object tag, String response) {
                 Log.i(TAG, "成功" + response);
                 processDialog.dismiss();
                 submitClick=true;
                 new AlertDialog.Builder(AddOrder.this)
                         .setTitle("系统提示")
                         .setMessage(response)
                         .setCancelable(false)
                         .setPositiveButton("确定",
                                 new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         finish();
                                     }
                                 }).show();

             }

             @Override
             public void onError(Object tag, Throwable e) {
                 submitClick=true;
                 Log.i(TAG, "失败");
                 Toast.makeText(AddOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onCancel(Object tag, Throwable e) {

             }
         });

     }
     }

}

