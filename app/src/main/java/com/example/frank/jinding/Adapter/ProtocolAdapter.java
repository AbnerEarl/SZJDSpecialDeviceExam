package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.frank.jinding.Bean.OrderBean.ConsignmentDetail;
import com.example.frank.jinding.Interface.CallBack;
import com.example.frank.jinding.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DELL on 2018/2/3.
 */

public class ProtocolAdapter extends BaseAdapter implements View.OnClickListener{
    public List<ConsignmentDetail> list;

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private CallBack callBack;
    public Boolean update = false;
    public Boolean statusVisible = false;
    public Boolean checkVisible=false;


    /*构造函数*/
    public ProtocolAdapter(Context context, List<ConsignmentDetail> consignmentList, CallBack callBack, Boolean update, Boolean statusVisible, Boolean checkVisible) {
        this.mInflater = LayoutInflater.from(context);
        this.list = consignmentList;
        this.callBack = callBack;
        this.update = update;
        this.statusVisible = statusVisible;
        this.checkVisible=checkVisible;
    }

    @Override
    public int getCount() {

        return list.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = mInflater.inflate(R.layout.td_protiocol_check_item, null);
            holder = new ViewHolder();
                /*得到各个控件的对象*/
            //xe
            convertView.setDrawingCacheEnabled(false);
            holder.number = (TextView) convertView.findViewById(R.id.textView10);
            holder.stylee = (TextView) convertView.findViewById(R.id.textView7);
            holder.protocolStatus = (TextView) convertView.findViewById(R.id.protocol_status);
            holder.protocolCheck = (TextView) convertView.findViewById(R.id.protocol_check);
            holder.delete_Ibn = (ImageButton) convertView.findViewById(R.id.delete_divice);
            holder.checkReferenceTv = (TextView) convertView.findViewById(R.id.check_reference);
            holder.checkPrice = (TextView) convertView.findViewById(R.id.check_price);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
          /*  for (int i=0;i<deviceTypeList.size();i++){
                    holder.stylee.setTag(list.get(position).getDeviceTypeId());
            }*/
        holder.stylee.setText(list.get(position).getDeviceTypeId());
        holder.number.setText(list.get(position).getDeviceNum() + "");
        holder.checkReferenceTv.setText(list.get(position).getMainCheckReference());
        holder.checkPrice.setText(list.get(position).getCheckCharge() + "元");
        // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/
        holder.delete_Ibn.setOnClickListener(this);
        holder.delete_Ibn.setTag(position);
        holder.protocolCheck.setTag(position);
        holder.protocolCheck.setOnClickListener(this);
        if (!statusVisible) {
            holder.protocolStatus.setVisibility(View.INVISIBLE);
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (list.get(position).getIsPassCheck()!=null&&list.get(position).getIsPassCheck().equals("2"))
            holder.protocolStatus.setText("待审核");
        else if (list.get(position).getIsPassCheck()!=null&&list.get(position).getIsPassCheck().equals("1")){
            if (list.get(position).getCheckPersonId()!=null)
                holder.protocolStatus.setText("已审核("+list.get(position).getCheckPersonId()+")\n"+sdf.format(list.get(position).getCheckTime()));
            else{
                holder.protocolStatus.setText("已审核");
            }
        }

        else if (list.get(position).getIsPassCheck()!=null&& list.get(position).getIsPassCheck().equals("0")){
            if (list.get(position).getCheckPersonId()!=null)
            holder.protocolStatus.setText("已拒绝("+list.get(position).getCheckPersonId()+")\n"+sdf.format(list.get(position).getCheckTime()));
        else{
                holder.protocolStatus.setText("已拒绝");
            }
        }else {
            holder.protocolStatus.setText(list.get(position).getConsignmentStatus());
        }

        if (!update) {
            holder.delete_Ibn.setVisibility(View.INVISIBLE);
        }
        if (!checkVisible){
            holder.protocolCheck.setVisibility(View.INVISIBLE);
        }
        //     holder.delete_Ibn.setVisibility(holder.delete_Ibn.INVISIBLE);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        callBack.callBack(v);
    }

     final class ViewHolder {
        public TextView stylee, number;
        public ImageButton delete_Ibn;
        public TextView checkReferenceTv;
        public TextView checkPrice;
        public TextView protocolStatus;
        public TextView protocolCheck;
    }
}

