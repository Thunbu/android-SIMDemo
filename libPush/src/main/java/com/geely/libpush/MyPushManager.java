package com.geely.libpush;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.BuildConfig;
import com.igexin.sdk.PushManager;

public class MyPushManager {
    public static final String TAG = "MyPushManager";
    private String cid;
    private UploadDeviceListener uploadDeviceListener;
    private AppPushReceiver appPushReceiver;

    private MyPushManager() {
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setUploadDeviceListener(UploadDeviceListener uploadDeviceListener) {
        this.uploadDeviceListener = uploadDeviceListener;
    }

    public void setAppPushReceiver(AppPushReceiver appPushReceiver) {
        this.appPushReceiver = appPushReceiver;
    }

    public AppPushReceiver getAppPushReceiver() {
        return appPushReceiver;
    }

    private static class SingleonHolder {
        public static MyPushManager holder = new MyPushManager();
    }

    public static MyPushManager getInstance() {
        return SingleonHolder.holder;
    }

    public void init(Context context) {
        PushManager.getInstance().initialize(context);
        PushManager.getInstance().setDebugLogger(context, s -> {
            if (BuildConfig.DEBUG) Log.e(TAG, s);
        });
    }

    public interface UploadDeviceListener {
        void uploadDeviceId(String cid);
    }


    public void uploadDeviceId() {
        if (!TextUtils.isEmpty(cid) && uploadDeviceListener != null) {
            uploadDeviceListener.uploadDeviceId(cid);
        }
    }
}
