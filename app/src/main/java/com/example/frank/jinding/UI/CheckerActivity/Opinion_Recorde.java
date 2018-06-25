package com.example.frank.jinding.UI.CheckerActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.BuildConfig;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.Upload.FtpClientUpload;
import com.example.frank.jinding.Utils.SaveImage;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Opinion_Recorde extends AppCompatActivity {

    private ImageView view;
    private Button cancel,comfirm;

    private String ffilename="",temffilename="",temfilename="";
    private ImageButton back;
    private TextView title;

    private String order_id="",consignment_id="",device_id="";

    private  String path = Environment.getExternalStorageDirectory() + "/Luban/image/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion__recorde);

        Intent intentda=getIntent();
        consignment_id=intentda.getStringExtra("consignmentId");
        order_id=intentda.getStringExtra("orderId");
        device_id=intentda.getStringExtra("deviceId");


        view= (ImageView)findViewById(R.id.imageView2op);
        cancel=(Button)this.findViewById(R.id.button_cancel_eqop);
        comfirm=(Button)this.findViewById(R.id.button_comfirm_eqop);

        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);

        //标题栏设置
        title.setText("检验说明");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CheckControl.isPhoto=false;
                finish();
            }
        });


        /*final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);*/

        //设置自定义存储路径
        //mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/systemCemer";
        //存储文件夹操作
        File outFilePath = new File(path);
        if (!outFilePath.exists()) {
            outFilePath.mkdirs();
        }
        //设置自定义照片的名字
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        temfilename=fileName+"1";
        temffilename=path + "/" + fileName + "1.jpg";
        path = path + "/" + fileName + ".jpg";

        //ffilename=path;
        File outFile = new File(path);
        //Intent intentpp = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this.getApplicationContext(), "com.example.frank.jinding.fileprovider", outFile);
            //intentpp.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(outFile);
        }
        //Uri uri = Uri.fromFile(outFile);
        //拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 305);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckControl.isPhoto=false;
                finish();
            }
        });


        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CheckInfo.listItem.clear();
                /*HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", ffilename);
                map.put("ItemText", infomation.getText().toString());
                map.put("Tag","0");
                CheckInfo.listItem.add(map);*/

                if (!ffilename.equals("")) {

                    //Toast.makeText(Opinion_Recorde.this, "保存成功", Toast.LENGTH_SHORT).show();



                    new AlertDialog.Builder(Opinion_Recorde.this)
                            .setTitle("系统提示")
                            .setMessage("\n您是否确定上传本张检验意见照片？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {



                                                                //上传文件到服务器
                                                                String eqfilename = ffilename;
                                                                String datafilename = eqfilename.substring(eqfilename.lastIndexOf("/") + 1, eqfilename.length());
                                                                FtpClientUpload.UploadFile(eqfilename, order_id + "/" + consignment_id + "/" + device_id + "/", Opinion_Recorde.this, datafilename);
                                                                //ff.upload(SelectEquipment.this,orderId+"/"+consignmentId+"/"+deviceId,mAdapter.listItem.get(i).get("ItemImage").toString());

                                                                //上传文字描述到服务器
                                                                String filename = ffilename;
                                                                String dd = order_id + "#" + consignment_id + "#" + device_id + "#" + filename.substring(filename.lastIndexOf("/") + 1, filename.lastIndexOf(".")) ;
                                                                Map<String, Object> paremetes = new HashMap<>();
                                                                paremetes.put("data", dd);
                                                                ApiService.GetString(Opinion_Recorde.this, "addDeviceCheckOpinion", paremetes, new RxStringCallback() {
                                                                    boolean flag = false;

                                                                    @Override
                                                                    public void onNext(Object tag, String response) {

                                                                        if (response.trim().equals("上传成功！")) {
                                                                            Toast.makeText(Opinion_Recorde.this, "上传成功", Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(Object tag, Throwable e) {
                                                                        Toast.makeText(Opinion_Recorde.this, "" + e, Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onCancel(Object tag, Throwable e) {
                                                                        Toast.makeText(Opinion_Recorde.this, "" + e, Toast.LENGTH_SHORT).show();

                                                                    }


                                                                });






                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).start();
                                        }
                                    }).show();




                }


                /*new  AlertDialog.Builder(Equipment_Recorde.this)
                        .setTitle("系统提示")
                        .setMessage("\n保存成功！")
                        .setPositiveButton("确定",
                                new  DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public  void  onClick(DialogInterface dialog, int  which)
                                    {
                                        finish();
                                    }
                                }).show();*/


                /*Intent intent1=new Intent(Equipment_Recorde.this, PictureCompress.class);
                intent1.putExtra("path",filename);
                intent1.putExtra("content",content.getText().toString().trim());
                startActivity(intent1);
                finish();*/

//                Intent intent1=new Intent(Equipment_Recorde.this, ImageCompress.class);
//                intent1.putExtra("path",filename);
//                startActivity(intent1);
            }
        });


    }



    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            //filename=name;
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;

            File file = new File(path);
            file.mkdirs();// 创建文件夹
            String fileName = Environment.getExternalStorageDirectory() + "/Luban/image/"+name;
            ffilename=fileName;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try
            {
                view.setImageBitmap(bitmap);// 将图片显示在ImageView里
            }catch(Exception e)
            {
                Log.e("error", e.getMessage());
            }

        }*/



        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            if (requestCode==305){
                if (data!=null){

                }else {

                    SaveImage.saveBitmap(path,temfilename);
                    ffilename=temffilename;

                    if (BuildConfig.DEBUG) Log.d("SystemCemerActivity", path);

                    final Bitmap bitmap = loadingImageBitmap(path);
                    if (bitmap!=null){
                        view.setImageBitmap(bitmap);
                    }
                }
            }

        }



       /* if (resultCode==RESULT_OK){
            if (requestCode==305){
                if (data!=null){

                }else {

                    if (BuildConfig.DEBUG) Log.d("SystemCemerActivity", path);

                    final Bitmap bitmap = loadingImageBitmap(mFilePath);
                    if (bitmap!=null){
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
*/
    }









    public Bitmap loadingImageBitmap(String imagePath) {
        /**
         * 获取屏幕的宽与高
         */
        final int width = getWindowManager().getDefaultDisplay().getWidth();
        final int height = getWindowManager().getDefaultDisplay().getHeight();
        /**
         * 通过设置optios来只加载大图的尺寸
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            /**
             * 计算手机宽高与显示大图的宽高，然后确定缩放有比例
             */
            int widthRaio = (int) Math.ceil(options.outWidth/(float)width);
            int heightRaio = (int) Math.ceil(options.outHeight/(float)height);
            if (widthRaio>1&&heightRaio>1){
                if (widthRaio>heightRaio){
                    options.inSampleSize = widthRaio;
                }else {
                    options.inSampleSize = heightRaio;
                }
            }
            /**
             * 设置加载缩放后的图片
             */
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }



}
