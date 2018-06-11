package com.example.frank.jinding.UI.DepartorActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.frank.jinding.ExtraPermission.Permission;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PersonAboutActivity.MessageInform;
import com.example.frank.jinding.UI.PublicMethodActivity.OrderDetails;
import com.example.frank.jinding.UI.PersonAboutActivity.PersonInformation;
import com.example.frank.jinding.UI.PublicMethodActivity.ReportActivity;
import com.example.frank.jinding.Utils.SnackbarUtils;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentHeadersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private long mExitTime = 0;
    private ListView lv_tasksss;
    private Button mutilb;
    private SwipeRefreshLayout refreshLayout;
    private static String TAG="DepartmentHeaderActivity";
    private MyAdapter mWaitAdapter,mAlreadyAdapter;
    private HashMap<Integer, Boolean> isChecked;
    private Spinner mSpinner;
    private List<String> mSpinnerList;
    private static   boolean permissioned=true;
    private PermissMyAdapter permissMyAdapter;
    private List<Map<String,Object>> orderList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_headers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TAG = DepartmentHeadersActivity.this.getClass().getSimpleName();
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "您有新的消息，请注意查看！", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lv_tasksss = (ListView) this.findViewById(R.id.lv_dispatching);
        mutilb = (Button) this.findViewById(R.id.button47);
        refreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.refresh_waitSubmissionOrder);


        mutilb.setText("派工");

        getData();
        permissioned=true;
        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,

                                    long arg3) {
                String msg="true";
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder.select.isChecked() == true) {
                    Log.i("radiobutton3","checkBox"+position+"被选择");
                    isChecked.put(position, true);
                } else {
                    isChecked.put(position, false);
                }
                Log.i(TAG, "item" + position);
                 if (permissioned){
                     msg="true";
                 }else
                     msg="false";
                Log.i(TAG, msg+"item" + position);
               Intent intent= new Intent(DepartmentHeadersActivity.this, OrderDetails.class);
                intent.putExtra("orderId",orderList.get(position).get("orderId").toString());
                intent.putExtra("update",false);
                startActivity(intent);

            }
        });
        lv_tasksss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
if (firstVisibleItem==0)
    refreshLayout.setEnabled(true);
