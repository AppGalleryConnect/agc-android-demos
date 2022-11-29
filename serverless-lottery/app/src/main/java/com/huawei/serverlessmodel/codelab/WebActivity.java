package com.huawei.serverlessmodel.codelab;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView wb = findViewById(R.id.webview);
        wb.getSettings().setJavaScriptEnabled(true);
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
        String uid = "";
        if (user != null) {
            uid = user.getUid();
        }
        String url = "https://drawmodel.dra.agchosting.link/#/ninegrid";
        String campaignId = "100001";
        String finUrl = url + "?userId=" + uid + "&campaignId=" + campaignId;
        Log.i("WebActivity1", finUrl.toString());
        wb.loadUrl(finUrl);
    }
}
