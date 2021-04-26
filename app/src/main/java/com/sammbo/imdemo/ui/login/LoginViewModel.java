package com.sammbo.imdemo.ui.login;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.data.repository.AppRepository;
import com.sammbo.imdemo.entity.SpinnerItemData;
import com.sammbo.imdemo.sdk.EnvService;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.register.RegisterActivity;
import com.sammbo.imdemo.ui.tab.MainActivity;
import com.sammbo.imdemo.utils.SRxUtils;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.viewadapter.spinner.IKeyAndValue;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * @author xin.zhou4
 * @date 2020/10/30
 * email：xin.zhou4@geely.com
 * description：
 */
public class LoginViewModel extends BaseViewModel<AppRepository> {

    public ObservableField<String> account = new ObservableField<>("");

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(() -> login());
    public BindingCommand registerOnClickCommand = new BindingCommand(() -> toRegister());

    @Override
    public void onCreate() {
        super.onCreate();
        //A_8589934624是汪大称
        account.set("A_8589934616");
    }

    private void login() {
        if (!TextUtils.isEmpty(account.get())) {
//            if (SDKManager.envService != EnvService.PRD) {
//
//                addSubscribe(model.login(account.get(), 0)
//                        .compose(SRxUtils.schedulersTransformer()) //线程调度
//                        .doOnSubscribe(disposable -> showDialog())
//                        .subscribe(response -> {
//                            dismissDialog();
//                            if (response.isOk()) {
//                                String userId = response.getData().getUserId();
//                                String userSing = response.getData().getUserSig();
//                                if (!TextUtils.isEmpty(userSing)) {
//                                    model.saveAccount(userId);
//                                    model.loginIM(userId, userSing);
//                                    startActivity(MainActivity.class);
//                                    finish();
//                                } else {
//                                    uc.toastEvent.setValue("登录失败");
//                                }
//                            } else {
//                                uc.toastEvent.setValue(response.getMessage());
//                            }
//                        }, e -> {
//                            e.printStackTrace();
//                            dismissDialog();
//                            uc.toastEvent.setValue("网络错误");
//                        }));
//            } else {
                addSubscribe(model.login(account.get(), 0)
                        .compose(SRxUtils.schedulersTransformer()) //线程调度
                        .doOnSubscribe(disposable -> showDialog())
                        .subscribe(response -> {
                            dismissDialog();
                            if (response.isOk()) {
                                String userSing = response.getData();
                                if (!TextUtils.isEmpty(userSing)) {
                                    model.saveAccount(account.get());
                                    model.loginIM(account.get(), userSing);
                                    startActivity(MainActivity.class);
                                    finish();
                                } else {
                                    uc.toastEvent.setValue("登录失败");
                                }
                            } else {
                                uc.toastEvent.setValue(response.getMessage());
                            }
                        }, e -> {
                            e.printStackTrace();
                            dismissDialog();
                            uc.toastEvent.setValue("网络错误");
                        }));
            }
//        }
    }

    public void toRegister() {
        startActivity(RegisterActivity.class);
    }

}
