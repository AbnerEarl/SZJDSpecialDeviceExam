package com.example.frank.jinding.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.frank.jinding.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetNetImage extends AppCompatActivity {

    private ImageView imageView;
    private Button button;


     Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            final Bitmap bitmap=getPicture("http://h.hiphotos.baidu.com/news/q%3D100/sign=be8e90f30cfa513d57aa68de0d6d554c/c75c10385343fbf29d10a30cb47eca8065388fe4.jpg");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_net_image);

        imageView=(ImageView)this.findViewById(R.id.imageView4);
        button=(Button)this.findViewById(R.id.button41);


                ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                thread.start();
            }
        });
    }


    public Bitmap getPicture(String path){
        Bitmap bm=null;
        try{
            URL url=new URL(path);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bm= BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bm;
    }


}
