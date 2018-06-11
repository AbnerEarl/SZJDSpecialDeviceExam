package com.example.frank.jinding.UI.TechnicorActivity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TecnialCheckReport extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;
    private MyAdapter mAdapter;
    private ListView lv_tasksss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnial_check_report);

        init();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getReportData();

            }
        });

    }



    private void init(){
        refreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.fresh_technical_check_report);
        lv_tasksss=(ListView)this.findViewById(R.id.lv_tecnical_check_report);
        mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter


        getReportData();
    }




    private void getReportData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {



                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data","report");
                    ApiService.GetString(TecnialCheckReport.this, "technicalerReport", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {

                            refreshLayout.setRefreshing(false);
                            if (response!=null&&!response.trim().equals("获取失败！")&&!response.trim().equals("暂时没有报告")) {
                                String data[]=response.split("##");
                                mAdapter.listItem.clear();
                                for (int i=0;i+1<data.length;i++){

                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put("ItemText", data[i]);
                                    mAdapter.listItem.add(map);


                                }
                                mAdapter.notifyDataSetChanged();


                            }else if (response.trim().equals("暂时没有报告")){

                                Toast.makeText(TecnialCheckReport.this,"暂时没有报告，请稍后再查看",Toast.LENGTH_SHORT).show();
                            }else if (response.trim().equals("获取失败！")){
                                Toast.makeText(TecnialCheckReport.this,"获取失败,请检查网络稍后重试",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(TecnialCheckReport.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(TecnialCheckReport.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
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

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
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
                convertView = mInflater.inflate(R.layout.report_list_all,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.report_list_detailid);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            holder.title.setText(listItem.get(position).get("ItemText").toString());



            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title;


    }



}
