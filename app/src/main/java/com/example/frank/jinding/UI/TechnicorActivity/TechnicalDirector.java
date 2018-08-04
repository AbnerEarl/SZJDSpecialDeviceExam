package com.example.frank.jinding.UI.TechnicorActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TechnicalDirector extends AppCompatActivity {

    private MyAdapter waitAdapter;
    private MyAdapterO confirmAdapter;
    private ListView lv_tasksss;
    private ImageButton back;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_director);
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);


        lv_tasksss = (ListView) this.findViewById(R.id.lv_technical_wait_opinion);


        //绑定适配器
        waitAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        //waitAdapter.listItem=CheckInfo.waitreport;


        confirmAdapter = new MyAdapterO(this);//得到一个MyAdapter对象
        //confirmAdapter.listItem=CheckInfo.confirmreport;


        final String tag=getIntent().getStringExtra("tag");

        if (tag.equals("0")){
            lv_tasksss.setAdapter(waitAdapter);//为ListView绑定Adapter
            title.setText("待审核意见");
            getData("0");
        }else if (tag.equals("1")){
            lv_tasksss.setAdapter(confirmAdapter);
            title.setText("已审核意见");
            getData("1");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //获得选中项的HashMap对象
                //HashMap<String,String> map=(HashMap<String,String>)lv_tasksss.getItemAtPosition(arg2);
//                String title=map.get("ItemTitle");
//                String content=map.get("itemContent");


                if (tag.equals("0")){
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", waitAdapter.listItem.get(arg2).get("DeviceRecordId").toString());
                    ApiService.GetString(TechnicalDirector.this, "getCheckDetailsPath", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (!response.trim().equals("查询失败！")) {

                                Intent intent = new Intent(TechnicalDirector.this, TechnicalViewDetails.class);
                                intent.putExtra("path", response.toString().trim());
                                intent.putExtra("TitleDecide",0);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();

                        }


                    });
                }else if (tag.equals("1")){
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", confirmAdapter.listItem.get(arg2).get("DeviceRecordId").toString());
                    ApiService.GetString(TechnicalDirector.this, "getCheckDetailsPath", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (!response.trim().equals("查询失败！")) {

                                Intent intent = new Intent(TechnicalDirector.this, TechnicalViewDetails.class);
                                intent.putExtra("path", response.toString().trim());
                                intent.putExtra("TitleDecide",1);

                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();

                        }


                    });
                }




            }
        });




    }






    private void getData(String tag) {


        if (tag.equals("0")) {

            Map<String, Object> paremetes = new HashMap<>();
            paremetes.put("data", tag);
            ApiService.GetString(TechnicalDirector.this, "getCheckOpinionResult", paremetes, new RxStringCallback() {
                boolean flag = false;

                @Override
                public void onNext(Object tag, String response) {
                    if (response.trim().contains("#")) {
                        String ls[] = response.split("##");
                        waitAdapter.listItem.clear();
                        for (int i = 0; i < ls.length; i++) {
                            String info[] = ls[i].split("#");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("ItemName", info[0].trim());
                            map.put("ItemNumber", info[1].trim());
                            if (info[2].trim().equals("0")) {
                                map.put("ItemAddress", "不合格");
                            } else if (info[2].trim().equals("1")) {
                                map.put("ItemAddress", "合格");
                            } else {
                                map.put("ItemAddress", "需复检（待确认）");
                            }

                            map.put("ItemDate", info[3].trim());
                            map.put("ItemOpinion", info[4].trim());
                            map.put("DeviceRecordId", info[5].trim());
                            waitAdapter.listItem.add(map);

                        }
                        waitAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TechnicalDirector.this, "暂时没有数据", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(TechnicalDirector.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancel(Object tag, Throwable e) {
                    Toast.makeText(TechnicalDirector.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();

                }


            });


        } else if (tag.equals("1")) {
            Map<String, Object> paremetes = new HashMap<>();
            paremetes.put("data", tag);
            ApiService.GetString(TechnicalDirector.this, "getCheckOpinionResult", paremetes, new RxStringCallback() {


                @Override
                public void onNext(Object tag, String response) {
                    if (response.trim().contains("#")) {
                        String ls[] = response.split("##");
                        confirmAdapter.listItem.clear();
                        for (int i = 0; i < ls.length; i++) {
                            String info[] = ls[i].split("#");
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("ItemName", info[0].trim());
                            map.put("ItemNumber", info[1].trim());
                            if (info[2].trim().equals("0")) {
                                map.put("ItemAddress", "不合格");
                            } else if (info[2].trim().equals("1")) {
                                map.put("ItemAddress", "合格");
                            } else {
                                map.put("ItemAddress", "需复检(待确认)");
                            }

                            map.put("ItemDate", info[3].trim());
                            map.put("ItemOpinion", info[4].trim());
                            map.put("DeviceRecordId", info[5].trim());
                            confirmAdapter.listItem.add(map);

                        }
                        confirmAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TechnicalDirector.this,"暂时没有数据", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(TechnicalDirector.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancel(Object tag, Throwable e) {
                    Toast.makeText(TechnicalDirector.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();

                }


            });

        }


    }






    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

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
                convertView = mInflater.inflate(R.layout.tecnical_check_opinion, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.advice_projectName);//工程名称
                holder.pnumber = (TextView) convertView.findViewById(R.id.advice_device_name);//设备名称
                holder.paddress = (TextView) convertView.findViewById(R.id.advice_conclusion);//检验结论
                holder.pdate = (TextView) convertView.findViewById(R.id.advice_location);//工程地址
                holder.opinion = (TextView) convertView.findViewById(R.id.advice_advice);//检验意见
                holder.bt_refuse = (Button) convertView.findViewById(R.id.btn_refuse);//拒绝
                holder.bt_agree = (Button) convertView.findViewById(R.id.btn_accept);//同意

                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.pname.setText(listItem.get(position).get("ItemName").toString());
            holder.pnumber.setText(listItem.get(position).get("ItemNumber").toString());
            holder.paddress.setText(listItem.get(position).get("ItemAddress").toString());
            holder.pdate.setText(listItem.get(position).get("ItemDate").toString());
            holder.opinion.setText(listItem.get(position).get("ItemOpinion").toString());
            holder.bt_agree.setTag(position);
            holder.bt_refuse.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

            // 1 代表是接受检验意见，2 代表是拒绝接受检验意见

            holder.bt_refuse.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    final EditText et = new EditText(TechnicalDirector.this);
                    new AlertDialog.Builder(TechnicalDirector.this)
                            .setMessage("请输入修改的意见：")
                            .setView(et)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            HashMap<String ,String> map_data=new HashMap<>();
                                            map_data.put("operateTag","2");
                                            map_data.put("deviceRecordId",waitAdapter.listItem.get(position).get("DeviceRecordId").toString() );
                                            map_data.put("reason",et.getText().toString().trim());

                                            Map<String, Object> paremetes = new HashMap<>();
                                            paremetes.put("data",JSON.toJSONString(map_data) );
                                            ApiService.GetString(TechnicalDirector.this, "examCheckOpinionResult", paremetes, new RxStringCallback() {
                                                boolean flag = false;

                                                @Override
                                                public void onNext(Object tag, String response) {

                                                    if (response.trim().equals("true")) {
                                                        Toast.makeText(TechnicalDirector.this, "审批成功", Toast.LENGTH_SHORT).show();

                                                    }
                                                }

                                                @Override
                                                public void onError(Object tag, Throwable e) {
                                                    Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel(Object tag, Throwable e) {
                                                    Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();

                                                }


                                            });


                                            waitAdapter.listItem.remove(position);
                                            waitAdapter.notifyDataSetChanged();

                                            // Toast.makeText(TechnicalDirectorsActivity.this,"查询成功！",Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();

                }
            });


            holder.bt_agree.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    HashMap<String ,String> map_data=new HashMap<>();
                    map_data.put("operateTag","1");
                    map_data.put("deviceRecordId",waitAdapter.listItem.get(position).get("DeviceRecordId").toString());

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", JSON.toJSONString(map_data));
                    ApiService.GetString(TechnicalDirector.this, "examCheckOpinionResult", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (response.trim().equals("true")) {
                                waitAdapter.listItem.remove(position);
                                waitAdapter.notifyDataSetChanged();
                                Toast.makeText(TechnicalDirector.this, "审批成功", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(TechnicalDirector.this, "" + e, Toast.LENGTH_SHORT).show();

                        }


                    });

                }
            });


            return convertView;
        }

    }

    /*存放控件*/
    public final class ViewHolder {
        public TextView pname, pnumber, paddress, pdate, opinion;
        public Button bt_refuse;
        public Button bt_agree;
    }


    //其他任务信息加载

    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapterO extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

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
                convertView = mInflater.inflate(R.layout.agree_checked_opinion, null);
                holder = new ViewHolderO();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.advice_projectName);//工程名称
                holder.pnumber = (TextView) convertView.findViewById(R.id.advice_device_name);//设备名称
                holder.paddress = (TextView) convertView.findViewById(R.id.advice_conclusion);//检验结论
                holder.pdate = (TextView) convertView.findViewById(R.id.advice_location);//工程地址
                holder.opinion = (TextView) convertView.findViewById(R.id.advice_advice);//检验意见
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolderO) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.pname.setText(listItem.get(position).get("ItemName").toString());
            holder.pnumber.setText(listItem.get(position).get("ItemNumber").toString());
            holder.paddress.setText(listItem.get(position).get("ItemAddress").toString());
            holder.pdate.setText(listItem.get(position).get("ItemDate").toString());
            holder.opinion.setText(listItem.get(position).get("ItemOpinion").toString());


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

            return convertView;
        }

    }

    /*存放控件*/
    public final class ViewHolderO {
        public TextView pname, pnumber, paddress, pdate, opinion;
    }


}
