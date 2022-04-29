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