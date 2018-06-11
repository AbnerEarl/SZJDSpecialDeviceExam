package com.example.frank.jinding.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.frank.jinding.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/11/28.
 */

public class DialogItemsAdapter extends BaseAdapter {




    //方法一
    /*private Activity activity;
    public DialogItemsAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = View.inflate(activity, R.layout.checker_name,null);
        TextView tvCarId = (TextView) view.findViewById(R.id.textView28);
        tvCarId.setText("豫A88888"+i);


        return view;
    }
*/











//方法二
    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater listContainer;

    public DialogItemsAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    public final class ListItemView {
        public TextView value;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < listItems.size()) {
            return listItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = new ListItemView();
        if (convertView == null) {
            convertView = listContainer.inflate(R.layout.checker_name,    //R.layout.add_checkers
                    null);
            listItemView.value = (TextView) convertView
                    .findViewById(R.id.textView28);

            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.value.setText((String) listItems.get(position).get("value"));
        return convertView;
    }





}
