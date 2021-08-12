package com.sammbo.imdemo.entity;

import androidx.databinding.BaseObservable;

import com.geely.imsdk.client.bean.message.SIMMessage;
import com.geely.imsdk.client.bean.message.elem.file.SIMFileElem;
import com.geely.imsdk.client.bean.message.elem.group.SIMGroupTipsElem;
import com.geely.imsdk.client.bean.message.elem.image.SIMImage;
import com.geely.imsdk.client.bean.message.elem.image.SIMImageElem;
import com.geely.imsdk.client.bean.message.elem.image.SIMImageType;
import com.geely.imsdk.client.bean.message.elem.text.SIMTextElem;
import com.geely.imsdk.client.bean.message.elem.video.SIMVideoElem;
import com.sammbo.imdemo.data.Global;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageEntity<T> extends BaseObservable {
    public static final int TYPE_TXT = 0;
    public static final int TYPE_IMG = 1;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_FILE = 4;
    public static final int TYPE_DELETE = 5;

    public static final int BOX_OUT = 0;
    public static final int BOX_IN = 1;

    private String messageId;
    private String sessionId;
    private int msgType;
    private int boxType;
    private long createTime;
    private String from;
    private String to;
    private T data;
    private String serialId;
    private boolean priDelFlag;
    private boolean sign;

    public static MessageEntity get(SIMMessage simMessage) {
        int boxType = simMessage.getSender().equals(Global.getAccount()) ? BOX_OUT : BOX_IN;
        MessageEntity messageEntity = MessageEntity.builder()
                .messageId(simMessage.getMsgId())
                .sessionId(simMessage.getSessionId())
                .msgType(simMessage.getMsgType().value())
                .boxType(boxType)
                .createTime(simMessage.getPacketTime())
                .from(simMessage.getSender())
                .to(simMessage.getReceiver())
                .serialId(simMessage.getSerialId())
                .priDelFlag(simMessage.isPriDelFlag())
                .sign(simMessage.isSign())
                .build();
        switch (simMessage.getMsgType()) {
            case IMAGE:
                SIMImageElem simImageElem = (SIMImageElem) simMessage.getElem();
                ImageInfo.ImageExt originExt = null,
                        largeExt = null,
                        thumbExt = null;
                for (SIMImage image : simImageElem.getImageList()) {
                    if (image.getType() == SIMImageType.Original) {
                        // 原图
                        originExt = new ImageInfo.ImageExt(image.getUrl(), image.getHeight(), image.getWidth(), image.getSize());
                    } else if (image.getType() == SIMImageType.Large) {
                        // 大图
                        largeExt = new ImageInfo.ImageExt(image.getUrl(), image.getHeight(), image.getWidth(), image.getSize());
                    } else {
                        // 缩略图
                        thumbExt = new ImageInfo.ImageExt(image.getUrl(), image.getHeight(), image.getWidth(), image.getSize());
                    }
                }
                messageEntity.setData(ImageInfo.builder().origin(originExt).large(largeExt).thumb(thumbExt).build());
                break;
            case VIDEO:
                SIMVideoElem simVideoElem = (SIMVideoElem) simMessage.getElem();
                messageEntity.setData(VideoInfo.builder()
                        .videoUrl(simVideoElem.getVideoUrl())
                        .videoType(simVideoElem.getVideoFormat() == null ? VideoInfo.VIDEO_TYPE_UNKNOWN : simVideoElem.getVideoFormat().value())
                        .videoSize(simVideoElem.getVideoSize())
                        .duration(simVideoElem.getDuration())
                        .coverUrl(simVideoElem.getCoverUrl())
                        .coverHeight(simVideoElem.getCoverHeight())
                        .coverWidth(simVideoElem.getCoverWidth())
                        .coverSize(simVideoElem.getCoverSize())
                        .coverType(simVideoElem.getCoverFormat() == null ? VideoInfo.COVER_TYPE_UNKNOWN : simVideoElem.getCoverFormat().value())
                        .build());
                break;
            case GROUP_CREATE:
                SIMGroupTipsElem simGroupTipsElem = (SIMGroupTipsElem) simMessage.getElem();
                messageEntity.setData("我创建了群");
                break;
            case TXT:
                SIMTextElem textElem = (SIMTextElem) simMessage.getElem();
                messageEntity.setData(textElem.getText());
                break;
            case FILE:
                SIMFileElem fileElem = (SIMFileElem) simMessage.getElem();
                FileInfo fileInfo = FileInfo.builder().filename(fileElem.getFilename()).size(fileElem.getSize()).downUrl(fileElem.getDownUrl()).build();
                messageEntity.setData(fileInfo);
                break;
        }
        return messageEntity;
    }
}
