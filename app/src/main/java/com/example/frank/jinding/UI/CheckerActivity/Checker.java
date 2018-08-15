package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.widget.PullRefreshLayout;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Checker extends AppCompatActivity {

    private List<String> list = new ArrayList<String>();
    private long mExitTime = 0;
    private NiceSpinner mySpinner;
    private ArrayAdapter<String> adapter;
    private ListView lv_tasksss;
    //private SwipeRefreshLayout refreshLayout;
    private PullRefreshLayout pullRefreshLayout;
    private ImageButton back;
    private TextView title;
    private FloatingActionButton fab;
    private String messageString;
    private Snackbar snackbar;
    private MyAdapter waitAdapter;
    private MyAdapterO confirmAdapter, establishAdapter, refuseAdapter;
    private int currentList = 0;
    private List<String>NiceSpinner;
    ArrayList<JSONObject> submissionOrderList;
    private String Type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_checkers_order);
        currentList = 1;

        init();
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
//        list.add("待确认派工");
//        list.add("已确认派工");
//        list.add("已成立派工");
//        list.add("已拒绝派工");
        Type="待确认派工";
        currentList = 1;
        getData(currentList);

        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
       // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        NiceSpinner = new LinkedList<String>(list);
//        //第四步：将适配器添加到下拉列表上
//        mySpinner.attachDataSource(NiceSpinner);
//
//        //mySpinner.setSelection(0,false);
//        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
//
//
////        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
////            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
////                //  TODO  Auto-generated  method  stub
////                /*  将所选mySpinner  的值带入myTextView  中*/
////
////                if ("待确认派工".equals(NiceSpinner.get(arg2).toString())) {
////                    //waitAdapter = new MyAdapter(CheckersActivity.this);//得到一个MyAdapter对象
////                    Log.i("待确认派工",""+currentList);
////                    currentList = 1;
////                    getData(currentList);
////                } else if ("已确认派工".equals(NiceSpinner.get(arg2).toString())) {
////
////                    currentList = 2;
////                    getData(currentList);
////                    //为ListView绑定Adapter
////                } else if ("已成立派工".equals(NiceSpinner.get(arg2).toString())) {
////                    currentList = 3;
////                    getData(currentList);//为ListView绑定Adapter
////                } else if ("已拒绝派工".equals(NiceSpinner.get(arg2).toString())) {
////                    currentList = 4;
////                    getData(currentList);
////                }
////                Type=NiceSpinner.get(arg2);
////
////
////            }
//
//            public void onNothingSelected(AdapterView<?> arg0) {
//                //  TODO  Auto-generated  method  stub
//                //myTextView.setText("NONE");
//            }
//
//        });



        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //获得选中项的HashMap对象
                //               HashMap<String,String> map=(HashMap<String,String>)lv_tasksss.getItemAtPosition(arg2);
