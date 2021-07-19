package com.sammbo.imdemo.data.repository;

import android.util.Log;
import android.util.Pair;

import com.geely.imsdk.client.bean.message.SIMMessage;
import com.geely.imsdk.client.bean.message.SIMSecType;
import com.geely.imsdk.client.bean.message.SIMSessionType;
import com.geely.imsdk.client.bean.message.elem.off.SIMOffDirect;
import com.geely.imsdk.client.bean.message.elem.off.SIMOffMsgParam;
import com.geely.imsdk.client.listener.SIMValueCallBack;
import com.geely.imsdk.client.manager.message.send.SIMMessageManager;
import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.service.SessionService;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.sdk.SDKManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import me.goldze.mvvmhabit.base.BaseModel;

/**
 * @date 2020/11/4
 * description：
 */
public class ChatRepository extends BaseModel {
    public static final String TAG = "ChatRepository";
    private volatile static ChatRepository INSTANCE = null;
    private SessionService appService; // todo

    private ChatRepository() {
        // todo
        appService =  RetrofitClient.getInstance().create(SessionService.class);
    }

    public static ChatRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (ChatRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChatRepository();
                }
            }
        }
        return INSTANCE;
    }

    public Single<List<MessageEntity>> fetchHistoryMsg(String sessionId, String boundaryID) {
        Single<List<MessageEntity>> single = Single.create(emitter -> {
            SIMOffMsgParam param = new SIMOffMsgParam();
            param.setRows(100);
            param.setDirect(SIMOffDirect.NEGATIVE);
            param.setMsgId(boundaryID);
            param.setSessionId(sessionId);
            param.setSecurityType(SIMSecType.NORMAL);
            param.setSessionType(SIMSessionType.getType(SDKManager.getInstance().getSessionType(sessionId)));
            SIMMessageManager.getInstance().getOffMsgList(param, new SIMValueCallBack<Pair<List<SIMMessage>, List<SIMMessage>>>() {
                @Override
                public void onError(int code, String desc) {
                    Log.i(TAG,"fetchHistoryMsg err:" + code + ";desc:" + desc);
                    emitter.onError(new Throwable("err:" + code + ";desc:" + desc));
                }

                @Override
                public void onSuccess(Pair<List<SIMMessage>, List<SIMMessage>> result) {
                    Log.i(TAG,"fetchHistoryMsg success");
                    List<MessageEntity> list = new ArrayList<>();
                    if (!result.first.isEmpty()) {
                        for (SIMMessage simMessage : result.first) {
                            list.add(MessageEntity.get(simMessage));
                        }
                    }
                    emitter.onSuccess(list);
                }
            });
        });
        return single;
    }
}
