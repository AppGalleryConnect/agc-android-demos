/*
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.agc.quickstart.remoteconfig;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;

import java.util.Map;

public class Test1Activity extends AppCompatActivity {
    private TextView textView;
    private AGConnectConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        textView = findViewById(R.id.textView);

        config = AGConnectConfig.getInstance();
        config.applyDefault(R.xml.remote_config);

        SharedPreferences preferences = this.getApplicationContext().getSharedPreferences("Remote_Config", MODE_PRIVATE);
        long fetchInterval = 12 * 60 * 60L;
        if (preferences.getBoolean("DATA_OLD", false)) {
            fetchInterval = 0;
        }
        config.fetch(fetchInterval).addOnSuccessListener(configValues -> {
            config.apply(configValues);
            showAllValues();
            preferences.edit().putBoolean("DATA_OLD", false).apply();
            Toast.makeText(getBaseContext(), "Fetch Success", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getBaseContext(), "Fetch Fail", Toast.LENGTH_LONG).show();
        });
        showAllValues();
    }

    private void showAllValues() {
        Map<String, Object> map = config.getMergedAll();
        StringBuilder string = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            string.append(entry.getKey());
            string.append(" : ");
            string.append(entry.getValue());
            string.append("\n");
        }
        textView.setText(string);
    }
}