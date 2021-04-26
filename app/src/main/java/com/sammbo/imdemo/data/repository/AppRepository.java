package com.sammbo.imdemo.data.repository;

import com.sammbo.imdemo.data.Global;
import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.data.http.service.AppService;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.login.bean.LoginReponse;
import com.sammbo.imdemo.ui.login.bean.UserInfo;

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

//    public Observable<SBaseResponse<LoginReponse>> login(String account, int terminal) {
//        return appService.login(account, terminal);
//    }

    public Observable<SBaseResponse<String>> login(String userid, int terminal) {
        return appService.login( userid, terminal);
    }

    public void saveAccount(String account) {
        Global.setAccount(account);
    }

    public String getAccount() {
        return Global.getAccount();
    }

    public void loginIM(String account, String userSign) {
        SDKManager.getInstance().loginIM(account, userSign);
    }

    public Observable<SBaseResponse<UserInfo>> register(String account,String name) {
        return appService.register( account, name, "https://bossfs.sammbo.com/0/1/head/penguin.png");
    }
}
