package com.henau.pictureselect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.henau.pictureselect.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选取相册图片适配器
 * Created by xxx on 2015/10/31.
 */
public class SelectImageAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mImageList;//某一文件夹内图片的集合
    private File mDir;
    private List<String> mSelectList = new ArrayList<>();//选中图片的集合

    public SelectImageAdapter(Context context, List<String> imageList, File dir) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mImageList = imageList;
        this.mDir = dir;
    }

    /**
     * 存储之前选中的图片
     */
    public void setSelectList(List<String> datas){
        mSelectList.clear();
        mSelectList = datas;
    }

    /**
     * 返回已经选中的图片
     */
    public List<String> getSelectList(){
        return mSelectList;
    }

    /**
     * 设置某一文件夹内的所有数据
     */
    public void setData(List<String> imageList, File dir){
        this.mImageList = imageList;
        this.mDir = dir;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_picture,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.item_button);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(R.mipmap.pic_failed);
        viewHolder.imageView.setColorFilter(null);
        viewHolder.imageButton.setImageResource(R.mipmap.pic_check_no);
        Glide.with(mContext).load(mDir.getAbsolutePath() + "/" + mImageList.get(position)).into(viewHolder.imageView);

        final String path = mDir.getAbsolutePath()+"/"+mImageList.get(position);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectList.contains(path)){
                    Log.d("remove",path+"---"+ mSelectList.size());
                    mSelectList.remove(path);
                    viewHolder.imageView.setColorFilter(null);
                    viewHolder.imageButton.setImageResource(R.mipmap.pic_check_no);
                }else{
                    Log.d("add",path+"---"+ mSelectList.size());
                    mSelectList.add(path);
                    viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.imageButton.setImageResource(R.mipmap.pic_check);
                }
//                notifyDataSetChanged();
            }
        });

        if(mSelectList.contains(path)){
            viewHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.imageButton.setImageResource(R.mipmap.pic_check);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageButton imageButton;
    }
}
