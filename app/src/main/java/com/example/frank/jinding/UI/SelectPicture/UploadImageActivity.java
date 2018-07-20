package com.example.frank.jinding.UI.SelectPicture;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;


import com.example.frank.jinding.Adapter.ShowImageAdapter;
import com.example.frank.jinding.Bean.Picture.User;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Utils.Picture.Constant;
import com.example.frank.jinding.Utils.Picture.SelectPicUtil;
import com.example.frank.jinding.View.Picture.SelectPicWayPop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UploadImageActivity extends AppCompatActivity {

    public List<String> mDataList = new ArrayList<>();
    public static final int MAX_IMAGE_SIZE = 9;
    private GridView mGridView;
    private ShowImageAdapter mAdapter;
    private SelectPicWayPop mSelectPic;//选取图片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        mGridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new ShowImageAdapter(this,mDataList,false);
        mGridView.setAdapter(mAdapter);

        initSelectPop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            SelectPicUtil.createSDDir(Constant.IMAGES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String mFilePath;// 拍照文件名
    private String mCropPath;// 裁剪文件名

    public void initSelectPop(){
        mSelectPic = new SelectPicWayPop(this);
        mSelectPic.setOnSelectPicListener(new SelectPicWayPop.OnSelectPicListener() {
            @Override
            public void onCapturePhoto() {
                mFilePath = SelectPicUtil.getRandomFilePath();
                SelectPicUtil.getImageFromCamera(getActivity(), mFilePath);
            }

            @Override
            public void onPickPhoto() {
                SelectPicUtil.getImageFromAlbum(getActivity());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == Constant.REQUEST_CODE_PICK && data.getData() != null) {
            Uri uri = data.getData();
            mCropPath = SelectPicUtil.getRandomFilePath();
            SelectPicUtil.cropImageUri(getActivity(), mCropPath, uri, 600, 600);
        }

        if (requestCode == Constant.REQUEST_CODE_CAPTURE) {
            Uri uri = Uri.fromFile(new File(mFilePath));
            mCropPath = SelectPicUtil.getRandomFilePath();
            SelectPicUtil.cropImageUri(getActivity(), mCropPath, uri, 600, 600);
        }

        if (requestCode == Constant.REQUEST_CODE_CROP) {
            if(!mDataList.contains(mCropPath)){
                mDataList.add(mCropPath);
                mAdapter.notifyDataSetChanged();
                for (String str : mDataList) {
                    Log.e("tag",str);
                }
            }
        }
    }

    private UploadImageActivity getActivity(){
        return UploadImageActivity.this;
    }

    public void sendData(View view){
        Intent intent = new Intent(this,SelectImageActivity.class);
        intent.putExtra("user",new User(0, "Spike", false));
        startActivity(intent);
    }

}
