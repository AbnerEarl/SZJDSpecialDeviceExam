package com.example.frank.jinding.UI.PersonAboutActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄庆 on 2018/1/20.
 */

public class EvaluateActivity extends AppCompatActivity{

    private ImageView image_back;
    private ListView listView_evaluate;
    private EvaluateAdapter evaluateAdapter;
    private List<CheckOrder> evaluateList=new ArrayList<>();
    private Gson gsonContainTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        evaluateAdapter=new EvaluateAdapter(this);
    }

    private void initView() {
        image_back=(ImageView)this.findViewById(R.id.image_back);
        listView_evaluate=(ListView)this.findViewById(R.id.listview_evaluate);
        Map<String, Object> parameters = new HashMap<>();
        ApiService.GetString(this, "getUserEvaluate", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                //Toast.makeText(EvaluateActivity.this,response,Toast.LENGTH_LONG).show();
                evaluateList=gsonContainTime.fromJson(response,new TypeToken<List<CheckOrder>>(){}.getType());
                evaluateAdapter.listItem=evaluateList;
                listView_evaluate.setAdapter(evaluateAdapter);
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });

    }

    private void initListener() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class EvaluateAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ViewHolder_Evaluate holder;
        private List<CheckOrder> listItem = new ArrayList<>();
        public EvaluateAdapter(Context context){
            this.mInflater=LayoutInflater.from(context);
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
                convertView = mInflater.inflate(R.layout.list_item_evaluate, null);
                holder = new ViewHolder_Evaluate();
                holder.textView_evaluate = (TextView) convertView.findViewById(R.id.textview_evaluate);
                holder.textView_name = (TextView) convertView.findViewById(R.id.textview_projectname);
                holder.textView_time = (TextView) convertView.findViewById(R.id.textview_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Evaluate) convertView.getTag();
            }
            holder.textView_name.setText(listItem.get(position).getProjectName());
            holder.textView_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(listItem.get(position).getCheckdateExpect()));
            holder.textView_evaluate.setText(listItem.get(position).getUserEvaluate());
            return convertView;
        }
    }

    public final class ViewHolder_Evaluate{
        public TextView textView_evaluate;
        public TextView textView_name;
        public TextView textView_time;
    }
}
