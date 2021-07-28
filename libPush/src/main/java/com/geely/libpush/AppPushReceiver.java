package com.geely.libpush;

import android.content.Context;

import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public interface AppPushReceiver {
    public static final String TAG = "AppPushReceiver";
    public void onReceiveServicePid(Context context, int pid) ;

    // 处理透传消息
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) ;

    // 接收 cid
    public void onReceiveClientId(Context context, String clientid) ;

    // cid 离线上线通知
    public void onReceiveOnlineState(Context context, boolean online) ;

    // 各种事件处理回执
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) ;

    // 通知到达，只有个推通道下发的通知会回调此方法
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) ;

    // 通知点击，只有个推通道下发的通知会回调此方法
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) ;
}
