package com.sammbo.imdemo.data.repository;

import com.geely.imsdk.client.bean.session.SIMSession;
import com.geely.imsdk.client.manager.system.SIMManager;
import com.sammbo.imdemo.data.Global;
import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.data.http.service.AppService;
import com.sammbo.imdemo.data.http.service.SessionService;
import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.login.bean.UploadAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;

/**
 * @date 2020/11/2
 * description：
 */
public class SessionRepository extends BaseModel {

    private volatile static SessionRepository INSTANCE = null;
    private AppService appService;

    private SessionRepository(){
        appService =  RetrofitClient.getInstance().create(AppService.class);
    }

    public static SessionRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (SessionRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionRepository();
                }
            }
        }
        return INSTANCE;
    }


    public Observable<List<SessionEntity>> updateSessions() {
        return SIMManager.getInstance().getSessionManager().getSessionList(0, 0)
                .map(response -> {
                    if (response.isSuccess()) {
                        List<SIMSession> listSIMSessions = response.getData();
                        if (listSIMSessions != null && !listSIMSessions.isEmpty()) {
                            List<SessionEntity> listSession = new ArrayList<>();
                            for (SIMSession simSession : listSIMSessions) {
                                if(simSession.getLashSIMMsg() == null) continue;
                                listSession.add(SessionEntity.get(simSession));
                            }
                            Collections.sort(listSession, (sessionEntity, t1) -> sessionEntity.getDateTime()>t1.getDateTime()?-1:0);
                            return listSession;
                        }
                    }
                    return null;
                });
    }

    public void removeAccount(){
        Global.removeAccount();
    }

    public Observable<SBaseResponse<UploadAddress>> getUploadAddress(){
        return appService.getUploadHost();
    }
}
