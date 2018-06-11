package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.frank.jinding.R;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/2/3.
 */

public class OrderMapAdapter extends BaseAdapter {
    private List<Map<String,Object>> orderList;
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

    /*构造函数*/
    public OrderMapAdapter(Context context, List<Map<String,Object>> orderList) {
        this.mInflater = LayoutInflater.from(context);
        this.orderList = orderList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getCount() {

        return orderList.size();//返回数组的长度
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*书中详细解释该方法*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //观察convertView随ListView滚动情况
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dispatching, null);
            holder = new ViewHolder();
                /*得到各个控件的对象*/
            holder.place = (TextView) convertView.findViewById(R.id.sendPlace);
            holder.actualTime=(TextView)convertView.findViewById(R.id.actual_time);
            holder.select = (CheckBox) convertView.findViewById(R.id.dispatching_checkBox);
            holder.title = (TextView) convertView.findViewById(R.id.dispatching_unit);
            holder.projectName=(TextView)convertView.findViewById(R.id.dispatching_projectName);
            holder.expectChecker=(TextView)convertView.findViewById(R.id.expect_checker_item);
            holder.expectCheckerLayout=(LinearLayout)convertView.findViewById(R.id.expect_checker_layout);
            holder.expectCheckerLayout.setVisibility(View.GONE);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

        //  holder.title.setText(orderList.get(position).getOrderUser().getProjectName());
        if (orderList.get(position).get("orderOrg")!=null)
        holder.projectName.setText(orderList.get(position).get("orderOrg").toString());
        holder.place.setText(orderList.get(position).get("projectAddress").toString() );
        holder.actualTime.setText(orderList.get(position).get("actrualDate").toString());
        Log.i("检验时间",holder.actualTime.getText().toString());
        holder.title.setText(orderList.get(position).get("status").toString());

        holder.select.setVisibility(View.INVISIBLE);
        return convertView;
    }
    /*存放控件*/
    public final class ViewHolder {
        public CheckBox select;
        public TextView title;
        public TextView place;
        public TextView actualTime;
        public TextView projectName;
        public TextView expectChecker;
        public LinearLayout expectCheckerLayout;
    }
}
