package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.frank.jinding.Adapter.ImageAdapter;
import com.example.frank.jinding.Bean.ImageBean;
import com.example.frank.jinding.Conf.CheckInfo;
import com.example.frank.jinding.Luban.Luban;
import com.example.frank.jinding.Luban.OnCompressListener;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Utils.SaveImage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.iwf.photopicker.PhotoPicker;
//import top.zibin.luban.Luban;
//import top.zibin.luban.OnCompressListener;

public class PictureCompress extends AppCompatActivity {
  private static final String TAG = "Luban";
  private String filename,content;
  private List<ImageBean> mImageList = new ArrayList<>();
  private ImageAdapter mAdapter = new ImageAdapter(mImageList);
  private TextView plain;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picture_compress);

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);

    Intent intent=getIntent();
    filename=intent.getStringExtra("path");
    content=intent.getStringExtra("content");

    Button fab = (Button) findViewById(R.id.fab);
    plain=(TextView)this.findViewById(R.id.textView23);
    plain.setText("图片说明："+content);


    SaveImage.saveBitmap(filename,"compress");

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        /*PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(PictureCompress.this, PhotoPicker.REQUEST_CODE);*/

       /* //获取imageview中显示的图片
        imageView1.buildDrawingCache(true);
        imageView1.buildDrawingCache();
        Bitmap bitmap = imageView1.getDrawingCache();
        saveBitmapFile(bitmap);
        imageView1.setDrawingCacheEnabled(false);*/

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemImage", filename);
        map.put("ItemText", content);
        CheckInfo.listItem.add(map);

        new  AlertDialog.Builder(PictureCompress.this)
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
                        }).show();

/*

//        异步上传图片
        new Thread(new Runnable() {
          @Override
          public void run() {
            String result="";
            try{

//          HttpUpload httpUpload=new HttpUpload();
//          httpUpload.upload(filename,PictureCompress.this);


          */
/*Aria.upload(PictureCompress.this).register();
          Aria.upload(PictureCompress.this)
                  .load(filename)
                  .setUploadUrl("http://192.168.191.1:8020/")
                  .setAttachment("file")
                  .start();*//*



//          Toast.makeText(PictureCompress.this,filename,Toast.LENGTH_LONG).show();
//
//          Aria.upload(PictureCompress.this).register();
//          UploadEntity entity = Aria.upload(PictureCompress.this).getUploadEntity(filename);
//          Aria.upload(PictureCompress.this).loadFtp(filename).setUploadUrl("ftp://192.168.191.1:21/UploadFiles/").login(null,null).start();
//
              //FtpUploadCommon ftpUploadCommon=new FtpUploadCommon();

              result=FtpUploadCommon.ftpUpload("ftp://192.168.191.1",21,"Frank","111111",filename);
              //result= FtpDownloadCommon.ftpDown("192.168.191.1",21,"Frank","111111",Environment.getExternalStorageDirectory() + "/Luban/image/","index.html","test.html");
              new  AlertDialog.Builder(PictureCompress.this)
                      .setTitle("系统提示")
                      .setMessage("上传成功！"+result)
                      .setPositiveButton("确定",
                              new  DialogInterface.OnClickListener()
                              {
                                @Override
                                public  void  onClick(DialogInterface dialog, int  which)
                                {
                                  finish();
                                }
                              }).show();
            }catch (Exception ex){
              new  AlertDialog.Builder(PictureCompress.this)
                      .setTitle("系统提示")
                      .setMessage("上传失败，请检查网络环境或稍后重试！"+result+ex.toString())
                      .setPositiveButton("确定",
                              new  DialogInterface.OnClickListener()
                              {
                                @Override
                                public  void  onClick(DialogInterface dialog, int  which)
                                {
                                  //finish();
                                }
                              }).show();
            }



          }
        }).start();
*/



      }
    });

    ArrayList<String> photos = new ArrayList<String>();
    photos.add(filename);
    compressWithRxs(photos);




  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
      if (data != null) {
        mImageList.clear();

        ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
//        compressWithLs(photos);
        compressWithRx(photos);
      }
    }
  }

  private void compressWithRx(final List<String> photos) {
    Flowable.just(photos)
            .observeOn(Schedulers.io())
            .map(new Function<List<String>, List<File>>() {
              @Override
              public List<File> apply(@NonNull List<String> list) throws Exception {
                return Luban.with(PictureCompress.this).load(list).get();
              }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<File>>() {
              @Override
              public void accept(@NonNull List<File> list) throws Exception {
                for (File file : list) {
                  showResult(photos, file);
                }
              }
            });
  }

  private void compressWithRxs(final List<String> photos) {
    Flowable.just(photos)
            .observeOn(Schedulers.io())
            .map(new Function<List<String>, List<File>>() {
              @Override
              public List<File> apply(@NonNull List<String> list) throws Exception {
                return Luban.with(PictureCompress.this).load(list).get();
              }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<File>>() {
              @Override
              public void accept(@NonNull List<File> list) throws Exception {
                for (File file : list) {
                  showResult(photos, file);
                }
              }
            });
  }

  /**
   * 压缩图片 Listener 方式
   */
  private void compressWithLs(final List<String> photos) {
    Luban.with(this)
            .load(photos)
            .ignoreBy(100)
            .setTargetDir(getPath())
            .setCompressListener(new OnCompressListener() {
              @Override
              public void onStart() {
              }

              @Override
              public void onSuccess(File file) {
                showResult(photos, file);
              }

              @Override
              public void onError(Throwable e) {
              }
            }).launch();
  }

  private String getPath() {
    String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
    File file = new File(path);
    if (file.mkdirs()) {
      return path;
    }
    return path;
  }

  private void showResult(List<String> photos, File file) {
    int[] originSize = computeSize(photos.get(mAdapter.getItemCount()));
    int[] thumbSize = computeSize(file.getAbsolutePath());
    String originArg = String.format(Locale.CHINA, "原图的参数：%d*%d, %dk", originSize[0], originSize[1], new File(photos.get(mAdapter.getItemCount())).length() >> 10);
    String thumbArg = String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", thumbSize[0], thumbSize[1], file.length() >> 10);

    ImageBean imageBean = new ImageBean(originArg, thumbArg, file.getAbsolutePath());
    mImageList.add(imageBean);
    mAdapter.notifyDataSetChanged();
  }

  private int[] computeSize(String srcImg) {
    int[] size = new int[2];

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    options.inSampleSize = 1;

    BitmapFactory.decodeFile(srcImg, options);
    size[0] = options.outWidth;
    size[1] = options.outHeight;

    return size;
  }





//  图片保存方法

  public void saveBitmapFile(Bitmap bitmap,String path,String picname){
    //File temp = new File("/sdcard/1delete/");//要保存文件先创建文件夹
    File temp = new File(path);
    if (!temp.exists()) {
      temp.mkdir();
    }
    ////重复保存时，覆盖原同名图片
    File file=new File(path+File.pathSeparator+picname);//将要保存图片的路径和图片名称
    //    File file =  new File("/sdcard/1delete/1.png");/////延时较长
    try {
      BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
      bos.flush();
      bos.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //    图片删除方法
  public boolean deleteFile(String picpath){

    try{
      //    File file = new File("/sdcard/1spray/1.png");
      File file = new File(picpath);
      if(file.exists()){
        file.delete();
      }
    }catch (Exception ex){
      ex.printStackTrace();
      return false;
    }
    return true;
  }


}
