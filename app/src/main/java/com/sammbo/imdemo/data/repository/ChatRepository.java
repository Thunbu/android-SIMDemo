package com.sammbo.imdemo.data.repository;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.geely.imsdk.client.bean.message.SIMMessage;
import com.geely.imsdk.client.bean.message.SIMSecType;
import com.geely.imsdk.client.bean.message.SIMSessionType;
import com.geely.imsdk.client.bean.message.elem.off.SIMOffDirect;
import com.geely.imsdk.client.bean.message.elem.off.SIMOffMsgParam;
import com.geely.imsdk.client.bean.message.elem.state.MessageState;
import com.geely.imsdk.client.bean.message.elem.state.MessageStateParam;
import com.geely.imsdk.client.listener.SIMValueCallBack;
import com.geely.imsdk.client.manager.message.send.SIMMessageManager;
import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.service.SessionService;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.sdk.SDKManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
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

    public Single<List<MessageEntity>> fetchHistoryMsg(String sessionId, String boundaryID,String msgId) {
        Single<List<MessageEntity>> single = Single.create(emitter -> {
            SIMOffMsgParam param = new SIMOffMsgParam();
            param.setRows(100);
            param.setDirect(SIMOffDirect.NEGATIVE);
            param.setMsgId(msgId);
            param.setSerialId(boundaryID);
            param.setSessionId(sessionId);
            param.setSecurityType(SIMSecType.NORMAL);
            param.setSessionType(SIMSessionType.getType(SDKManager.getInstance().getSessionType(sessionId)));
            SIMMessageManager.getInstance().getMessageListBySerialId(param, new SIMValueCallBack<List<SIMMessage>>() {
                @Override
                public void onError(int code, String desc) {
                    Log.i(TAG,"fetchHistoryMsg err:" + code + ";desc:" + desc);
                    emitter.onError(new Throwable("err:" + code + ";desc:" + desc));
                }

                @Override
                public void onSuccess(List<SIMMessage> result) {
                    Log.i(TAG,"fetchHistoryMsg success");
                    List<MessageEntity> list = new ArrayList<>();
                    if (!result.isEmpty()) {
                        for (SIMMessage simMessage : result) {
                            list.add(MessageEntity.get(simMessage));
                        }
                    }
                    emitter.onSuccess(list);
                }
            });
        });
        return single;
    }

    public Single<Map<String,Map<String, ArrayList<String>>>> fetchCdMsg() {
        Single<Map<String,Map<String, ArrayList<String>>>> single =Single.create(emitter -> {
            SIMMessageManager.getInstance().getCdMsgList(1628225213000l, new SIMValueCallBack<Map<String, Map<String, ArrayList<String>>>>() {
                @Override
                public void onError(int code, String desc) {
                    Log.i(TAG,"fetchCdMsg err:" + code + ";desc:" + desc);
                    emitter.onError(new Throwable("err:" + code + ";desc:" + desc));
                }

                @Override
                public void onSuccess(Map<String,Map<String, ArrayList<String>>> result) {
                    Log.i(TAG,"fetchCdMsg success");
                    emitter.onSuccess(result);
                }
            });
        });
        return single;
    }

    public Single<List<MessageState>> getMessagesStatus(String sessionId, String boundaryID){
        Single<List<MessageState>> single = Single.create(emitter -> {
            MessageStateParam param = new MessageStateParam();
            param.setSessionId(sessionId);
            param.setSecurityType(SIMSecType.NORMAL);
            param.setEndSerialId(boundaryID);
            long startid = (Long.valueOf(boundaryID) - 100);
            if(startid<0) startid = 0;
            param.setStartSerialId(startid+"");
            param.setSessionType(SIMSessionType.getType(SDKManager.getInstance().getSessionType(sessionId)));
            SIMMessageManager.getInstance().getMessagesStatus(param, new SIMValueCallBack<List<MessageState>>() {
                @Override
                public void onError(int code, String desc) {
                    Log.i(TAG,"fetchHistoryMsg err:" + code + ";desc:" + desc);
                    emitter.onError(new Throwable("err:" + code + ";desc:" + desc));
                }

                @Override
                public void onSuccess(List<MessageState> result) {
                    Log.i(TAG,"fetchHistoryMsg success");

                    emitter.onSuccess(result);
                }
            });
        });
        return single;
    }
}
