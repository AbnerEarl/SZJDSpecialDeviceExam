package com.example.frank.jinding.UI.SelectPicture;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;


import com.example.frank.jinding.Adapter.ShowImageAdapter;
import com.example.frank.jinding.R;
import com.example.frank.jinding.UI.CheckerActivity.SelectEquipment;
import com.example.frank.jinding.Utils.Picture.Constant;
import com.example.frank.jinding.Utils.Picture.SelectPicUtil;
import com.example.frank.jinding.Utils.Picture.UIUtils;
import com.example.frank.jinding.View.Picture.SelectPicWayPop;

import java.io.File;
import java.util.ArrayList;

import static com.example.frank.jinding.UI.SelectPicture.MyPhotoActivity.runnable;


public class MyPhotoFragment extends Fragment {
    private Button btn_finish;
    private View mRootView;
    private ArrayList<String> mDataList = new ArrayList<>();//存储选取图片路径
    private GridView mGridView;
    private ShowImageAdapter mAdapter;
    private SelectPicWayPop mSelectPic;//选取图片方式

    public static boolean finish_selected=false;

    private String mFilePath;// 拍照文件名
    private String mCropPath;// 裁剪文件名

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSelectPop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView == null) mRootView = inflater.inflate(R.layout.fragment_my_photo,null);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mDataList = bundle.getStringArrayList(Constant.IMAGE_LIST);
        }

        mGridView = (GridView) mRootView.findViewById(R.id.gridView);
        btn_finish=(Button)mRootView.findViewById(R.id.btn_photo_select_finish);
        mAdapter = new ShowImageAdapter(mRootView.getContext(),mDataList);
        mAdapter.setOnClickImageListener(new ShowImageAdapter.OnClickImageListener() {
            @Override
            public void onClickAdd() {
                UIUtils.lightOff(getActivity());
                mSelectPic.showAtLocation(mGridView, Gravity.BOTTOM,0,0);
            }

            @Override
            public void onClickImage(int position) {
                mDataList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        mGridView.setAdapter(mAdapter);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_selected=true;
                MyPhotoActivity.handler.postDelayed(runnable, 1000);
                Toast.makeText(getContext(),"操作成功，正在读取数据",Toast.LENGTH_LONG).show();
               MyPhotoActivity.finishPhotoSelect=true;
               onDestroy();

            }
        });
    }

    public void initSelectPop(){
        mSelectPic = new SelectPicWayPop(getActivity());
        mSelectPic.setOnSelectPicListener(new SelectPicWayPop.OnSelectPicListener() {
            @Override
            public void onCapturePhoto() {
                mFilePath = SelectPicUtil.getRandomFilePath();
                SelectPicUtil.getImageFromCamera(getActivity(), mFilePath);
            }

            @Override
            public void onPickPhoto() {
                Intent intent = new Intent(getContext(),SelectImageActivity.class);
                intent.putStringArrayListExtra(Constant.IMAGE_LIST,mDataList);
                startActivityForResult(intent,Constant.REQUEST_CODE_PICK_MULTI);
            }
        });
        mSelectPic.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UIUtils.lightOn(getActivity());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        if(requestCode == Constant.REQUEST_CODE_PICK_MULTI){
            ArrayList<String> stringArrayList = data.getStringArrayListExtra(Constant.IMAGE_LIST);
            if(mDataList != null && stringArrayList != null){
                mDataList.clear();
                mDataList.addAll(stringArrayList);
                mAdapter.notifyDataSetChanged();
            }
        }

        if (requestCode == Constant.REQUEST_CODE_CROP) {
            if(!mDataList.contains(mCropPath)){
                mDataList.add(mCropPath);
                mAdapter.notifyDataSetChanged();
            }
        }

        SelectEquipment.mDataList=mDataList;
    }

    public static MyPhotoFragment newInstance(ArrayList<String> datas){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.IMAGE_LIST,datas);

        MyPhotoFragment fragment = new MyPhotoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
