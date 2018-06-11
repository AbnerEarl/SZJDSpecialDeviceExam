package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frank.jinding.Bean.ImageBean;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Utils.SaveImage;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
  private Context mContext;
  private List<ImageBean> mImageList = new ArrayList<>();

  public ImageAdapter(List<ImageBean> imageList) {
    mImageList = imageList == null ? mImageList : imageList;
  }

  @Override
  public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    return new ImageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false));
  }

  @Override
  public void onBindViewHolder(final ImageHolder holder, int position) {
    ImageBean imageinfo = mImageList.get(position);

    holder.originArg.setText(imageinfo.getOriginArg());
    holder.thumbArg.setText(imageinfo.getThumbArg());
    Glide.with(mContext)
        .load(imageinfo.getImage())
        .into(holder.image);
    holder.image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //获取imageview中显示的图片
        holder.image.buildDrawingCache(true);
        holder.image.buildDrawingCache();
        Bitmap bitmap = holder.image.getDrawingCache();
        SaveImage.saveBitmapFile(bitmap, Environment.getExternalStorageDirectory() + "/Luban/image/","test.jpg");
        holder.image.setDrawingCacheEnabled(false);
      }
    });
  }

  @Override
  public int getItemCount() {
    return mImageList.size();
  }

  class ImageHolder extends RecyclerView.ViewHolder {
    private TextView originArg;
    private TextView thumbArg;
    private ImageView image;

    ImageHolder(View view) {
      super(view);

      originArg = (TextView) view.findViewById(R.id.origin_arg);
      thumbArg = (TextView) view.findViewById(R.id.thumb_arg);
      image = (ImageView) view.findViewById(R.id.image);

      /*image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //获取imageview中显示的图片
          image.buildDrawingCache(true);
          image.buildDrawingCache();
          Bitmap bitmap = image.getDrawingCache();
          SaveImage.saveBitmapFile(bitmap, Environment.getExternalStorageDirectory() + "/Luban/image/","test.jpg");
          image.setDrawingCacheEnabled(false);
        }
      });*/
    }


  }
}