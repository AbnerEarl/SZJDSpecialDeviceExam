package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultOpinion extends TabActivity {

    private TabHost menuinfo;
    private Intent hadpass;
    private Intent notpass;
    private ImageButton back;
    private TextView title;

    private MyAdapter passAdapter,checkingAdapter;
    private MyAdapterNotpass notAdapter;
    private ListView lv_passed, lv_notpass, lv_checking;
    private String submission_id="",device_id="",instrment_codes="";
    private String orderId="";
   // private SwipeRefreshLayout refreshLayoutpassed,refreshLayoutnotpass,refreshLayoutchecking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_opinion);

        Intent intent=getIntent();
        submission_id=intent.getStringExtra("submission_id");
        orderId=intent.getStringExtra("orderId");
        device_id=intent.getStringExtra("deviceId");

        init();


        //标题栏设置
        title.setText("检测意见审核结果");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final TabHost tabHost = getTabHost();
        TabHost.TabSpec page1 = tabHost.newTabSpec("tab1")
                .setIndicator("已通过")
                .setContent(R.id.lpassed);
        tabHost.addTab(page1);
        TabHost.TabSpec page2 = tabHost.newTabSpec("tab2")
                .setIndicator("未通过")
                .setContent(R.id.lnotpass);
        tabHost.addTab(page2);
        TabHost.TabSpec page3= tabHost.newTabSpec("tab3")
                .setIndicator("审核中")
                .setContent(R.id.lchecking);
        tabHost.addTab(page3);

        /*tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("1");
            }
        });

        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("2");
            }
        });
        tabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("0");
            }
        });
*/

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.trim().equals("tab1")){
                    getData("1");

                }else if (tabId.trim().equals("tab2")){
                    getData("2");
                }else if (tabId.trim().equals("tab3")){
                    getData("0");
                }

                //Log.i("table:",tabId);
            }
        });

        // /*为ListView添加点击事件*/


        lv_passed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                HashMap<String, Object> mapinfo=passAdapter.listItem.get(arg2);
                new AlertDialog.Builder(ResultOpinion.this)
                        .setTitle("详细信息")
                        .setMessage("\n检验结论：" + mapinfo.get("ItemResult")+"\n检验意见："+mapinfo.get("ItemOpinion")+"\n审核状态："+mapinfo.get("ItemStatus"))
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();


            }
        });



        // /*为ListView添加点击事件*/


        lv_notpass.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                HashMap<String, Object> mapinfo=notAdapter.listItem.get(arg2);
                new AlertDialog.Builder(ResultOpinion.this)
                        .setTitle("详细信息")
                        .setMessage("\n检验结论：" + mapinfo.get("ItemResult")+"\n检验意见："+mapinfo.get("ItemOpinion")+"\n审核状态："+mapinfo.get("ItemStatus")+"\n修改建议："+mapinfo.get("ItemSuggestion"))
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

            }
        });



        // /*为ListView添加点击事件*/


        lv_checking.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                HashMap<String, Object> mapinfo=checkingAdapter.listItem.get(arg2);
                new AlertDialog.Builder(ResultOpinion.this)
                        .setTitle("详细信息")
                        .setMessage("\n检验结论：" + mapinfo.get("ItemResult")+"\n检验意见："+mapinfo.get("ItemOpinion")+"\n审核状态："+mapinfo.get("ItemStatus"))
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
            }
        });



        /*refreshLayoutpassed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayoutpassed.setRefreshing(true);
                getData("1");

            }
        });

        refreshLayoutchecking.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayoutchecking.setRefreshing(true);
                getData("0");

            }
        });
        refreshLayoutnotpass.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayoutnotpass.setRefreshing(true);
                getData("2");

            }
        });*/


    }

    private void init() {
        back = (ImageButton) this.findViewById(R.id.titleback);
        title = (TextView) this.findViewById(R.id.titleplain);

        lv_passed = (ListView) this.findViewById(R.id.lv_passed);
        lv_notpass = (ListView) this.findViewById(R.id.lv_notpass);
        lv_checking = (ListView) this.findViewById(R.id.lv_checking);

        /*refreshLayoutchecking=(SwipeRefreshLayout)this.findViewById(R.id.refresh_checking);
        refreshLayoutpassed=(SwipeRefreshLayout)this.findViewById(R.id.refresh_passed);
        refreshLayoutnotpass=(SwipeRefreshLayout)this.findViewById(R.id.refresh_notpass);
*/
        passAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        //passAdapter.listItem = CheckInfo.listpassed;
        lv_passed.setAdapter(passAdapter);//为ListView绑定Adapter
        notAdapter = new MyAdapterNotpass(this);//得到一个MyAdapter对象
        //notAdapter.listItem = CheckInfo.listnotpass;
        lv_notpass.setAdapter(notAdapter);//为ListView绑定Adapter
        checkingAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        //checkingAdapter.listItem = CheckInfo.listchecking;
        lv_checking.setAdapter(checkingAdapter);//为ListView绑定Adapter

       getData("1");

       //getInstrumentCodes();

    }


    private void getData(final String statuscode){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    //String data=statuscode+"#"+device_id;
                    HashMap<String,String> map_data=new HashMap<>();
                    map_data.put("statuscode",statuscode);
                    map_data.put("device_id",device_id);

                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data", JSON.toJSONString(map_data));
                    ApiService.GetString(ResultOpinion.this, "lookCheckOpinionResult", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            HashMap<String,String> map_data=JSON.parseObject(response.trim(),new TypeReference<HashMap<String,String>>(){});
                            if (map_data!=null&&map_data.get("result").equals("true")) {

                                    if (map_data.get("statusCode").trim().equals("0")) {

                                        checkingAdapter.listItem.clear();
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        if (map_data.get("examResult").trim().equals("0")) {
                                            map.put("ItemResult", "不合格");
                                        } else if (map_data.get("examResult").trim().equals("1")) {
                                            map.put("ItemResult", "合格");
                                        } else if (map_data.get("examResult").trim().equals("2")) {
                                            map.put("ItemResult", "需复检（待确认）");
                                        }
                                        map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                        map.put("ItemStatus", "检测意见正在审核中，请稍后查看！");

                                        checkingAdapter.listItem.add(map);
                                        checkingAdapter.notifyDataSetChanged();

                                    } else if (map_data.get("statusCode").trim().equals("1")) {
                                        passAdapter.listItem.clear();
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        if (map_data.get("examResult").trim().equals("0")) {
                                            map.put("ItemResult", "不合格");
                                        } else if (map_data.get("examResult").trim().equals("1")) {
                                            map.put("ItemResult", "合格");
                                        } else if (map_data.get("examResult").trim().equals("2")) {
                                            map.put("ItemResult", "需复检（待确认）");
                                        }
                                        map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                        map.put("ItemStatus", "检测意见已通过审核，请注意查看！");

                                        passAdapter.listItem.add(map);
                                        passAdapter.notifyDataSetChanged();
                                    } else if (map_data.get("statusCode").trim().equals("2")) {
                                        notAdapter.listItem.clear();
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        if (map_data.get("examResult").trim().equals("0")) {
                                            map.put("ItemResult", "不合格");
                                        } else if (map_data.get("examResult").trim().equals("1")) {
                                            map.put("ItemResult", "合格");
                                        } else if (map_data.get("examResult").trim().equals("2")) {
                                            map.put("ItemResult", "需复检（待确认）");
                                        }
                                        map.put("ItemOpinion", map_data.get("problemSuggestion"));
                                        map.put("ItemStatus", "检测意见未通过审核，请修改意见！");
                                        map.put("ItemSuggestion", map_data.get("changeSuggestion"));

                                        notAdapter.listItem.add(map);
                                        notAdapter.notifyDataSetChanged();
                                    }



                            }else {
                                Toast.makeText(ResultOpinion.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(ResultOpinion.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(ResultOpinion.this, "暂时没有数据" , Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




    /*
    * 新建一个类继承BaseAdapter，实现视图与数据的绑定
    */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

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
                convertView = mInflater.inflate(R.layout.opnion_result_passed, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.roresult = (TextView) convertView.findViewById(R.id.ro_result);
                holder.roopinion = (TextView) convertView.findViewById(R.id.ro_opinion);
                holder.rostatus = (TextView) convertView.findViewById(R.id.ro_status);


                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            try{
                holder.roresult.setText(listItem.get(position).get("ItemResult").toString());
                holder.roopinion.setText(listItem.get(position).get("ItemOpinion").toString());
                holder.rostatus.setText(listItem.get(position).get("ItemStatus").toString());

            }catch (Exception e){
                e.printStackTrace();
            }



            return convertView;
        }

    }

    /*存放控件*/
    public final class ViewHolder {
        public TextView roresult,roopinion,rostatus;


    }


    /*
   * 新建一个类继承BaseAdapter，实现视图与数据的绑定
   */
    private class MyAdapterNotpass extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        /*构造函数*/
        public MyAdapterNotpass(Context context) {
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
            final ViewHolderNotpass holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.opnion_result_notpass, null);
                holder = new ViewHolderNotpass();
                /*得到各个控件的对象*/
                holder.orresult = (TextView) convertView.findViewById(R.id.or_resultt);
                holder.oropinion = (TextView) convertView.findViewById(R.id.or_opinionn);
                holder.orstatus = (TextView) convertView.findViewById(R.id.or_statuss);
                holder.orsuggestion = (TextView) convertView.findViewById(R.id.or_suggestionn);
                holder.modify=(Button)convertView.findViewById(R.id.or_modify);


                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolderNotpass) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/


            holder.orresult.setText(listItem.get(position).get("ItemResult").toString());
            holder.oropinion.setText(listItem.get(position).get("ItemOpinion").toString());
            holder.orstatus.setText(listItem.get(position).get("ItemStatus").toString());
            holder.orsuggestion.setText(listItem.get(position).get("ItemSuggestion").toString());


            holder.modify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.modify_check_opion, (ViewGroup) findViewById(R.id.modify_check_opion));
                     List<String> list = new ArrayList<String>();
                     ArrayAdapter<String> spadapter;
                    final EditText etcontent = (EditText) layout.findViewById(R.id.editText_check_opinion);
                    final Spinner resultsp = (Spinner) layout.findViewById(R.id.spinner_check_opinion);

                    list.add("请选择检验结论：");
                    list.add("不合格");
                    list.add("合格");
                    list.add("需复检（待确认）");

                    //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
                    spadapter  =  new ArrayAdapter<String>(ResultOpinion.this,android.R.layout.simple_spinner_item,  list);
                    //第三步：为适配器设置下拉列表下拉时的菜单样式。
                    spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //第四步：将适配器添加到下拉列表上
                    resultsp.setAdapter(spadapter);

                    new AlertDialog.Builder(ResultOpinion.this)
                            .setTitle("修改报告")
                            .setView(layout)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("提交",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            /*if (instrment_codes.trim().equals("")||instrment_codes.trim().length()<2){
                                                getInstrumentCodes();
                                            }*/


                                            if (resultsp.getSelectedItemId()!=0&&etcontent.getText().toString().trim().length()>2){

                                                new  AlertDialog.Builder(ResultOpinion.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n请核对检测意见！\n\n检测结论："+resultsp.getSelectedItem()+"\n\n检测意见："+etcontent.getText()+"\n\n\n\n确认无误后，点击“确定”进行提交，点击“取消”返回修改！")
                                                        .setNegativeButton("取消",null)
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {
                                                                        HashMap<String ,String> map_data=new HashMap<>();

                                                                        String exam_result=(resultsp.getSelectedItemId()-1)+"";
                                                                        String problem_suggestion=etcontent.getText().toString();
                                                                        //String data=submission_id+"#"+device_id+"#"+instrment_codes+"#"+exam_result+"#"+problem_suggestion;
                                                                        map_data.put("exam_result",exam_result);
                                                                        map_data.put("problem_suggestion",problem_suggestion);
                                                                        map_data.put("submission_id",submission_id);
                                                                        map_data.put("device_id",device_id);
                                                                        map_data.put("orderId",orderId);
                                                                        Map<String, Object> paremetes = new HashMap<>();
                                                                        paremetes.put("data", JSON.toJSONString(map_data));
                                                                        ApiService.GetString(ResultOpinion.this, "addCheckOpinionResult", paremetes, new RxStringCallback() {
                                                                            boolean flag = false;

                                                                            @Override
                                                                            public void onNext(Object tag, String response) {

                                                                                if (response.trim().equals("重复提交")){
                                                                                    //Toast.makeText(ResultOpinion.this,"该台设备已经提交过检测意见，请查看审核结果",Toast.LENGTH_SHORT).show();
                                                                                    Toast.makeText(ResultOpinion.this,"该台设备的检测意见修改成功",Toast.LENGTH_SHORT).show();
                                                                                    notAdapter.listItem.remove(position);
                                                                                    notAdapter.notifyDataSetChanged();

                                                                                }else if (response.trim().equals("拒绝修改")){
                                                                                    Toast.makeText(ResultOpinion.this, "该台设备的检测意见已经审核通过，拒绝再次修改", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                else if (response.trim().equals("提交成功！")&& CheckControl.sign) {
                                                                                    etcontent.setText("");
                                                                                    CheckControl.start=true;
                                                                                    OperationProcess.handler.postDelayed(OperationProcess.runnable, 100);

                                                                                    Toast.makeText(ResultOpinion.this,"检测意见提交成功，请等待审核结果",Toast.LENGTH_SHORT).show();

                                                                                }else {
                                                                                    Toast.makeText(ResultOpinion.this, "提交失败" , Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onError(Object tag, Throwable e) {
                                                                                Toast.makeText(ResultOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();


                                                                            }

                                                                            @Override
                                                                            public void onCancel(Object tag, Throwable e) {
                                                                                Toast.makeText(ResultOpinion.this, "提交失败" + e, Toast.LENGTH_SHORT).show();

                                                                            }


                                                                        });


                                                                    }
                                                                }).show();
                                            }else if (etcontent.getText().toString().trim().length()<3){
                                                new  AlertDialog.Builder(ResultOpinion.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n请填写不少于3个字符的检测意见！")
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {

                                                                    }
                                                                }).show();
                                            }else {
                                                new  AlertDialog.Builder(ResultOpinion.this)
                                                        .setTitle("系统提示")
                                                        .setMessage("\n请选择检验结论！")
                                                        .setPositiveButton("确定",
                                                                new  DialogInterface.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public  void  onClick(DialogInterface dialog, int  which)
                                                                    {

                                                                    }
                                                                }).show();
                                            }
                                           /* HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("ItemName", holder.pname.getText());
                                            checkingAdapter.listItem.add(map);
                                            checkingAdapter.notifyDataSetChanged();
                                            //mySpinner.setSelected(true);
                                            notAdapter.listItem.remove(position);
                                            notAdapter.notifyDataSetChanged();
                                            Toast.makeText(ResultOpinion.this,"提交成功！",Toast.LENGTH_SHORT).show();*/
                                        }
                                    }).show();

                }
            });




            return convertView;
        }

    }

    /*存放控件*/
    public final class ViewHolderNotpass {
        public TextView orresult,oropinion,orstatus,orsuggestion;
        public Button modify;


    }




    
    
}
