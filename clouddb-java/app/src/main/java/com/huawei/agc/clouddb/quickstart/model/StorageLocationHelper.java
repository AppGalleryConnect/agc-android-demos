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

package com.huawei.agc.clouddb.quickstart.model;

import com.huawei.agc.clouddb.quickstart.CloudDBQuickStartApplication;
import com.huawei.agconnect.AGCRoutePolicy;

import java.util.ArrayList;
import java.util.List;

public class StorageLocationHelper {

    private List<StorageLocationHelper.StorageLocationChangeCallback> mCallbacks = new ArrayList<>();

    public void changeLocation(AGCRoutePolicy routePolicy) {
        if (CloudDBQuickStartApplication.getRegionRoutePolicy() == routePolicy) {
            return;
        }
        CloudDBQuickStartApplication.setRegionRoutePolicy(routePolicy);
        for (StorageLocationChangeCallback callback : mCallbacks) {
            callback.onStorageLocationChanged();
        }
    }

    public void addCallback(StorageLocationHelper.StorageLocationChangeCallback callback) {
        mCallbacks.add(callback);
    }
    public interface StorageLocationChangeCallback {
        void onStorageLocationChanged();
    }
}
