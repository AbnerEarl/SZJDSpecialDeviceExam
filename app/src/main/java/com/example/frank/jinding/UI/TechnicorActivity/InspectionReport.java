package com.example.frank.jinding.UI.TechnicorActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.R;
import com.example.frank.jinding.UI.PublicMethodActivity.ViewReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InspectionReport extends AppCompatActivity {


    private RadioButton task,hadtask;
    private ListView lv_tasksss;

    private ImageButton back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_report);
        init();


        title.setText("待审核检测意见");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter

        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //获得选中项的HashMap对象
                //               HashMap<String,String> map=(HashMap<String,String>)lv_tasksss.getItemAtPosition(arg2);
//                String title=map.get("ItemTitle");
//                String content=map.get("itemContent");

                Intent intent=new Intent(InspectionReport.this,ViewReport.class);
                intent.putExtra("content","任务编号："+arg2);
                startActivity(intent);

            }
        });


        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MyAdapter mAdapter = new MyAdapter(InspectionReport.this);//得到一个MyAdapter对象
                //lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter
            }
        });

        hadtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MyAdapter mAdapter = new MyAdapter(InspectionReport.this);//得到一个MyAdapter对象
                //lv_tasksss.setAdapter(null);//为ListView绑定Adapter
            }
        });



    }

    private void init(){
        task=(RadioButton)this.findViewById(R.id.radioButton7);
        hadtask=(RadioButton)this.findViewById(R.id.radioButton8);
        lv_tasksss=(ListView)this.findViewById(R.id.lv_inspect_report);

        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);

    }




    

    /*添加一个得到数据的方法，方便使用*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
        String task[]={"订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！","订单：WEEWR213121423 已完成检验，等待审核！"};
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");



        /*为动态数组添加数据*/

        for(int i=0;i<task.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", "\n"+ task[i]+"\n\n"+ formatter.format(new Date(System.currentTimeMillis())) );
            //map.put("ItemText", formatter.format(new Date(System.currentTimeMillis())));
            listItem.add(map);
        }
        return listItem;

    }







    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /*构造函数*/
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
            return 0;
        }
        /*书中详细解释该方法*/
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final InspectionReport.ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.report,null);
                holder = new InspectionReport.ViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.text_report_content);
                holder.bt_chakan = (Button) convertView.findViewById(R.id.button_report_pass);
                holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_report_refuse);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (InspectionReport.ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.title.setText(getDate().get(position).get("ItemTitle").toString());
            holder.bt_chakan.setTag(position);
            holder.bt_beizhu.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/




            holder.bt_beizhu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(InspectionReport.this)
                            .setTitle("系统提示")
                            .setMessage("请输入修改意见：")
                            .setView(new EditText(InspectionReport.this))
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
                                            Toast.makeText(InspectionReport.this,"发布成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });


            holder.bt_chakan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(InspectionReport.this)
                            .setTitle("系统提示")
                            .setMessage("审核成功！")
                            .setPositiveButton("确定",
                                    new  DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public  void  onClick(DialogInterface dialog, int  which)
                                        {
                                        }
                                    }).show();

                }
            });




            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title;
        public Button bt_chakan;
        public Button bt_beizhu;
    }





}
