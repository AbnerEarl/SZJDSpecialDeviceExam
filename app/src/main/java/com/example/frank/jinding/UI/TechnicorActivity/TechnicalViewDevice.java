package com.example.frank.jinding.UI.TechnicorActivity;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TechnicalViewDevice extends AppCompatActivity {

    private ImageButton back;
    private TextView title;
    private ListView lv_tasksss;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_view_device);


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

                Intent intent=new Intent(TechnicalViewDevice.this,TechnicalViewDetails.class);
                intent.putExtra("content","任务编号："+arg2);
                startActivity(intent);

            }
        });



    }



    private void init(){
        
        lv_tasksss=(ListView)this.findViewById(R.id.lv_view_device);

        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);

    }





    /*添加一个得到数据的方法，方便使用*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        /*为动态数组添加数据*/

        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemName", "大型起动机");
            map.put("ItemModel", "AS2312");
            map.put("ItemStyle", "高精度");
            map.put("ItemAddress", "TSG Q7015-09798");

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
                convertView = mInflater.inflate(R.layout.device_check_infomation,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.name = (TextView) convertView.findViewById(R.id.textView73);
                holder.model = (TextView) convertView.findViewById(R.id.textView74);
                holder.style = (TextView) convertView.findViewById(R.id.textView75);
                holder.address = (TextView) convertView.findViewById(R.id.textView76);



                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.name.setText(getDate().get(position).get("ItemName").toString());
            holder.model.setText(getDate().get(position).get("ItemModel").toString());
            holder.style.setText(getDate().get(position).get("ItemStyle").toString());
            holder.address.setText(getDate().get(position).get("ItemAddress").toString());



            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView name,model,style,address;

    }

    
    

}
