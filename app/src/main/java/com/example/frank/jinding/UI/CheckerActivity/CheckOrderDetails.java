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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckOrderDetails extends AppCompatActivity implements View.OnClickListener{

    private String content;

    private ImageButton titleleft,titleright;
    private TextView title;
    private ListView device_lv;
    private static int REQUEST_CODE;
    private static int ITEM_POSITION;
    private  MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order_details);
        initView();

        Intent intent=getIntent();
        REQUEST_CODE=intent.getIntExtra("requestCode",-1);
        ITEM_POSITION=intent.getIntExtra("ItemPosition",-1);
    }
    protected void initView()
    {
        titleleft=(ImageButton)this.findViewById(R.id.titleleft);
        titleright=(ImageButton)this.findViewById(R.id.titleright);
        title=(TextView)this.findViewById(R.id.titlecenter);
        device_lv=(ListView)this.findViewById(R.id.lv_check_order_details);
//标题栏设置
        title.setText("协议详情");
        titleleft.setOnClickListener(this);
        titleright.setVisibility(titleright.INVISIBLE);
        titleright.setOnClickListener(this);
        mAdapter=new MyAdapter(this);
        device_lv.setAdapter(mAdapter);

        String name[]={"起重机","吊车","和泥机","推土机"};
        String stylee[]={"SES235424","SDF324234","SAD34234","WAD2343"};
        String datee[]={"2017/2/15","2017/4/23","2017/6/22","2017/7/11"};
         /*为动态数组添加数据*/

        for(int i=0;i<name.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemId", "设备信息"+ (i+1));
            map.put("ItemName", "设备名称："+ name[0]);
            map.put("ItemStylee", "设备型号："+ stylee[i]);
            map.put("ItemDatee", "购买日期："+ datee[i]);

            mAdapter.listItem.add(map);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleright:
                addDivice();
                break;
            case R.id.titleleft:
                finish();
        }
    }
    /*添加一个得到数据的方法，方便使用*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        String numbertype[]={"起重机","吊车","和泥机","推土机"};
        String number[]={"SES235424","SDF324234","SAD34234","WAD2343"};
        String height[]={"15米","23米","22米","11米"};
        String autoNumber[]={"SES235424","SDF324234","SAD34234","WAD2343"};
        String checktype[]={"高度是否合格","高度是否合格","高度是否合格","高度是否合格",};


        /*为动态数组添加数据*/

        for(int i=0;i<number.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemNumber", "机器编号："+ number[i]);
            map.put("ItemNumberType", "编号类型："+ numbertype[i]);
            map.put("ItemCheckType","检查类型："+checktype[i]);
            map.put("ItemHeight","安装高度："+height[i]);
            map.put("ItemAuroNumber","自编号："+autoNumber[i]);
            listItem.add(map);
        }
        return listItem;

    }
    private void addDivice(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.add_divice, (ViewGroup) findViewById(R.id.add_device));


        final EditText et1 = (EditText)layout.findViewById(R.id.device_number);
        final EditText et2 = (EditText)layout.findViewById(R.id.number_type);
        final EditText et3 = (EditText)layout.findViewById(R.id.check_type);
        final EditText et4 = (EditText)layout.findViewById(R.id.height);
        final EditText et5 = (EditText)layout.findViewById(R.id.auto_number);


        new AlertDialog.Builder(CheckOrderDetails.this).setTitle("添加设备").setView(layout)
                .setPositiveButton("确定", new  DialogInterface.OnClickListener()
                {
                    @Override
                    public  void  onClick(DialogInterface dialog, int  which)
                    {
                        String content="机器类型："+et1.getText();

                        Toast.makeText(CheckOrderDetails.this,"添加成功",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new  DialogInterface.OnClickListener()
                {
                    @Override
                    public  void  onClick(DialogInterface dialog, int  which)
                    {
                    }
                }).show();
    }

    /*private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        *//*构造函数*//*
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public int getCount() {

            return getDate().size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        *//*书中详细解释该方法*//*
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_order_divice,null);
                holder = new CheckOrderDetails.ViewHolder();
                *//*得到各个控件的对象*//*
                //xe
                holder.number = (TextView) convertView.findViewById(R.id.number_type);
                holder.numberType=(TextView)convertView.findViewById(R.id.number_type);
                holder.height=(TextView)convertView.findViewById(R.id.height);
                holder.checkType=(TextView)convertView.findViewById(R.id.check_type);
                holder.autoNumber=(TextView)convertView.findViewById(R.id.auto_number);
                holder.delete_img=(ImageView) convertView.findViewById(R.id.delete_agree);
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (CheckOrderDetails.ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            *//*设置TextView显示的内容，即我们存放在动态数组中的数据*//*

            holder.number.setText(getDate().get(position).get("ItemNumber").toString());
            holder.numberType.setText(getDate().get(position).get("ItemNumberType").toString());
            holder.checkType.setText(getDate().get(position).get("ItemCheckType").toString());
            holder.height.setText(getDate().get(position).get("ItemHeight").toString());

            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            *//*为Button添加点击事件*//*



            holder.delete_img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(CheckOrderDetails.this)
                            .setTitle("系统提示")
                            .setMessage("您确定要删除这个设备吗？")
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
                                            Toast.makeText(CheckOrderDetails.this,"委托协议删除成功！",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });

            return convertView;
        }

    }
    *//*存放控件*//*
    public final class ViewHolder{
        public TextView number,numberType,checkType,height,autoNumber;
        public ImageView delete_img;
    }*/


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
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
            final CheckOrderDetails.ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.protoclo_details_device_adapter,null);
                holder = new CheckOrderDetails.ViewHolder();
                /*得到各个控件的对象*/
                holder.did=(TextView)convertView.findViewById(R.id.textView14);
                holder.name = (TextView) convertView.findViewById(R.id.textView26);
                holder.stylee = (TextView) convertView.findViewById(R.id.textView37);
                holder.datee = (TextView) convertView.findViewById(R.id.textView38);
                holder.delete=(Button) convertView.findViewById(R.id.button57);
                holder.altermodel=(Button) convertView.findViewById(R.id.button58);
                holder.alterdate=(Button) convertView.findViewById(R.id.button64);


                holder.delete.setVisibility(holder.delete.INVISIBLE);
                holder.alterdate.setVisibility(holder.alterdate.INVISIBLE);
                holder.altermodel.setVisibility(holder.altermodel.INVISIBLE);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (CheckOrderDetails.ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.did.setText(listItem.get(position).get("ItemId").toString());
            holder.name.setText(listItem.get(position).get("ItemName").toString());
            holder.stylee.setText(listItem.get(position).get("ItemStylee").toString());
            holder.datee.setText(listItem.get(position).get("ItemDatee").toString());

            holder.delete.setTag(position);
            holder.altermodel.setTag(position);
            holder.alterdate.setTag(position);

            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/



            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(CheckOrderDetails.this)
                            .setTitle("系统提示")
                            .setMessage("您确定要删除这条设备信息吗？")
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
                                            mAdapter.listItem.remove(position);
                                            mAdapter.notifyDataSetChanged();
                                            Toast.makeText(CheckOrderDetails.this,"设备信息删除成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });

            holder.altermodel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    final EditText et=new EditText(CheckOrderDetails.this);
                    new  AlertDialog.Builder(CheckOrderDetails.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入正确的设备型号：")
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

                                            holder.stylee.setText("设备型号："+et.getText().toString().trim());
                                            Toast.makeText(CheckOrderDetails.this,"设备数量更新成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });


       /*     holder.alterdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(ProtocolDetails.this)
                            .setTitle("系统提示")
                            .setMessage("请输入正确的机器型号：")
                            .setView(new EditText(ProtocolDetails.this))
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
                                            holder.stylee.setText("\n机器型号：由于系统原因，更新内容暂时无法显示！");
                                            Toast.makeText(ProtocolDetails.this,"机器型号更新成功！",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });*/


            holder.alterdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    final EditText ett=new EditText(CheckOrderDetails.this);
                    new  AlertDialog.Builder(CheckOrderDetails.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入正确的购买日期：")
                            .setView(ett)
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

                                            holder.datee.setText("购买日期："+ett.getText().toString().trim());
                                            Toast.makeText(CheckOrderDetails.this,"设备数量更新成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });




            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView did,name,stylee,datee;
        public Button delete,altermodel,alterdate;

    }




    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("ItemPosition",ITEM_POSITION);
        intent.putExtra("DeviceNumber",5);
        setResult(REQUEST_CODE,intent);
        super.onBackPressed();
    }
}