//                String title=map.get("ItemTitle");
//                String content=map.get("itemContent");

                Intent intent = new Intent(Checker.this, OrderDetails.class);
                intent.putExtra("update", false);
                intent.putExtra("orderId",submissionOrderList.get(arg2).get("orderId").toString());
                startActivity(intent);

            }
        });



    }




    private void init() {



        //绑定控件
        lv_tasksss = (ListView) this.findViewById(R.id.lv_tasksss_ch);
//        mySpinner = (NiceSpinner) findViewById(R.id.spinner3_ch);
       // refreshLayout=(SwipeRefreshLayout) findViewById(R.id.refresh_submission_ch);
        pullRefreshLayout=(PullRefreshLayout)findViewById(R.id.refreshCheckerAcceptOrder);
        back=(ImageButton)findViewById(R.id.titleback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title=(TextView)findViewById(R.id.titleplain);
        title.setText("派工管理");

        Log.i("init","test");
        submissionOrderList=new ArrayList<>();
        //绑定适配器
        //getData(1);
        Log.i("初始化","getdata1");
        waitAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        waitAdapter.listItem =submissionOrderList;
        lv_tasksss.setAdapter(waitAdapter);//为ListView绑定Adapter
        waitAdapter.notifyDataSetChanged();
        Log.i("初始化结束","initover");
        confirmAdapter = new MyAdapterO(Checker.this);//得到一个MyAdapter对象
        establishAdapter = new MyAdapterO(Checker.this);//得到一个MyAdapter对象
        refuseAdapter = new MyAdapterO(Checker.this);//得到一个MyAdapter对象
        /*refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getData(currentList);
            }
        });*/

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(currentList);
            }
        });

    }






    private void getData(final int requestCode) {
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final android.support.v7.app.AlertDialog processDialog=new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        submissionOrderList=new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("requestCode", requestCode);
        ApiService.GetString(this, "submissionOrder", parameters, new RxStringCallback(){
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                if (response != null) {
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        submissionOrderList.add(jsonObject);
                    }
                }
                if (requestCode == 1 && (submissionOrderList.size() > 0)) {
                    messageString = "您有待确认的派工如下：\n";
                    for (int i = 0; i < submissionOrderList.size(); i++) {
                        messageString += submissionOrderList.get(i).get("projectName").toString() + "\n";
                    }
                    //fab.setVisibility(View.INVISIBLE);
                    //snackbar.show();
                }
                if (submissionOrderList.size()==0) {
                    new AlertDialog.Builder(Checker.this).setTitle("暂无" + Type.toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
                if (currentList == 1) {
                    waitAdapter.listItem = submissionOrderList;
                    lv_tasksss.setAdapter(waitAdapter);
                } else if (currentList == 2) {
                    confirmAdapter.listItem = submissionOrderList;
                    lv_tasksss.setAdapter(confirmAdapter);
                } else if (currentList == 3) {
                    establishAdapter.listItem = submissionOrderList;
                    lv_tasksss.setAdapter(establishAdapter);
                } else {
                    refuseAdapter.listItem = submissionOrderList;
                    lv_tasksss.setAdapter(refuseAdapter);
                }
                //refreshLayout.setRefreshing(false);
                pullRefreshLayout.setRefreshing(false);
                Log.i("获取数据结束", "getdataover");
            }
            @Override
            public void onError(Object tag, Throwable e) {
               // refreshLayout.setRefreshing(false);
                pullRefreshLayout.setRefreshing(false);
                processDialog.dismiss();
                Log.i("失败",""+e.getMessage()+"  "+e.getCause()+tag.toString());
                Toast.makeText(Checker.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                pullRefreshLayout.setRefreshing(false);
            }

        });

    }

    private void rejectSubmission(final int position, String id, final String reason) {

        View processView=View.inflate(Checker.this,R.layout.simple_processbar,null);
        final android.support.v7.app.AlertDialog processDialog=new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reason", reason);
        parameters.put("submissionId", id);
        ApiService.GetString(this, "rejectSubmission", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")) {
                    submissionOrderList.remove(position);
                    waitAdapter.notifyDataSetChanged();
                    Toast.makeText(Checker.this, "拒绝成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Checker.this, "拒绝失败", Toast.LENGTH_SHORT).show();

                }

                processDialog.dismiss();
            }

            @Override
            public void onError(Object tag, Throwable e) {
                processDialog.dismiss();
                Toast.makeText(Checker.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                processDialog.dismiss();
            }
        });

    }
    private void confirmSubmission(final int position,String id){

        View processView=View.inflate(Checker.this,R.layout.simple_processbar,null);
        final android.support.v7.app.AlertDialog processDialog=new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("submissionId", id);
        ApiService.GetString(this, "confirmSubmission", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")) {
                    submissionOrderList.remove(position);
                    waitAdapter.notifyDataSetChanged();
                    Toast.makeText(Checker.this, "接单成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Checker.this, "接单失败", Toast.LENGTH_SHORT).show();

                }

                processDialog.dismiss();
            }

            @Override
            public void onError(Object tag, Throwable e) {
                processDialog.dismiss();
                Toast.makeText(Checker.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                processDialog.dismiss();
            }
        });
    }





    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<JSONObject> listItem = new ArrayList<JSONObject>();
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
                convertView = mInflater.inflate(R.layout.task,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_unit);//委托单位
                holder.pnumber = (TextView) convertView.findViewById(R.id.dispatching_projectName);//工程名字
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);//工程地址
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);//工程时间
                holder.bt_chakan = (Button) convertView.findViewById(R.id.btn_refuse);//拒绝
                holder.bt_beizhu=(Button) convertView.findViewById(R.id.btn_accept);//接受
                holder.ptaskIcon = (ImageView)convertView.findViewById(R.id.task_icon);
                if (currentList!=1){
                    holder.bt_chakan.setVisibility(View.GONE);
                    holder.bt_beizhu.setVisibility(View.GONE);
                }
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            holder.pname.setText(listItem.get(position).get("orderOrg").toString());
            holder.pnumber.setText(listItem.get(position).get("projectName").toString());
            holder.paddress.setText(listItem.get(position).get("projectAddress").toString());
            holder.pdate.setText(listItem.get(position).get("checkTime").toString());
            String ptaskname=listItem.get(position).get("projectName").toString();
            if(ptaskname.indexOf("(复检)")!=-1)
            holder.ptaskIcon.setImageResource(R.drawable.third_order);
            else
                holder.ptaskIcon.setImageResource(R.drawable.first_order);

            holder.bt_chakan.setTag(position);
            holder.bt_beizhu.setTag(position);
            //fLog.i(holder.pname.getText().toString(),""+holder.pnumber.getText().toString());


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/




            holder.bt_chakan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    final EditText et=new EditText(Checker.this);

                    new  AlertDialog.Builder(Checker.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入拒绝的理由：")
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
                                            rejectSubmission(position,listItem.get(position).get("submissionId").toString(),et.getText().toString());
                                        }
                                    }).show();

                }
            });


            holder.bt_beizhu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(Checker.this)
                            .setTitle("系统提示")
                            .setMessage("确认接受："+listItem.get(position).getString("projectName")+"？")
                            .setPositiveButton("确定",
                                    new  DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public  void  onClick(DialogInterface dialog, int  which)
                                        {
                                            confirmSubmission(position,listItem.get(position).get("submissionId").toString());
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            });




            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView pname,pnumber,paddress,pdate;
        public Button bt_chakan;
        public Button bt_beizhu;
        public ImageView ptaskIcon;
    }
    //其他任务信息加载

    /*
   * 新建一个类继承BaseAdapter，实现视图与数据的绑定
   */
    private class MyAdapterO extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<JSONObject> listItem = new ArrayList<JSONObject>();
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
                convertView = mInflater.inflate(R.layout.taskothers,null);
                holder = new ViewHolderO();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_unit);//委托单位
                holder.pnumber = (TextView) convertView.findViewById(R.id.dispatching_projectName);//工程名字
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);//工程地址
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);//时间
                holder.ptaskIcon=(ImageView)convertView.findViewById(R.id.task_icon);//图标
//                holder.bt_chakan = (Button) convertView.findViewById(R.id.button_chakan);
//                holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_beizhu);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolderO)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            holder.pname.setText(listItem.get(position).get("orderOrg").toString());
            holder.pnumber.setText(listItem.get(position).get("projectName").toString());
            holder.paddress.setText(listItem.get(position).get("projectAddress").toString());
            holder.pdate.setText(listItem.get(position).get("checkTime").toString());
            String ppprojectname=listItem.get(position).get("projectName").toString();
            if(ppprojectname.indexOf("(复检)")!=-1)
                holder.ptaskIcon.setImageResource(R.drawable.third_order);
            else holder.ptaskIcon.setImageResource(R.drawable.first_order);
//            holder.bt_chakan.setTag(position);
//            holder.bt_beizhu.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolderO{
        public TextView pname,pnumber,paddress,pdate;
        public ImageView ptaskIcon;
//        public Button bt_chakan;
//        public Button bt_beizhu;
    }








}
