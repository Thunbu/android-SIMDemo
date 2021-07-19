package com.sammbo.imdemo.sdk.converter;

import android.text.TextUtils;

import com.geely.imsdk.client.bean.message.SIMMessage;
import com.geely.imsdk.client.bean.message.SIMSecType;
import com.geely.imsdk.client.bean.message.SIMSessionType;
import com.geely.imsdk.client.bean.message.push.SIMOfflinePushInfo;
import com.sammbo.imdemo.data.Global;
import com.sammbo.imdemo.entity.MessageEntity;

/**
 * @date 2020/12/11
 * description：
 */
public interface IAdapter {

    SIMMessage to(MessageEntity messageEntity);
    MessageEntity from(SIMMessage simMessage);

    default void convert(SIMMessage from, MessageEntity to){

    }

    default void convert(MessageEntity from, SIMMessage to){
        to.setSessionId(from.getSessionId());
        to.setReceiver(from.getSessionId());
        to.setSender(from.getFrom());
        to.setPacketTime(from.getCreateTime());
        to.setChatType(SIMSessionType.getType(getSessionType(from.getSessionId())));
        to.setSecurityType(SIMSecType.NORMAL);
    }

    default int getSessionType(String sessionId) {
        if (!TextUtils.isEmpty(sessionId) && !sessionId.toUpperCase().startsWith("A")) {
            return 1; // GROUP
        } else {
            return 0; // p2p
        }
    }

    default MessageEntity createMessage(String to) {
        return MessageEntity.builder()
                .sessionId(to)
                .to(to)
                .createTime(System.currentTimeMillis())
                .from(Global.getAccount())
                .boxType(MessageEntity.BOX_OUT)
                //todo send state
                .build();
    }
}