else
refreshLayout.setEnabled(false);
            }
        });

        mutilb.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getData();
            }
        });

    }
    private void initpermission(){
        permissMyAdapter=new PermissMyAdapter(DepartmentHeadersActivity.this);
        getPermissions();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.permissions_list, (ViewGroup) findViewById(R.id.permissions_list));
        final ListView lv_permissions = (ListView) layout.findViewById(R.id.permissions_listview);
        //final Spinner resultsp = (Spinner) layout.findViewById(R.id.spinner_check_opinion);
        lv_permissions.setAdapter(permissMyAdapter);
        lv_permissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                HashMap<String,Object> map=permissMyAdapter.listItem.get(arg2);
                Permission.excutePermissions(map.get("ItemText").toString(),DepartmentHeadersActivity.this);

            }
        });


        new AlertDialog.Builder(DepartmentHeadersActivity.this)
                .setTitle("额外的权限：")
                .setView(layout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                            }
                        }).show();

    }

    private void getPermissions(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data","pp");
                    ApiService.GetString(DepartmentHeadersActivity.this, "extraPermissions", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().split(":")[0].equals("true")) {
                                permissMyAdapter.listItem.clear();
                                permissMyAdapter.listItem=Permission.getPermissions(response.trim().split(":")[1]);
                                permissMyAdapter.notifyDataSetChanged();
                            }else if (response.trim().equals("没有额外权限！")){
                                Toast.makeText(DepartmentHeadersActivity.this,"没有额外权限",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(DepartmentHeadersActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(DepartmentHeadersActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private void getData(){
        orderList.clear();
     Map<String,Object> map=new HashMap<>();
        ApiService.GetString(this, "getOrderDispatching", map, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                refreshLayout.setRefreshing(false);
                Log.i("orderList","获取待派工订单成功"+response);
             JSONArray jsonArray= JSONArray.parseArray(response);
                for (Object object:jsonArray){
                    JSONObject jsonObject=(JSONObject) object;
                    HashMap<String,Object> item=new HashMap<>();
                        item.put("orderOrg",jsonObject.getString("orderOrg"));
                        item.put("checkdateExpect",jsonObject.getString("checkdateExpect"));
                        item.put("orderId",jsonObject.getString("orderId"));
                        item.put("projectName",jsonObject.getString("projectName"));
                        item.put("projectAddress",jsonObject.getString("projectAddress"));
                         item.put("checkerExpect",jsonObject.getString("checkerExpect"));
                    orderList.add(item);
                }
                if (orderList!=null&&orderList.size()!=0) {
                    isChecked = new HashMap<>();
                    for (int i = 0; i < orderList.size(); i++) {
                        isChecked.put(i, false);
                    }
                   mWaitAdapter=new MyAdapter(DepartmentHeadersActivity.this,orderList);
                    lv_tasksss.setAdapter(mWaitAdapter);
                }else {
                    new AlertDialog.Builder(DepartmentHeadersActivity.this).setTitle("暂无需要派工的订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }

            }

            @Override
            public void onError(Object tag, Throwable e) {
             Log.i(TAG,""+e.getMessage());
             refreshLayout.setRefreshing(false);
             Toast.makeText(DepartmentHeadersActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0x05) {
            if (data.getBooleanExtra("isCommit", false)) {
                List<String> integers = data.getStringArrayListExtra("dispatchingNumber");
                for (int i = 0; i < integers.size(); i++) {
                 for (int j=0;j<orderList.size();j++)
                    if (orderList.get(j).get("orderId").equals(integers.get(i)))
                        orderList.remove(j);

                }
                isChecked = new HashMap<>();
                for (int i = 0; i < orderList.size(); i++) {
                    mWaitAdapter.isSelected.put(i, false);
                }
                if (mutilb.getText().toString().equals("派工"))
                    mWaitAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (System.currentTimeMillis() - mExitTime > 2000) {
            mExitTime = System.currentTimeMillis();
            SnackbarUtils.show(this, R.string.click2exit);
        } else {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.department_headers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_testtask5) {
            Intent intent = new Intent(DepartmentHeadersActivity.this, DispatchRecord.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
        }  else if (id == R.id.nav_salerequest5) {
            Intent intent = new Intent(DepartmentHeadersActivity.this, ReportActivity.class);
            intent.putExtra("filecode","SzjdBzQz15");
            startActivity(intent);
        } else if (id == R.id.nav_otherquest5) {
            Intent intent=new Intent(this,DepartmentHeaderFinance.class);
            startActivity(intent);

        } else if (id == R.id.nav_messageinfo5) {
            Intent intent = new Intent(DepartmentHeadersActivity.this, MessageInform.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
        } else if (id == R.id.nav_personinfo5) {
            Intent intent = new Intent(DepartmentHeadersActivity.this, PersonInformation.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
        } else if (id == R.id.nav_exit5) {
            finish();
        }else if (id==R.id.extra_permission_department){
            initpermission();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //主页显示内容

    /*添加一个得到数据的方法，方便使用*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button47: {
                if (orderList.size()==0||orderList==null){
                    Toast.makeText(DepartmentHeadersActivity.this,"暂时没有可派工订单",Toast.LENGTH_SHORT).show();
                }else {
                    List<String> orderIds = new ArrayList<>();
                    for (int i = 0; i < mWaitAdapter.isSelected.size(); i++) {
                        if (mWaitAdapter.getIsSelected().get(i)) {
                            isChecked.put(i, true);
                            orderIds.add(orderList.get(i).get("orderId").toString());
                        } else {
                            isChecked.put(i, false);
                        }
                    }
                    if (orderIds.size() == 0) {
                        Toast.makeText(this, "请先选择需要派工的订单", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(DepartmentHeadersActivity.this, Dispatching.class);
                        intent.putStringArrayListExtra("dispatchingNumber", (ArrayList<String>) orderIds);
                        startActivityForResult(intent, 0x05);
                    }
                }
            }
                break;
            case R.id.fab:

                break;
            default:
                break;
        }
    }


    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {
        private List<Map<String,Object>> orderList;
        private HashMap<Integer, Boolean> isSelected;
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /*构造函数*/
        public MyAdapter(Context context, List<Map<String,Object>> orderList) {
            this.mInflater = LayoutInflater.from(context);
            this.orderList = orderList;
            this.isSelected = new HashMap<>();
            setIsSelected(isChecked);
        }
        private void init(){
            for (int i = 0; i < orderList.size(); i++) {
                getIsSelected().put(i, false);
            }
        }
        public void clear() {
            orderList.clear();
            notifyDataSetChanged();
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
            final DepartmentHeadersActivity.ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.dispatching, null);
                holder = new DepartmentHeadersActivity.ViewHolder();
                /*得到各个控件的对象*/
                holder.place = (TextView) convertView.findViewById(R.id.sendPlace);
                holder.actualTime=(TextView)convertView.findViewById(R.id.actual_time);
                holder.select = (CheckBox) convertView.findViewById(R.id.dispatching_checkBox);
                holder.title = (TextView) convertView.findViewById(R.id.dispatching_unit);
               holder.expectChecker=(TextView)convertView.findViewById(R.id.expect_checker_item);
               holder.projectName=(TextView)convertView.findViewById(R.id.dispatching_projectName);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (DepartmentHeadersActivity.ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

          //  holder.title.setText(orderList.get(position).getOrderUser().getProjectName());
            holder.projectName.setText(orderList.get(position).get("projectName").toString());
            holder.place.setText(orderList.get(position).get("projectAddress").toString() );
            holder.select.setChecked(getIsSelected().get(position));
            holder.actualTime.setText(orderList.get(position).get("checkdateExpect").toString());
            holder.title.setText(orderList.get(position).get("orderOrg").toString());
            holder.expectChecker.setText(orderList.get(position).get("checkerExpect").toString());
            holder.select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.i(TAG,"onchecked");
                    holder.select.setChecked(isChecked);
                   isSelected.put(position, isChecked);
                }
            });
            if (!permissioned){
                Log.i("dispatching","setINVISIBLE");
                holder.select.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        private HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        private void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            this.isSelected = isSelected;
        }

    }

    /*存放控件*/
    public final class ViewHolder {
        public CheckBox select;
        public TextView title;
        public TextView place;
        public TextView actualTime;
        public TextView projectName;
        public TextView expectChecker;
    }


    private class PermissMyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        /*构造函数*/
        public PermissMyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public int getCount() {

            return listItem.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        /*书中详细解释该方法*/
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PermissViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.extra_permissions,null);
                holder = new PermissViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.textView_permissions);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (PermissViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.title.setText(listItem.get(position).get("ItemText").toString());

            return convertView;
        }

    }
    /*存放控件*/
    public final class PermissViewHolder{
        public TextView title;

    }



}
