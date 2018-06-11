package com.example.frank.jinding.UI.PublicMethodActivity;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.example.frank.jinding.R;
import com.example.frank.jinding.UI.CheckerActivity.CheckOrderDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TestTask extends AppCompatActivity {

    private ListView lv_task;
    private ImageButton back;
    private TextView title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_task);

        init();

        //标题栏设置
        title.setText("订单任务");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_task.setAdapter(mAdapter);//为ListView绑定Adapter
/*为ListView添加点击事件*/
        lv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//                Log.v("MyListViewBase", "你点击了ListView条目" + arg2);//在LogCat中输出信息

                Intent intent=new Intent(TestTask.this,CheckOrderDetails.class);
                intent.putExtra("content","任务编号："+arg2);
                startActivity(intent);

            }
        });


    }


    private void init(){
        lv_task=(ListView)this.findViewById(R.id.lv_task);
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);

    }












    /*添加一个得到数据的方法，方便使用*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        String task[]={"深圳市宝安区有个工程需要检测！","安徽市宝安区有个工程需要检测！","荆门市宝安区有个工程需要检测！","武汉市宝安区有个工程需要检测！","广东市宝安区有个工程需要检测！","流浪市宝安区有个工程需要检测！","桂剧市宝安区有个工程需要检测！","乾州市宝安区有个工程需要检测！","澳洲市宝安区有个工程需要检测！","江苏市宝安区有个工程需要检测！","深圳市宝安区有个工程需要检测！","湖北市宝安区有个工程需要检测！","浙江市宝安区有个工程需要检测！","深圳市宝安区有个工程需要检测！","上海市宝安区有个工程需要检测！","天津市宝安区有个工程需要检测！","深圳市宝安区有个工程需要检测！","仙桃市宝安区有个工程需要检测！","武汉市宝安区有个工程需要检测！","深圳市宝安区有个工程需要检测！","广州市宝安区有个工程需要检测！"};

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
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.checktask,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.text_checktask);
//                holder.bt_chakan = (Button) convertView.findViewById(R.id.button_chakan);
//                holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_beizhu);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.title.setText(getDate().get(position).get("ItemTitle").toString());
//            holder.bt_chakan.setTag(position);
//            holder.bt_beizhu.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/





            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title;
//        public Button bt_chakan;
//        public Button bt_beizhu;
    }





}
