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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frank.jinding.ExtraPermission.Permission;
import com.example.frank.jinding.R;
import com.example.frank.jinding.Service.ApiService;
import com.example.frank.jinding.UI.PersonAboutActivity.MessageInform;
import com.example.frank.jinding.UI.PersonAboutActivity.PersonInformation;
import com.example.frank.jinding.Utils.MenuMessage;
import com.example.frank.jinding.Utils.SnackbarUtils;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckersActivityNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> list = new ArrayList<String>();
    private long mExitTime = 0;


    private ListView lv_message;


    private FloatingActionButton fab;
    private String messageString;
    private Snackbar snackbar;

    private int currentList = 0;

    private MyAdapter myAdapter;

    private PermissMyAdapter permissMyAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkers);

        init();


        // /*为ListView添加点击事件*/
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //获得选中项的HashMap对象
                String menuname_item=myAdapter.listItem.get(arg2).get("ItemName").toString();

                if (menuname_item.contains("派工确认")||menuname_item.contains("订单管理")){
                    Intent intent = new Intent(CheckersActivityNew.this, Checker.class);
                    startActivityForResult(intent, 6666);
                }else if (menuname_item.contains("仪器申领")||menuname_item.contains("检验仪器")){
                    Intent intent = new Intent(CheckersActivityNew.this, OrderSelectActivity.class);
                    startActivityForResult(intent, 6666);
                }else if (menuname_item.contains("检验现场")){
                    Intent intent = new Intent(CheckersActivityNew.this, SelectOrder.class);
                    startActivityForResult(intent, 6666);
                }else if (menuname_item.contains("校核检验报告")||menuname_item.contains("校核报告")){
                    Intent intent = new Intent(CheckersActivityNew.this, CheckReport.class);
                    intent.putExtra("option",1);
                    startActivityForResult(intent, 6666);
                }


                //Toast.makeText(CheckersActivityNew.this, "快捷事件处理方式正在研发中……" , Toast.LENGTH_SHORT).show();
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
        lv_message = (ListView) this.findViewById(R.id.lv_checker_messages);
        myAdapter=new MyAdapter(CheckersActivityNew.this);
        lv_message.setAdapter(myAdapter);

        getMessageInfo();


    }



    public void getMessageInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    String data="查询待处理事务";
                    Map<String, Object> paremetes = new HashMap<>();
                    paremetes.put("data",data);
                    ApiService.GetString(CheckersActivityNew.this, "getMessageInfo", paremetes, new RxStringCallback() {
                        boolean flag = false;

                        @Override
                        public void onNext(Object tag, String response) {

                            if (!response.trim().equals("获取失败！")&&response.trim().length()>3) {

                                System.out.println("===========待处理信息=============="+response);
                                String data[]=response.split("##");
                                myAdapter.listItem.clear();
                                for (int i=0;i+1<data.length;i++){

                                    String dd[]=data[i].split("#");

                                    if (dd[2]!=null&&!dd[2].equals("")&&Integer.parseInt(dd[2])>0){

                                        if (MenuMessage.checker.contains(dd[1])){

                                            HashMap<String, Object> map=new HashMap<>();
                                            map.put("ItemName",dd[1]);
                                            map.put("ItemCount",dd[2]+"");
                                            myAdapter.listItem.add(map);

                                        }
                                    }

                                }
                                myAdapter.notifyDataSetChanged();


                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(CheckersActivityNew.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(CheckersActivityNew.this, "获取失败" + e, Toast.LENGTH_SHORT).show();
                            //refreshLayout.setRefreshing(false);
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==6666) {
            getMessageInfo();
        }
    }


    private void initpermission(){
        permissMyAdapter=new PermissMyAdapter(CheckersActivityNew.this);
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
                Permission.excutePermissions(map.get("ItemText").toString(),CheckersActivityNew.this);

            }
        });


        new AlertDialog.Builder(CheckersActivityNew.this)
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
                    ApiService.GetString(CheckersActivityNew.this, "extraPermissions", paremetes, new RxStringCallback() {


                        @Override
                        public void onNext(Object tag, String response) {


                            if (response.trim().split(":")[0].equals("true")) {
                                permissMyAdapter.listItem.clear();
                                permissMyAdapter.listItem=Permission.getPermissions(response.trim().split(":")[1]);
                                permissMyAdapter.notifyDataSetChanged();
                            }else if (response.trim().equals("没有额外权限！")){
                                Toast.makeText(CheckersActivityNew.this,"没有额外权限",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Object tag, Throwable e) {
                            Toast.makeText(CheckersActivityNew.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancel(Object tag, Throwable e) {
                            Toast.makeText(CheckersActivityNew.this, "获取失败" + e, Toast.LENGTH_SHORT).show();

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

        if (id == R.id.nav_task) {
            Intent intent = new Intent(CheckersActivityNew.this, HistoryOrder.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            //finish();
        } else if (id == R.id.nav_instrument) {
            //Intent intent = new Intent(CheckersActivity.this, ApplyInstrument.class);
            Intent intent = new Intent(CheckersActivityNew.this, OrderSelectActivity.class);
            //intent.putExtra("userid",userid);
            //startActivity(intent);
            startActivityForResult(intent, 6666);
            // finish();
        }/* else if (id == R.id.nav_signleave) {
            //Intent intent=new Intent(CheckersActivity.this,SignLeave.class);
            //intent.putExtra("userid",userid);
            //startActivity(intent);
            //finish();

            Intent intent=new Intent(CheckersActivity.this,LocationActivity.class);
            startActivity(intent);


        }*/ else if (id == R.id.nav_check) {
            Intent intent = new Intent(CheckersActivityNew.this, SelectOrder.class);
            //intent.putExtra("userid",userid);
            //startActivity(intent);
            startActivityForResult(intent, 6666);

            //finish();
        } else if (id == R.id.nav_opinion) {
            Intent intent = new Intent(CheckersActivityNew.this, CheckReport.class);
            //intent.putExtra("userid",userid);
            //startActivity(intent);
            intent.putExtra("option",1);
            startActivityForResult(intent, 6666);
            // finish();
        } else if (id == R.id.nav_messageinfo) {
            Intent intent = new Intent(CheckersActivityNew.this, MessageInform.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            // finish();
        } else if (id == R.id.nav_personinfomation) {
            Intent intent = new Intent(CheckersActivityNew.this, PersonInformation.class);
            //intent.putExtra("userid",userid);
            startActivity(intent);
            // finish();
        } else if (id == R.id.nav_exit) {
            finish();
        }else if (id==R.id.extra_permisson_checkers){
            initpermission();
        }else if (id==R.id.nav_order_manage){
            Intent intent = new Intent(CheckersActivityNew.this, Checker.class);
            //intent.putExtra("userid",userid);
            //startActivity(intent);
            startActivityForResult(intent, 6666);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    /*
     * 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局


        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();

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
                convertView = mInflater.inflate(R.layout.message_tip,null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/

                holder.title = (TextView) convertView.findViewById(R.id.message_menu_name);
                holder.itemcount = (TextView) convertView.findViewById(R.id.message_item_count);

                convertView.setTag(holder);//绑定ViewHolder对象
            }
            else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }

            holder.title.setText(listItem.get(position).get("ItemName").toString());
            holder.itemcount.setText(listItem.get(position).get("ItemCount").toString());

            return convertView;
        }

    }
    /*存放控件*/
    public final class ViewHolder{
        public TextView title,itemcount;

    }
    //其他任务信息加载



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
