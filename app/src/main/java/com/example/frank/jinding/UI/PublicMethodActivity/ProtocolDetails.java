package com.example.frank.jinding.UI.PublicMethodActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolDetails extends AppCompatActivity {

    private ListView lv_task;
    private ImageButton titleleft,titleright;
    private TextView title;
    private MyAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_details);


        init();

        title.setText("协议详情");
        titleright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.add_equipment, (ViewGroup) findViewById(R.id.add_equipment));


                final EditText et1 = (EditText)layout.findViewById(R.id.editText6);
                final EditText et2 =(EditText)layout.findViewById(R.id.editText7);
                final EditText et3 =(EditText)layout.findViewById(R.id.editText8);
                //final EditText et4 =(EditText)layout.findViewById(R.id.editText9);


                new AlertDialog.Builder(ProtocolDetails.this).setTitle("添加设备信息").setView(layout)
                        .setPositiveButton("确定", new  DialogInterface.OnClickListener()
                        {
                            @Override
                            public  void  onClick(DialogInterface dialog, int  which)
                            {
                                //String content="机器名称："+et1.getText()+"\n机器型号："+et2.getText()+"\n"+et3.getText()+"\n检测地址："+et4.getText()+"\n\n";

                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("ItemId", "设备信息"+ 0);
                                map.put("ItemName", ""+ et1.getText());
                                map.put("ItemStylee", ""+ et2.getText());
                                map.put("ItemDatee", ""+ et3.getText());
                                mAdapter.listItem.add(map);
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(ProtocolDetails.this,"添加成功",Toast.LENGTH_SHORT).show();
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
        });

        titleleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent();
                intent.putExtra("num",mAdapter.listItem.size());
                setResult(1001,intent);*/
                finish();
            }
        });


        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                HashMap<String, Object> map =mAdapter.listItem.get(arg2);
                final String ItemId =map.get("ItemId").toString();
                String ItemName =map.get("ItemName").toString();
                String ItemStylee=map.get("ItemStylee").toString();
                String ItemDatee=map.get("ItemDatee").toString();


                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.add_equipment, (ViewGroup) findViewById(R.id.add_equipment));


                final EditText et1 = (EditText)layout.findViewById(R.id.editText6);
                final EditText et2 =(EditText)layout.findViewById(R.id.editText7);
                final EditText et3 =(EditText)layout.findViewById(R.id.editText8);
                //final EditText et4 =(EditText)layout.findViewById(R.id.editText9);

                et1.setText(ItemName);
                et2.setText(ItemStylee);
                et3.setText(ItemDatee);
                new AlertDialog.Builder(ProtocolDetails.this).setTitle("修改设备信息").setView(layout)
                        .setPositiveButton("确定", new  DialogInterface.OnClickListener()
                        {
                            @Override
                            public  void  onClick(DialogInterface dialog, int  which)
                            {
                                //String content="机器名称："+et1.getText()+"\n机器型号："+et2.getText()+"\n"+et3.getText()+"\n检测地址："+et4.getText()+"\n\n";

                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("ItemId", ItemId);
                                map.put("ItemName", ""+ et1.getText());
                                map.put("ItemStylee", ""+ et2.getText());
                                map.put("ItemDatee", ""+ et3.getText());
                                mAdapter.listItem.add(map);
                                mAdapter.listItem.remove(arg2);
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(ProtocolDetails.this,"添加成功",Toast.LENGTH_SHORT).show();
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
        });



    }


   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent = new Intent();
            intent.putExtra("num",mAdapter.listItem.size());
            setResult(1001,intent);
            DeviceNumber.num=mAdapter.listItem.size();
            finish();
        }

        return false;

    }*/


    private void init(){
        lv_task=(ListView)this.findViewById(R.id.lv_protocol_details);
        titleleft=(ImageButton)this.findViewById(R.id.titleleft);
        titleright=(ImageButton)this.findViewById(R.id.titleright);
        title=(TextView)this.findViewById(R.id.titlecenter);


        String number[]={"DG2013015-T-02","DG2013015-T-023","DG2013015-T-023"};
        String autoNumber[]={"11101569454442","11101569459478","1110156978540"};
        String checktype[]={"委托检验（安装验收）","委托检验（最大使用高度）","委托检验（年检）"};

        mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_task.setAdapter(mAdapter);//为ListView绑定Adapter


        /*为动态数组添加数据*/

        for(int i=0;i<number.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemId", "设备信息"+ (i+1));
            map.put("ItemName", ""+ number[i]);
            map.put("ItemStylee", ""+ checktype[i]);
            map.put("ItemDatee", ""+ autoNumber[i]);

            mAdapter.listItem.add(map);
        }

    }




    /*添加一个得到数据的方法，方便使用*/
    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        String name[]={"起重机","吊车","和泥机","推土机"};
        String stylee[]={"SES235424","SDF324234","SAD34234","WAD2343"};
        String datee[]={"2017/2/15","2017/4/23","2017/6/22","2017/7/11"};



        *//*为动态数组添加数据*//*

        for(int i=0;i<name.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemId", "设备信息"+ (i+1));
            map.put("ItemName", ""+ name[0]);
            map.put("ItemStylee", ""+ stylee[i]);
            map.put("ItemDatee", ""+ datee[i]);

            listItem.add(map);
        }
        return listItem;

    }*/







    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
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
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.protoclo_details_device_adapter,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                holder.did=(TextView)convertView.findViewById(R.id.textView14);
                holder.name = (TextView) convertView.findViewById(R.id.textView26);
                holder.stylee = (TextView) convertView.findViewById(R.id.textView37);
                holder.datee = (TextView) convertView.findViewById(R.id.textView38);
                holder.delete=(Button) convertView.findViewById(R.id.button57);
                holder.altermodel=(Button) convertView.findViewById(R.id.button58);
                holder.alterdate=(Button) convertView.findViewById(R.id.button64);


                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.did.setText(listItem.get(position).get("ItemId").toString());
            holder.name.setText(listItem.get(position).get("ItemName").toString());
            holder.stylee.setText(listItem.get(position).get("ItemStylee").toString());
            holder.datee.setText(listItem.get(position).get("ItemDatee").toString());

            holder.delete.setTag(position);
            holder.altermodel.setTag(position);
            holder.alterdate.setTag(position);

            holder.altermodel.setVisibility( holder.altermodel.INVISIBLE);
            holder.alterdate.setVisibility( holder.alterdate.INVISIBLE);

            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/



            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(ProtocolDetails.this)
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
                                            Toast.makeText(ProtocolDetails.this,"设备信息删除成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });

            holder.altermodel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    final EditText et=new EditText(ProtocolDetails.this);
                    new  AlertDialog.Builder(ProtocolDetails.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入正确的")
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

                                            holder.stylee.setText(""+et.getText().toString().trim());
                                            Toast.makeText(ProtocolDetails.this,"设备数量更新成功",Toast.LENGTH_SHORT).show();
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
                    final EditText ett=new EditText(ProtocolDetails.this);
                    new  AlertDialog.Builder(ProtocolDetails.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入正确的")
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

                                            holder.datee.setText(""+ett.getText().toString().trim());
                                            Toast.makeText(ProtocolDetails.this,"设备数量更新成功",Toast.LENGTH_SHORT).show();
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




}
