package com.example.frank.jinding.UI.CheckerActivity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frank.jinding.Bean.Instrument.InstrumentStatus;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NetSelect extends AppCompatActivity {

    private ImageView image_back;
    private TextView textview_submit;


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<View> viewContainter = new ArrayList<>();
    private ArrayList<String> titleContainer = new ArrayList<>();
    private MyPagerAdapter adapter;

    private ArrayList<Map<String, String>> isSelectedList = new ArrayList<>();
    private List<String> isSelectedIdList = new ArrayList<>();
    private int index = 0;
    private Gson gsonContainTime = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Gson gson = new Gson();


    private List<QAdapter> list_adapter = new ArrayList<>();
    private List<ListView> list_listview = new ArrayList<>();
    private List<CheckBox> list_checkbox = new ArrayList<>();
    private List<List<InstrumentStatus>> list_listinstrument = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_select);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        if (getIntent().getStringExtra("selectList") != null) {
            ArrayList<Map<String, String>> list = gson.fromJson(getIntent().getStringExtra("selectList"), new TypeToken<ArrayList<Map<String, String>>>() {
            }.getType());
            for (Map<String, String> map : list) {
                isSelectedList.add(map);
            }
        }
        for (Map map : isSelectedList) {
            isSelectedIdList.add(map.get("instrumentId").toString());
        }
        viewContainter.clear();
        titleContainer.clear();

        index = isSelectedList.size();
    }

    private void initView() {



        image_back = (ImageView) this.findViewById(R.id.image_back);
        textview_submit = (TextView) this.findViewById(R.id.textview_submit);

        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) this.findViewById(R.id.tablayout);
        new Thread(new Runnable() {
            @Override
            public void run() {
        Map<String, Object> parameters = new HashMap<>();
        ApiService.GetString(NetSelect.this, "getInstrumentBox", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                List<String> boxCodeList = gsonContainTime.fromJson(response, new TypeToken<List<String>>() {
                }.getType());
                Collections.sort(boxCodeList);
                if(boxCodeList.size()>4){
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
                for (String title : boxCodeList) {
                    titleContainer.add(title);
                }
                for (int i = 0; i < boxCodeList.size(); i++) {
                    View view = LayoutInflater.from(NetSelect.this).inflate(R.layout.fragment_instrument_1, null);
                    viewContainter.add(view);
                }
                adapter = new MyPagerAdapter();
                adapter.titleContainer = titleContainer;
                adapter.viewContainter = viewContainter;
                viewPager.setAdapter(adapter);
                //viewPager.setOffscreenPageLimit(10);
                tabLayout.setupWithViewPager(viewPager);
                initFragmentData();
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
            }
        }).start();
    }

    private void initListener() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectList", gson.toJson(isSelectedList));
                setResult(2, intent);
                finish();
            }
        });
        textview_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NetSelect.this, isSelectedList.toString(), Toast.LENGTH_LONG).show();
                if (isSelectedList.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("selectList", gson.toJson(isSelectedList));
                    setResult(2, intent);
                    finish();
                } else {
                    Toast.makeText(NetSelect.this, "您还没有选取您要申请的仪器", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initFragmentData() {
        new Thread(new Runnable() {
            @Override
            public void run() {


        Map<String, Object> parameters = new HashMap<>();
        ApiService.GetString(NetSelect.this, "getNotBrokenInstrumentList", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Map<String, List<InstrumentStatus>> map = gsonContainTime.fromJson(response, new TypeToken<Map<String, List<InstrumentStatus>>>() {
                }.getType());



                for (int i = 0; i < titleContainer.size(); i++) {
                    list_listinstrument.add(map.get(titleContainer.get(i)));
                }

                for (int i = 0; i < list_listinstrument.size(); i++) {
                    List<InstrumentStatus> list = list_listinstrument.get(i);
                    final ListView listView = viewContainter.get(i).findViewById(R.id.listview_q1);
                    final CheckBox checkBox = viewContainter.get(i).findViewById(R.id.checkbox_all);
                    final QAdapter qAdapter = new QAdapter(NetSelect.this);
                    list_listview.add(listView);
                    list_checkbox.add(checkBox);
                    list_adapter.add(qAdapter);
                    for (InstrumentStatus instrumentStatus : list) {
                        list_adapter.get(i).listItem.add(instrumentStatus);
                    }
                    //全选按钮

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    CheckBox checkBox1;
                            if(isChecked){

                                for(int j=0;j<qAdapter.listItem.size();j++){
                                     checkBox1=listView.getChildAt(j).findViewById(R.id.checkBox_instru_select);
                                    if(!checkBox1.isChecked()){
                                        checkBox1.setChecked(true);

                                    }
                                }


                            }else{
                                for(int j=0;j<qAdapter.listItem.size();j++){
                                   checkBox1=listView.getChildAt(j).findViewById(R.id.checkBox_instru_select);
                                    if(checkBox1.isChecked()){
                                        checkBox1.setChecked(false);
                                    }
                                }

                            }
                            //Toast.makeText(NetSelect.this,"数量"+isSelectedList.size(),Toast.LENGTH_SHORT).show();
                                }
                            }).start();
                        }
                    });



                    list_listview.get(i).setAdapter(list_adapter.get(i));
                    list_listview.get(i).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CheckBox checkBox=view.findViewById(R.id.checkBox_instru_select);
                            if (checkBox.isChecked()) {
                                checkBox.setChecked(false);
                            } else {
                                checkBox.setChecked(true);
                            }
                        }
                    });




                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
            }
        }).start();

    }

    public class QAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private ArrayList<InstrumentStatus> listItem = new ArrayList<>();
        public ViewHolder holder = new ViewHolder();

        public QAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
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
            View view;
            ViewHolder holder;
            if(null==convertView){
                view=View.inflate(NetSelect.this,R.layout.instrument_infomation_net,null);
                holder=new ViewHolder();
                holder.instruNum = (TextView) view.findViewById(R.id.text_number_instru);
                holder.instruNam = (TextView) view.findViewById(R.id.text_name_instru);
                holder.selectinstu = (CheckBox) view.findViewById(R.id.checkBox_instru_select);

                view.setTag(holder);
            }
            else {
                view =convertView;
                holder = (ViewHolder) view.getTag();
            }

                holder.instruNam.setText(listItem.get(position).getInstrumentType().toString());
                holder.instruNum.setText(listItem.get(position).getInstrumentCode().toString());
                Log.i("adad:",listItem.get(position).getIsUsing());
                holder.selectinstu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        boolean flag = false;
                        Map<String, String> map = new HashMap<>();
                        map.put("Index", ++index + "");
                        map.put("instrumentId", listItem.get(position).getInstrumentId());
                        map.put("instrumentCode", listItem.get(position).getInstrumentCode().toString());
                        map.put("instrumentType", listItem.get(position).getInstrumentType().toString());
                        map.put("validateDate", listItem.get(position).getValidateDate().toString());
                        map.put("instrumentBoxCode", listItem.get(position).getInstrumentBoxCode().toString());
                        map.put("isUsing", listItem.get(position).getIsUsing().toString());
                        map.put("isSubmitted", listItem.get(position).getIsSubmitted().toString());
                        map.put("isBroken", listItem.get(position).getIsBroken().toString());
                        for (Map<String, String> m : isSelectedList) {
                            if (m.get("instrumentId").equals(map.get("instrumentId"))) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            isSelectedList.add(map);
                            isSelectedIdList.add(listItem.get(position).getInstrumentId());
                        }
                    } else {
                        for (int i = 0; i < isSelectedList.size(); i++) {
                            Map<String, String> m = isSelectedList.get(i);
                            if (m.get("instrumentId").equals(listItem.get(position).getInstrumentId().toString())) {
                                isSelectedList.remove(m);
                                isSelectedIdList.remove(m.get("instrumentId"));
                            }
                        }
                    }
                }
            });

            if (gsonContainTime.toJson(isSelectedIdList).contains(listItem.get(position).getInstrumentId())) {
                holder.selectinstu.setChecked(true);
            }else{
                holder.selectinstu.setChecked(false);
            }

            return view;
        }
    }

    public final class ViewHolder {
        public TextView instruNum, instruNam;
        public CheckBox selectinstu;

    }

    public class MyPagerAdapter extends PagerAdapter {

        private ArrayList<View> viewContainter = new ArrayList<View>();
        private ArrayList<String> titleContainer = new ArrayList<String>();

        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewContainter.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (viewContainter.get(position) != null) {
                container.removeView(viewContainter.get(position));
            }
            container.addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleContainer.get(position);
        }
    }
}
