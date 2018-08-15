package com.example.frank.jinding.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.frank.jinding.Conf.ThreadAndFileTag;
import com.example.frank.jinding.Conf.URLConfig;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Report.ReportRule;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.CheckerActivity.CheckReport;
import com.example.frank.jinding.Utils.DownloadProgressListener;
import com.example.frank.jinding.Utils.FileDownloader;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/5/30.
 */

public class ReportAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private ViewHolder_Order holder;
    private Context context;
    private Activity activity;
    private List<HashMap<String,Object>> listItem;
    private View.OnClickListener clickListener;
    private int option=-1;
    private String datainfo="";
    private AlertDialog alertDialog;
    private int newoption;

    public ReportAdapter(Context context,Activity activity,int option,int newoption,List<HashMap<String,Object>> list) {
        this.context=context;
        this.activity=activity;
        this.mInflater = LayoutInflater.from(context);
        listItem = new ArrayList<>();
        this.option=option;
        this.newoption=newoption;
        listItem=list;
    }
    public void setClickListener(View.OnClickListener clickListener){
        this.clickListener=clickListener;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.report_lv_item, null);
            holder = new ViewHolder_Order();
            holder.place = (TextView) convertView.findViewById(R.id.sendPlace);
            holder.actualTime = (TextView) convertView.findViewById(R.id.actual_time);
            holder.reportDetailBtn = (Button) convertView.findViewById(R.id.report_detail_button);

            holder.orderOrgTv = (TextView) convertView.findViewById(R.id.dispatching_unit);
            holder.deviceType = (TextView) convertView.findViewById(R.id.device_type);
            holder.reportNumber=(TextView)convertView.findViewById(R.id.report_number);
            holder.expectCheckerLayout = (LinearLayout) convertView.findViewById(R.id.report_person_layout);
            holder.iconAddress=(ImageView)convertView.findViewById(R.id.icon_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Order) convertView.getTag();
        }
        holder.reportDetailBtn.setVisibility(View.GONE);
        //holder.reportDetailBtn.setOnClickListener(this::onClick);
        holder.reportDetailBtn.setTag(position);
        holder.orderOrgTv.setText(listItem.get(position).get("orderOrg").toString());
        holder.deviceType.setText(listItem.get(position).get("deviceTypeName").toString());
        //holder.reportNumber.setText(listItem.get(position).get("reportId").toString());//修改为报告的report_code
        holder.reportNumber.setText(listItem.get(position).get("reportCode").toString());
        if (option!=3){
            holder.place.setText(listItem.get(position).get("projectAddress").toString());
        }else{
            holder.iconAddress.setVisibility(View.INVISIBLE);
            holder.place.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    /*  @Override
      public void onClick(View v) {
          int position=(Integer) v.getTag();
           initReportView(position);
      }*/
    public void initReportView(int position){
        View view=View.inflate(context,R.layout.report_detail_dialog,null);
        TextView firstPersonFrontTv=view.findViewById(R.id.report_first_front);
        TextView firstPerson=view.findViewById(R.id.report_first_person);
        TextView firstTimeFront=view.findViewById(R.id.report_first_time_front);
        TextView firstTime=view.findViewById(R.id.report_first_time);
        TextView secondPersonFrontTv=view.findViewById(R.id.report_second_person_front);
        TextView secondPerson=view.findViewById(R.id.report_second_person);
        TextView secondTime=view.findViewById(R.id.second_time);
        LinearLayout thirdPersonLayout=view.findViewById(R.id.third_person_layout);
        LinearLayout thirdTimeLayout=view.findViewById(R.id.third_time_layout);
        TextView thirdPerson=view.findViewById(R.id.report_third_person);
        TextView thirdTime=view.findViewById(R.id.third_time);
        LinearLayout fourthPersonLayout=view.findViewById(R.id.fourth_person_layout);
        LinearLayout fourthTimeLayout=view.findViewById(R.id.fourth_time_layout);
        TextView fourthPerson=view.findViewById(R.id.report_fourth_person);
        TextView fourthTime=view.findViewById(R.id.fourth_time);
        Button actionBtnPass=view.findViewById(R.id.action_pass);
        Button actionBtnRefuse=view.findViewById(R.id.action_refuse);
        ImageButton exitBtn=view.findViewById(R.id.exit_report_detail);





        if (option==1){
            thirdPersonLayout.setVisibility(View.GONE);
            thirdTimeLayout.setVisibility(View.GONE);
            fourthPersonLayout.setVisibility(View.GONE);
            fourthTimeLayout.setVisibility(View.GONE);
            firstPerson.setText(listItem.get(position).get("mainchecker").toString());
            firstTime.setText(listItem.get(position).get("checkDate").toString());
            secondPerson.setText(listItem.get(position).get("modifiedPeople").toString());
            secondTime.setText(listItem.get(position).get("modifiedTime").toString());
        }else if (option==2||option==3){
            //待审核报告
            if (option==2) {
                thirdPersonLayout.setVisibility(View.GONE);
                thirdTimeLayout.setVisibility(View.GONE);
                fourthPersonLayout.setVisibility(View.GONE);
                fourthTimeLayout.setVisibility(View.GONE);
                firstPerson.setText(listItem.get(position).get("createPeople").toString());
                firstTime.setText(listItem.get(position).get("createTime").toString());
                //actionBtn.setText("前往审核");
                if (newoption==1){
                    actionBtnPass.setVisibility(View.INVISIBLE);
                    actionBtnRefuse.setVisibility(View.INVISIBLE);
                }else {
                    actionBtnPass.setVisibility(View.VISIBLE);
                    actionBtnRefuse.setVisibility(View.VISIBLE);
                }
            }
            firstPersonFrontTv.setText("报告提交人");
            firstTimeFront.setText("提交时间");
            secondPersonFrontTv.setText("报告校核人");
            secondPerson.setText(listItem.get(position).get("checkPeople").toString());
            secondTime.setText(listItem.get(position).get("checkTime").toString());
            if (option==3){
                if(newoption==1) {
                    //actionBtn.setText("前往审批");
                    actionBtnPass.setVisibility(View.GONE);
                    actionBtnRefuse.setVisibility(View.GONE);
                    fourthPersonLayout.setVisibility(View.GONE);
                    fourthTimeLayout.setVisibility(View.GONE);
                    firstPerson.setText(listItem.get(position).get("modifiedPeople").toString());
                    firstTime.setText(listItem.get(position).get("modifiedTime").toString());
                    System.out.println("适配器取得审核人：" + listItem.get(position).get("auditPeople").toString());
                    thirdPerson.setText(listItem.get(position).get("auditPeople").toString());
                    thirdTime.setText(listItem.get(position).get("auditTime").toString());
                }
                else if(newoption==2){

                    //actionBtn.setText("前往审批");
                    fourthPersonLayout.setVisibility(View.GONE);
                    fourthTimeLayout.setVisibility(View.GONE);
                    firstPerson.setText(listItem.get(position).get("modifiedPeople").toString());
                    firstTime.setText(listItem.get(position).get("modifiedTime").toString());
                    System.out.println("适配器取得审核人：" + listItem.get(position).get("auditPeople").toString());
                    thirdPerson.setText(listItem.get(position).get("auditPeople").toString());
                    thirdTime.setText(listItem.get(position).get("auditTime").toString());
                    secondPerson.setText(listItem.get(position).get("checkPeople").toString());
                    secondTime.setText(listItem.get(position).get("checkTime").toString());


                }
            }
        }
        else if(option==4){
            actionBtnPass.setVisibility(View.GONE);
            actionBtnRefuse.setVisibility(View.GONE);
            firstPerson.setText(listItem.get(position).get("modifiedPeople").toString());
            firstTime.setText(listItem.get(position).get("modifiedTime").toString());
            System.out.println("适配器取得审核人：" + listItem.get(position).get("auditPeople").toString());
            secondPerson.setText(listItem.get(position).get("checkPeople").toString());
            secondTime.setText(listItem.get(position).get("checkTime").toString());
            thirdPerson.setText(listItem.get(position).get("auditPeople").toString());
            thirdTime.setText(listItem.get(position).get("auditTime").toString());
            fourthPerson.setText(listItem.get(position).get("approvePeople").toString());
            fourthTime.setText(listItem.get(position).get("approveTime").toString());

        }
        alertDialog=new AlertDialog.Builder(context).setView(view).create();
        alertDialog.show();


        //下载文件初试相关参数，并进行参数准备，
        ThreadAndFileTag.Thread1=false;
        ThreadAndFileTag.Thread2=false;
        ThreadAndFileTag.Thread3=false;
        String deviceTypeId= listItem.get(position).get("deviceTypeId").toString();
        String checkTypeId=listItem.get(position).get("checkTypeId").toString();
        String monitorStatus="";
        if (listItem.get(position).get("monitorStatus")!=null) {
            monitorStatus = listItem.get(position).get("monitorStatus").toString();
        }

        String projectCity=listItem.get(position).get("projectCity").toString();
        String reportId=listItem.get(position).get("reportId").toString();
        String deviceDetailId=listItem.get(position).get("deviceDetailId").toString();
        String mainCheckReference=listItem.get(position).get("mainCheckReference").toString();
        String report=ReportRule.deviceReportCorrespondence(deviceTypeId,checkTypeId,monitorStatus,mainCheckReference);
        //String reportTypeCode=listItem.get(position).get("reportCode").toString();
        datainfo= report+"#"+deviceDetailId+"#"+reportId;
        HashMap<String,String> map_data=new HashMap<>();
        map_data.put("report",report);
        map_data.put("deviceDetailId",deviceDetailId);
        map_data.put("reportId",reportId);
        System.out.println("原始数据的信息："+datainfo);
        getData(JSON.toJSONString(map_data),datainfo);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        /*actionBtnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent intent=new Intent(context, ViewReport.class);
                context.startActivity(intent);*//*

                //String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //String filePath = datapath+"ConferenceFiles/"+mAdapter.listItem.get(arg2).get("ItemText").toString().trim();
                //String filePath = URLConfig.ServiceURL+"uploads/ConferenceFiles/"+mAdapter.listItem.get(arg2).get("ItemText").toString().trim();



                String deviceTypeId= listItem.get(position).get("deviceTypeId").toString();
                String checkTypeId=listItem.get(position).get("checkTypeId").toString();
                String monitorStatus="";
                if (listItem.get(position).get("monitorStatus")!=null) {
                    monitorStatus = listItem.get(position).get("monitorStatus").toString();
               }
                String projectCity=listItem.get(position).get("projectCity").toString();
                String reportId=listItem.get(position).get("reportId").toString();
               *//* String orderId=listItem.get(position).get("orderId").toString();
                String submissionId=listItem.get(position).get("submissionId").toString();
                String reportTypeCode=listItem.get(position).get("reportTypeCode").toString();*//*
                String deviceDetailId=listItem.get(position).get("deviceDetailId").toString();

                Log.d("结论：",deviceTypeId+"===="+checkTypeId+"===="+monitorStatus+"===="+projectCity+"===="+reportId+"===="+deviceDetailId);
                String report=ReportRule.deviceReportCorrespondence(deviceTypeId,checkTypeId,monitorStatus,projectCity);
                Log.d("report结论:",""+report);
                System.out.println("报告名称："+report);
                if (!report.equals("")){
                    FileDisplayActivity.data=report+"#"+deviceDetailId+"#"+"订单id测试"+"#"+"派工id测试"+"#"+reportId+"#"+"报告类型测试";
                    //FileDisplayActivity.data=report+"#"+deviceDetailId+"#"+orderId+"#"+submissionId+"#"+reportId+"#"+reportTypeCode;
                   // String filePath = "http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc";
                    FileDisplayActivity.show(context, "");

                }



                *//*if (!EasyPermissions.hasPermissions(context, perms)) {
                    EasyPermissions.requestPermissions(context, "需要访问手机存储权限！", 10086, perms);
                } else {
                    FileDisplayActivity.show(context, filePath);
                }*//*
            }
        });*/

        actionBtnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBtnPass.setEnabled(false);
                actionBtnRefuse.setEnabled(false);
                String temd[]=datainfo.split("#");
                //String dd=temd[1]+"#1#  #"+temd[2];
                HashMap<String,String> map_data=new HashMap<>();
                map_data.put("deviceId",temd[1]);
                map_data.put("status","1");
                map_data.put("reason","");
                map_data.put("reportId",temd[2]);
                checkReport(JSON.toJSONString(map_data),position);
                actionBtnRefuse.setEnabled(true);
                actionBtnPass.setEnabled(true);
            }
        });

        actionBtnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et=new EditText(context);
                new  AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setMessage("\n请输入拒绝的理由：")
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
                                        actionBtnRefuse.setEnabled(false);
                                        actionBtnPass.setEnabled(false);
                                        String temd[]=datainfo.split("#");
                                        //String dd=temd[1]+"#0#"+et.getText().toString()+" #"+temd[2];
                                        HashMap<String,String> map_data=new HashMap<>();
                                        map_data.put("deviceId",temd[1]);
                                        map_data.put("status","0");
                                        map_data.put("reason",et.getText().toString());
                                        map_data.put("reportId",temd[2]);

                                        checkReport(JSON.toJSONString(map_data),position);
                                        actionBtnRefuse.setEnabled(true);
                                        actionBtnPass.setEnabled(true);
                                    }
                                }).show();
            }
        });
    }

    final class ViewHolder_Order {
        public Button reportDetailBtn;

        public TextView deviceType;
        public TextView place,orderOrgTv;
        public TextView actualTime;
        public TextView projectName;
        public TextView reportNumber;
        public LinearLayout expectCheckerLayout;
        public ImageView iconAddress;
    }




    private void getData(String map_data,String dd) {

        String savedir = Environment.getExternalStorageDirectory() + "/Luban/Doc/";
        ProgressBar progressBar=new ProgressBar(context);
        progressBar.setMinimumHeight(200);
        AlertDialog alertDialog=new AlertDialog.Builder(context).setView(progressBar).create();
        alertDialog.show();

        Map<String, Object> paremetes = new HashMap<>();
        paremetes.put("data", map_data);
        Log.e("映射路径","exportReport"+(dd.split("#")[0]));
        ApiService.GetString(activity, "exportReport"+(dd.split("#")[0]), paremetes, new RxStringCallback() {
            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNext(Object tag, String response) {
                System.out.println("报告地址："+response.trim());
                if (response!=null&&response.trim().length()>3){
                    String modifypath=response.replace("\\","/");
                    String path= URLConfig.ReportURL+modifypath;
                    //String path= URLConfig.ReportURL+"static/reportExport/tt.doc";
                    String outFileName=path.substring(path.lastIndexOf("/")+1);
                    String LocalFileName=savedir+outFileName;


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File savemk=new File(savedir);
                            FileDownloader loader = new FileDownloader(context, path, savemk, 3);
                            try {
                                loader.download(new DownloadProgressListener() {
                                    @Override
                                    public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度

                                        System.out.println("我的实时下载文件进度："+size);
                                        if (ThreadAndFileTag.Thread1&&ThreadAndFileTag.Thread2&&ThreadAndFileTag.Thread3){
                                            getWordFileIntent(LocalFileName);
                                            alertDialog.dismiss();
                                        }

                                    }
                                });
                            } catch (Exception e) {

                            }
                        }
                    }).start();


                }else {
                    Toast.makeText(context, "报告获取失败，请再试一次" , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();

            }


        });






    }


    private void checkReport(String dd,int pos) {


        //下面写的冗余代码是为了让提示信息不一样，和以后的操作扩展


        Map<String, Object> paremetes = new HashMap<>();
        paremetes.put("data", dd);
        if (option==1) {
            ApiService.GetString(activity, "checkReport" + (datainfo.split("#")[0]), paremetes, new RxStringCallback() {
                boolean flag = false;

                @Override
                public void onNext(Object tag, String response) {

                    if (response.equals("true")) {
                        alertDialog.dismiss();
                        Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                        listItem.remove(pos);
                        CheckReport.isOperate=true;
                        Message massage=new Message();
                        massage.what=101;
                        CheckReport.mHandler.sendMessage(massage);

                    } else if (response.equals("false")) {
                        alertDialog.dismiss();
                        Toast.makeText(context, "操作失败，请查看报告状态", Toast.LENGTH_SHORT).show();
                        CheckReport.isOperate=true;
                        Message massage=new Message();
                        massage.what=101;
                        CheckReport.mHandler.sendMessage(massage);
                    } else if (response.equals("重新登录")) {
                        Toast.makeText(context, "您需要重新登录，才有权限进行此操作", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancel(Object tag, Throwable e) {
                    Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();

                }


            });

        }else if (option==2){
            ApiService.GetString(activity, "auditReport" + (datainfo.split("#")[0]), paremetes, new RxStringCallback() {
                boolean flag = false;

                @Override
                public void onNext(Object tag, String response) {

                    if (response.equals("true")) {
                        alertDialog.dismiss();
                        Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                        listItem.remove(pos);
                        CheckReport.isOperate=true;
                        Message massage=new Message();
                        massage.what=101;
                        CheckReport.mHandler.sendMessage(massage);
                    } else if (response.equals("false")) {
                        alertDialog.dismiss();
                        Toast.makeText(context, "操作失败，请查看报告状态", Toast.LENGTH_SHORT).show();
                        CheckReport.isOperate=true;
                        Message massage=new Message();
                        massage.what=101;
                        CheckReport.mHandler.sendMessage(massage);
                    } else if (response.equals("重新登录")) {
                        Toast.makeText(context, "您需要重新登录，才有权限进行此操作", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancel(Object tag, Throwable e) {
                    Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();

                }


            });
        }else if (option==3){

            ApiService.GetString(activity, "approveReport" + (datainfo.split("#")[0]), paremetes, new RxStringCallback() {
                boolean flag = false;

                @Override
                public void onNext(Object tag, String response) {

                    if (response.equals("true")) {
                        alertDialog.dismiss();
                        Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                        listItem.remove(pos);
                        CheckReport.isOperate=true;
                        Message massage=new Message();
                        massage.what=101;
                        CheckReport.mHandler.sendMessage(massage);
                    } else if (response.equals("false")) {
                        alertDialog.dismiss();
                        Toast.makeText(context, "操作失败，请查看报告状态", Toast.LENGTH_SHORT).show();
                        CheckReport.isOperate=true;
                        Message massage=new Message();
                        massage.what=101;
                        CheckReport.mHandler.sendMessage(massage);
                    } else if (response.equals("重新登录")) {
                        Toast.makeText(context, "您需要重新登录，才有权限进行此操作", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancel(Object tag, Throwable e) {
                    Toast.makeText(context, "查询失败" + e, Toast.LENGTH_SHORT).show();

                }


            });


        }

    }

    private  void getWordFileIntent ( String path ) {
        File docFile = new File(path);

        Intent in = new Intent("android.intent.action.VIEW");
        in.addCategory("android.intent.category.DEFAULT");
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, "com.example.frank.jinding.fileprovider", docFile);
            // 给目标应用一个临时授权
            in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(docFile);
        }
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.setDataAndType(data, "application/msword");
        context.startActivity(in);
    }


}



