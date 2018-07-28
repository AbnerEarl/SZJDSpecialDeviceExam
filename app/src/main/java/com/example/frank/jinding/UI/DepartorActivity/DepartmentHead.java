package com.example.frank.jinding.UI.DepartorActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.ExtraPermission.Permission;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.CheckerActivity.SelectEquipment;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DepartmentHead extends AppCompatActivity implements View.OnClickListener{


    private long mExitTime = 0;
    private ListView lv_tasksss;
    private Button mutilb;
    private SwipeRefreshLayout refreshLayout;
    private static String TAG="DepartmentHeaderActivity";
    private MyAdapter mWaitAdapter,mAlreadyAdapter;
    private HashMap<Integer, Boolean> isChecked;
    private Spinner mSpinner;
    private List<String> mSpinnerList;
    private static   boolean permissioned=true;
    private  int startIndex=0;
    private  int numberShow=10;
    private  int firstVisibleItemTag=0;
    private static boolean requestFlag=false;
    private int totalItemFlag=0;

    private List<Map<String,Object>> orderList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_department_headers);


        lv_tasksss = (ListView) this.findViewById(R.id.lv_dispatching);
        mutilb = (Button) this.findViewById(R.id.button47);
        refreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.refresh_waitSubmissionOrder);


        mutilb.setText("派工");

        getData(startIndex,numberShow);
        permissioned=true;
        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,

                                    long arg3) {
                String msg="true";
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder.select.isChecked() == true) {
                    Log.i("radiobutton3","checkBox"+position+"被选择");
                    isChecked.put(position, true);
                } else {
                    isChecked.put(position, false);
                }
                Log.i(TAG, "item" + position);
                if (permissioned){
                    msg="true";
                }else
                    msg="false";
                Log.i(TAG, msg+"item" + position);
                Intent intent= new Intent(DepartmentHead.this, OrderDetails.class);
                intent.putExtra("orderId",orderList.get(position).get("orderId").toString());
                intent.putExtra("update",false);
                startActivity(intent);

            }
        });
        lv_tasksss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem==0)
                    refreshLayout.setEnabled(true);
                else
                    refreshLayout.setEnabled(false);
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&totalItemCount>totalItemFlag) {
                    totalItemFlag=totalItemCount;
                    if (requestFlag){
                        requestFlag=false;
                        getData(startIndex,numberShow);
                    }

                }

                //判断ListView的滑动方向
                if (firstVisibleItemTag == firstVisibleItem) {
                    Log.e("滑动分页：", "未发生滑动");
                } else if (firstVisibleItemTag > firstVisibleItem) {
                    Log.e("滑动分页：", "发生下滑");
                } else {
                    Log.e("滑动分页：", "发生上滑");
                }
                firstVisibleItemTag = firstVisibleItem;
            }
        });

        mutilb.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getData(startIndex,numberShow);
            }
        });



    }







    private void getData(int startIndexPos,int numberShowSum){
        //orderList.clear();
        Map<String,Object> map=new HashMap<>();
        map.put("startIndex",startIndexPos);
        map.put("number",numberShowSum);
        View processView = View.inflate(this, R.layout.simple_processbar, null);
        final android.support.v7.app.AlertDialog processDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();


        ApiService.GetString(this, "getOrderDispatching", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                refreshLayout.setRefreshing(false);
                if(response!=null&&!TextUtils.isEmpty(response)){
                    startIndex=startIndex+numberShow;
                    requestFlag=true;
                    Log.i("orderList", "获取待派工订单成功" + response);
                JSONArray jsonArray = JSONArray.parseArray(response);
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("orderOrg", jsonObject.getString("orderOrg"));
                    item.put("checkdateExpect", jsonObject.getString("checkdateExpect"));
                    item.put("orderId", jsonObject.getString("orderId"));
                    item.put("projectName", jsonObject.getString("projectName"));
                    item.put("projectAddress", jsonObject.getString("projectAddress"));
                    item.put("checkerExpect", jsonObject.getString("checkerExpect"));
                    item.put("isFirstSubmission", jsonObject.getString("isFirstSubmission"));
                    orderList.add(item);
                }
                if (orderList != null && orderList.size() != 0) {
                    isChecked = new HashMap<>();
                    for (int i = 0; i < orderList.size(); i++) {
                        isChecked.put(i, false);
                    }
                    mWaitAdapter = new MyAdapter(DepartmentHead.this, orderList);
                    lv_tasksss.setAdapter(mWaitAdapter);
                } else {
                    new AlertDialog.Builder(DepartmentHead.this).setTitle("暂无需要派工的订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }


            }

            @Override
            public void onError(Object tag, Throwable e) {
                Log.i(TAG,""+e.getMessage());
                refreshLayout.setRefreshing(false);
                Toast.makeText(DepartmentHead.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0x05) {
            if (data.getBooleanExtra("isCommit", false)) {
                List<String> integers = data.getStringArrayListExtra("dispatchingNumber");
                for (int i = 0; i < integers.size(); i++) {
                    for (int j=0;j<orderList.size();j++)
                        if (orderList.get(j).get("orderId").equals(integers.get(i)))
                            orderList.remove(j);

                }
                isChecked = new HashMap<>();
                for (int i = 0; i < orderList.size(); i++) {
                    mWaitAdapter.isSelected.put(i, false);
                }
                if (mutilb.getText().toString().equals("派工"))
                    mWaitAdapter.notifyDataSetChanged();
            }
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button47: {
                if (orderList.size()==0||orderList==null){
                    Toast.makeText(DepartmentHead.this,"暂时没有可派工订单",Toast.LENGTH_SHORT).show();
                }else {
                    List<String> orderIds = new ArrayList<>();
                    for (int i = 0; i < mWaitAdapter.isSelected.size(); i++) {
                        if (mWaitAdapter.getIsSelected().get(i)) {
                            isChecked.put(i, true);
                            orderIds.add(orderList.get(i).get("orderId").toString());
                        } else {
                            isChecked.put(i, false);
                        }
                    }
                    if (orderIds.size() == 0) {
                        Toast.makeText(this, "请先选择需要派工的订单", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(DepartmentHead.this, Dispatching.class);
                        intent.putStringArrayListExtra("dispatchingNumber", (ArrayList<String>) orderIds);
                        startActivityForResult(intent, 0x05);
                    }
                }
            }
            break;
            case R.id.fab:

                break;
            default:
                break;
        }
    }


    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {
        private List<Map<String,Object>> orderList;
        private HashMap<Integer, Boolean> isSelected;
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /*构造函数*/
        public MyAdapter(Context context, List<Map<String,Object>> orderList) {
            this.mInflater = LayoutInflater.from(context);
            this.orderList = orderList;
            this.isSelected = new HashMap<>();
            setIsSelected(isChecked);
        }
        private void init(){
            for (int i = 0; i < orderList.size(); i++) {
                getIsSelected().put(i, false);
            }
        }
        public void clear() {
            orderList.clear();
            notifyDataSetChanged();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public int getCount() {

            return orderList.size();//返回数组的长度
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Object getItem(int position) {
            return orderList.get(position);
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
                convertView = mInflater.inflate(R.layout.dispatching, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                holder.place = (TextView) convertView.findViewById(R.id.sendPlace);
                holder.taskIcon=(ImageView)convertView.findViewById(R.id.task_icon);
                holder.actualTime=(TextView)convertView.findViewById(R.id.actual_time);
                holder.select = (CheckBox) convertView.findViewById(R.id.dispatching_checkBox);
                holder.title = (TextView) convertView.findViewById(R.id.dispatching_unit);
                holder.expectChecker=(TextView)convertView.findViewById(R.id.expect_checker_item);
                holder.projectName=(TextView)convertView.findViewById(R.id.dispatching_projectName);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            //  holder.title.setText(orderList.get(position).getOrderUser().getProjectName());
            holder.projectName.setText(orderList.get(position).get("projectName").toString());
            holder.place.setText(orderList.get(position).get("projectAddress").toString() );
            holder.select.setChecked(getIsSelected().get(position));
            holder.actualTime.setText(orderList.get(position).get("checkdateExpect").toString());
            holder.title.setText(orderList.get(position).get("orderOrg").toString());
            holder.expectChecker.setText(orderList.get(position).get("checkerExpect").toString());
            if (orderList.get(position).get("isFirstSubmission").equals("true")){
                String project_name=orderList.get(position).get("projectName").toString();
                if(project_name.indexOf("(复检)")!=-1)
                    holder.taskIcon.setImageResource(R.drawable.third_order);
                else
                holder.taskIcon.setImageResource(R.drawable.first_order);
            }else {
                holder.taskIcon.setImageResource(R.drawable.second_order);
            }
            holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG,"onchecked");
                    holder.select.setChecked(isChecked);
                    isSelected.put(position, isChecked);
                }
            });
            if (!permissioned){
                Log.i("dispatching","setINVISIBLE");
                holder.select.setVisibility(View.INVISIBLE);
            }

            holder.taskIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderList.get(position).get("isFirstSubmission").equals("true")){

                    }else {
                        getFailSubmission(orderList.get(position).get("orderId").toString());

                    }
                }
            });


            return convertView;
        }

        private HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        private void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            this.isSelected = isSelected;
        }

    }

    /*存放控件*/
    public final class ViewHolder {
        public ImageView taskIcon;
        public CheckBox select;
        public TextView title;
        public TextView place;
        public TextView actualTime;
        public TextView projectName;
        public TextView expectChecker;
    }


    private void getFailSubmission(String orderId){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("orderId",orderId);
                    ApiService.GetString(DepartmentHead.this, "getSubmissionFailReason", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {

                            String data_info[]=response.split("##");
                            LinearLayout linearLayout=new LinearLayout(DepartmentHead.this);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);

                            for (int i=0;i<data_info.length;i=i+2){
                                TextView t_name=new TextView(DepartmentHead.this);
                                t_name.setText("\n    拒绝人："+data_info[i]);

                                TextView t_reson=new TextView(DepartmentHead.this);
                                t_reson.setText("    拒绝原因："+data_info[i+1]);

                                linearLayout.addView(t_name);
                                linearLayout.addView(t_reson);
                            }



                            new AlertDialog.Builder(DepartmentHead.this)
                                    .setTitle("派工失败原因")
                                    .setView(linearLayout)
                                    .setPositiveButton("确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {



                                                }
                                            }).show();

                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(DepartmentHead.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(DepartmentHead.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
