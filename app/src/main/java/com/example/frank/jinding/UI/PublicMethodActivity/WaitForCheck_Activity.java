package com.example.frank.jinding.UI.PublicMethodActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.frank.jinding.Adapter.RecyclerViewAdapter;
import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.List;


public class WaitForCheck_Activity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewOnItemListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private List<String> list=new ArrayList<>();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_check);
        context=this;
        mRecyclerView=(RecyclerView)findViewById(R.id.wait_check_recyclerView);
        //创建线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //默认vertical,可以不写
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new RecyclerViewAdapter(this);
        adapter.setList(list);
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(this);
        initData();
    }
    protected void initData(){

    }

    @Override
    public void onItemClick(View view, int position) {
        LayoutInflater inflater=getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.waitfor_check_dialog,null);
        ImageView imgView=(ImageView)dialogView.findViewById(R.id.dialog_img);
        AlertDialog dialog=new AlertDialog.Builder(context).create();
        ViewGroup.LayoutParams lpimg = imgView.getLayoutParams();
        dialog.show();
        dialog.setContentView(dialogView);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
        lpimg.width = lp.width;
        lpimg.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imgView.setLayoutParams(lpimg);
    }
}
