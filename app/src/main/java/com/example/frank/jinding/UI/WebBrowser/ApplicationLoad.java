package com.example.frank.jinding.UI.WebBrowser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.frank.jinding.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApplicationLoad extends AppCompatActivity {

    public WebView myWebView;
    private ProgressDialog dialog=null;
    String urrl;

    private ImageButton back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_load);

        Intent intent=getIntent();
        urrl= intent.getStringExtra("url");


        back=(ImageButton)this.findViewById(R.id.titleback);
        title=(TextView)this.findViewById(R.id.titleplain);
        //标题栏设置
        title.setText(R.string.system_name);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String str = urrl;
        Context context=ApplicationLoad.this;
        myWebView = (WebView) findViewById(R.id.webview1);

        //同步cookie
        //new HttpCookie(mHandler,str).start();
        syncCookie(context,str);
        // 设置可以访问文件  
        myWebView.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript  
        myWebView.getSettings().setJavaScriptEnabled(true);

        //设置允许访问文件数据  
        myWebView.getSettings().setAllowFileAccess(true);

        myWebView.setHorizontalScrollBarEnabled(false);// 水平不显示滚动条  
        myWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);// 禁止即在网页顶出现一个空白，又自动回去。  
        myWebView.setWebChromeClient(new webChromClient());
        myWebView.setWebViewClient(new webClient());//防止外部浏览器



        myWebView.setWebViewClient(new WebViewClient());
        WebSettings settings = myWebView.getSettings();
        myWebView.loadUrl(str);


        myWebView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(event.getAction()== KeyEvent.ACTION_DOWN){
                    if(keyCode== KeyEvent.KEYCODE_BACK&&myWebView.canGoBack()){
                        myWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });





    }




    private class webChromClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress){
            // TODO Auto-generated method stub  
            super.onProgressChanged(view, newProgress);
        }


        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback){
            super.getVisitedHistory(callback);
            //Log.i(TAG,"getVisitedHistory");
        }

        @Override
        public void onCloseWindow(WebView window){
            super.onCloseWindow(window);
            // Log.i(TAG,"onCloseWindow");
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg){
            // Log.i(TAG, "onCreateWindow");
            return super.onCreateWindow(view,isDialog,isUserGesture,resultMsg);
        }

        @Override
        @Deprecated
        public void onExceededDatabaseQuota(String url,
                                            String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota,
                                            WebStorage.QuotaUpdater quotaUpdater){
            super.onExceededDatabaseQuota(url,databaseIdentifier,quota, estimatedDatabaseSize,totalQuota,quotaUpdater);
            //Log.i(TAG,"onExceededDatabaseQuota");
        }
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon){
            super.onReceivedIcon(view,icon);
            //Log.i(TAG,"gonReceivedIcon");
        }

        @Override
        public void onReceivedTitle(WebView view, String title){
            super.onReceivedTitle(view,title);
            //Log.i(TAG,"onReceivedTitle");
        }

        @Override
        public void onRequestFocus(WebView view){
            super.onRequestFocus(view);
            // Log.i(TAG,"onRequestFocus");
        }



        public void openFileChooser(ValueCallback<Uri> uploadMsg){
            ValueCallback<Uri> mUploadMessag;
            mUploadMessag = uploadMsg;
            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            ApplicationLoad.this.startActivityForResult(
                    Intent.createChooser(i,"File Chooser"),ApplicationLoad.RESULT_FIRST_USER);
        }
        public void openFileChooser(ValueCallback uploadMsg, String acceptType){

            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            ApplicationLoad.this.startActivityForResult(
                    Intent.createChooser(i,"File Browser"),ApplicationLoad.RESULT_FIRST_USER);
        }
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
            ValueCallback<Uri> mUploadMessage;
            mUploadMessage= uploadMsg;
            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            ApplicationLoad.this.startActivityForResult(
                    Intent.createChooser(i,"File Chooser"),ApplicationLoad.RESULT_FIRST_USER);
        }


    }






    private class webClient extends WebViewClient {

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon){
            // TODO Auto-generated method stub  
            super.onPageStarted(view, url,favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url){
            // TODO Auto-generated method stub  
            // super.onPageFinished(view, url);  
            dialog.dismiss();

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub  
            super.onReceivedError(view,errorCode,description,failingUrl);

            // guo add  
            //  Toast.makeText(ApplicationLoad.this,"网页加载出错！", Toast.LENGTH_LONG).show();

            //view.loadUrl("file:///android_asset/defaultpage/index1.html");// 加载一个默认的本地网页  
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            // TODO Auto-generated method stub  
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onLoadResource(WebView view, String url){
            super.onLoadResource(view,url);
            // Log.i(TAG,"onLoadResource: ");
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                                              HttpAuthHandler handler, String host, String realm){
            super.onReceivedHttpAuthRequest(view,handler,host,realm);
            // Log.i(TAG,"onReceivedHttpAuthRequest: ");
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            super.onReceivedSslError(view,handler,error);
            //Log.i(TAG,"onReceivedSslError: ");
        }

        @Override
        @Deprecated
        public void onTooManyRedirects(WebView view, Message cancelMsg,
                                       Message continueMsg){
            super.onTooManyRedirects(view,cancelMsg,continueMsg);
            // Log.i(TAG,"onTooManyRedirects");
        }




    }

    @Override
    protected void onResume(){
// TODO Auto-generated method stub  
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        // TODO Auto-generated method stub  
        super.onDestroy();
    }

    // 网络状态判断  
    public boolean isNetworkConnected(Context context){
        if(context!=null){
            ConnectivityManager mConnectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo!=null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;

    }





    /**
     * Sync Cookie
     */
    private void syncCookie(Context context, String url){
        try{
            // Log.d("Nat: webView.syncCookie.url", url);
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if(oldCookie != null){
                // Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }
            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s","INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s","INPUT YOUR PATH STRING"));
            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();
            String newCookie = cookieManager.getCookie(url);
            if(newCookie != null){
                // Log.d("Nat: webView.syncCookie.newCookie", newCookie);
            }
        }catch(Exception e){
            //Log.e("Nat: webView.syncCookie failed", e.toString());
        }
    }






}
