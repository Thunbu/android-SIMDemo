package com.geely.libpush;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * 如果需要自己处理推送的逻辑,需要在MyPushManager设置
 * */
public class MyIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onReceiveServicePid(context, pid);
        }
    }

    // 处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        // 透传消息的处理
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onReceiveMessageData(context, msg);
        }
    }

    // 接收 cid
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        MyPushManager.getInstance().setCid(clientid);
        MyPushManager.getInstance().uploadDeviceId();
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onReceiveClientId(context, clientid);
        }
    }

    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onReceiveOnlineState(context, online);
        }
    }

    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onReceiveCommandResult(context, cmdMessage);
        }
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onNotificationMessageArrived(context, msg);
        }
    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
        if (MyPushManager.getInstance().getAppPushReceiver() != null) {
            MyPushManager.getInstance().getAppPushReceiver().onNotificationMessageClicked(context, msg);
        }
    }

}
