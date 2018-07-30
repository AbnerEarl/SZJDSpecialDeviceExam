package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderDetails;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryOrder extends AppCompatActivity {

    private ListView lv_task;
    private ImageButton back;
    private TextView title;
    private Calendar cal;
    private Button search;
    private TextView startdate, enddate;
    private int year, month, day;
    private List<JSONObject> submissionList;
    private MyAdapterO mAdapter;
    private EditText orderOrgEt;
    private  int startIndex=0;
    private  int numberShow=10;
    private  int firstVisibleItemTag=0;
    private static boolean requestFlag=false;
    private int totalItemFlag=0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_task);

        init();

        //标题栏设置
        title.setText("历史检测订单");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //测试数据


        startdate.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
               /*
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.date_picker_select, (ViewGroup) findViewById(R.id.date_pichker_select));

                final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
                //final String result = "";
                //获取日历的一个对象
                cal = Calendar.getInstance();
                //获取年月日时分秒的信息
                year = cal.get(Calendar.YEAR);
                //month从0开始计算(一月month = 0)
                month = cal.get(Calendar.MONTH) + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);

                new AlertDialog.Builder(HistoryOrder.this).setTitle("开始时间").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.i("选择开始时间","1");
                                datePicker.init(year, cal.get(Calendar.MONTH), day, new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        //result =(year+"-"+(monthOfYear+1)+"-"+dayOfMonth).toString();
                                       Log.i("确定开始时间","2");
                                        startdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        Toast.makeText(HistoryOrder.this, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                                    }


                                });


                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                        */
               chooseDate(1);
            }

        });


        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate(2);
/*


                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.date_picker_select, (ViewGroup) findViewById(R.id.date_pichker_select));

                final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
                //final String result = "";

                //获取日历的一个对象
                cal = Calendar.getInstance();
                //获取年月日时分秒的信息
                year = cal.get(Calendar.YEAR);
                //month从0开始计算(一月month = 0)
                month = cal.get(Calendar.MONTH) + 1;
                day = cal.get(Calendar.DAY_OF_MONTH);

                new AlertDialog.Builder(HistoryOrder.this).setTitle("结束时间").setView(layout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                   Log.i("选择结束时间","1");
                                datePicker.init(year, cal.get(Calendar.MONTH), day, new DatePicker.OnDateChangedListener() {
                                    @Override
                                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        //result =(year+"-"+(monthOfYear+1)+"-"+dayOfMonth).toString();
                                        Log.i("获得结束时间","2");
                                        enddate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        Toast.makeText(HistoryOrder.this, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            }
                        }).show();

*/

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(startIndex,numberShow);

            }
        });
        submissionList = new ArrayList<>();
         mAdapter = new MyAdapterO(this);//得到一个MyAdapter对象
        mAdapter.listItem = submissionList;
        lv_task.setAdapter(mAdapter);//为ListView绑定Adapter
/*为ListView添加点击事件*/
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            Intent intent = new Intent(HistoryOrder.this, OrderDetails.class);
                intent.putExtra("update", false);
                intent.putExtra("orderId",submissionList.get(arg2).get("orderId").toString());
                startActivity(intent);

            }
        });


        lv_task.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE://停止滑动
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://正在滑动
                        break;
                    case SCROLL_STATE_FLING://滑动ListView离开后，由于惯性继续滑动

                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //用于底部加载更多数据的判断逻辑,在这个地方调用自己的方法请求网络数据，一次性请求10条或者15条等
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&totalItemCount>totalItemFlag) {
                    totalItemFlag=totalItemCount;
                    if (requestFlag){
                        requestFlag=false;
                        search(startIndex,numberShow);
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



    }
    protected void chooseDate(final int code) {

//控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        java.util.Calendar selectedDate = java.util.Calendar.getInstance();
        final java.util.Calendar startDate = java.util.Calendar.getInstance();
        startDate.set(2013, 0, 23);
        final java.util.Calendar endDate = java.util.Calendar.getInstance();
        endDate.set(2100, 11, 28);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                if (code==1)
                startdate.setText(getTime(date));
                else
                    enddate.setText(getTime(date));


            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime.show();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void search(int startIndexPos,int numberShowSum) {
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final AlertDialog processDialog=new AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("requestCode", 5);
        if (startdate.getText() != null)
            map.put("startDate", startdate.getText().toString());
        else
            map.put("startDate", "2010-02-01");
        if (enddate.getText() != null)
            map.put("endDate", enddate.getText().toString());
        else
            map.put("endDate", "2100-02-01");
        if (orderOrgEt.getText()!=null&&TextUtils.isEmpty(orderOrgEt.getText()))
            map.put("orderOrg",orderOrgEt.getText());
        map.put("startIndex",startIndexPos);
        map.put("number",numberShowSum);
        ApiService.GetString(this, "submissionOrder", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                //submissionList.clear();
                if (response!= null && !TextUtils.isEmpty(response)) {
                    startIndex=startIndex+numberShow;
                    requestFlag=true;
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        submissionList.add(jsonObject);
                    }
                    Log.i("获得历史订单",submissionList.size()+"");
                    mAdapter.notifyDataSetChanged();

                }
                if (submissionList.size()==0) {
                    new AlertDialog.Builder(HistoryOrder.this).setTitle("历史订单为空").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }

            }

            @Override
            public void onError(Object tag, Throwable e) {
              processDialog.dismiss();
                Toast.makeText(HistoryOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        lv_task = (ListView) this.findViewById(R.id.lv_task);
        back = (ImageButton) this.findViewById(R.id.titleback);
        title = (TextView) this.findViewById(R.id.titleplain);

        startdate = (TextView) this.findViewById(R.id.start_date);
        enddate = (TextView) this.findViewById(R.id.end_date);
        search = (Button) this.findViewById(R.id.search_history);
        orderOrgEt=(EditText)this.findViewById(R.id.order_search_org);

    }
    //其他任务信息加载

    /*
   * 新建一个类继承BaseAdapter，实现视图与数据的绑定
   */
    private class MyAdapterO extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        List<JSONObject> listItem = new ArrayList<JSONObject>();
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
            final HistoryOrder.ViewHolderO holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.taskothers,null);
                holder = new HistoryOrder.ViewHolderO();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_unit);
                holder.pnumber = (TextView) convertView.findViewById(R.id.dispatching_projectName);
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);
//                holder.bt_chakan = (Button) convertView.findViewById(R.id.button_chakan);
//                holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_beizhu);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (HistoryOrder.ViewHolderO)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.pname.setText(listItem.get(position).get("orderOrg").toString());
            holder.pnumber.setText(listItem.get(position).get("projectName").toString());
            holder.paddress.setText(listItem.get(position).get("projectAddress").toString());
            Log.i("projectAddress",listItem.get(position).get("projectAddress").toString());


            holder.pdate.setText(listItem.get(position).get("checkTime").toString());

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolderO{
        public TextView pname,pnumber,paddress,pdate;
//        public Button bt_chakan;
//        public Button bt_beizhu;
    }




}