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

package com.huawei.agc.clouddb.quickstart

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agc.clouddb.quickstart.model.BookEditFields
import com.huawei.agc.clouddb.quickstart.model.BookEditFields.EditMode
import com.huawei.agc.clouddb.quickstart.utils.DateUtils
import java.util.*


class EditActivity : AppCompatActivity() {
    private var searchButton: Button? = null
    private var addButton: Button? = null
    private var bookNameEdit: EditText? = null
    private var fieldAuthor: View? = null
    private var authorEdit: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        initViews()
    }

    private fun initControl() {
        bookNameEdit = findViewById(R.id.edit_bookname)
        addButton = findViewById(R.id.add)
        searchButton = findViewById(R.id.search)
        fieldAuthor = findViewById(R.id.field_author)
        authorEdit = findViewById(R.id.edit_author)
    }

    private fun initViews() {
        val intent = intent
        val action = intent.action
        initControl();
        when (action) {
            ACTION_ADD -> {
                val editMode = EditMode.valueOf(
                        intent.getStringExtra(BookEditFields.EDIT_MODE)!!)
                fieldAuthor?.visibility = View.VISIBLE;
                val authorEdit = findViewById<EditText>(R.id.edit_author)
                val fieldPublisher = findViewById<View>(R.id.field_publisher)
                fieldPublisher.visibility = View.VISIBLE
                val publisherEdit = findViewById<EditText>(R.id.edit_publisher)
                val fieldPublishTime = findViewById<View>(R.id.field_publish_time)
                fieldPublishTime.visibility = View.VISIBLE
                val publishTimeEdit = findViewById<EditText>(R.id.edit_publish_time)
                val fieldPriceEdit = findViewById<View>(R.id.field_price)
                fieldPriceEdit.visibility = View.VISIBLE
                val priceEdit = findViewById<EditText>(R.id.edit_price)
                val calendar = Calendar.getInstance()
                if (editMode == EditMode.MODIFY) {
                    setTitle(R.string.edit_book)
                    bookNameEdit?.setText(intent.getStringExtra(BookEditFields.BOOK_NAME))
                    authorEdit.setText(intent.getStringExtra(BookEditFields.AUTHOR))
                    priceEdit.setText(String.format(Locale.getDefault(), "%.2f",
                            intent.getDoubleExtra(BookEditFields.PRICE, Double.MIN_VALUE)))
                    publisherEdit.setText(intent.getStringExtra(BookEditFields.PUBLISHER))
                    val borrowTime = intent.getStringExtra(BookEditFields.PUBLISH_TIME)
                    val date = DateUtils.parseDate(borrowTime)
                    calendar.time = date
                    publishTimeEdit.setText(borrowTime)
                    addButton?.setText(R.string.modify)
                }
                publishTimeEdit.setOnClickListener {
                    DatePickerDialog(this@EditActivity, android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,
                            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                                // Month start from 0
                                val dateTime = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
                                calendar[year, month] = dayOfMonth
                                publishTimeEdit.setText(dateTime)
                            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DATE]).show()
                }
                val bookId = intent.getIntExtra(BookEditFields.BOOK_ID, -1)
                addButton?.setOnClickListener {
                    if ("" == bookNameEdit?.text.toString() && "" == authorEdit.text.toString() && "" == publisherEdit.text.toString()) {
                        onBackPressed()
                        return@setOnClickListener
                    }
                    val resultIntent = Intent()
                    resultIntent.putExtra(BookEditFields.BOOK_ID, bookId)
                    resultIntent.putExtra(BookEditFields.BOOK_NAME, bookNameEdit?.text.toString())
                    if ("" != priceEdit.text.toString()) {
                        resultIntent.putExtra(BookEditFields.PRICE, priceEdit.text.toString().toDouble())
                    }
                    resultIntent.putExtra(BookEditFields.AUTHOR, authorEdit.text.toString())
                    resultIntent.putExtra(BookEditFields.PUBLISHER, publisherEdit.text.toString())
                    resultIntent.putExtra(BookEditFields.PUBLISH_TIME, publishTimeEdit.text.toString())
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                searchButton?.visibility = View.GONE
            }
            ACTION_SEARCH -> {
                actionSearch()
            }
            else -> {
                // Something wrong, just return
                finish()
                return
            }
        }
        val cancelButton = findViewById<Button>(R.id.cancel)
        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun actionSearch() {
        setTitle(R.string.search_book)
        val bookNameOrTv = findViewById<TextView>(R.id.tv_book_name_or)
        bookNameOrTv.visibility = View.VISIBLE;
        val bookNameET2 = findViewById<EditText>(R.id.edit_bookname2)
        bookNameET2.visibility = View.VISIBLE;

        val authorOr = findViewById<TextView>(R.id.tv_author_or)
        authorOr.visibility = View.VISIBLE;
        val authorET2 = findViewById<EditText>(R.id.edit_author2)
        authorET2.visibility = View.VISIBLE
        fieldAuthor?.setVisibility(View.VISIBLE)

        val fieldShowCount = findViewById<View>(R.id.field_show_count)
        fieldShowCount.visibility = View.VISIBLE
        val showCountEdit = findViewById<EditText>(R.id.edit_show_count)
        val fieldSearchPriceView = findViewById<View>(R.id.field_search_price)
        fieldSearchPriceView.visibility = View.VISIBLE
        val lowestPriceEdit = findViewById<EditText>(R.id.lowest_price)
        val highestPriceEdit = findViewById<EditText>(R.id.highest_price)
        searchButton?.setOnClickListener {
            val resultIntent = Intent()
            putExtra(resultIntent, BookEditFields.BOOK_NAME, bookNameEdit!!.text.toString(), String::class.java)
            putExtra(resultIntent, BookEditFields.BOOK_NAME_OR, bookNameET2.text.toString(), String::class.java)
            putExtra(resultIntent, BookEditFields.AUTHOR, authorEdit!!.text.toString(), String::class.java)
            putExtra(resultIntent, BookEditFields.AUTHOR_OR, authorET2.text.toString(), String::class.java)
            putExtra(resultIntent, BookEditFields.LOWEST_PRICE, lowestPriceEdit.text.toString(), Double::class.java)
            putExtra(resultIntent, BookEditFields.HIGHEST_PRICE, highestPriceEdit.text.toString(), Double::class.java)
            putExtra(resultIntent, BookEditFields.SHOW_COUNT, showCountEdit.text.toString(), Int::class.java)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        addButton?.visibility = View.GONE
    }

    private fun <T> putExtra(resultIntent: Intent, name: String, value: String, tClass: Class<T>) {
        if (value.trim { it <= ' ' }.isEmpty()) {
            return
        }
        if (tClass == Double::class.java) {
            resultIntent.putExtra(name, value.toDouble())
        } else if (tClass == Int::class.java) {
            resultIntent.putExtra(name, value.toInt())
        } else {
            resultIntent.putExtra(name, value)
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    companion object {
        const val ACTION_ADD = "com.huawei.agc.clouddb.quickstart.ADD"
        const val ACTION_SEARCH = "com.huawei.agc.clouddb.quickstart.SEARCH"
    }
}
