package com.example.frank.jinding.PushMessage;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.frank.jinding.Service.LoginService;
import com.example.frank.jinding.UI.AuthorActivity.AuthorizedPersonsActivity;
import com.example.frank.jinding.UI.CheckerActivity.CheckReport;
import com.example.frank.jinding.UI.CheckerActivity.CheckersActivity;
import com.example.frank.jinding.UI.CheckerActivity.CheckersActivityNew;
import com.example.frank.jinding.UI.DepartorActivity.DepartmentHeadersActivity;
import com.example.frank.jinding.UI.CheckerActivity.OrderSelectActivity;
import com.example.frank.jinding.UI.CheckerActivity.SelectOrder;
import com.example.frank.jinding.UI.SurveyorsActivity.SurveyorsActivity;
import com.example.frank.jinding.UI.TechnicorActivity.TDCheckOrder;
import com.example.frank.jinding.UI.TechnicorActivity.TechnicalDirectorsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

				//context.sendBroadcast(new Intent("net.deniro.android.MY_BROADCAST"));
				//进行相关消息的刷新
				/*String activityName=getRunningActivityName(context);
				if (activityName.equals("CheckersActivityNew")){
					//检验员

				}else if(activityName.equals("AuthorizedPersonsActivity")){
					//授权签字人

				}else  if(activityName.equals("DepartmentHeadersActivityNew")){
					//部门负责人

				}else  if(activityName.equals("SalesmansActivity")){

					//业务员
				}else  if(activityName.equals("activity_surveyors")){
					//检验师

				}else  if(activityName.equals("TechnicalDirectorsActivity")){

					//技术负责人
				}*/
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
				//初始化用户信息
				initUser(context);
				//打开自定义的Activity
				goToSpecifyActivity(context,bundle);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	//获得窗体名称
	private String getRunningActivityName(Context context){
		ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		return runningActivity;
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}


	private void initUser(Context context){
		Map<String,String> mapUserInfo=LoginService.getSaveIfo(context);
		Map<String,String> mapToken=LoginService.getSaveToken(context);
		String username=mapUserInfo.get("用户名");
		String password=mapUserInfo.get("密码");
		String token=mapToken.get("token");
		LoginService.LoginByHttpClientPost(username,password,token);
	}

	private void goToSpecifyActivity(Context context,Bundle bundle){
		Intent intent=null;
		String pageType="";
		try{
			JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
			Iterator<String> it =  json.keys();

			while (it.hasNext()) {
				String myKey = it.next();
				pageType=json.optString(myKey);
			}
		}catch (JSONException e){

		}

		switch (pageType){
			case "0000":
				intent=new Intent(context, com.example.frank.jinding.UI.MainActivity.class);
				break;
			case "0101":
				intent=new Intent(context,CheckersActivityNew.class);
				break;
			case "0102":
				intent=new Intent(context, OrderSelectActivity.class);
				break;
			case "0103":
				intent=new Intent(context, SelectOrder.class);
				break;
			case "0104":
				intent=new Intent(context, CheckReport.class);
				break;
			case "0201":
				intent=new Intent(context, SurveyorsActivity.class);
				break;
			case "0301":
				intent=new Intent(context, TDCheckOrder.class);
				break;
			case "0302":
				intent=new Intent(context, TechnicalDirectorsActivity.class);
				break;
			case "0501":
				intent=new Intent(context, AuthorizedPersonsActivity.class);
				break;
			case "0601":
				intent=new Intent(context, DepartmentHeadersActivity.class);
				break;
			case "0602":
				intent=new Intent(context,DepartmentHeadersActivity.class);
				break;
			default:
				intent=new Intent(context, com.example.frank.jinding.UI.MainActivity.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
		context.startActivity(intent);
	}
}
