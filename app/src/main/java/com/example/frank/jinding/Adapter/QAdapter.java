package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frank.jinding.Bean.OrderBean.CheckReference;
import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/2/3.
 */

public  class QAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<CheckReference> listItem = new ArrayList<>();
    public ViewHolder holder;
    public List<Boolean>isSelected=new ArrayList<>();
    public Context context;

    public QAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.checker_item, null);
            holder = new ViewHolder();
            holder.checkerNam= (TextView) convertView.findViewById(R.id.text_checker_name);
            holder.selectinstu = (CheckBox) convertView.findViewById(R.id.checkBox_checker_select);
            holder.checkBoxImageView=(ImageView)convertView.findViewById(R.id.check_box_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkerNam.setText(listItem.get(position).getReference_name());

        holder.checkBoxImageView.setImageDrawable(context.getResources().getDrawable( R.drawable.reference));
        holder.selectinstu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkPosition(position);
                } else {
                    isSelected.set(position, false);
                }
            }
        });
        holder.selectinstu.setChecked(isSelected.get(position));
        return convertView;
    }
    public void checkPosition(int position) {
        for (int i = 0; i < isSelected.size(); i++) {
            if (position == i) {// 设置已选位置
                isSelected.set(i, true);
            } else {
                isSelected.set(i, false);
            }
        }
        notifyDataSetChanged();
    }
    public  final class ViewHolder {
        public TextView checkerNam;
        public CheckBox selectinstu;
        public ImageView checkBoxImageView;
    }
}

