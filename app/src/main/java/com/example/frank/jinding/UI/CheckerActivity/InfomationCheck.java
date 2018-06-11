package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.Conf.CheckInfo;
import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InfomationCheck extends AppCompatActivity {

    private ListView lv_tasksss;
    private ImageButton back;
    private TextView title;

    private MyAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_check);



        init();

        title.setText("检验详情");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter

        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {


            }
        });



        
        
    }



    private void init(){
        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        lv_tasksss=(ListView)this.findViewById(R.id.lv_infomation_check);
    }




    /*添加一个得到数据的方法，方便使用*/
   /* @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        *//*String task[]={"12月1日，中共中央总书记、国家主席习近平在人民大会堂出席中国共产党与世界政党高层对话会开幕式，并发表题为《携手建设更加美好的世界》的主旨讲话。面对来自世界各国近300个政党和政治组织的领导人，习近平金句频出，对世界关心的问题发出“中国声音”，一起来看！"};
        *//**//*为动态数组添加数据*//**//*

        for(int i=0;i<11;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", task[0]);
            map.put("ItemImg", R.drawable.report);
            listItem.add(map);
        }
*//*
        listItem= CheckInfo.listItem;

        return listItem;

    }
*/






    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        ArrayList<HashMap<String, Object>> listItem = CheckInfo.listItem;
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
                convertView = mInflater.inflate(R.layout.inspection_report,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.textView33);
                holder.delete=(Button)convertView.findViewById(R.id.button60);
                holder.modify=(Button)convertView.findViewById(R.id.button61);
                holder.pic = (ImageView) convertView.findViewById(R.id.imageView5);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            /*holder.title.setText(getDate().get(position).get("ItemText").toString());
            holder.pic.setImageResource(Integer.parseInt(getDate().get(position).get("ItemImage").toString()));*/
            holder.title.setText(listItem.get(position).get("ItemText").toString());
            //holder.pic.setImageURI(Uri.parse(getDate().get(position).get("ItemImg").toString()));
            Bitmap bm = BitmapFactory.decodeFile(listItem.get(position).get("ItemImage").toString());
            holder.pic.setImageBitmap(bm);


            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // final InfomationCheck.ViewHolder holder;
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_report, (ViewGroup) findViewById(R.id.dalog_report));
                    final ImageView img = (ImageView) layout.findViewById(R.id.imageView6);
                    final TextView textv =(TextView) layout.findViewById(R.id.textView34);

                    //img.setImageResource(R.drawable.report);

                    //获取imageview中显示的图片
                    holder.pic.buildDrawingCache(true);
                    holder.pic.buildDrawingCache();
                    Bitmap bitmap = holder.pic.getDrawingCache();
                    img.setImageBitmap(bitmap);
                    holder.pic.setDrawingCacheEnabled(false);


                    textv.setText(holder.title.getText());
                    new AlertDialog.Builder(InfomationCheck.this).setTitle("详细信息").setView(layout)
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

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new  AlertDialog.Builder(InfomationCheck.this)
                            .setTitle("系统提示")
                            .setMessage("\n您确定删除本条检测记录吗？")
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
                                            mAdapter.listItem.remove(position);
                                            mAdapter.notifyDataSetChanged();
                                            Toast.makeText(InfomationCheck.this,"删除成功",Toast.LENGTH_SHORT).show();


                                        }
                                    }).show();
                }
            });

            holder.modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final EditText et=new EditText(InfomationCheck.this);
                    new  AlertDialog.Builder(InfomationCheck.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入新的检验情况说明：")
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
                                            String temimg=mAdapter.listItem.get(position).get("ItemImage").toString();
                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("ItemImage", temimg);
                                            map.put("ItemText", et.getText());
                                            mAdapter.listItem.remove(position);
                                            mAdapter.listItem.add(map);
                                            mAdapter.notifyDataSetChanged();
                                            Toast.makeText(InfomationCheck.this,"修改成功",Toast.LENGTH_SHORT).show();


                                        }
                                    }).show();

                }
            });


            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title;
        public Button delete,modify;
        public ImageView pic;

    }
    
    
    
}
