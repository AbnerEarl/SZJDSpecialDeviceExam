package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PublicMethodActivity.TLog;
import com.example.frank.jinding.Utils.Md5Tool;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FileDisplayActivity extends AppCompatActivity {


    private String TAG = "FileDisplayActivity";
    SuperFileView2 mSuperFileView;

    String filePath;
    public static String data="";

    private Button btn_pass,btn_reject;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        init();
    }


    public void init() {
        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });

        /*Intent intent = this.getIntent();
        String path = (String) intent.getSerializableExtra("path");

        if (!TextUtils.isEmpty(path)) {
            TLog.d(TAG, "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();*/

        btn_pass=(Button)this.findViewById(R.id.btn_pass_report);
        btn_reject=(Button)this.findViewById(R.id.btn_reject_report);


        btn_pass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打印Button的点击信息
                new  AlertDialog.Builder(FileDisplayActivity.this)
                        .setTitle("系统提示")
                        .setMessage("是否确定允许通过该报告的审核?")
                        .setPositiveButton("确定",
                                new  DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public  void  onClick(DialogInterface dialog, int  which)
                                    {
                                        String temd[]=data.split("#");

                                       String dd=temd[1]+"#1#  #"+temd[2]+"#"+temd[3]+"#"+temd[4]+"#"+temd[5];
                                       checkReport(dd);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });



        btn_reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打印Button的点击信息
                final EditText et=new EditText(FileDisplayActivity.this);
                new  AlertDialog.Builder(FileDisplayActivity.this)
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
                                        String temd[]=data.split("#");
                                        String dd=temd[1]+"#0#"+et.getText().toString()+" #"+temd[2]+"#"+temd[3]+"#"+temd[4]+"#"+temd[5];

                                        //String dd=data.split("#")[1]+"#0#"+et.getText().toString()+" ";
                                        checkReport(dd);
                                    }
                                }).show();

            }
        });


        getData(data);

    }


    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {


        if (getFilePath().contains("http")) {//网络地址要先下载

            downLoadFromNet(getFilePath(),mSuperFileView2);

        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        TLog.d("FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }


    public static void show(Context context, String url) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }

    private void downLoadFromNet(final String url,final SuperFileView2 mSuperFileView2) {

        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                TLog.d(TAG, "删除空文件！！");
                cacheFile.delete();
                return;
            }
        }



        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.d(TAG, "下载文件-->onResponse");
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        TLog.d(TAG, "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    TLog.d(TAG, "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        TLog.d(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    TLog.d(TAG, "文件下载成功,准备展示文件。");
                    //2.ACache记录文件的有效期
                    mSuperFileView2.displayFile(fileN);
                } catch (Exception e) {
                    TLog.d(TAG, "文件下载异常 = " + e.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, java.lang.Throwable t) {
                TLog.d(TAG, "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    TLog.d(TAG, "删除下载失败文件");
                    file.delete();
                }
            }

        });


    }

    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");

    }
    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/"
                + getFileName(url));
        TLog.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            TLog.d(TAG, "paramString---->null");
            return str;
        }
        TLog.d(TAG,"paramString:"+paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            TLog.d(TAG,"i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        TLog.d(TAG,"paramString.substring(i + 1)------>"+str);
        return str;
    }




    private void getData(String dd) {

        String path = Environment.getExternalStorageDirectory() + "/Luban/image/tt.doc";

        /*File file=new File(path);

        OpenFile openFile=new OpenFile();
        openFile.openFile(FileDisplayActivity.this,file);*/

       getWordFileIntent(path);


        //测试方便
        //String path= URLConfig.ReportURL+"static/reportExport/SZJD-BZ-QZ-21_out.doc";
/*
        if (!TextUtils.isEmpty(path)) {
            TLog.d(TAG, "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();*/


          /*  Map<String, Object> paremetes = new HashMap<>();
            paremetes.put("data", dd);
            ApiService.GetString(FileDisplayActivity.this, "exportReport"+(data.split("#")[0]), paremetes, new RxStringCallback() {
                boolean flag = false;

                @Override
                public void onNext(Object tag, String response) {
                    System.out.println("报告地址："+response.trim());
                   if (response!=null&&response.trim().length()>3){
                        String modifypath=response.replace("\\","/");
                        String path= URLConfig.ReportURL+modifypath;
                       //String path= URLConfig.ReportURL+"static/reportExport/tt.doc";

                        if (!TextUtils.isEmpty(path)) {
                            TLog.d(TAG, "文件path:" + path);
                            setFilePath(path);

                        }
                        mSuperFileView.show();
                    }else {
                        Toast.makeText(FileDisplayActivity.this, "报告获取失败，请再试一次" , Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(FileDisplayActivity.this, "查询失败" + e, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancel(Object tag, Throwable e) {
                    Toast.makeText(FileDisplayActivity.this, "查询失败" + e, Toast.LENGTH_SHORT).show();

                }


            });
*/




    }


    private  void getWordFileIntent ( String path ) {
        File docFile = new File(path);

        Intent in = new Intent("android.intent.action.VIEW");
        in.addCategory("android.intent.category.DEFAULT");
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(FileDisplayActivity.this, "com.example.frank.jinding.fileprovider", docFile);
            // 给目标应用一个临时授权
            in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(docFile);
        }
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.setDataAndType(data, "application/msword");
        startActivity(in);
    }

    private void checkReport(String dd) {




        Map<String, Object> paremetes = new HashMap<>();
        paremetes.put("data", dd);
        ApiService.GetString(FileDisplayActivity.this, "checkReport"+(data.split("#")[0]), paremetes, new RxStringCallback() {
            boolean flag = false;

            @Override
            public void onNext(Object tag, String response) {

                if (response.equals("true")){
                    Toast.makeText(FileDisplayActivity.this, "操作成功" , Toast.LENGTH_SHORT).show();
                }else if (response.equals("false")){
                    Toast.makeText(FileDisplayActivity.this, "操作失败" , Toast.LENGTH_SHORT).show();
                }else if (response.equals("重新登录")){
                    Toast.makeText(FileDisplayActivity.this, "您需要重新登录，才有权限进行此操作" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(FileDisplayActivity.this, "查询失败" + e, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                Toast.makeText(FileDisplayActivity.this, "查询失败" + e, Toast.LENGTH_SHORT).show();

            }


        });





    }



}
