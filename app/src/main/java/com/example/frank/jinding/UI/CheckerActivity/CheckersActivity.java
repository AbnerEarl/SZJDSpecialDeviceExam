package com.example.frank.jinding.UI.CheckerActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.frank.jinding.Utils.SnackbarUtils;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> list = new ArrayList<String>();
    private long mExitTime = 0;
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;
    private ListView lv_tasksss;
    private SwipeRefreshLayout refreshLayout;

    private FloatingActionButton fab;
    private String messageString;
    private Snackbar snackbar;
    private MyAdapter waitAdapter;
    private MyAdapterO confirmAdapter, establishAdapter, refuseAdapter;
    private int currentList = 0;
    ArrayList<JSONObject> submissionOrderList;
    private PermissMyAdapter permissMyAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkers);

        init();
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
        list.add("待确认派工");
        list.add("已确认派工");
        list.add("已成立派工");
        list.add("已拒绝派工");

        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mySpinner.setAdapter(adapter);
        //mySpinner.setSelection(0,false);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中

        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //  TODO  Auto-generated  method  stub
                /*  将所选mySpinner  的值带入myTextView  中*/

                if ("待确认派工".equals(mySpinner.getSelectedItem().toString().trim())) {
                    //waitAdapter = new MyAdapter(CheckersActivity.this);//得到一个MyAdapter对象
                  Log.i("待确认派工",""+currentList);
                      currentList = 1;
                      getData(currentList);
                } else if ("已确认派工".equals(mySpinner.getSelectedItem().toString().trim())) {

                    currentList = 2;
                    getData(currentList);
                   //为ListView绑定Adapter
                } else if ("已成立派工".equals(mySpinner.getSelectedItem().toString().trim())) {
                    currentList = 3;
                    getData(currentList);//为ListView绑定Adapter
                } else if ("已拒绝派工".equals(mySpinner.getSelectedItem().toString().trim())) {
                    currentList = 4;
                    getData(currentList);
                }


            }

            public void onNothingSelected(AdapterView<?> arg0) {
                //  TODO  Auto-generated  method  stub
                //myTextView.setText("NONE");
            }

        });



        // /*为ListView添加点击事件*/
        lv_tasksss.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //获得选中项的HashMap对象
                //               HashMap<String,String> map=(HashMap<String,String>)lv_tasksss.getItemAtPosition(arg2);
