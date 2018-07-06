package com.example.frank.commemorativebook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LookPhotoActivity extends AppCompatActivity {

    private Button welcome_btn;
    private ViewPager pager;
    private List<View> list=new ArrayList<>();
    private List<Map<String, Object>> list_show= new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_photo);

        list_show=PersonPhotoActivity.list_show_photo;
        initViewPager();

    }



    //初始化ViewPager的方法
    public void initViewPager() {

       /*ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.welcome);
        list.add(iv1);*/

         /*ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.report);
        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.drawable.welcome);
        list=new ArrayList<View>();

        list.add(iv2);
        list.add(iv3);*/

        for (int i=0;i<list_show.size();i++){
            ImageView img= new ImageView(this);
            Glide.with(LookPhotoActivity.this).load(list_show.get(i).get("image").toString()).into(img);
            list.add(img);
        }

        pager=(ViewPager)findViewById(R.id.fragmentviewpager);
        pager.setAdapter(new ViewPagerAdapter(list));
        //监听ViewPager滑动效果
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

                System.out.println("1111111111========="+arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                System.out.println("1111111111========="+arg0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }




}
