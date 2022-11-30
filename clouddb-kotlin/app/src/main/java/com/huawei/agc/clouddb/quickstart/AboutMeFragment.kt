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

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.huawei.agc.clouddb.quickstart.model.LoginHelper.OnLoginEventCallBack
import com.huawei.agc.clouddb.quickstart.model.StorageLocationHelper
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.auth.SignInResult
import java.util.*

class AboutMeFragment : Fragment(), OnLoginEventCallBack {
    private val TAG : String? = AboutMeFragment::class.simpleName
    private var mHintLoginView: View? = null
    private var mLoginUserInfoView: View? = null
    private var mUserNameView: TextView? = null
    private var mAccountNameView: TextView? = null
    private var mLocationView : TextView? = null
    private var mActivity: MainActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity?
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity!!.loginHelper!!.addLoginCallBack(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_about_me, container, false)
        initLoginView(rootView)
        initUserDetailView(rootView)
        return rootView
    }

    private fun initLoginView(rootView: View) {
        mHintLoginView = rootView.findViewById(R.id.hint_login_card)
        mHintLoginView?.run {
            setOnClickListener { logIn() }
        }
    }

    private fun initUserDetailView(rootView: View) {
        mLoginUserInfoView = rootView.findViewById(R.id.login_user_info)
        mLoginUserInfoView?.run {
            val nickNameView = findViewById<View>(R.id.nick_name)
            val nickNameTitleView = findViewById<TextView>(R.id.title)
            nickNameTitleView.setText(R.string.nick_name)
            mUserNameView = nickNameView.findViewById(R.id.details)
            val accountView = findViewById<View>(R.id.account)
            val accountNameTitleView = accountView.findViewById<TextView>(R.id.title)
            accountNameTitleView.setText(R.string.account)
            mAccountNameView = accountView.findViewById(R.id.details)
            val passwordView = findViewById<View>(R.id.password)
            val passwordTitleView = passwordView.findViewById<TextView>(R.id.title)
            passwordTitleView.setText(R.string.password)
            val passwordDetailView = passwordView.findViewById<TextView>(R.id.details)
            passwordDetailView.text = "*****"
            val storageLocaltionView =
                mLoginUserInfoView!!.findViewById<View>(R.id.storage_location)
            val locationTitleView = storageLocaltionView.findViewById<TextView>(R.id.title)
            locationTitleView.setText(R.string.storage_location)
            val locationDetailView = storageLocaltionView.findViewById<TextView>(R.id.details)
            locationDetailView.setText(CloudDBQuickStartApplication.regionRoutePolicy.routeName)
            locationDetailView.setOnClickListener { changeStorageLocation() }
            mLocationView = locationDetailView
            val logoutView = findViewById<View>(R.id.logout)
            logoutView.setOnClickListener { logOut() }
        }
    }

    private fun logIn() {
        mActivity!!.loginHelper!!.login()
    }

    private fun logOut() {
        mActivity!!.loginHelper!!.logOut()
        onLogOut(false)
    }

    private fun changeStorageLocation() {
        val currentPolicy: AGCRoutePolicy = CloudDBQuickStartApplication.regionRoutePolicy
        var targetPolicy = AGCRoutePolicy.SINGAPORE
        if (currentPolicy === AGCRoutePolicy.SINGAPORE) {
            targetPolicy = AGCRoutePolicy.CHINA
        }
        var message =
            Objects.requireNonNull(activity)?.resources?.getString(R.string.change_storage_location_message)
        message += targetPolicy.routeName + "?"
        val finalTargetPolicy = targetPolicy
        val dialog = AlertDialog.Builder(activity)
            .setMessage(message).setTitle(R.string.change_storage_location_title)
            .setPositiveButton(R.string.ok) { dialog1, which ->
                val locationHelper = mActivity!!.storageLocationHelper
                // Change storage location as you wish, need open multi-storage service first
                // As demo, here will change to Singapore.
                locationHelper!!.changeLocation(finalTargetPolicy)
                mLocationView?.text = CloudDBQuickStartApplication.regionRoutePolicy.routeName
            }.setNegativeButton(R.string.cancel) { dialog1, which ->
                Log.e(TAG, "Cancel change storage location")
            }.create()
        dialog.show()
    }

    override fun onLogin(showLoginUserInfo: Boolean, signInResult: SignInResult?) {
        if (showLoginUserInfo) {
            mHintLoginView!!.visibility = View.GONE
            mLoginUserInfoView!!.visibility = View.VISIBLE
            if (signInResult != null) {
                val displayName = signInResult.user.displayName
                mUserNameView!!.text = if (TextUtils.isEmpty(displayName)) "null" else displayName
                mAccountNameView!!.text = if (TextUtils.isEmpty(displayName)) "null" else displayName
            }
        } else {
            mHintLoginView!!.visibility = View.VISIBLE
            mLoginUserInfoView!!.visibility = View.GONE
        }
    }

    override fun onLogOut(showLoginUserInfo: Boolean) {
        if (showLoginUserInfo) {
            mHintLoginView!!.visibility = View.GONE
            mLoginUserInfoView!!.visibility = View.VISIBLE
        } else {
            mHintLoginView!!.visibility = View.VISIBLE
            mLoginUserInfoView!!.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): Fragment {
            return AboutMeFragment()
        }
    }
}
