package com.example.frank.jinding.UI.CheckerActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.BuildConfig;
import com.example.frank.jinding.Conf.CheckControl;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Utils.SaveImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Equipment_Recorde extends AppCompatActivity {

    private ImageView view;
    private Button cancel,comfirm;
    private EditText infomation,contentrrr;
    private String ffilename="",temffilename="",temfilename="";
    private ImageButton back;
    private TextView title;

    private  String path = Environment.getExternalStorageDirectory() + "/Luban/image/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment__recorde);


        view= (ImageView)findViewById(R.id.imageView2);
        cancel=(Button)this.findViewById(R.id.button_cancel_eq);
        comfirm=(Button)this.findViewById(R.id.button_comfirm_eq);
        //contentrrr=(EditText)this.findViewById(R.id.editText5_equipment_recorde);
        infomation=(EditText)this.findViewById(R.id.inpust_check_introduc);

        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);

        //标题栏设置
        title.setText("检验说明");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckControl.isPhoto=false;
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
                CheckControl.isPhoto=false;
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
                    CheckControl.isPhoto = true;
                    String intro = infomation.getText().toString();
                    String data = "true##" + ffilename + "##" + intro + "##0";
                    Intent intent2 = new Intent();
                    intent2.putExtra("dd", data);
                    Equipment_Recorde.this.setResult(5202, intent2);
                    Toast.makeText(Equipment_Recorde.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(Equipment_Recorde.this, "照片拍摄有问题，您可以点击“取消”或者“返回”到检验界面", Toast.LENGTH_SHORT).show();
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