//                String title=map.get("ItemTitle");
//                String content=map.get("itemContent");

                Intent intent = new Intent(CheckersActivity.this, OrderDetails.class);
                intent.putExtra("update", false);
                intent.putExtra("orderId",submissionOrderList.get(arg2).get("orderId").toString());
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
{
    refreshLayout.setEnabled(false);
}
            }
        });
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        messageString="暂时还没有消息哦！";

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                snackbar= Snackbar.make(view,  messageString, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        fab.setVisibility(View.VISIBLE);
                    }
                });
                snackbar.setActionTextColor(Color.WHITE);
                TextView textView=(TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextSize(17);
                textView.setMaxLines(20);
                snackbar.show();
                ViewGroup.LayoutParams lp=snackbar.getView().getLayoutParams();
                lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                snackbar.getView().setLayoutParams(lp);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //绑定控件
        lv_tasksss = (ListView) this.findViewById(R.id.lv_tasksss);
        mySpinner = (Spinner) findViewById(R.id.spinner3);
        refreshLayout=(SwipeRefreshLayout) findViewById(R.id.refresh_submission);

        Log.i("init","test");
       submissionOrderList=new ArrayList<>();
        //绑定适配器
        //getData(1);
        Log.i("初始化","getdata1");
        waitAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        waitAdapter.listItem =submissionOrderList;
        lv_tasksss.setAdapter(waitAdapter);//为ListView绑定Adapter
        waitAdapter.notifyDataSetChanged();
        Log.i("初始化结束","initover");
        confirmAdapter = new MyAdapterO(CheckersActivity.this);//得到一个MyAdapter对象
        establishAdapter = new MyAdapterO(CheckersActivity.this);//得到一个MyAdapter对象
        refuseAdapter = new MyAdapterO(CheckersActivity.this);//得到一个MyAdapter对象
      refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getData(currentList);
            }
        });
    }


    private void initpermission(){
        permissMyAdapter=new PermissMyAdapter(CheckersActivity.this);
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
                Permission.excutePermissions(map.get("ItemText").toString(),CheckersActivity.this);

            }
        });


        new AlertDialog.Builder(CheckersActivity.this)
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
                    ApiService.GetString(CheckersActivity.this, "extraPermissions", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().split(":")[0].equals("true")) {
                                permissMyAdapter.listItem.clear();
                                permissMyAdapter.listItem=Permission.getPermissions(response.trim().split(":")[1]);
                                permissMyAdapter.notifyDataSetChanged();
                            }else if (response.trim().equals("没有额外权限！")){
                                Toast.makeText(CheckersActivity.this,"没有额外权限",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(CheckersActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(CheckersActivity.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       /* if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        //getMenuInflater().inflate(R.menu.checkers, menu);
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

        if (id == R.id.nav_checker_order_history) {
            Intent intent = new Intent(CheckersActivity.this, HistoryOrder.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            //finish();
        } else if (id == R.id.nav_checker_apply_instrument) {
            //Intent intent = new Intent(CheckersActivity.this, ApplyInstrument.class);
            Intent intent = new Intent(CheckersActivity.this, OrderSelectActivity.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            // finish();
        }else if (id == R.id.nav_checker_start_check) {
            Intent intent = new Intent(CheckersActivity.this, SelectOrder.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);

            //finish();
        } else if (id == R.id.nav_checker_report_check) {
            Intent intent = new Intent(CheckersActivity.this, CheckReport.class);
            //intent.putExtra("userid",userid);
            intent.putExtra("option",1);
            startActivity(intent);
            // finish();
        } else if (id == R.id.nav_checker_report_history) {
            Intent intent = new Intent(CheckersActivity.this, CheckReport.class);
            intent.putExtra("option",1);
            intent.putExtra("newoption",1);
            startActivityForResult(intent, 6666);
        } else if (id == R.id.nav_messageinfo) {
            Intent intent = new Intent(CheckersActivity.this, MessageInform.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            // finish();
        } else if (id == R.id.nav_personinfomation) {
            Intent intent = new Intent(CheckersActivity.this, PersonInformation.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            // finish();
        } else if (id == R.id.nav_exit) {
            finish();
        }else if (id==R.id.extra_permisson_checkers){
            initpermission();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getData(final int requestCode) {
        View processView=View.inflate(this,R.layout.simple_processbar,null);
        final android.support.v7.app.AlertDialog processDialog=new android.support.v7.app.AlertDialog.Builder(this).create();
        processDialog.setView(processView);
        processDialog.show();
        submissionOrderList=new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("requestCode", requestCode);
        ApiService.GetString(this, "submissionOrder", parameters, new RxStringCallback(){
            @Override
            public void onNext(Object tag, String response) {
                processDialog.dismiss();
                if (response != null) {
                    JSONArray jsonArray = JSONObject.parseArray(response);
                    for (Object object : jsonArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        submissionOrderList.add(jsonObject);
                    }
                }
                    if (requestCode == 1 && (submissionOrderList.size() > 0)) {
                        messageString = "您有待确认的派工如下：\n";
                        for (int i = 0; i < submissionOrderList.size(); i++) {
                            messageString += submissionOrderList.get(i).get("projectName").toString() + "\n";
                        }
                        //fab.setVisibility(View.INVISIBLE);
                        //snackbar.show();
                    }
                    if (submissionOrderList.size()==0) {
                        new AlertDialog.Builder(CheckersActivity.this).setTitle("暂无" + mySpinner.getSelectedItem().toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                    if (currentList == 1) {
                        waitAdapter.listItem = submissionOrderList;
                        lv_tasksss.setAdapter(waitAdapter);
                    } else if (currentList == 2) {
                        confirmAdapter.listItem = submissionOrderList;
                        lv_tasksss.setAdapter(confirmAdapter);
                    } else if (currentList == 3) {
                        establishAdapter.listItem = submissionOrderList;
                        lv_tasksss.setAdapter(establishAdapter);
                    } else {
                        refuseAdapter.listItem = submissionOrderList;
                        lv_tasksss.setAdapter(refuseAdapter);
                    }
                    refreshLayout.setRefreshing(false);
                    Log.i("获取数据结束", "getdataover");
            }
            @Override
            public void onError(Object tag, Throwable e) {
                refreshLayout.setRefreshing(false);
                processDialog.dismiss();
                Log.i("失败",""+e.getMessage()+"  "+e.getCause()+tag.toString());
                Toast.makeText(CheckersActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

        });

    }

    private void rejectSubmission(final int position, String id, final String reason) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reason", reason);
        parameters.put("submissionId", id);
        ApiService.GetString(this, "rejectSubmission", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")) {
                    submissionOrderList.remove(position);
                    waitAdapter.notifyDataSetChanged();
                    Toast.makeText(CheckersActivity.this, "拒绝成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckersActivity.this, "拒绝失败", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(CheckersActivity.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });

    }
    private void confirmSubmission(final int position,String id){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("submissionId", id);
        ApiService.GetString(this, "confirmSubmission", parameters, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (response.equals("success")) {
                    submissionOrderList.remove(position);
                    waitAdapter.notifyDataSetChanged();
                    Toast.makeText(CheckersActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckersActivity.this, "接单失败", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                Toast.makeText(CheckersActivity.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }





    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<JSONObject> listItem = new ArrayList<JSONObject>();
        /*构造函数*/
        public MyAdapter(Context context) {
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
            final ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.task,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_unit);//委托单位
                holder.pnumber = (TextView) convertView.findViewById(R.id.dispatching_projectName);//工程名字
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);//工程地址
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);//工程时间
                holder.bt_chakan = (Button) convertView.findViewById(R.id.btn_refuse);//拒绝
                holder.bt_beizhu=(Button) convertView.findViewById(R.id.btn_accept);//接受
              if (currentList!=1){
                  holder.bt_chakan.setVisibility(View.GONE);
                  holder.bt_beizhu.setVisibility(View.GONE);
              }
                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            holder.pname.setText(listItem.get(position).get("orderOrg").toString());
            holder.pnumber.setText(listItem.get(position).get("projectName").toString());
            holder.paddress.setText(listItem.get(position).get("projectAddress").toString());
            holder.pdate.setText(listItem.get(position).get("checkTime").toString());

            holder.bt_chakan.setTag(position);
            holder.bt_beizhu.setTag(position);
            //fLog.i(holder.pname.getText().toString(),""+holder.pnumber.getText().toString());


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/




            holder.bt_chakan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    final EditText et=new EditText(CheckersActivity.this);

                    new  AlertDialog.Builder(CheckersActivity.this)
                            .setTitle("系统提示")
                            .setMessage("\n请输入拒绝的理由：")
                            .setView(et)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定",
                                    new  DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public  void  onClick(DialogInterface dialog, int  which)
                                        {
                                            rejectSubmission(position,listItem.get(position).get("submissionId").toString(),et.getText().toString());
                                        }
                                    }).show();

                }
            });


            holder.bt_beizhu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //打印Button的点击信息
                    new  AlertDialog.Builder(CheckersActivity.this)
                            .setTitle("系统提示")
                            .setMessage("确认接受"+listItem.get(position).getString("projectName")+"?")
                            .setPositiveButton("确定",
                                    new  DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public  void  onClick(DialogInterface dialog, int  which)
                                        {
                                            confirmSubmission(position,listItem.get(position).get("submissionId").toString());
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            });




            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView pname,pnumber,paddress,pdate;
        public Button bt_chakan;
        public Button bt_beizhu;
    }
    //其他任务信息加载

    /*
   * 新建一个类继承BaseAdapter，实现视图与数据的绑定
   */
    private class MyAdapterO extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        ArrayList<JSONObject> listItem = new ArrayList<JSONObject>();
        /*构造函数*/
        public MyAdapterO(Context context) {
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
            final ViewHolderO holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.taskothers,null);
                holder = new ViewHolderO();
                /*得到各个控件的对象*/

                holder.pname = (TextView) convertView.findViewById(R.id.dispatching_unit);//委托单位
                holder.pnumber = (TextView) convertView.findViewById(R.id.dispatching_projectName);//工程名字
                holder.paddress = (TextView) convertView.findViewById(R.id.sendPlace);//工程地址
                holder.pdate = (TextView) convertView.findViewById(R.id.actual_time);//时间
//                holder.bt_chakan = (Button) convertView.findViewById(R.id.button_chakan);
//                holder.bt_beizhu=(Button) convertView.findViewById(R.id.button_beizhu);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolderO)convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/

            holder.pname.setText(listItem.get(position).get("orderOrg").toString());
            holder.pnumber.setText(listItem.get(position).get("projectName").toString());
            holder.paddress.setText(listItem.get(position).get("projectAddress").toString());
            holder.pdate.setText(listItem.get(position).get("checkTime").toString());
//            holder.bt_chakan.setTag(position);
//            holder.bt_beizhu.setTag(position);


            // ItemListener itemListener = new ItemListener(position);//监听器记录了所在行，于是绑定到各个控件后能够返回具体的行，以及触发的控件
            /*为Button添加点击事件*/

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolderO{
        public TextView pname,pnumber,paddress,pdate;
//        public Button bt_chakan;
//        public Button bt_beizhu;
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
