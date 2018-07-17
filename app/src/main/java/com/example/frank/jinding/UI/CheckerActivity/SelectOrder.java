package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectOrder extends AppCompatActivity {

    private ListView lv_task;
    private ImageButton back;
    private TextView title;
    private List<JSONObject> submissionOrderList;
    private  MyAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private Spinner mSpinner;
    private ArrayAdapter spinnerAdapter;
    private int spinnerSelectedItem=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_order);

        init();
        //标题栏设置
        title.setText("检验现场");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        List<String> spinnerList=new ArrayList<>();
        spinnerList.add("等待检验订单");
        spinnerList.add("正在检验订单");
        spinnerList.add("已经检验订单");

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                               public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                                   //  TODO  Auto-generated  method  stub
                /*  将所选mySpinner  的值带入myTextView  中*/
                                                   if (mSpinner.getSelectedItem().equals("等待检验订单")) {
                                                       spinnerSelectedItem=1;
                                                       mAdapter= new MyAdapter(SelectOrder.this);//得到一个MyAdapter对象
                                                       //获取建立派工单
                                                       mAdapter.listItem=submissionOrderList;
                                                       getData(6);
                                                       lv_task.setAdapter(mAdapter);//为ListView绑定Adapter
                                                   } else if (mSpinner.getSelectedItem().equals("已经检验订单")) {
                                                       spinnerSelectedItem=3;
                                                       mAdapter= new MyAdapter(SelectOrder.this);//得到一个MyAdapter对象
                                                       //获取建立派工单
                                                       mAdapter.listItem=submissionOrderList;
                                                       getData(7);
                                                       lv_task.setAdapter(mAdapter);//为ListView绑定Adapter
                                                   }else if (mSpinner.getSelectedItem().equals("正在检验订单")) {
                                                       spinnerSelectedItem=2;
                                                       mAdapter= new MyAdapter(SelectOrder.this);//得到一个MyAdapter对象
                                                       //获取建立派工单
                                                       mAdapter.listItem=submissionOrderList;
                                                       getData(8);
                                                       lv_task.setAdapter(mAdapter);//为ListView绑定Adapter
                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {

                                               }
                                           });


/*为ListView添加点击事件*/
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

               /* if (spinnerSelectedItem==3){
                    Intent intent = new Intent(SelectOrder.this, CheckStartProtocl.class);
                    intent.putExtra("submission_id",submissionOrderList.get(arg2).get("submissionId").toString());
                    intent.putExtra("order_id",submissionOrderList.get(arg2).get("orderId").toString());
                    intent.putExtra("isMainChecker",submissionOrderList.get(arg2).getBoolean("isMainChecker"));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SelectOrder.this, OperationProcess.class);
                    intent.putExtra("orderId", submissionOrderList.get(arg2).get("orderId").toString());
                    intent.putExtra("isMainChecker", submissionOrderList.get(arg2).getBoolean("isMainChecker"));
                    //intent.putExtra("userid",userid);
                    intent.putExtra("submission_id", submissionOrderList.get(arg2).get("submissionId").toString());
                    startActivity(intent);
                }*/

                Intent intent = new Intent(SelectOrder.this, OperationProcess.class);
                intent.putExtra("orderId", submissionOrderList.get(arg2).get("orderId").toString());
                intent.putExtra("isMainChecker", submissionOrderList.get(arg2).getBoolean("isMainChecker"));
                //intent.putExtra("userid",userid);
                intent.putExtra("submission_id", submissionOrderList.get(arg2).get("submissionId").toString());
                //startActivity(intent);
                startActivityForResult(intent,123);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (spinnerSelectedItem==1){
                    getData(6);
                }
                else if (spinnerSelectedItem==3){
                    getData(7);
                }else if (spinnerSelectedItem==2){
                    getData(8);
                }

            }
        });

        lv_task.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem==0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
       if (requestCode ==123) {

           refreshLayout.setRefreshing(true);
           if (spinnerSelectedItem==1){
               getData(6);
           }
           else if (spinnerSelectedItem==3){
               getData(7);
           }else if (spinnerSelectedItem==2){
               getData(8);
           }
        }
    }

    private void init(){

        lv_task=(ListView)this.findViewById(R.id.lv_order);
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        mSpinner=(Spinner)this.findViewById(R.id.check_scene_spinner);
        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.check_spot_checkOrder_refresh);
        submissionOrderList=new ArrayList<>();
    }

    private void getData(int requestCode) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("requestCode", requestCode);
        ApiService.GetString(this, "submissionOrder", parameters, new RxStringCallback(){
            @Override
            public void onNext(Object tag, String response) {
                refreshLayout.setRefreshing(false);
                Log.i(TAG,response.toString());
                if (response!=null&&!response.equals("failed")) {
                  submissionOrderList.clear();
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        submissionOrderList.add(jsonObject);
                    }
                    mAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
                if (submissionOrderList.size()==0||response==null){
                   new  AlertDialog.Builder(SelectOrder.this).setTitle("暂时没有"+mSpinner.getSelectedItem().toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   }).create().show();
                }
                Log.i("获取数据结束","getdataover");
                }
            @Override
            public void onError(Object tag, Throwable e) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(SelectOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                refreshLayout.setRefreshing(false);
            }

        });

    }


    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private List<JSONObject> listItem=new ArrayList<>();
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
                convertView = mInflater.inflate(R.layout.receive_order,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.pno = (TextView) convertView.findViewById(R.id.dispatching_unit);//单位
                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_projectName);//工程名字
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);//时间
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);//地址
                holder.more = (TextView) convertView.findViewById(R.id.dispatching_more);//更多
                holder.ptaskIcon=(ImageView)convertView.findViewById(R.id.task_icon);
                //holder.bt_chakan = (Button) convertView.findViewById(R.id.button27);
                //holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_beizhu);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.pno.setText(listItem.get(position).get("orderOrg").toString());
            holder.pname.setText(listItem.get(position).get("projectName").toString());
            holder.paddress.setText(listItem.get(position).get("projectAddress").toString());
            holder.pdate.setText(listItem.get(position).get("checkTime").toString());
            String project_name=listItem.get(position).get("projectName").toString();
            if(project_name.indexOf("(复检)")!=-1)
                holder.ptaskIcon.setImageResource(R.drawable.third_order);
            else
                holder.ptaskIcon.setImageResource(R.drawable.first_order);
            //如果传过来的工程名字中有复检则为复检
            Log.i("SelectOrder","是否是主检验员："+listItem.get(position).getBoolean("isMainChecker"));
//            holder.bt_beizhu.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/



            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SelectOrder.this,OrderDetails.class);
                    intent.putExtra("orderId",listItem.get(position).get("orderId").toString());
                    //是否可更新
                    intent.putExtra("update",false);
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView pno,pname,pdate,paddress,more;
        private ImageView ptaskIcon;

    }




    
}
