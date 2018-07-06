package com.example.frank.jinding.UI.CheckerActivity;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.Conf.URLConfig;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.Utils.CameraPermissionCompat;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOpinion extends AppCompatActivity {

    private EditText etcontent;
    private Button submit;
    private ImageButton back;
    private TextView title;
    private List<String> list = new ArrayList<String>();
    private Spinner opionsp;
    private ArrayAdapter<String> spadapter;

    private String consignmentId="",submission_id="",device_id="",instrment_codes="",exam_result="",problem_suggestion="";
    private MyAdapter mAdapter;
    private String orderId="";
    private Button add_recheck,add_opinion_photo,look_opinion;
    private ListView lv_rcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_opinion);

        Intent intent=getIntent();
        submission_id=intent.getStringExtra("submission_id");
        orderId=intent.getStringExtra("orderId");
        device_id=intent.getStringExtra("deviceId");
        consignmentId=intent.getStringExtra("consignmentId");

        init();

        //标题栏设置
        title.setText("检验意见");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //mySpinner.setSelection(0,false);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中

        opionsp.setOnItemSelectedListener(new  Spinner.OnItemSelectedListener(){
            public  void  onItemSelected(AdapterView<?> arg0, View  arg1, int  arg2, long  arg3)  {
                //  TODO  Auto-generated  method  stub

                if (opionsp.getSelectedItem().equals("需复检（待确认）")){
                    add_recheck.setVisibility(View.VISIBLE);
                    lv_rcheck.setVisibility(View.VISIBLE);
                    etcontent.setVisibility(View.INVISIBLE);
                    add_opinion_photo.setVisibility(View.VISIBLE);
                    look_opinion.setVisibility(View.VISIBLE);

                }else if (opionsp.getSelectedItem().equals("请选择检验结论：")){
                    add_recheck.setVisibility(View.INVISIBLE);
                    lv_rcheck.setVisibility(View.INVISIBLE);
                    etcontent.setVisibility(View.INVISIBLE);
                    add_opinion_photo.setVisibility(View.INVISIBLE);
                    look_opinion.setVisibility(View.INVISIBLE);
                }else {
                    add_recheck.setVisibility(View.INVISIBLE);
                    lv_rcheck.setVisibility(View.INVISIBLE);
                    etcontent.setVisibility(View.VISIBLE);
                    add_opinion_photo.setVisibility(View.VISIBLE);
                    look_opinion.setVisibility(View.VISIBLE);
                }



            }
            public  void  onNothingSelected(AdapterView<?>  arg0)  {
                //  TODO  Auto-generated  method  stub
                //myTextView.setText("NONE");
            }
        });


        //*下拉菜单弹出的内容选项触屏事件处理*//*
        opionsp.setOnTouchListener(new  Spinner.OnTouchListener(){
            public  boolean  onTouch(View  v,  MotionEvent event)  {
                //  TODO  Auto-generated  method  stub

                return  false;
            }
        });
        opionsp.setOnFocusChangeListener(new  Spinner.OnFocusChangeListener(){
            public  void  onFocusChange(View  v,  boolean  hasFocus)  {
                //  TODO  Auto-generated  method  stub

            }
        });

        add_recheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et=new EditText(CheckOpinion.this);
               // et.setBackground(R.drawable.editext_shape);
                et.setHint("请输入条目内容！");
                new  AlertDialog.Builder(CheckOpinion.this)
                        .setTitle("添加复检条目")
                        //.setMessage("\n请修改新的检验情况说明：")
                        .setView(et)
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

                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        map.put("ItemNumber", (mAdapter.listItem.size()+1)+"");
                                        map.put("ItemText", et.getText());
                                        mAdapter.listItem.add(map);
                                        mAdapter.notifyDataSetChanged();



                                    }
                                }).show();
            }
        });



        add_opinion_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean permission= CameraPermissionCompat.checkCameraPermission(CheckOpinion.this, new CameraPermissionCompat.OnCameraPermissionListener() {
                    @Override
                    public void onGrantResult(boolean granted) {

                        Log.i("相机权限：",granted+"");
                    }
                });

                if (permission) {
                    Intent intent = new Intent(CheckOpinion.this, Opinion_Recorde.class);
                    intent.putExtra("consignmentId", consignmentId);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("deviceId",device_id);
                    startActivity(intent);
                    //startActivityForResult(intent, 5201);
                }
                else {
                    new AlertDialog.Builder(CheckOpinion.this).setTitle("系统提示").setMessage("您还没有给该应用赋予拍照的权限，请前往手机设置里面手动赋予该应用相机权限").show();
                }
            }
        });


        look_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent11=new Intent(CheckOpinion.this,OpinionPicturesActivity.class);
                intent11.putExtra("consignmentId", consignmentId);
                intent11.putExtra("orderId", orderId);
                intent11.putExtra("deviceId",device_id);
                startActivity(intent11);
                //getOpinionPhoto();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (instrment_codes.trim().equals("")||instrment_codes.trim().length()<2){
                    getInstrumentCodes();
                }*/

                if (opionsp.getSelectedItem().equals("需复检（待确认）")){
                    submit.setEnabled(false);
                    exam_result = (opionsp.getSelectedItemId() - 1) + "";
                    problem_suggestion = "该设备需要复检，详情条目见表详情！";
                    String data = submission_id + "#" + device_id + "#" + exam_result + "#" + problem_suggestion;
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", data);
                    ApiService.GetString(CheckOpinion.this, "addCheckOpinionResult", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (response.trim().equals("重复提交")) {
                                CheckControl.start=true;
                                Toast.makeText(CheckOpinion.this, "该台设备已经提交过检测意见，请查看审核结果", Toast.LENGTH_SHORT).show();

                            }
                            if (response.trim().equals("提交失败！")){
                                Toast.makeText(CheckOpinion.this, "提交失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                            else if (response.trim().equals("session为空")){
                                Toast.makeText(CheckOpinion.this, "提交失败，需要重新登录", Toast.LENGTH_SHORT).show();
                            }
                            else if (!response.trim().equals("")){
                                if (response.trim().equals("device")){

                                    CheckControl.device_finish=true;
                                }
                                if (response.trim().equals("protocol")){

                                    CheckControl.device_finish=true;
                                    CheckControl.protocol_finish=true;
                                }
                                if (response.trim().equals("order")){
                                    CheckControl.device_finish=true;
                                    CheckControl.protocol_finish=true;
                                    CheckControl.order_finish=true;
                                }



                                CheckControl.start=true;

                                String recheckdata="";

                                for (int k=0;k<mAdapter.listItem.size();k++){
                                    HashMap<String, Object> map =mAdapter.listItem.get(k);
                                    recheckdata=recheckdata+map.get("ItemNumber")+"##"+map.get("ItemText")+"##";
                                }


                                String data = orderId + "##" + consignmentId + "##" + device_id + "##" + recheckdata;
                                Map<String, Object> paremetes = new HashMap<>();
                                paremetes.put("data", data);
                                ApiService.GetString(CheckOpinion.this, "recheckInfomation", paremetes, new RxStringCallback() {
                                    boolean flag = false;

                                    @Override
                                    public void onNext(Object tag, String response) {

                                        if (response.trim().equals("")){
                                            etcontent.setText("");
                                            CheckControl.start = true;
                                            submit.setEnabled(false);
                                            Toast.makeText(CheckOpinion.this, "检测意见提交成功，请等待审核结果", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onError(Object tag, Throwable e) {
                                        Toast.makeText(CheckOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();
                                        submit.setEnabled(true);

                                    }

                                    @Override
                                    public void onCancel(Object tag, Throwable e) {
                                        Toast.makeText(CheckOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();
                                        submit.setEnabled(true);
                                    }


                                });


                            } else {
                                submit.setEnabled(true);
                                Toast.makeText(CheckOpinion.this, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(CheckOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();
                            submit.setEnabled(true);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(CheckOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();
                            submit.setEnabled(true);
                        }


                    });




                }else {

                    if (opionsp.getSelectedItemId() != 0 && etcontent.getText().toString().trim().length() > 2) {


                        new AlertDialog.Builder(CheckOpinion.this)
                                .setTitle("系统提示")
                                .setMessage("\n请核对检测意见\n\n检测结论：" + opionsp.getSelectedItem() + "\n\n检测意见：" + etcontent.getText() + "\n\n\n\n确认无误后，点击“确定”进行提交，点击“取消”返回修改")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                submit.setEnabled(false);
                                                exam_result = (opionsp.getSelectedItemId() - 1) + "";
                                                problem_suggestion = etcontent.getText().toString();
                                                String data = submission_id + "#" + device_id  + "#" + exam_result + "# " + problem_suggestion+" #"+orderId;
                                                Map<String, Object> paremetes = new HashMap<>();
                                                paremetes.put("data", data);
                                                ApiService.GetString(CheckOpinion.this, "addCheckOpinionResult", paremetes, new RxStringCallback() {
                                                    boolean flag = false;

                                                    @Override
                                                    public void onNext(Object tag, String response) {

                                                        if (response.trim().equals("重复提交")) {
                                                            Toast.makeText(CheckOpinion.this, "该台设备已经提交过检测意见，请查看审核结果", Toast.LENGTH_SHORT).show();
                                                            CheckControl.start=true;
                                                        }
                                                        else if (response.trim().equals("提交失败！")){
                                                            Toast.makeText(CheckOpinion.this, "提交失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else if (response.trim().equals("session为空")){
                                                            Toast.makeText(CheckOpinion.this, "提交失败，需要重新登录", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            if (response.trim().equals("device")){

                                                                CheckControl.device_finish=true;
                                                            }
                                                            if (response.trim().equals("protocol")){

                                                                CheckControl.device_finish=true;
                                                                CheckControl.protocol_finish=true;
                                                            }
                                                            if (response.trim().equals("order")){
                                                                CheckControl.device_finish=true;
                                                                CheckControl.protocol_finish=true;
                                                                CheckControl.order_finish=true;
                                                            }


                                                            etcontent.setText("");
                                                            CheckControl.start = true;
                                                            submit.setEnabled(false);
                                                            Toast.makeText(CheckOpinion.this, "检测意见提交成功，请等待审核结果", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Object tag, Throwable e) {
                                                        Toast.makeText(CheckOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();
                                                        submit.setEnabled(true);

                                                    }

                                                    @Override
                                                    public void onCancel(Object tag, Throwable e) {
                                                        Toast.makeText(CheckOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();
                                                        submit.setEnabled(true);
                                                    }


                                                });


                                            }
                                        }).show();
                    } else if (etcontent.getText().toString().trim().length() < 3) {
                        new AlertDialog.Builder(CheckOpinion.this)
                                .setTitle("系统提示")
                                .setMessage("\n请填写不少于3个字符的检测意见")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                    } else {
                        new AlertDialog.Builder(CheckOpinion.this)
                                .setTitle("系统提示")
                                .setMessage("\n请选择检验结论")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                    }
                }

            }
        });



    }

    private void init(){
        etcontent=(EditText)this.findViewById(R.id.editText4);
        submit=(Button)this.findViewById(R.id.button24);
        add_recheck=(Button)this.findViewById(R.id.btn_add_recheck_item);
        add_opinion_photo=(Button)this.findViewById(R.id.btn_opinion_photo);
        look_opinion=(Button)this.findViewById(R.id.btn_look_opinion);
        lv_rcheck=(ListView)this.findViewById(R.id.lv_recheck_item);

        opionsp=(Spinner)this.findViewById(R.id.opinion_spinner);
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_rcheck.setAdapter(mAdapter);//为ListView绑定Adapter


        list.add("请选择检验结论：");
        list.add("不合格");
        list.add("合格");
        list.add("需复检（待确认）");


        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        spadapter  =  new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,  list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        opionsp.setAdapter(spadapter);

        //getInstrumentCodes();

    }

    private void getInstrumentCodes(){


        Map<String, Object> paremetes = new HashMap<>();
        paremetes.put("data", submission_id);
        ApiService.GetString(CheckOpinion.this, "instrumentCodes", paremetes, new RxStringCallback() {
            boolean flag = false;

            @Override
            public void onNext(Object tag, String response) {

                if ((response.trim().split(":")[0]).equals("true")) {
                    instrment_codes=response.trim().split(":")[1];
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(CheckOpinion.this, "" + e, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                Toast.makeText(CheckOpinion.this, "" + e, Toast.LENGTH_SHORT).show();

            }


        });

    }




    private void getOpinionPhoto(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    String data=orderId+"#"+consignmentId+"#"+device_id;
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(CheckOpinion.this, "getDeviceCheckOpinion", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (!response.trim().equals("没有记录")&&!response.trim().equals("上传失败！")) {
                                //Toast.makeText(SelectEquipment.this, "获取到的数据：" + response, Toast.LENGTH_SHORT).show();
                                //String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
                                   //String data[]=response.split("##");
                                    //ff.download(CheckOpinion.this,orderId+"/"+consignmentId+"/"+deviceId+"/",data[i]+".jpg");
                                String imgurl= URLConfig.CompanyURL+orderId+"/"+consignmentId+"/"+device_id+"/"+response.trim()+".jpg";

                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.dialog_report, (ViewGroup) findViewById(R.id.dalog_report));
                                final ImageView img = (ImageView) layout.findViewById(R.id.imageView6);
                                final TextView textv =(TextView) layout.findViewById(R.id.textView34);

                                Glide.with(CheckOpinion.this).load(imgurl).into(img);
                                //textv.setText("现场检验意见");
                                new AlertDialog.Builder(CheckOpinion.this).setTitle("现场检验意见").setView(layout)
                                        .setPositiveButton("确定", new  DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public  void  onClick(DialogInterface dialog, int  which)
                                            {

                                            }
                                        })
                                        .setNegativeButton("取消", new  DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public  void  onClick(DialogInterface dialog, int  which)
                                            {
                                            }
                                        }).show();



                            }else {
                                Toast.makeText(CheckOpinion.this, "没有记录" , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(CheckOpinion.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(CheckOpinion.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }





    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        //ArrayList<HashMap<String, Object>> listItem = CheckInfo.listItem;
        private  ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
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
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.add_recheck_item,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.number = (TextView) convertView.findViewById(R.id.text_recheck_number);
                holder.content=(TextView) convertView.findViewById(R.id.text_recheck_content_rc);
                holder.delete=(Button)convertView.findViewById(R.id.btn_delete_recheck_item);


                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }


            holder.number.setText(listItem.get(position).get("ItemNumber").toString());
            holder.content.setText(listItem.get(position).get("ItemText").toString());


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int k=position;k+1<mAdapter.listItem.size();k++){
                        HashMap<String, Object> dd=mAdapter.listItem.get(k+1);
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("ItemNumber", (Integer.parseInt(dd.get("ItemNumber").toString())-1)+"");
                        map.put("ItemText", dd.get("ItemText"));
                        int tt=k+1;
                        mAdapter.listItem.remove(tt);
                        mAdapter.listItem.add(map);
                    }
                    mAdapter.listItem.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });







            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView number,content;
        public Button delete;

    }



}
