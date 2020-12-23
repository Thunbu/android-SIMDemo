package com.sammbo.imdemo.data.repository;

import com.geely.imsdk.client.bean.session.SIMSession;
import com.geely.imsdk.client.manager.system.SIMManager;
import com.sammbo.imdemo.data.Global;
import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.data.http.service.AppService;
import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.sdk.SDKManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;

public class AppRepository extends BaseModel {
    private volatile static AppRepository INSTANCE = null;
    private AppService appService;

    private AppRepository() {
        appService =  RetrofitClient.getInstance().create(AppService.class);
    }

    public static AppRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppRepository();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<SBaseResponse<String>> login(String account, int terminal) {
        return appService.login(account, terminal);
    }

    public void saveAccount(String account) {
        Global.setAccount(account);
    }

    public void loginIM(String account, String userSign) {
        SDKManager.getInstance().loginIM(account, userSign);
    }
}
