package com.example.frank.jinding.UI.TechnicorActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.frank.jinding.Conf.URLConfig;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.Upload.FtpUpload;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TechnicalViewDetails extends AppCompatActivity {


    private ImageButton back;
    private TextView title;
    private ListView lv_tasksss;
    private String path="";
    private MyAdapter mAdapter;
    FtpUpload ff=new FtpUpload();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_view_details);

        /*StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        */

        path=getIntent().getStringExtra("path");

        init();


        title.setText("待审核检测意见");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            }
        });

        
        
    }



    private void init(){

        lv_tasksss=(ListView)this.findViewById(R.id.lv_technical_view_details);

        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        lv_tasksss.setAdapter(mAdapter);//为ListView绑定Adapter


        getCheckDetails();

    }






    private void getCheckDetails(){

        //String data=orderId+"#"+consignmentId+"#"+deviceId;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",path);
                    ApiService.GetString(TechnicalViewDetails.this, "checkDetails", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            HashMap<String,String> map_info= JSON.parseObject(path,new TypeReference<HashMap<String,String>>(){});
                            if (!response.trim().equals("获取失败！")) {
                                String data[]=response.split("##");
                                for (int i=0;i+1<data.length;i+=2){
                                   String imgurl= URLConfig.CompanyURL+map_info.get("orderId")+"/"+map_info.get("consignmentId")+"/"+map_info.get("deviceId")+"/"+data[i]+".jpg";
                                    //Toast.makeText(TechnicalViewDetails.this, "获取到："+imgurl , Toast.LENGTH_SHORT).show();
                                    Log.i("测试信息：",response.toString()+"=="+imgurl);
                                    //ff.download(TechnicalViewDetails.this,pp[0]+"/"+pp[1]+"/"+pp[2]+"/",data[i]+".jpg");
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                   // map.put("ItemImage", path+data[i]+".jpg");
                                    map.put("ItemImage", imgurl);
                                    map.put("ItemText", data[i+1]);
                                    mAdapter.listItem.add(map);


                                }
                                mAdapter.notifyDataSetChanged();


                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(TechnicalViewDetails.this, "加载失败" + e, Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(TechnicalViewDetails.this, "加载失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();





    }








    /*添加一个得到数据的方法，方便使用*/
    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<HashMap<String, Object>> getDate(){

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();

        *//*为动态数组添加数据*//*

        for(int i=0;i<10;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemName", "此设备经过一起检测没有发现问题，人工查看也没有问题。");
            map.put("ItemImage", R.drawable.report);


            listItem.add(map);
        }
        return listItem;

    }*/







    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private ArrayList<HashMap<String, Object>> listItem= new ArrayList<HashMap<String,     Object>>();
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
                convertView = mInflater.inflate(R.layout.technial_check_infomation,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.name = (TextView) convertView.findViewById(R.id.textView80);
                holder.pic=(ImageView)convertView.findViewById(R.id.imageView7) ;



                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.name.setText(listItem.get(position).get("ItemText").toString());
           //holder.pic.setImageResource(Integer.parseInt(listItem.get(position).get("ItemImage").toString()));
          //  Bitmap bm = BitmapFactory.decodeFile(listItem.get(position).get("ItemImage").toString());
        //    holder.pic.setImageBitmap(bm);
            Glide.with(TechnicalViewDetails.this).load(listItem.get(position).get("ItemImage")).into(holder.pic);

            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* LayoutInflater inflater = getLayoutInflater();
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

                    textv.setText(holder.name.getText());
                    new AlertDialog.Builder(TechnicalViewDetails.this).setTitle("详细信息").setView(layout)
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
                            }).show();*/



                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_report, (ViewGroup) findViewById(R.id.dalog_report));
                    final ImageView img = (ImageView) layout.findViewById(R.id.imageView6);
                    final TextView textv =(TextView) layout.findViewById(R.id.textView34);
                    String picname=mAdapter.listItem.get(position).get("ItemImage").toString();
                    Glide.with(TechnicalViewDetails.this).load(picname).into(img);
                    textv.setText(holder.name.getText());
                    new AlertDialog.Builder(TechnicalViewDetails.this).setTitle("详细信息").setView(layout)
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

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView name;
        public ImageView pic;

    }

    
    
}
