package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/2/3.
 */

public class OrderAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ViewHolder_Order holder;
    public List<CheckOrder> listItem;

    public OrderAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        listItem = new ArrayList<>();
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
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dispatching, null);
            convertView.setDrawingCacheEnabled(false);
            holder = new ViewHolder_Order();
            holder.place = (TextView) convertView.findViewById(R.id.sendPlace);
            holder.actualTime = (TextView) convertView.findViewById(R.id.actual_time);
            holder.select = (CheckBox) convertView.findViewById(R.id.dispatching_checkBox);
            holder.title = (TextView) convertView.findViewById(R.id.dispatching_unit);
            holder.projectName = (TextView) convertView.findViewById(R.id.dispatching_projectName);
            holder.expectCheckerLayout = (LinearLayout) convertView.findViewById(R.id.expect_checker_layout);
            holder.taskIcon=(ImageView)convertView.findViewById(R.id.task_icon);
            holder.expectCheckerLayout.setVisibility(View.GONE);
            holder.select.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Order) convertView.getTag();
        }


        holder.projectName.setText(listItem.get(position).getProjectName());
        if(listItem.get(position).getProjectAddress()!=null&&listItem.get(position).getProjectAddress()!="")
        holder.place.setText(listItem.get(position).getProvince() + listItem.get(position).getCity() + listItem.get(position).getArea() + listItem.get(position).getProjectAddress());
        else
            holder.place.setText(listItem.get(position).getProvince() + listItem.get(position).getCity() + listItem.get(position).getArea());
        holder.actualTime.setText(listItem.get(position).getOrderStatus());
        if(listItem.get(position).getOrderStatus().indexOf("复检")!=-1)
            holder.taskIcon.setImageResource(R.drawable.third_order);
        else holder.taskIcon.setImageResource(R.drawable.first_order);
        holder.title.setText(listItem.get(position).getOrderOrg());
        return convertView;
    }

    final class ViewHolder_Order {
        public CheckBox select;
        public TextView title;
        public TextView place;
        public TextView actualTime;
        public TextView projectName;
        public LinearLayout expectCheckerLayout;
        public ImageView taskIcon;
    }

}



