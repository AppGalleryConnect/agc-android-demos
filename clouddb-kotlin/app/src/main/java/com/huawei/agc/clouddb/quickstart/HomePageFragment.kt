/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.SparseBooleanArray
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.fragment.app.Fragment
import com.huawei.agc.clouddb.quickstart.model.BookEditFields
import com.huawei.agc.clouddb.quickstart.model.BookInfo
import com.huawei.agc.clouddb.quickstart.model.CloudDBZoneWrapper
import com.huawei.agc.clouddb.quickstart.model.CloudDBZoneWrapper.UiCallBack
import com.huawei.agc.clouddb.quickstart.model.LoginHelper.OnLoginEventCallBack
import com.huawei.agc.clouddb.quickstart.model.StorageLocationHelper
import com.huawei.agc.clouddb.quickstart.utils.DateUtils
import com.huawei.agconnect.auth.SignInResult
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import java.util.*
import java.util.Collections.sort


class HomePageFragment : Fragment(), UiCallBack, OnLoginEventCallBack,
    StorageLocationHelper.StorageLocationChangeCallback {
    // Store current state of sort order
    private val mSortState = SortState()
    private val mQueryInfo = QueryInfo()
    private val mCloudDBZoneWrapper: CloudDBZoneWrapper = CloudDBZoneWrapper()
    private var mBookInfoAdapter: BookInfoAdapter? = null
    private var mHeaderView: View? = null
    private var mBottomPanelView: View? = null
    private var mOptionMenu: Menu? = null
    private var mInSearchMode = false
    private var mActivity: MainActivity? = null
    private val mHandler = MyHandler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mActivity = activity as MainActivity?
        mBookInfoAdapter = BookInfoAdapter(mActivity)
        mHandler.post {
            val loginHelper = mActivity!!.loginHelper
            loginHelper!!.addLoginCallBack(this)
            loginHelper.login()
            val locationHelper = mActivity!!.storageLocationHelper
            locationHelper!!.addCallback(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        initBookListHeaderViews(rootView)
        initBookListView(rootView)
        initBottomPanelView(rootView)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mOptionMenu = menu
        inflater.inflate(R.menu.book_manager_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        if (context != null) {
            intent.setClass(context!!, EditActivity::class.java)
        }
        when (item.itemId) {
            R.id.search_button -> {
                intent.action = EditActivity.ACTION_SEARCH
                startActivityForResult(intent, REQUEST_SEARCH)
            }
            R.id.add_button -> {
                intent.putExtra(BookEditFields.EDIT_MODE, BookEditFields.EditMode.ADD.name)
                intent.action = EditActivity.ACTION_ADD
                startActivityForResult(intent, REQUEST_ADD)
            }
            R.id.select_all -> {
                mBookInfoAdapter!!.onSelectAllMenuClicked()
                updateSelectAllMenuHint(item)
            }
            R.id.show_all -> {
                restoreQueryState()
                mHandler.post { mCloudDBZoneWrapper.queryAllBooks() }
                toggleShowAllState(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        restoreQueryState()
        if (requestCode == REQUEST_ADD) {
            processAddAction(data)
        } else {
            processSearchAction(data)
        }
    }

    private fun restoreQueryState() {
        mQueryInfo.clean()
        cleanSortState()
    }

    private fun processSearchAction(data: Intent?) {
        mQueryInfo.bookNameOr = data!!.getStringExtra(BookEditFields.BOOK_NAME_OR);
        mQueryInfo.bookName = data.getStringExtra(BookEditFields.BOOK_NAME);
        mQueryInfo.authorOr = data.getStringExtra(BookEditFields.AUTHOR_OR);
        mQueryInfo.author = data.getStringExtra(BookEditFields.AUTHOR);

        var lowestPrice = data.getDoubleExtra(BookEditFields.LOWEST_PRICE, 0.0)
        var highestPrice = data.getDoubleExtra(BookEditFields.HIGHEST_PRICE, 0.0)
        if (lowestPrice > highestPrice) {
            val temp = lowestPrice
            lowestPrice = highestPrice
            highestPrice = temp
        }
        mQueryInfo.lowestPrice = lowestPrice
        mQueryInfo.highestPrice = highestPrice

        val showCount = data.getIntExtra(BookEditFields.SHOW_COUNT, 0)
        if (showCount > 0) {
            mQueryInfo.showCount = showCount
        }
        val query = mQueryInfo.generalQuery()
        toggleShowAllState(true)
        mHandler.post { mCloudDBZoneWrapper.queryBooks(query!!) }
    }

    private fun processAddAction(data: Intent?) {
        val bookInfo = BookInfo()
        val bookID = data!!.getIntExtra(BookEditFields.BOOK_ID, -1)
        if (bookID == -1) {
            bookInfo.id = mCloudDBZoneWrapper.bookIndex + 1
        } else {
            bookInfo.id = bookID
        }
        bookInfo.bookName = data.getStringExtra(BookEditFields.BOOK_NAME)
        bookInfo.author = data.getStringExtra(BookEditFields.AUTHOR)
        bookInfo.price = data.getDoubleExtra(BookEditFields.PRICE, Double.MIN_VALUE)
        bookInfo.publisher = data.getStringExtra(BookEditFields.PUBLISHER)
        val dateTime = data.getStringExtra(BookEditFields.PUBLISH_TIME)
        bookInfo.publishTime = DateUtils.parseDate(dateTime)
        toggleShowAllState(false)
        mHandler.post { mCloudDBZoneWrapper.upsertBookInfos(bookInfo) }
    }

    override fun onDestroy() {
        mHandler.post { mCloudDBZoneWrapper.closeCloudDBZone() }
        super.onDestroy()
    }

    private fun initBookListHeaderViews(rootView: View) {
        // mHeaderView = inflater.inflate(R.layout.book_manager_header, null);
        mHeaderView = rootView.findViewById(R.id.table_header)
        mHeaderView?.run {
            val bookNameHeaderView = findViewById<View>(R.id.header_book_name)
            val bookNameTitleView = bookNameHeaderView.findViewById<TextView>(R.id.title_name)
            bookNameTitleView.setText(R.string.book_name)
            bookNameTitleView.setOnClickListener {
                updateSortState(BookEditFields.BOOK_NAME, bookNameHeaderView)
                queryWithOrder()
            }
            val authorHeaderView = findViewById<View>(R.id.header_author)
            val authorTitleView = authorHeaderView.findViewById<TextView>(R.id.title_name)
            authorTitleView.setText(R.string.author)
            authorHeaderView.setOnClickListener {
                updateSortState(BookEditFields.AUTHOR, authorHeaderView)
                queryWithOrder()
            }
            val priceHeaderView = findViewById<View>(R.id.header_price)
            val priceTitleView = priceHeaderView.findViewById<TextView>(R.id.title_name)
            priceTitleView.setText(R.string.price)
            priceHeaderView.setOnClickListener {
                updateSortState(BookEditFields.PRICE, priceHeaderView)
                queryWithOrder()
            }
            val borrowerHeaderView = findViewById<View>(R.id.header_publisher)
            val publisherTitleView = borrowerHeaderView.findViewById<TextView>(R.id.title_name)
            publisherTitleView.setText(R.string.publisher)
            borrowerHeaderView.setOnClickListener {
                updateSortState(BookEditFields.PUBLISHER, borrowerHeaderView)
                queryWithOrder()
            }
            val publishTimeHeaderView = findViewById<View>(R.id.header_publish_time)
            val publishTimeTitleView = publishTimeHeaderView.findViewById<TextView>(R.id.title_name)
            publishTimeTitleView.setText(R.string.publish_time)
            publishTimeHeaderView.setOnClickListener {
                updateSortState(BookEditFields.PUBLISH_TIME, publishTimeHeaderView)
                queryWithOrder()
            }
        }

    }

    private fun initBookListView(rootView: View) {
        val bookListView = rootView.findViewById<ListView>(R.id.book_list)
        bookListView.adapter = mBookInfoAdapter
        bookListView.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            if (mBookInfoAdapter!!.isMultipleMode) {
                mBookInfoAdapter!!.check(position)
                updateSelectAllMenuHint(mOptionMenu!!.findItem(R.id.select_all))
            } else {
                val bookInfo = mBookInfoAdapter!!.getItem(position) as BookInfo
                val intent = Intent()
                if (context != null) {
                    intent.setClass(context!!, EditActivity::class.java)
                }
                intent.putExtra(BookEditFields.BOOK_ID, bookInfo.id)
                intent.putExtra(BookEditFields.BOOK_NAME, bookInfo.bookName)
                intent.putExtra(BookEditFields.PRICE, bookInfo.price)
                intent.putExtra(BookEditFields.AUTHOR, bookInfo.author)
                intent.putExtra(BookEditFields.PUBLISHER, bookInfo.publisher)
                intent.putExtra(BookEditFields.PUBLISH_TIME, DateUtils.formatDate(bookInfo.publishTime))
                intent.putExtra(BookEditFields.EDIT_MODE, BookEditFields.EditMode.MODIFY.name)
                intent.action = EditActivity.ACTION_ADD
                startActivityForResult(intent, REQUEST_ADD)
            }
        }
        bookListView.onItemLongClickListener = OnItemLongClickListener { _: AdapterView<*>?, _: View?, _: Int, _: Long ->
            if (!mBookInfoAdapter!!.isMultipleMode) {
                mBookInfoAdapter!!.isMultipleMode = true
                invalidateViewInMultipleChoiceMode()
            }
            false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (mBookInfoAdapter!!.isMultipleMode) {
            invalidateViewInMultipleChoiceMode()
        } else {
            invalidateViewInNormalMode()
        }
    }

    private fun initBottomPanelView(rootView: View) {
        mBottomPanelView = rootView.findViewById(R.id.buttonPanel)
        mBottomPanelView?.run {
            val deleteButton = findViewById<Button>(R.id.delete)
            deleteButton.setOnClickListener {
                val bookInfoList = mBookInfoAdapter!!.selectedItems
                mHandler.post { mCloudDBZoneWrapper.deleteBookInfos(bookInfoList) }
                invalidateViewInNormalMode()
            }
            val cancelButton = findViewById<Button>(R.id.cancel)
            cancelButton.setOnClickListener { invalidateViewInNormalMode() }
        }
    }

    private fun invalidateViewInMultipleChoiceMode() {
        mHeaderView!!.findViewById<View>(R.id.checkbox).visibility = View.INVISIBLE
        mBottomPanelView!!.visibility = View.VISIBLE
        mActivity!!.hideNavigationBar()
        val menuSize = mOptionMenu!!.size()
        for (i in 0 until menuSize) {
            val menuItem = mOptionMenu!!.getItem(i)
            if (menuItem.itemId == R.id.select_all) {
                menuItem.isVisible = true
                menuItem.setTitle(R.string.select_all)
            } else {
                menuItem.isVisible = false
            }
        }
    }

    private fun invalidateViewInNormalMode() {
        mHeaderView!!.findViewById<View>(R.id.checkbox).visibility = View.GONE
        mBottomPanelView!!.visibility = View.GONE
        mActivity!!.showNavigationBar()
        mBookInfoAdapter!!.isMultipleMode = false
        val menuSize = mOptionMenu!!.size()
        for (i in 0 until menuSize) {
            val menuItem = mOptionMenu!!.getItem(i)
            when (menuItem.itemId) {
                R.id.select_all -> {
                    menuItem.isVisible = false
                }
                R.id.add_button -> {
                    menuItem.isVisible = !mInSearchMode
                }
                R.id.show_all -> {
                    menuItem.isVisible = mInSearchMode
                }
                else -> {
                    menuItem.isVisible = true
                }
            }
        }
    }

    private fun updateSelectAllMenuHint(menuItem: MenuItem) {
        if (mBookInfoAdapter!!.isAllSelected) {
            menuItem.setTitle(R.string.select_none)
        } else {
            menuItem.setTitle(R.string.select_all)
        }
    }

    private fun toggleShowAllState(inSearchMode: Boolean) {
        mInSearchMode = inSearchMode
        invalidateViewInNormalMode()
        mOptionMenu!!.findItem(R.id.show_all).isVisible = mInSearchMode
    }


    override fun onSubscribe(bookInfoList: List<BookInfo>?) {
        if (!mInSearchMode) {
            mHandler.post {
                if (bookInfoList == null || bookInfoList.isEmpty()) {
                    Toast.makeText(activity, R.string.empty_book_list, Toast.LENGTH_SHORT).show()
                }
                mBookInfoAdapter!!.addBooks(bookInfoList)
            }
        }
    }

    override fun onDelete(bookInfoList: List<BookInfo?>?) {
        mHandler.post { mBookInfoAdapter!!.deleteBooks(bookInfoList) }
    }

    override fun updateUiOnError(errorMessage: String?) {
        // dummy
    }

    private fun updateSortState(field: String, view: View) {
        cleanSortState()
        mSortState.field = field
        val newDownArrowView = view.findViewById<ImageView>(R.id.arrow_down)
        val newUpArrowView = view.findViewById<ImageView>(R.id.arrow_up)
        if (mSortState.state == SortState.State.UP) {
            mSortState.state = SortState.State.DOWN
            newDownArrowView.setImageResource(R.drawable.arrow_down_selected)
        } else {
            newUpArrowView.setImageResource(R.drawable.arrow_up_selected)
            mSortState.state = SortState.State.UP
        }
        mSortState.downArrowView = newDownArrowView
        mSortState.upArrowView = newUpArrowView
    }

    private fun cleanSortState() {
        mSortState.field = null
        if (mSortState.upArrowView != null) {
            mSortState.upArrowView!!.setImageResource(R.drawable.arrow_up)
        }
        if (mSortState.downArrowView != null) {
            mSortState.downArrowView!!.setImageResource(R.drawable.arrow_down)
        }
    }

    private fun queryWithOrder() {
        invalidateViewInNormalMode()
        val query = mQueryInfo.generalQuery();
        if (mSortState.state == SortState.State.UP) {
            query?.orderByAsc(mSortState.field!!)
        } else {
            query?.orderByDesc(mSortState.field!!)
        }
        mHandler.post { mCloudDBZoneWrapper.queryBooks(query!!) }
    }

    override fun onLogin(showLoginUserInfo: Boolean, signInResult: SignInResult?) {
        mHandler.postDelayed({
            mCloudDBZoneWrapper.addCallBacks(this@HomePageFragment)
            mCloudDBZoneWrapper.createObjectType()
            mCloudDBZoneWrapper.openCloudDBZoneV2()
        }, 500)
    }

    override fun onLogOut(showLoginUserInfo: Boolean) {
        Toast.makeText(mActivity!!.applicationContext, "Sign in from agc failed", Toast.LENGTH_SHORT).show()
    }

    override fun onStorageLocationChanged() {
        //Should create objet type and open CloudDBZone after change storage location
        mCloudDBZoneWrapper.setStorageLocation(this.context)
        mCloudDBZoneWrapper.createObjectType()
        mCloudDBZoneWrapper.openCloudDBZoneV2()
    }

    private class SortState {
        var field: String? = null
        var upArrowView: ImageView? = null
        var downArrowView: ImageView? = null
        var state: State? = null

        enum class State {
            UP, DOWN
        }
    }

    private class MyHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // dummy
        }
    }

    private class BookInfoAdapter(context: Context?) : BaseAdapter() {
        private val mBookList: MutableList<BookInfo> = ArrayList()
        private val mCheckState = SparseBooleanArray()
        private val mInflater = LayoutInflater.from(context)
        private var mIsMultipleMode = false
        fun addBooks(bookInfoList: List<BookInfo>?) {
            mBookList.clear()
            mBookList.addAll(bookInfoList!!)
            notifyDataSetChanged()
        }

        fun deleteBooks(bookInfoList: List<BookInfo?>?) {
            if (bookInfoList != null) {
                for (bookInfo in bookInfoList) {
                    mBookList.remove(bookInfo)
                }
            }
            notifyDataSetChanged()
        }

        fun check(position: Int) {
            if (mCheckState[position]) {
                mCheckState.delete(position)
            } else {
                mCheckState.put(position, true)
            }
            notifyDataSetChanged()
        }

        fun onSelectAllMenuClicked() {
            // Current state is all elements selected, so switch the state to none selected
            if (mCheckState.size() == count) {
                mCheckState.clear()
            } else {
                mCheckState.clear()
                for (i in 0 until count) {
                    mCheckState.put(i, true)
                }
            }
            notifyDataSetChanged()
        }

        val isAllSelected: Boolean
            get() = mCheckState.size() == mBookList.size
        val selectedItems: List<BookInfo>
            get() {
                val bookInfoList: MutableList<BookInfo> = ArrayList()
                val selectCount = mCheckState.size()
                for (i in 0 until selectCount) {
                    bookInfoList.add(mBookList[mCheckState.keyAt(i)])
                }
                return bookInfoList
            }
        var isMultipleMode: Boolean
            get() = mIsMultipleMode
            set(isMultipleMode) {
                mIsMultipleMode = isMultipleMode
                if (!isMultipleMode) {
                    mCheckState.clear()
                }
                notifyDataSetChanged()
            }

        override fun getCount(): Int {
            return mBookList.size
        }

        override fun getItem(position: Int): Any {
            return mBookList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val localView: View
            val viewHolder: ViewHolder
            if (convertView == null) {
                viewHolder = ViewHolder()
                localView = mInflater.inflate(R.layout.book_list_item, null)
                viewHolder.authorView = localView.findViewById(R.id.author)
                viewHolder.bookNameView = localView.findViewById(R.id.book_name)
                viewHolder.priceView = localView.findViewById(R.id.price)
                viewHolder.publisherView = localView.findViewById(R.id.publisher)
                viewHolder.publishTimeView = localView.findViewById(R.id.publish_time)
                viewHolder.checkBox = localView.findViewById(R.id.checkbox)
                localView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
                localView = convertView
            }
            val bookInfo = mBookList[position]
            viewHolder.authorView!!.text = bookInfo.author
            viewHolder.bookNameView!!.text = bookInfo.bookName
            viewHolder.priceView!!.text = String.format(Locale.getDefault(), "%.2f", bookInfo.price)
            viewHolder.publisherView!!.text = bookInfo.publisher
            viewHolder.publishTimeView!!.text = DateUtils.formatDate(bookInfo.publishTime)
            if (mIsMultipleMode) {
                viewHolder.checkBox!!.visibility = View.VISIBLE
            } else {
                viewHolder.checkBox!!.visibility = View.GONE
            }
            viewHolder.checkBox!!.isChecked = mCheckState[position, false]
            return localView
        }
    }

    private class ViewHolder {
        var checkBox: CheckBox? = null
        var bookNameView: TextView? = null
        var authorView: TextView? = null
        var priceView: TextView? = null
        var publisherView: TextView? = null
        var publishTimeView: TextView? = null
    }

    private class QueryInfo {
        var bookName: String? = ""
        var bookNameOr: String? = ""
        var author: String? = ""
        var authorOr: String? = ""
        var lowestPrice = 0.0
        var highestPrice = 0.0
        var showCount = 0

        fun clean() {
            bookName = ""
            lowestPrice = 0.0
            highestPrice = 0.0
            showCount = 0
        }

        fun generalQuery(): CloudDBZoneQuery<BookInfo>? {
            val query = CloudDBZoneQuery.where(BookInfo::class.java)
            jointFieldWithOr(query, bookNameOr, bookName, BookEditFields.BOOK_NAME)
            jointFieldWithOr(query, authorOr, author, BookEditFields.AUTHOR)
            query.greaterThanOrEqualTo(BookEditFields.PRICE, lowestPrice)
            if (lowestPrice != highestPrice || lowestPrice != 0.0) {
                query.lessThanOrEqualTo(BookEditFields.PRICE, highestPrice)
            }
            if (showCount > 0) {
                query.limit(showCount)
            }
            return query
        }

        fun jointFieldWithOr(query: CloudDBZoneQuery<BookInfo?>, fieldOrValue: String?, fieldValue: String?, fieldName: String?) {
            if (TextUtils.isEmpty(fieldValue) && !TextUtils.isEmpty(fieldOrValue)) {
                query.contains(fieldName!!, fieldOrValue!!)
                return
            }
            if (!TextUtils.isEmpty(fieldOrValue)) {
                query.beginGroup()
            }
            if (!TextUtils.isEmpty(fieldValue)) {
                query.contains(fieldName!!, fieldValue!!)
            }
            if (!TextUtils.isEmpty(fieldOrValue)) {
                query.or().contains(fieldName!!, fieldOrValue!!).endGroup()
            }
        }
    }

    companion object {
        // Request code for clicking search menu
        private const val REQUEST_SEARCH = 1

        // Request code for clicking add menu
        private const val REQUEST_ADD = 2
        fun newInstance(): Fragment {
            return HomePageFragment()
        }
    }

    override fun onAddOrQuery(bookInfoList: List<BookInfo>) {
        mHandler.post {
            // Object is sorted by default encoding, so we need to resort it by pinyin for Chinese
            if (mSortState.state != null && (BookEditFields.BOOK_NAME == mSortState.field
                            || BookEditFields.AUTHOR == mSortState.field
                            || BookEditFields.PUBLISHER == mSortState.field)) {
                sort(bookInfoList) { o1: BookInfo, o2: BookInfo ->
                    val result: Int
                    result = when (mSortState.field) {
                        BookEditFields.BOOK_NAME -> {
                            o1.bookName.compareTo(o2.bookName)
                        }
                        BookEditFields.AUTHOR -> {
                            o1.author.compareTo(o2.author)
                        }
                        else -> {
                            o1.publisher.compareTo(o2.publisher)
                        }
                    }
                    if (mSortState.state == SortState.State.UP) {
                        return@sort result
                    } else {
                        return@sort -result
                    }
                }
            }
            mBookInfoAdapter!!.addBooks(bookInfoList)
        }
    }

}