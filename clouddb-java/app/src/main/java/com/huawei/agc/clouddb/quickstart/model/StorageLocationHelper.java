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
