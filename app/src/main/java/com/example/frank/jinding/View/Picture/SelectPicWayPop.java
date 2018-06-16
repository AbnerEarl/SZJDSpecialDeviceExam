package com.henau.pictureselect.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.henau.pictureselect.R;

/**
 * 选取图片方式PopWindow
 */
public class SelectPicWayPop extends PopupWindow implements OnClickListener {

    private View mRootView;
    private final LayoutInflater mInflater;
    private TextView tvPhotoCamera, tvPhotoAlbum, tvCancel;//拍照 选择相册 取消
    private OnSelectPicListener mListener;

    public interface OnSelectPicListener{
        void onCapturePhoto();
        void onPickPhoto();
    }

    public SelectPicWayPop(Activity context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        setListener();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRootView = mInflater.inflate(R.layout.pop_select_pic_way, null);
        this.setContentView(mRootView);
        tvPhotoCamera = (TextView) mRootView.findViewById(R.id.add_photo_camra);
        tvPhotoAlbum = (TextView) mRootView.findViewById(R.id.add_photo_album);
        tvCancel = (TextView) mRootView.findViewById(R.id.add_photo_cancel);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        tvPhotoCamera.setOnClickListener(this);
        tvPhotoAlbum.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void initData(){
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.pop_show_style);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        // mRootView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_photo_camra:
                if(mListener != null) mListener.onCapturePhoto();
                dismiss();
                break;
            case R.id.add_photo_album:
                if(mListener != null) mListener.onPickPhoto();
                dismiss();
                break;
            case R.id.add_photo_cancel:
                dismiss();
                break;
        }
    }

    public void setOnSelectPicListener(OnSelectPicListener listener){
        this.mListener = listener;
    }
}
