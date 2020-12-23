package com.sammbo.imdemo.sdk;

import com.geely.imsdk.client.bean.custom.SIMCustomTip;
import com.geely.imsdk.client.bean.friend.tip.SIMSNSChangeInfo;
import com.geely.imsdk.client.bean.message.SIMMessage;
import com.geely.imsdk.client.bean.offline.SIMRecentMsgPage;
import com.geely.imsdk.client.bean.session.SIMSessionTip;
import com.geely.imsdk.client.bean.system.SIMLoginTip;
import com.geely.imsdk.client.bean.system.SIMOffLineTip;
import com.geely.imsdk.client.listener.SIMListener;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.busevent.EventConnectState;

import me.goldze.mvvmhabit.bus.RxBus;

public class ChatListener implements SIMListener {
    @Override
    public void onReceivedMessage(SIMMessage msg) {
        RxBus.getDefault().post(MessageEntity.get(msg));
    }

    @Override
    public void onReadSYNMessage(SIMMessage elem) {

    }

    @Override
    public void onReadTipMessage(SIMMessage elem) {

    }

    @Override
    public void onRevokeMessage(SIMMessage elem) {

    }

    @Override
    public void onDeleteMessage(SIMMessage elem) {

    }

    @Override
    public void onSignMessage(SIMMessage elem) {

    }

    @Override
    public void onReplyMessage(SIMMessage elem) {

    }

    @Override
    public void onReceiveSessionTips(SIMSessionTip tip) {

    }

    @Override
    public void onReceiveGroupTips(SIMMessage tips) {

    }

    @Override
    public void onReceiveSystemTips(SIMMessage tips) {

    }

    @Override
    public void onReceiveSNSMessage(SIMSNSChangeInfo changeInfo) {

    }

    @Override
    public void onReceiveCustomTip(SIMCustomTip customTip) {

    }

    @Override
    public void onReceiveRecentMessages(SIMRecentMsgPage page) {

    }

    @Override
    public void onReceiveLoginTips(SIMLoginTip tip) {

    }

    @Override
    public void onReceiveKickOff(SIMOffLineTip tip) {

    }
}
