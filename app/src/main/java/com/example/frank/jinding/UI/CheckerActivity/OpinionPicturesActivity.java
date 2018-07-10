package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.frank.jinding.Conf.URLConfig;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpinionPicturesActivity extends AppCompatActivity {

    private GridView gview;
    private MyGridViewAdapter myAdapter;
    public static List<Map<String, Object>> list_show_photo= new ArrayList<Map<String, Object>>();
    private Map<String, Object> checked_list= new HashMap<String, Object>();
    private ImageButton titleleft;
    private TextView title;
    private Button delete_picture;
    private String order_id="",consignment_id="",device_id="",pic_id_data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_pictures);

        Intent intentda=getIntent();
        consignment_id=intentda.getStringExtra("consignmentId");
        order_id=intentda.getStringExtra("orderId");
        device_id=intentda.getStringExtra("deviceId");

        titleleft=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        delete_picture=(Button)this.findViewById(R.id.btn_delete_opinion_picture);
        gview = (GridView) findViewById(R.id.gv_opinion_pictures);
        myAdapter=new MyGridViewAdapter(this);
        //配置适配器
        gview.setAdapter(myAdapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Map<String, Object> map = myAdapter.list_item.get(i);
                list_show_photo=myAdapter.list_item;
                Intent intent=new Intent(OpinionPicturesActivity.this,LookPhotoActivity.class);
                startActivity(intent);
            }
        });

        title.setText("检验意见照片");
        titleleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        delete_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked_list.size()>0) {
                    pic_id_data="";
                    for (Map.Entry<String, Object> entry : checked_list.entrySet()) {
                        String key = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        //list.add(value);
                        pic_id_data=pic_id_data+value+"##";

                    }
                    new  android.app.AlertDialog.Builder(OpinionPicturesActivity.this)
                            .setTitle("系统提示")
                            .setMessage("\n您是否确定要删除选中的检验意见照片？")
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
                                            deletePhotoById(pic_id_data);
                                        }
                                    }).show();

                }else {
                    Toast.makeText(OpinionPicturesActivity.this, "请选择需要删除的图片", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getOpinionPhoto();
    }


    public class MyGridViewAdapter extends BaseAdapter {

        private Context mContext = null;
        private LayoutInflater mLayoutInflater = null;
        //        private List<String> mList = null;
        private List<Map<String, Object>> list_item= new ArrayList<Map<String, Object>>();


        //private int width = 120;//每个Item的宽度,可以根据实际情况修改
        //private int height = 150;//每个Item的高度,可以根据实际情况修改


        public  class MyGridViewHolder{
            public ImageView imageview_thumbnail;
            public CheckBox checkBox_select;
        }

        public MyGridViewAdapter(Context context) {
            // TODO Auto-generated constructor stub
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list_item.size();
        }


        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            MyGridViewAdapter.MyGridViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new MyGridViewAdapter.MyGridViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.item_show_photo, null);
                viewHolder.imageview_thumbnail = (ImageView)convertView.findViewById(R.id.img_show_photo);
                viewHolder.checkBox_select = (CheckBox) convertView.findViewById(R.id.check_box_select);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (MyGridViewAdapter.MyGridViewHolder)convertView.getTag();
            }

            Glide.with(OpinionPicturesActivity.this).load(list_item.get(position).get("image").toString()).into(viewHolder.imageview_thumbnail);
            //viewHolder.checkBox_select.setText(list_item.get(position).get("text").toString());


            viewHolder.checkBox_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        //选中操作
                        Map<String ,Object> map=list_item.get(position);
                        checked_list.put(position+"",map.get("pic_id"));
                    }else {
                        checked_list.remove(position+"");
                    }
                }
            });




            return convertView;
        }



    }

    private void deletePhotoById(final String data){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(OpinionPicturesActivity.this, "deleteDeviceCheckOpinion", paremetes, new RxStringCallback() {
                        @Override
                        public void onNext(Object tag, String response) {
                           if (response.trim().equals("true")){
                               Toast.makeText(OpinionPicturesActivity.this, "删除成功" , Toast.LENGTH_SHORT).show();

                               getOpinionPhoto();
                           }else {
                               Toast.makeText(OpinionPicturesActivity.this, "删除失败，请退出后重新进入再操作" , Toast.LENGTH_SHORT).show();
                           }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(OpinionPicturesActivity.this, "删除失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(OpinionPicturesActivity.this, "删除失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private void getOpinionPhoto(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    //String data=order_id+"#"+consignment_id+"#"+device_id;
                    HashMap<String,String> map_data=new HashMap<>();
                    map_data.put("order_id",order_id);
                    map_data.put("consignment_id",consignment_id);
                    map_data.put("device_id",device_id);
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", JSON.toJSONString(map_data));
                    ApiService.GetString(OpinionPicturesActivity.this, "getDeviceCheckOpinion", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (response.contains("#")&&!response.trim().equals("没有记录")&&!response.trim().equals("上传失败！")) {
                                myAdapter.list_item.clear();
                                String data_pic[]=response.split("##");
                                for (int i=0;i<data_pic.length;i=i+2){
                                    String imgurl= URLConfig.CompanyURL+order_id+"/"+consignment_id+"/"+device_id+"/"+data_pic[i+1]+".jpg";
                                    Map<String, Object> map1 =new HashMap<>();
                                    map1.put("pic_id",data_pic[i]);
                                    map1.put("image",imgurl);
                                    myAdapter.list_item.add(map1);
                                }

                                myAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(OpinionPicturesActivity.this, "没有记录" , Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(OpinionPicturesActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(OpinionPicturesActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


}
