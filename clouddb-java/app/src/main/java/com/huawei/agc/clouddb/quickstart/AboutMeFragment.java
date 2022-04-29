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

package com.huawei.agc.clouddb.quickstart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huawei.agc.clouddb.quickstart.model.LoginHelper;
import com.huawei.agc.clouddb.quickstart.model.StorageLocationHelper;
import com.huawei.agconnect.AGCRoutePolicy;
import com.huawei.agconnect.auth.SignInResult;

import java.util.Objects;

public class AboutMeFragment extends Fragment implements LoginHelper.OnLoginEventCallBack {
    private final static String TAG = AboutMeFragment.class.getSimpleName();

    private View mHintLoginView;

    private View mLoginUserInfoView;

    private TextView mUserNameView;

    private TextView mAccountNameView;

    private TextView mLocationView;

    private MainActivity mActivity;

    static Fragment newInstance() {
        return new AboutMeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.getLoginHelper().addLoginCallBack(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        initLoginView(rootView);
        initUserDetailView(rootView);
        return rootView;
    }

    private void initLoginView(View rootView) {
        mHintLoginView = rootView.findViewById(R.id.hint_login_card);
        mHintLoginView.setOnClickListener(v -> logIn());
    }

    private void initUserDetailView(View rootView) {
        mLoginUserInfoView = rootView.findViewById(R.id.login_user_info);
        View nickNameView = mLoginUserInfoView.findViewById(R.id.nick_name);
        TextView nickNameTitleView = mLoginUserInfoView.findViewById(R.id.title);
        nickNameTitleView.setText(R.string.nick_name);
        mUserNameView = nickNameView.findViewById(R.id.details);

        View accountView = mLoginUserInfoView.findViewById(R.id.account);
        TextView accountNameTitleView = accountView.findViewById(R.id.title);
        accountNameTitleView.setText(R.string.account);
        mAccountNameView = accountView.findViewById(R.id.details);

        View passwordView = mLoginUserInfoView.findViewById(R.id.password);
        TextView passwordTitleView = passwordView.findViewById(R.id.title);
        passwordTitleView.setText(R.string.password);
        TextView passwordDetailView = passwordView.findViewById(R.id.details);
        passwordDetailView.setText("*****");

        View storageLocaltionView = mLoginUserInfoView.findViewById(R.id.storage_location);
        TextView locationTitleView = storageLocaltionView.findViewById(R.id.title);
        locationTitleView.setText(R.string.storage_location);
        TextView locationDetailView = storageLocaltionView.findViewById(R.id.details);
        locationDetailView.setText(
            CloudDBQuickStartApplication.getRegionRoutePolicy().getRouteName());
        locationDetailView.setOnClickListener(v -> changeStorageLocation());
        mLocationView = locationDetailView;

        View logoutView = mLoginUserInfoView.findViewById(R.id.logout);
        logoutView.setOnClickListener(v -> logOut());
    }

    private void logIn() {
        mActivity.getLoginHelper().login();
    }

    private void logOut() {
        mActivity.getLoginHelper().logOut();
        onLogOut(false);
    }

    private void changeStorageLocation() {
        AGCRoutePolicy currentPolicy = CloudDBQuickStartApplication.getRegionRoutePolicy();
        AGCRoutePolicy targetPolicy = AGCRoutePolicy.SINGAPORE;
        if (currentPolicy == AGCRoutePolicy.SINGAPORE) {
            targetPolicy = AGCRoutePolicy.CHINA;
        }
        String message = Objects.requireNonNull(getActivity()).getResources().getString(R.string.change_storage_location_message);
        message += targetPolicy.getRouteName() + "?";
        AGCRoutePolicy finalTargetPolicy = targetPolicy;
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
            .setMessage(message).setTitle(R.string.change_storage_location_title)
            .setPositiveButton(R.string.ok, ((dialog1, which) -> {
                StorageLocationHelper locationHelper = mActivity.getStorageLocationHelper();
                // Change storage location as you wish, need open multi-storage service first
                // As demo, here will change to Singapore.
                locationHelper.changeLocation(finalTargetPolicy);
                mLocationView.setText(CloudDBQuickStartApplication.getRegionRoutePolicy().getRouteName());
            })).setNegativeButton(R.string.cancel, ((dialog1, which) -> {
                Log.e(TAG, "Cancel change storage location");
            })).create();
        dialog.show();
    }

    @Override
    public void onLogin(boolean showLoginUserInfo, SignInResult signInResult) {
        if (showLoginUserInfo) {
            mHintLoginView.setVisibility(View.GONE);
            mLoginUserInfoView.setVisibility(View.VISIBLE);
            String displayName = signInResult.getUser().getDisplayName();
            mUserNameView.setText(TextUtils.isEmpty(displayName) ? "null" : displayName);
            mAccountNameView.setText(TextUtils.isEmpty(displayName) ? "null" : displayName);
        } else {
            mHintLoginView.setVisibility(View.VISIBLE);
            mLoginUserInfoView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLogOut(boolean showLoginUserInfo) {
        if (showLoginUserInfo) {
            mHintLoginView.setVisibility(View.GONE);
            mLoginUserInfoView.setVisibility(View.VISIBLE);
        } else {
            mHintLoginView.setVisibility(View.VISIBLE);
            mLoginUserInfoView.setVisibility(View.GONE);
        }
    }
}
