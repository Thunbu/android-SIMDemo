package com.sammbo.imdemo.sdk;

import android.text.TextUtils;
import android.util.Log;

import com.geely.imsdk.client.bean.message.SIMMessage;
import com.geely.imsdk.client.bean.message.SIMMsgType;
import com.geely.imsdk.client.bean.message.SIMSecType;
import com.geely.imsdk.client.bean.message.SIMSessionType;
import com.geely.imsdk.client.bean.message.elem.image.SIMImage;
import com.geely.imsdk.client.bean.message.elem.image.SIMImageElem;
import com.geely.imsdk.client.bean.message.elem.image.SIMImageFormat;
import com.geely.imsdk.client.bean.message.elem.image.SIMImageType;
import com.geely.imsdk.client.bean.message.elem.text.SIMTextElem;
import com.geely.imsdk.client.bean.message.elem.video.SIMVideoElem;
import com.geely.imsdk.client.bean.message.elem.video.SIMVideoFormat;
import com.geely.imsdk.client.bean.message.push.SIMOfflinePushInfo;
import com.geely.imsdk.client.config.SDKConfig;
import com.geely.imsdk.client.listener.SIMConnectListener;
import com.geely.imsdk.client.listener.SIMValueCallBack;
import com.geely.imsdk.client.manager.message.send.SIMMessageManager;
import com.geely.imsdk.client.manager.system.SIMManager;
import com.sammbo.imdemo.app.SApplication;
import com.sammbo.imdemo.entity.ImageInfo;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.VideoInfo;
import com.sammbo.imdemo.entity.busevent.EventConnectState;

import me.goldze.mvvmhabit.bus.RxBus;

public class SDKManager {
    public static final String TAG = "SDKManager";

    private static SDKManager INSTANCE = new SDKManager();
    private SDKManager(){}
    public static SDKManager getInstance() {
        return INSTANCE;
    }

    public void loginIM(String userId, String userSig) {
        // 判断SDK是否已经初始化
        if (!SIMManager.getInstance().isInitialed()) {
            //初始化SDK
            SIMManager.getInstance().init(SApplication.getInstance(), "1000000217", new SDKConfig.Builder()
                    .setHttpServer("https://sdktest-gateway.sammbo.com:18777/")
                    .setTcpServerHost("wss://sdktest-websocket.sammbo.com")
                    .setTcpServerPort(18326)
                    .create());
        }
        // 已经初始化成功，直接进行注册
        SIMManager.getInstance().setSIMListener(new ChatListener());
        SIMManager.getInstance().setSIMConnectListener(connectListener);
        // 登录
        SIMManager.getInstance().login(userId, userSig, null);
    }

    private SIMConnectListener connectListener = (state, error) -> {
        Log.i(TAG, "connect state change : "  + state);
        RxBus.getDefault().post(EventConnectState.builder().state(state).result(error).build());
    };

    public MessageEntity sendMessage(MessageEntity messageEntity) {
        Log.i(TAG, "sendMessage..." + messageEntity.toString());
        SIMMessage simMessage = convertMessage(messageEntity);
        messageEntity.setMessageId(simMessage.getMsgId());
        SIMMessageManager.getInstance().sendMessage(simMessage, new SIMValueCallBack<SIMMessage>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(SIMMessage result) {

            }
        });
        return messageEntity;
    }

    private SIMMessage convertMessage(MessageEntity messageEntity) {
        SIMMessage simMessage = new SIMMessage();
        switch (messageEntity.getMsgType()) {
            case MessageEntity.TYPE_IMG:
                simMessage.setMsgType(SIMMsgType.IMAGE);
                ImageInfo imageInfo = (ImageInfo)messageEntity.getData();
                SIMImageElem imageElem = new SIMImageElem();
                SIMImage origin = new SIMImage(),
                        large = new SIMImage(),
                        thumb = new SIMImage();
                fillImage(origin, imageInfo.getOrigin());
                fillImage(large, imageInfo.getLarge());
                fillImage(thumb, imageInfo.getThumb());
                origin.setType(SIMImageType.Original);
                large.setType(SIMImageType.Large);
                thumb.setType(SIMImageType.Thumb);
                imageElem.addImage(origin);
                imageElem.addImage(large);
                imageElem.addImage(thumb);
                imageElem.setImageFormat(SIMImageFormat.getType(imageInfo.getType()));
                simMessage.setElem(imageElem);
                break;
            case MessageEntity.TYPE_VIDEO:
                simMessage.setMsgType(SIMMsgType.VIDEO);
                VideoInfo videoInfo = (VideoInfo)messageEntity.getData();
                SIMVideoElem simVideoElem = new SIMVideoElem();
                simVideoElem.setVideoUrl(videoInfo.getVideoUrl());
                simVideoElem.setVideoSize(videoInfo.getVideoSize());
                simVideoElem.setVideoFormat(SIMVideoFormat.getType(videoInfo.getVideoType()));
                simVideoElem.setDuration(videoInfo.getDuration());
                simVideoElem.setCoverUrl(videoInfo.getCoverUrl());
                simVideoElem.setCoverHeight(videoInfo.getCoverHeight());
                simVideoElem.setCoverWidth(videoInfo.getCoverWidth());
                simVideoElem.setCoverFormat(SIMImageFormat.getType(videoInfo.getCoverType()));
                simVideoElem.setCoverSize(videoInfo.getCoverSize());
                simMessage.setElem(simVideoElem);
                break;
            case MessageEntity.TYPE_TXT:
            default:
                String body = (String)messageEntity.getData();
                simMessage.setMsgType(SIMMsgType.TXT);
                SIMTextElem elem = new SIMTextElem();
                elem.setText(body);
                simMessage.setElem(elem);
                break;
        }
        simMessage.setSessionId(messageEntity.getSessionId());
        simMessage.setReceiver(messageEntity.getSessionId());
        simMessage.setSender(messageEntity.getFrom());
        simMessage.setPacketTime(messageEntity.getCreateTime());
        simMessage.setChatType(SIMSessionType.getType(getSessionType(messageEntity.getSessionId())));
        simMessage.setSecurityType(SIMSecType.NORMAL);
        return simMessage;
    }

    private void fillImage(SIMImage simImage, ImageInfo.ImageExt imageExt) {
        simImage.setUrl(imageExt.url);
        simImage.setHeight(imageExt.height);
        simImage.setWidth(imageExt.width);
        simImage.setSize(imageExt.size);
    }

    private int getSessionType(String sessionId) {
        if (!TextUtils.isEmpty(sessionId) && !sessionId.toUpperCase().startsWith("A")) {
            return 1; // GROUP
        } else {
            return 0; // p2p
        }
    }
}
