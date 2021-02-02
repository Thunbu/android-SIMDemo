package com.sammbo.imdemo.entity;

import androidx.databinding.BaseObservable;

import com.geely.imsdk.client.bean.session.SIMSession;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.geely.imsdk.client.constans.Constants.SESSION_DISTURB;
import static com.geely.imsdk.client.constans.Constants.SESSION_NOT_DISTURB;

@Data
@Builder
@EqualsAndHashCode(callSuper = false, of = "sessionId")
public class SessionEntity extends BaseObservable {
    public static final int TOP_Y = SIMSession.TOP;
    public static final int TOP_N = SIMSession.TOP_NOT;
    public static final int NOTDISTURB_Y = SESSION_NOT_DISTURB;
    public static final int NOTDISTURB_N = SESSION_DISTURB;

    private String sessionId;
    private String sessionName;
    private String avatar;
    private int sessionType;
    private String snippet;
    private int unreadCount;
    private long dateTime;
    private int top;
    private int notDisturb;

    public static SessionEntity get(SIMSession simSession) {
        String snippet = "";
        long dateTime = 0;
        if (simSession.getLastSIMMsg() != null) {
            MessageEntity messageEntity = MessageEntity.get(simSession.getLastSIMMsg());
            dateTime = messageEntity.getCreateTime();
            switch (messageEntity.getMsgType()) {
                case MessageEntity.TYPE_TXT:
                    snippet = (String) messageEntity.getData();
                    break;
                case MessageEntity.TYPE_IMG:
                    snippet = "[图片]";
                    break;
                case MessageEntity.TYPE_VIDEO:
                    snippet = "[视频]";
                    break;
                case MessageEntity.TYPE_FILE:
                    snippet = "[文件]";
                    break;
                default:
                    break;
            }
        }
        return SessionEntity.builder()
                .sessionId(simSession.getSessionId())
                .sessionName(simSession.getSessionName())
                .avatar(simSession.getAvatar())
                .sessionType(simSession.getSessionType())
                .snippet(snippet)
                .unreadCount(simSession.getMessageCount())
                .dateTime(dateTime)
                .top(simSession.getIsTop())
                .notDisturb(simSession.getNotDisturb())
                .build();
    }

    public void updateSnippet(MessageEntity messageEntity) {
        switch (messageEntity.getMsgType()) {
            case MessageEntity.TYPE_TXT:
                snippet = (String) messageEntity.getData();
                break;
            case MessageEntity.TYPE_IMG:
                snippet = "[图片]";
                break;
            case MessageEntity.TYPE_VIDEO:
                snippet = "[视频]";
                break;
            default:
                break;
        }
    }
}
