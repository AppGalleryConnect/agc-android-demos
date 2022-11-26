/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.agc.clouddb.quickstart.model

import com.huawei.agc.clouddb.quickstart.CloudDBQuickStartApplication
import com.huawei.agconnect.AGCRoutePolicy
import java.util.ArrayList

class StorageLocationHelper {
    private val mCallbacks: MutableList<StorageLocationChangeCallback> = ArrayList()

    fun changeLocation(routePolicy: AGCRoutePolicy?) {
        if (CloudDBQuickStartApplication.regionRoutePolicy === routePolicy) {
            return
        }
        if (routePolicy != null) {
            CloudDBQuickStartApplication.regionRoutePolicy = routePolicy
        }
        for (callback in mCallbacks) {
            callback.onStorageLocationChanged()
        }
    }

    fun addCallback(callback: StorageLocationChangeCallback) {
        mCallbacks.add(callback)
    }

    interface StorageLocationChangeCallback {
        fun onStorageLocationChanged()
    }
}