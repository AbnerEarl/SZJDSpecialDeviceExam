package com.example.frank.jinding.UI.SalesmanActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.example.frank.jinding.Bean.OrderBean.CheckOrder;
import com.example.frank.jinding.Picker.Bean.GetJsonDataUtil;
import com.example.frank.jinding.Picker.Bean.JsonBean;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.DepartorActivity.ChooseChecker;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderSearch;
import com.google.gson.Gson;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddOrderInformation extends AppCompatActivity {
    ArrayList<JsonBean> options1Items = new ArrayList<>();
    ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    @BindView(R.id.danwei_tv)
    TextView danweiTv;
    @BindView(R.id.choose_danwei)
    ImageButton chooseDanwei;
    @BindView(R.id.expect_check_person)
    TextView expectCheckPerson;
    @BindView(R.id.choose_checker)
    ImageButton chooseChecker;
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    @BindView(R.id.order_place)
    TextView orderPlace;
    @BindView(R.id.danwei)
    EditText danwei;
    @BindView(R.id.jingbanren)
    EditText jingbanren;
    @BindView(R.id.phone)
    EditText phone;

    @BindView(R.id.use_unit)
    EditText useUnit;
    @BindView(R.id.install_unit)
    EditText installUnit;
    @BindView(R.id.pay_way)
    EditText payWay;
    @BindView(R.id.report_get_way)
    EditText reportGetWay;
    @BindView(R.id.project_name)
    EditText projectName;
    @BindView(R.id.expect_checkdate)
    EditText expectCheckdate;
    @BindView(R.id.project_address)
    EditText projectAddress;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.titleplain)
    TextView titleplain;
    @BindView(R.id.titleback)
    ImageButton titleback;
    private CheckOrder mCheckOrder = new CheckOrder();
    private Date mDate;
    private static int requestCode;
    final String[] payStr = new String[]{"现金", "支票", "转账"};
    final String[] getReportStr = new String[]{"自取", "邮寄"};
    List<String> orgList;
    ArrayList<String>checkerIdList;
    public static int REQUEST_CODE = 0xad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_person_infomation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.getSerializableExtra("checkOrder") != null) {
            mCheckOrder = (CheckOrder) intent.getSerializableExtra("checkOrder");
            orderPlace.setText(mCheckOrder.getProvince() + mCheckOrder.getCity() + mCheckOrder.getArea());
            danwei.setText(mCheckOrder.getOrderOrg());
            jingbanren.setText(mCheckOrder.getApplicantName());
            if (mCheckOrder.getApplicantPhonenum() != null) {
                phone.setText(mCheckOrder.getApplicantPhonenum());
            }
                expectCheckPerson.setText(mCheckOrder.getCheckerLoginnames());
            useUnit.setText(mCheckOrder.getDeviceUseOrg());
            if (mCheckOrder.getDeviceInstallOrg() != null) {
                installUnit.setText(mCheckOrder.getDeviceInstallOrg());
            }
            if (mCheckOrder.getPaymentType() != null) {
                payWay.setText(payStr[Integer.parseInt(mCheckOrder.getPaymentType()) - 1]);
            }
            if (mCheckOrder.getGetReportType() != null) {
                reportGetWay.setText(getReportStr[Integer.parseInt(mCheckOrder.getGetReportType()) - 1]);
            }
            if (mCheckOrder.getCheckdateExpect()!=null)
            mDate = new Date(mCheckOrder.getCheckdateExpect().toString());
            projectName.setText(mCheckOrder.getProjectName());
            if (mDate!=null)
            expectCheckdate.setText(getTime(new Date(mCheckOrder.getCheckdateExpect().toString())));
            projectAddress.setText(mCheckOrder.getProjectAddress());

        }
        requestCode = intent.getIntExtra("requestCode", 0);
        if (requestCode == 0x01) {
            titleplain.setText("编辑订单信息");
        } else  {
            titleplain.setText("查看订单信息");
            orderPlace.setFocusable(false);
            orderPlace.setEnabled(false);
            danwei.setClickable(false);
            danwei.setFocusable(false);
            danwei.setEnabled(false);
            chooseDanwei.setVisibility(View.INVISIBLE);
            jingbanren.setClickable(false);
            jingbanren.setFocusable(false);
            jingbanren.setEnabled(false);
            phone.setClickable(false);
            phone.setFocusable(false);
            phone.setEnabled(false);
            useUnit.setClickable(false);
            useUnit.setFocusable(false);
            useUnit.setEnabled(false);
            installUnit.setClickable(false);
            installUnit.setFocusable(false);
            installUnit.setEnabled(false);
            chooseChecker.setVisibility(View.INVISIBLE);
            payWay.setClickable(false);
            payWay.setFocusable(false);
            payWay.setEnabled(false);
            reportGetWay.setClickable(false);
            reportGetWay.setFocusable(false);
            reportGetWay.setEnabled(false);
            projectName.setClickable(false);
            projectName.setFocusable(false);
            projectName.setEnabled(false);
            expectCheckdate.setClickable(false);
            expectCheckdate.setFocusable(false);
            expectCheckdate.setEnabled(false);
            projectAddress.setClickable(false);
            projectAddress.setFocusable(false);
            projectAddress.setEnabled(false);
            save.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.order_place, R.id.pay_way, R.id.report_get_way, R.id.expect_checkdate, R.id.save, R.id.titleback, R.id.choose_danwei,R.id.choose_checker})
    public void onViewClicked(View view) {
        if (requestCode != OrderSearch.LOOK_REQUEST_CODE) {
            switch (view.getId()) {
                case R.id.order_place:
                    chooseCity();
                    break;
                case R.id.pay_way:
                    choosePayWay();
                    break;
                case R.id.report_get_way:
                    chooseGetWay();
                    break;
                case R.id.expect_checkdate:
                    chooseDate();
                    break;
                case R.id.titleback:
                    onBackPressed();
                    break;
                case R.id.choose_danwei:
                    getUnit();
                    break;
                case R.id.save:
                    commit();
                    break;
                case R.id.choose_checker:
                {
                    Intent intent=new Intent(AddOrderInformation.this,ChooseChecker.class);
                    intent.putExtra("requestCode",REQUEST_CODE);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            }
        }
    }

    private void getUnit() {
        Map<String, Object> map = new HashMap<>();
        orgList = new ArrayList<>();
        ApiService.GetString(this, "orgList", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response != null && !TextUtils.isEmpty(response)) {
                    orgList = JSON.parseArray(response, String.class);
                    final AlertDialog dialog = new AlertDialog.Builder(AddOrderInformation.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    ListView orgLv = new ListView(AddOrderInformation.this);
                    ArrayAdapter adapter = new ArrayAdapter(AddOrderInformation.this, android.R.layout.simple_list_item_1, orgList);
                    orgLv.setAdapter(adapter);
                    orgLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            danwei.setText(orgList.get(position));
                            dialog.dismiss();
                        }
                    });
                    dialog.setView(orgLv);
                    dialog.show();
                } else {
                    Toast.makeText(AddOrderInformation.this, "委托单位为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(AddOrderInformation.this, e.getMessage() + "暂时不能获取委托单位数据", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });

    }

    protected void chooseDate() {

//控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2100, 11, 28);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                expectCheckdate.setText(getTime(date));
                mDate = date;
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime.show();
    }

    protected void choosePayWay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择支付方式");
        builder.setSingleChoiceItems(payStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payWay.setText(payStr[which]);
                int payType = which + 1;
                mCheckOrder.setPaymentType(payType + "");
                dialog.dismiss();
            }
        });
        builder.show();
    }

    protected void chooseCity() {
        initJsonData();
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                mCheckOrder.setProvince(options1Items.get(options1).getName() + "");
                mCheckOrder.setCity(options2Items.get(options1).get(options2));
                mCheckOrder.setArea(options3Items.get(options1).get(options2).get(options3));
                String tx = options1Items.get(options1).getName() + options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3);
                orderPlace.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器*/
        //pvOptions.setPicker(options1Items, options2Items);
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    protected void chooseGetWay() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择报告领取方式");
        builder.setSingleChoiceItems(getReportStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportGetWay.setText(getReportStr[which]);
                int getType = which + 1;
                mCheckOrder.setGetReportType(getType + "");
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            options3Items.add(Province_AreaList);
        }

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void commit() {
        String result = checkEmpty();
        if (result.equals("填写完成")) {
            //mCheckOrder.setOrderCode("");
            if (requestCode != OrderSearch.UPDATE_REQUEST_CODE)
                mCheckOrder.setOrderStatus("01");
            mCheckOrder.setOrderOrg(danwei.getText().toString());
            mCheckOrder.setApplicantName(jingbanren.getText().toString());
            mCheckOrder.setApplicantPhonenum(phone.getText().toString());
          /*  if (facsimile.getText() != null)
                mCheckOrder.setFax(facsimile.getText().toString());*/
            mCheckOrder.setDeviceUseOrg(useUnit.getText().toString());
            if (installUnit.getText() != null) {
                mCheckOrder.setDeviceInstallOrg(installUnit.getText().toString());
            }
            mCheckOrder.setProjectName(projectName.getText().toString());
           if(expectCheckPerson.getText()!=null){
               mCheckOrder.setCheckerLoginnames(expectCheckPerson.getText().toString());
           }
            mCheckOrder.setCheckdateExpect(mDate);
            mCheckOrder.setProjectAddress(projectAddress.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("checkOrder", mCheckOrder);
            intent.putExtra("isSaved", true);
            setResult(0x01, intent);
            finish();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private String checkEmpty() {
        if (orderPlace.getText() == null || TextUtils.isEmpty(orderPlace.getText())) {
            return "未选择省市区";
        } else if (danwei.getText() == null || TextUtils.isEmpty(danwei.getText())) {
            return "未填写委托单位";
        } else if (jingbanren.getText() == null || TextUtils.isEmpty(danwei.getText())) {
            return "未填写经办人";
        } else if (phone.getText() == null || TextUtils.isEmpty(phone.getText())) {
            return "未填写经办人电话";
        } else if (useUnit.getText() == null || TextUtils.isEmpty(useUnit.getText())) {
            return "未填写使用单位";
        } /*else if (payWay.getText() == null || TextUtils.isEmpty(payWay.getText())) {
            return "未选择支付方式";
        }*/ else if (reportGetWay.getText() == null) {
            return "未选择获取报告方式";
        } else if (projectName.getText() == null || TextUtils.isEmpty(projectName.getText())) {
            return "未填写工程名称";
        } /*else if (expectCheckdate.getText() == null || TextUtils.isEmpty(expectCheckdate.getText())) {
            return "未选择期待检验日期";
        } */else if (projectAddress.getText() == null || TextUtils.isEmpty(projectAddress.getText())) {
            return "未填写工程地址";
        }
        return "填写完成";
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isSaved", false);
        setResult(requestCode, intent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE&&data.getBooleanExtra("isCommited",false)){
            expectCheckPerson.setText(null);
            expectCheckPerson.setText(data.getStringExtra("checkerName"));
        }

    }
}
