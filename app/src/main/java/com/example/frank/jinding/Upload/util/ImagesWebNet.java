/*
package com.example.frank.jinding.Upload.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImagesWebNet extends AppCompatActivity {
    private Button btn;
   private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) this.findViewById(R.id.button);
              imageview = (ImageView) this.findViewById(R.id.imageView);

               StrictMode.setThreadPolicy(new
                       StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                             StrictMode.setVmPolicy(
                       new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

              btn.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View arg0) {
                             btn.setEnabled(false);
                                //String strURL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517153369470&di=cd4db2326b64efd41eeeb787be508dcc&imgtype=0&src=http%3A%2F%2Fs2.yiban.cn%2F20100825%2F13265062223_158.jpg";
                                String strURL = "ftp://119.23.243.96/20180128_073543.jpg";
                                try {
                                      Bitmap bitmap = getBitmap(strURL);
                                     imageview.setImageBitmap(bitmap);
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                      e.printStackTrace();
                                }
                        }
        });


    }


    public Bitmap getBitmap(String path) throws IOException {
                try {
                       URL url = new URL(path);
                       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                       conn.setConnectTimeout(5000);
                        conn.setRequestMethod("GET");
                        if (conn.getResponseCode() == 200) {
                               InputStream inputStream = conn.getInputStream();
                              Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                             return bitmap;
                         }
                   } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
                return null;
            }



}
*/
