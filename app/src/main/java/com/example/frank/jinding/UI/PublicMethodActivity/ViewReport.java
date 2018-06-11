package com.example.frank.jinding.UI.PublicMethodActivity;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewReport extends AppCompatActivity {

    private ListView lv_tasksss;
    private ImageButton back;
    private TextView title;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);


        init();

        title.setText("检验详情");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MyAdapter mAdapter = new  MyAdapter(this);//得到一个MyAdapter对象
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter

        // /*为ListView添加点击事件*/


        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

               // final ViewReport.ViewHolder holder;
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_report, (ViewGroup) findViewById(R.id.dalog_report));
                final ImageView img = (ImageView) layout.findViewById(R.id.imageView6);
                final TextView textv =(TextView) layout.findViewById(R.id.textView34);

                img.setImageResource(R.drawable.report);
                textv.setText("测试信息");

                new AlertDialog.Builder(ViewReport.this).setTitle("详细信息").setView(layout)
                        .setPositiveButton("确定", new  DialogInterface.OnClickListener()
                        {
                            @Override
                            public  void  onClick(DialogInterface dialog, int  which)
                            {

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



    private void init(){
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        lv_tasksss=(ListView)this.findViewById(R.id.lv_view_report);
    }




    /*添加一个得到数据的方法，方便使用*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        String task[]={"详情测试信息！"};
        /*为动态数组添加数据*/

        for(int i=0;i<11;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", task[0]);
            map.put("ItemImg", R.drawable.report);
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
                convertView = mInflater.inflate(R.layout.inspection_report,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.textView33);
                holder.pic = (ImageView) convertView.findViewById(R.id.imageView5);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.title.setText(getDate().get(position).get("ItemTitle").toString());
            holder.pic.setImageResource(Integer.parseInt(getDate().get(position).get("ItemImg").toString()));


            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title;
        public ImageView pic;

    }
    
    
    
    
    
    
}
