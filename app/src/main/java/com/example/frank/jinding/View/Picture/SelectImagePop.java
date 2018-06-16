package com.henau.pictureselect.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.henau.pictureselect.R;
import com.henau.pictureselect.bean.FolderBean;

import java.util.List;

/**
 * 选取相册图片PopWindow
 */
public class SelectImagePop extends PopupWindow{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mWidth;
    private int mHeight;
    private View mContentView;
    private ListView mListView;
    private List<FolderBean> mFolderList;
    private ImagePopAdapter mAdapter;

    private OnSelectDirListener mListener;
    public interface OnSelectDirListener{
        void selectDir(FolderBean bean);
    }
    public void setOnSelectDirListener(OnSelectDirListener listener){
        this.mListener = listener;
    }

    public SelectImagePop(Context context, List<FolderBean> datas) {
        caculateSize(context);
        this.mContext = context;
        this.mFolderList = datas;
        mLayoutInflater = LayoutInflater.from(context);
        mContentView = mLayoutInflater.inflate(R.layout.pop_select_image, null);
        setContentView(mContentView);
        setWidth(mWidth);
        setHeight(mHeight);

        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.pop_show_style);

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                return false;
            }
        });

        initViews();
        initListener();

    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mListener != null){
                    mListener.selectDir(mFolderList.get(position));
                }
                dismiss();
            }
        });
    }

    private void initViews() {
        mListView = (ListView) mContentView.findViewById(R.id.listView);
        if(mAdapter == null){
            mAdapter = new ImagePopAdapter();
        }
        mListView.setAdapter(mAdapter);
    }

    private void caculateSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
        mHeight = (int) (displayMetrics.heightPixels * 0.7);
    }

    private class ImagePopAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mFolderList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFolderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.item_select_image,null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_iamge);
                viewHolder.picDir = (TextView) convertView.findViewById(R.id.item_pic_dir);
                viewHolder.picCount = (TextView) convertView.findViewById(R.id.item_pic_count);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FolderBean folderBean = mFolderList.get(position);
            viewHolder.imageView.setImageResource(R.mipmap.pic_failed);
            Glide.with(mContext).load(folderBean.getFirstImagePath()).into(viewHolder.imageView);
            viewHolder.picDir.setText(folderBean.getName());
            viewHolder.picCount.setText(folderBean.getCount()+"张");

            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView picDir;
            TextView picCount;
        }
    }
}
