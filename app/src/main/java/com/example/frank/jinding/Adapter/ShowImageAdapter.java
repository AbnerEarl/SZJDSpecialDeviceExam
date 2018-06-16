package com.henau.pictureselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.henau.pictureselect.R;

import java.util.List;

/**
 * 显示图片适配器
 * Created by xxx on 2016/6/30.
 */
public class ShowImageAdapter extends BaseAdapter{
    private final int MAX_IMAGE_SIZE = 9;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDataList;

    private OnClickImageListener mListener;
    public interface OnClickImageListener{
        void onClickAdd();
        void onClickImage(int position);
    }
    public void setOnClickImageListener(OnClickImageListener listener){
        this.mListener = listener;
    }

    public ShowImageAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDataList = datas;
    }

    @Override
    public int getCount() {
        if (mDataList.size() == MAX_IMAGE_SIZE) {// 设置图片的需要选取的个数
            return mDataList.size();
        }
        return (mDataList.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_picture, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);
            holder.button = (ImageButton) convertView.findViewById(R.id.item_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == mDataList.size()) {
            Glide.with(mContext).load(R.drawable.img_image_add).into(holder.image);
            holder.button.setVisibility(View.INVISIBLE);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) mListener.onClickAdd();
                }
            });
        } else {
            Glide.with(mContext).load(mDataList.get(position)).into(holder.image);
            holder.button.setVisibility(View.VISIBLE);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null) mListener.onClickImage(position);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        ImageButton button;
    }

}
