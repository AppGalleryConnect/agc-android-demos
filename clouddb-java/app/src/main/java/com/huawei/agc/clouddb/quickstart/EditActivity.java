/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2022. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.agc.clouddb.quickstart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.agc.clouddb.quickstart.model.BookEditFields;
import com.huawei.agc.clouddb.quickstart.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    static final String ACTION_ADD = "com.huawei.agc.clouddb.quickstart.ADD";

    static final String ACTION_SEARCH = "com.huawei.agc.clouddb.quickstart.SEARCH";

    private Button searchButton;
    private Button addButton;
    private EditText bookNameEdit;
    private View fieldAuthor;
    private EditText authorEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
    }

    private void initControl() {
        bookNameEdit = findViewById(R.id.edit_bookname);
        addButton = findViewById(R.id.add);
        searchButton = findViewById(R.id.search);
        fieldAuthor = findViewById(R.id.field_author);
        authorEdit = findViewById(R.id.edit_author);
    }

    private void initViews() {
        Intent intent = getIntent();
        String action = intent.getAction();
        initControl();
        if (ACTION_ADD.equals(action)) {
            BookEditFields.EditMode editMode = BookEditFields.EditMode.valueOf(
                intent.getStringExtra(BookEditFields.EDIT_MODE));
            fieldAuthor.setVisibility(View.VISIBLE);

            View fieldPublisher = findViewById(R.id.field_publisher);
            fieldPublisher.setVisibility(View.VISIBLE);
            EditText publisherEdit = findViewById(R.id.edit_publisher);

            View fieldPublishTime = findViewById(R.id.field_publish_time);
            fieldPublishTime.setVisibility(View.VISIBLE);
            EditText publishTimeEdit = findViewById(R.id.edit_publish_time);

            View fieldPriceEdit = findViewById(R.id.field_price);
            fieldPriceEdit.setVisibility(View.VISIBLE);
            EditText priceEdit = findViewById(R.id.edit_price);
            Calendar calendar = Calendar.getInstance();
            if (editMode == BookEditFields.EditMode.MODIFY) {
                setTitle(R.string.edit_book);
                bookNameEdit.setText(intent.getStringExtra(BookEditFields.BOOK_NAME));
                authorEdit.setText(intent.getStringExtra(BookEditFields.AUTHOR));
                priceEdit.setText(String.format(Locale.getDefault(), "%.2f",
                    intent.getDoubleExtra(BookEditFields.PRICE, Double.MIN_VALUE)));
                publisherEdit.setText(intent.getStringExtra(BookEditFields.PUBLISHER));
                String borrowTime = intent.getStringExtra(BookEditFields.PUBLISH_TIME);
                Date date = DateUtils.parseDate(borrowTime);
                calendar.setTime(date);
                publishTimeEdit.setText(borrowTime);
                addButton.setText(R.string.modify);
            }

            publishTimeEdit.setOnClickListener(
                v -> new DatePickerDialog(EditActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                    (view, year, month, dayOfMonth) -> {
                        // Month start from 0
                        String dateTime = year + "-" + (month + 1) + "-" + dayOfMonth;
                        calendar.set(year, month, dayOfMonth);
                        publishTimeEdit.setText(dateTime);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show());

            final int bookId = intent.getIntExtra(BookEditFields.BOOK_ID, -1);
            addButton.setOnClickListener(v -> {
                if ("".equals(bookNameEdit.getText().toString()) && "".equals(authorEdit.getText().toString())
                    && "".equals(publisherEdit.getText().toString())) {
                    onBackPressed();
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra(BookEditFields.BOOK_ID, bookId);
                resultIntent.putExtra(BookEditFields.BOOK_NAME, bookNameEdit.getText().toString());
                if (!"".equals(priceEdit.getText().toString())) {
                    resultIntent.putExtra(BookEditFields.PRICE, Double.parseDouble(priceEdit.getText().toString()));
                }
                resultIntent.putExtra(BookEditFields.AUTHOR, authorEdit.getText().toString());
                resultIntent.putExtra(BookEditFields.PUBLISHER, publisherEdit.getText().toString());
                resultIntent.putExtra(BookEditFields.PUBLISH_TIME, publishTimeEdit.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            });
            searchButton.setVisibility(View.GONE);
        } else if (ACTION_SEARCH.equals(action)) {
            actionSearch();
        } else {
            // Something wrong, just return
            finish();
            return;
        }

        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void actionSearch() {
        setTitle(R.string.search_book);

        TextView bookNameOrTV = findViewById(R.id.tv_book_name_or);
        bookNameOrTV.setVisibility(View.VISIBLE);
        EditText bookNameET2 = findViewById(R.id.edit_bookname2);
        bookNameET2.setVisibility(View.VISIBLE);

        TextView authorOr = findViewById(R.id.tv_author_or);
        authorOr.setVisibility(View.VISIBLE);
        EditText authorET2 = findViewById(R.id.edit_author2);
        authorET2.setVisibility(View.VISIBLE);
        fieldAuthor.setVisibility(View.VISIBLE);

        View fieldShowCount = findViewById(R.id.field_show_count);
        fieldShowCount.setVisibility(View.VISIBLE);
        EditText showCountEdit = findViewById(R.id.edit_show_count);

        View fieldSearchPriceView = findViewById(R.id.field_search_price);
        fieldSearchPriceView.setVisibility(View.VISIBLE);
        EditText lowestPriceEdit = findViewById(R.id.lowest_price);
        EditText highestPriceEdit = findViewById(R.id.highest_price);
        searchButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            putExtra(resultIntent, BookEditFields.BOOK_NAME, bookNameEdit.getText().toString(), String.class);
            putExtra(resultIntent, BookEditFields.BOOK_NAME_OR, bookNameET2.getText().toString(), String.class);
            putExtra(resultIntent, BookEditFields.AUTHOR, authorEdit.getText().toString(), String.class);
            putExtra(resultIntent, BookEditFields.AUTHOR_OR, authorET2.getText().toString(), String.class);
            putExtra(resultIntent, BookEditFields.LOWEST_PRICE, lowestPriceEdit.getText().toString(), Double.class);
            putExtra(resultIntent, BookEditFields.HIGHEST_PRICE, highestPriceEdit.getText().toString(), Double.class);
            putExtra(resultIntent, BookEditFields.SHOW_COUNT, showCountEdit.getText().toString(), Integer.class);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        addButton.setVisibility(View.GONE);
    }

    private <T> void putExtra(Intent resultIntent, String name, String value, Class<T> tClass) {
        if (value.trim().isEmpty()) {
            return;
        }
        if (tClass.equals(Double.class)) {
            resultIntent.putExtra(name, Double.parseDouble(value));
        } else if (tClass.equals(Integer.class)) {
            resultIntent.putExtra(name, Integer.parseInt(value));
        } else {
            resultIntent.putExtra(name, value);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
