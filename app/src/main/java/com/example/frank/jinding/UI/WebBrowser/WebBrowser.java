package com.example.frank.jinding.UI.WebBrowser;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.frank.jinding.R;


public class WebBrowser extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        Intent geturl=getIntent();
        url=geturl.getStringExtra("url");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://play.jb51.net");
        intent.setData(content_url);
        startActivity(intent);
        //Log.e(TAG, "start onResume~~~");
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
        //Log.e(TAG, "start onStop~~~");
    }
}
