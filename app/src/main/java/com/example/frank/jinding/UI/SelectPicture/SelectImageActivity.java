package com.henau.pictureselect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henau.pictureselect.adapter.SelectImageAdapter;
import com.henau.pictureselect.bean.FolderBean;
import com.henau.pictureselect.utils.Constant;
import com.henau.pictureselect.utils.UIUtils;
import com.henau.pictureselect.view.SelectImagePop;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 选取相册照片页面
 */
public class SelectImageActivity extends AppCompatActivity {

    private GridView gridView;
    private RelativeLayout rlBottom;
    private TextView tvComplete;
    private TextView tvDirName;
    private TextView tvPicNum;

    private List<String> mImageList;//某一文件夹内图片列表
    private List<FolderBean> mFolderList;//文件夹列表
    private List<String> mSelectList;//存储选取图片路径
    private File mCurrentDir;
    private int mMaxCount;

    private ProgressDialog mProgressDialog;
    private SelectImageAdapter mImageAdapter;
    private SelectImagePop mPopWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            refreshView();
            initPopWindow();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        Intent intent = getIntent();
        if(intent != null) mSelectList = intent.getStringArrayListExtra(Constant.IMAGE_LIST);

        initViews();
        initDatas();
        initListener();
    }

    private void refreshView() {
        if(mCurrentDir == null){
            Toast.makeText(this,"未扫描到任何图片",Toast.LENGTH_LONG).show();
            return;
        }

        mImageList = Arrays.asList(mCurrentDir.list());
        if(mImageAdapter == null){
            mImageAdapter = new SelectImageAdapter(this,mImageList,mCurrentDir);
        }
        if(mSelectList != null) mImageAdapter.setSelectList(mSelectList);
        gridView.setAdapter(mImageAdapter);
        refreshUI();
    }

    private void refreshUI(){
        tvPicNum.setText(mMaxCount+"张");
        tvDirName.setText(mCurrentDir.getName());
    }

    private void initPopWindow() {
        mPopWindow = new SelectImagePop(this,mFolderList);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UIUtils.lightOn(getActivity());
            }
        });
        mPopWindow.setOnSelectDirListener(new SelectImagePop.OnSelectDirListener() {
            @Override
            public void selectDir(FolderBean bean) {
                mCurrentDir = new File(bean.getDir());
                mImageList = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if(filename.endsWith("jpg") || filename.endsWith("jpeg")
                                || filename.endsWith("png")){
                            return true;
                        }else {
                            return false;
                        }
                    }
                }));
                mMaxCount = mImageList.size();
                mImageAdapter.setData(mImageList,mCurrentDir);
                refreshUI();
            }
        });
    }

    private void initListener() {
        rlBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.showAsDropDown(rlBottom,0,0);
                UIUtils.lightOff(getActivity());
            }
        });
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(Constant.IMAGE_LIST,
                        (ArrayList<String>) mImageAdapter.getSelectList());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initDatas() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"当前存储卡不可用",Toast.LENGTH_LONG).show();
            return;
        }
        if(mFolderList == null){
            mFolderList = new ArrayList<>();
        }
        mProgressDialog = ProgressDialog.show(SelectImageActivity.this, null, "正在扫描中...");
        ScanImageTask mScanImageTask = new ScanImageTask();
        mScanImageTask.run();
    }

    private void initViews() {
        gridView = (GridView) findViewById(R.id.gridView);
        tvComplete = (TextView) findViewById(R.id.select_complete);
        rlBottom = (RelativeLayout) findViewById(R.id.select_bottom);
        tvDirName = (TextView) findViewById(R.id.select_dir_name);
        tvPicNum = (TextView) findViewById(R.id.select_pic_num);
    }

    private class ScanImageTask implements Runnable{
        @Override
        public void run() {
            ContentResolver cr = getContentResolver();
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            当前使用URI---content://media/external/images/media
//            Uri mImageUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
//            当前使用URI---content://media/internal/images/media
//            Uri mImageUri = Uri.parse(MediaStore.Images.Media.CONTENT_TYPE);
//            当前使用URI---vnd.android.cursor.dir/image
            Log.d("imageUri","当前使用URI---"+mImageUri.toString());
            Cursor cursor = cr.query(mImageUri, null
                    , MediaStore.Images.Media.MIME_TYPE + " = ? or "
                    + MediaStore.Images.Media.MIME_TYPE + " = ?"
                    , new String[]{"image/jpeg", "image/png"}
                    , MediaStore.Images.Media.DATE_MODIFIED);

            Set<String> mDirSet = new HashSet<>();

            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                File parentFile = new File(path).getParentFile();
                if(parentFile == null){
                    continue;
                }
                String dirPath = parentFile.getAbsolutePath();
                if(mDirSet.contains(dirPath)){
                    continue;
                }else {
                    mDirSet.add(dirPath);
                    FolderBean folderBean = new FolderBean();
                    folderBean.setDir(dirPath);
                    folderBean.setFirstImagePath(path);
                    if(parentFile.list() == null){
                        continue;
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith("jpg") || filename.endsWith("jpeg")
                                    || filename.endsWith("png")){
                                return true;
                            }else {
                                return false;
                            }
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderList.add(folderBean);
                    if(picSize > mMaxCount){
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
            }
            cursor.close();
            mHandler.sendEmptyMessage(0x110);
        }
    }

    public AppCompatActivity getActivity(){
        return SelectImageActivity.this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.complete:
                Log.e("tag","点击完成");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
