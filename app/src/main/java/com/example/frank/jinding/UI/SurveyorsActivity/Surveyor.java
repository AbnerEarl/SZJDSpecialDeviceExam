package com.example.frank.jinding.UI.SurveyorsActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.Conf.CheckInfo;
import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.HashMap;


public class Surveyor extends AppCompatActivity {

    private ListView lv_tasksss;

    private MyAdapter waitAdapter;
    private MyAdapterO confirmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyor);



        lv_tasksss = (ListView) this.findViewById(R.id.lv_surveyor_wait);
        //测试数据

        String taskname[]={"深圳潜孔工程有限公司","情谊工程联合有限公司","情谊工程联合有限公司","情谊工程联合有限公司","情谊工程联合有限公司","情谊工程联合有限公司","情谊工程联合有限公司","情谊工程联合有限公司"};
        String taskaddress[]={"广东省深圳市宝安区石岩工业区"};
        //Random random=new Random();
        /*为动态数组添加数据*/

        for(int i=0;i<taskname.length;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemName", taskname[i]);
            map.put("ItemStyle", "高精度检测");
            map.put("ItemAddress", taskaddress[0]);
            map.put("ItemDate", "2017/12/20");
            CheckInfo.waittask.add(map);
        }



        //绑定适配器
        waitAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        waitAdapter.listItem=CheckInfo.waittask;

        confirmAdapter = new MyAdapterO(this);//得到一个MyAdapter对象
        confirmAdapter.listItem=CheckInfo.comfirmtask;

        lv_tasksss.setAdapter(waitAdapter);//为ListView绑定Adapter
    }









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
                convertView = mInflater.inflate(R.layout.surveyors_wait_check,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_projectName);
                holder.pstyle = (TextView) convertView.findViewById(R.id.dispatching_unit);
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);
                holder.bt_refuse = (Button) convertView.findViewById(R.id.btn_refuse);
                holder.bt_accept=(Button) convertView.findViewById(R.id.btn_accept);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.pname.setText(listItem.get(position).get("ItemName").toString());
            holder.pstyle.setText(listItem.get(position).get("ItemStyle").toString());
            holder.paddress.setText(listItem.get(position).get("ItemAddress").toString());
            holder.pdate.setText(listItem.get(position).get("ItemDate").toString());
            holder.bt_refuse.setTag(position);
            holder.bt_accept.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/




            holder.bt_refuse.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(Surveyor.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入修改的意见：")
                            .setView(new EditText(Surveyor.this))
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

                                            /*HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("ItemName", holder.pname.getText());
                                            map.put("ItemNumber", holder.pnumber.getText());
                                            map.put("ItemAddress", holder.paddress.getText());
                                            map.put("ItemDate", holder.pdate.getText());
                                            refuseAdapter.listItem.add(map);*/

                                            waitAdapter.listItem.remove(position);
                                            waitAdapter.notifyDataSetChanged();

                                            Toast.makeText(Surveyor.this,"报告审核成功",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });


            holder.bt_accept.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(Surveyor.this)
                            .setTitle("系统提示")
                            .setMessage("\n报告审批成功！")
                            .setPositiveButton("确定",
                                    new  DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public  void  onClick(DialogInterface dialog, int  which)
                                        {
                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("ItemName", holder.pname.getText());
                                            map.put("ItemStyle", holder.pstyle.getText());
                                            map.put("ItemAddress", holder.paddress.getText());
                                            map.put("ItemDate", holder.pdate.getText());
                                            confirmAdapter.listItem.add(map);

                                            waitAdapter.listItem.remove(position);
                                            waitAdapter.notifyDataSetChanged();

                                        }
                                    }).show();

                }
            });




            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView pname,pstyle,paddress,pdate;
        public Button bt_refuse;
        public Button bt_accept;
    }



    //其他任务信息加载

    /*
   * 新建一个类继承BaseAdapter，实现视图与数据的绑定
   */
    private class MyAdapterO extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
        /*构造函数*/
        public MyAdapterO(Context context) {
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
            final ViewHolderO holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.surveyors_had_check,null);
                holder = new ViewHolderO();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_projectName);
                holder.pstyle = (TextView) convertView.findViewById(R.id.dispatching_unit);
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);
//                holder.bt_chakan = (Button) convertView.findViewById(R.id.button_chakan);
//                holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_beizhu);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolderO)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.pname.setText(listItem.get(position).get("ItemName").toString());
            holder.pstyle.setText(listItem.get(position).get("ItemStyle").toString());
            holder.paddress.setText(listItem.get(position).get("ItemAddress").toString());
            holder.pdate.setText(listItem.get(position).get("ItemDate").toString());
//            holder.bt_chakan.setTag(position);
//            holder.bt_beizhu.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolderO{
        public TextView pname,pstyle,paddress,pdate;
//        public Button bt_chakan;
//        public Button bt_beizhu;
    }



}
