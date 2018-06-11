package com.example.frank.jinding.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.frank.jinding.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/5/25.
 */

public class FinanceAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private ViewHolder_Order holder;
    private List<Map<String,Object>> listItem;
    private HashMap<Integer,Boolean>isSelected;
    private View.OnClickListener clickListener;
    private int mode;

    public FinanceAdapter(Context context,List<Map<String,Object>> listItem,int mode) {
        this.mInflater = LayoutInflater.from(context);
        this.listItem = listItem;
        isSelected=new HashMap<>();
        this.mode=mode;
    }
    public void setClickListener(View.OnClickListener clickListener){
        this.clickListener=clickListener;
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
            convertView = mInflater.inflate(R.layout.item_finance, null);
            holder = new ViewHolder_Order();
            holder.deviceType=convertView.findViewById(R.id.device_type);
            holder.orderOrg=convertView.findViewById(R.id.order_org);
            holder.actualTime=convertView.findViewById(R.id.apply_time);
            holder.status=convertView.findViewById(R.id.status);
            holder.person=convertView.findViewById(R.id.finance_man);
            holder.checkCharge=convertView.findViewById(R.id.check_charge);
            holder.payCharge=convertView.findViewById(R.id.already_pay);
            holder.select=convertView.findViewById(R.id.finance_checkBox);
            holder.reportNumber=convertView.findViewById(R.id.report_number);
            holder.checkBtn=convertView.findViewById(R.id.finance_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Order) convertView.getTag();
        }
        if (mode==0)
        holder.deviceType.setText(listItem.get(position).get("devigetDeviceFinancialStatusceTypeName").toString());
       else{
           holder.deviceType.setText(listItem.get(position).get("deviceTypeName").toString());
        }
        if (listItem.get(position).get("deviceDetailCode")!=null)
        holder.reportNumber.setText(listItem.get(position).get("deviceDetailCode").toString());
        if (listItem.get(position).get("orderOrg")!=null)
        holder.orderOrg.setText(listItem.get(position).get("orderOrg").toString());
        //holder.checkCharge.setText(listItem.get(position).get("deviceCharge").toString());
        holder.payCharge.setText(listItem.get(position).get("paidExpenses").toString());
        if (mode==0||mode==3){
            //待申请异动,待审核异动

            if (mode==0){
                holder.checkBtn.setVisibility(View.INVISIBLE);
                holder.person.setText(listItem.get(position).get("salesmanName").toString());
            }
            else{
                holder.person.setText(listItem.get(position).get("applicantId").toString());
                holder.select.setVisibility(View.INVISIBLE);
            }


//            Log.i("finance",listItem.get(position).get("salesmanName").toString());
        }else if (mode==1||mode==4){
            //申请历史,审核历史
            holder.select.setVisibility(View.INVISIBLE);
            holder.actualTime.setText(listItem.get(position).get("applicantTime").toString());
            holder.checkBtn.setText("详情");
            String statusStr=listItem.get(position).get("applicationStatus").toString();
            if (listItem.get(position).get("approvalId")!=null)
                holder.person.setText(listItem.get(position).get("approvalId").toString());
            holder.status.setText(statusStr);
        }
        holder.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setTag(position);
                clickListener.onClick(v);
            }
        });
        holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("slectsdka", "onCheckedChanged: ");
                isSelected.put(position,isChecked);
            }
        });
        return convertView;
    }
    public HashMap<Integer,Boolean> getIsSelected(){
        return isSelected;
    }
    public void setIsSelected(HashMap<Integer,Boolean> map){
        isSelected=map;
    }
    final class ViewHolder_Order {
        public CheckBox select;
        public TextView orderOrg;
        public TextView deviceType;
        public TextView actualTime;
        public TextView status;
        public TextView person;
        public TextView reportNumber;
        public TextView checkCharge,payCharge;
        public Button checkBtn;
    }

}




