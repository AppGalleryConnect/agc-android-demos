package com.huawei.serverlessmodel.codelab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.loginbtn);
        TextView id = findViewById(R.id.userid);
        login.setOnClickListener(view -> AGConnectAuth.getInstance().signInAnonymously().addOnSuccessListener(signInResult -> {
            String userId = signInResult.getUser().getUid();
            id.setText(userId);
        }));
        Button draw = findViewById(R.id.draw);
        draw.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, WebActivity.class);
            startActivity(intent);
        });
    }
}