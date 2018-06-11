package com.example.frank.jinding.Service;

import android.util.Log;

import com.example.frank.jinding.Conf.Path;
import com.example.frank.jinding.Tools.StreamTool;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddOrderService {


	public static String AddOrder(String order,String token){
		try {
			//1打开一个浏览器
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			HttpClient client=new DefaultHttpClient();
			//2输入地址
			String path= Path.AddOrderPath;
			HttpPost httpPost=new HttpPost(path);
			httpPost.setHeader("token",token);
			//3指定要提交的数据实体
			List<NameValuePair> parameters=new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("orderJson", order));
		//	parameters.add(new BasicNameValuePair("password",password));
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			
			HttpResponse httpRes=client.execute(httpPost);
			Log.i("addOrder","连接");
			int code=httpRes.getStatusLine().getStatusCode();
			Log.i("addOrder",code+"test");
			if(code==200){
				InputStream is=httpRes.getEntity().getContent();
				String text=StreamTool.streamtoText(is, "utf-8");
				return text;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "order失败1";
		}
		return "order失败";
		
	}












	/*public static String loginByGet(String username,String password)
	{
		try {
			String path="http://192.168.191.1:8080/day7/servlet/LoginController?username="+username+"&password="+password;
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			String charset="utf-8";
			int code=conn.getResponseCode();
			if(code==200){
				//请求成功
				String data= StreamTool.streamtoText(conn.getInputStream(), charset);
				if(data==null)
					return "输入流为空";
				else
					return data;
			}else{
				return "网络请求异常1";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "网络请求异常2";
		}

	}

	public static String  LoginByPost(String username,String password)
	{

	try {//使用笔记本建立的局域网时需要关闭防火墙
		String path="http://192.168.191.1:8080/day7/servlet/LoginController";
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		StringBuffer sb=new StringBuffer();
	    sb.append("username=").append(URLEncoder.encode(username, "utf-8")).append("&password=").append(URLEncoder.encode(password, "utf-8"));
		String data=sb.toString();
		System.out.println(data);
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", data.length()+"");

		//post 是浏览器把数据写给服务器 要允许写数据
		conn.setDoOutput(true);
		conn.setDoInput(true);
		OutputStream os;
		os = conn.getOutputStream();
		os.write(data.getBytes("utf-8"));
		String charset="utf-8";
		int code = conn.getResponseCode();
		if(code==200){
			String data2 = StreamTool.streamtoText(conn.getInputStream(), charset);
			if(data2==null)
				return "输入流为空";
			else
				return data2;
		}else{
			System.out.println("code:"+code);
			return "网络请求异常1";
		}
	} catch (Exception e) {
		return "网络请求异常2";
	}

	}
	*/




}
 