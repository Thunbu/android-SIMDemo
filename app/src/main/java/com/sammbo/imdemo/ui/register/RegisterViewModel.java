package com.sammbo.imdemo.ui.register;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.data.repository.AppRepository;
import com.sammbo.imdemo.ui.login.LoginActivity;
import com.sammbo.imdemo.utils.SRxUtils;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/26
 * desc   :
 * version:
 */
public class RegisterViewModel extends BaseViewModel<AppRepository> {
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();

    public BindingCommand registerOnClickCommand = new BindingCommand(() -> register());

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    }

    public RegisterViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    public void register() {
        String mobile=account.get();
        String userName=name.get();
        if(!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(userName)){
            addSubscribe(model.register(account.get(),name.get())
                    .compose(SRxUtils.schedulersTransformer())
                    .doOnSubscribe(disposable -> showDialog())
                    .subscribe(userInfoSBaseResponse -> {
                        dismissDialog();
                        if (userInfoSBaseResponse.isOk()) {
                            startActivity(LoginActivity.class);
                            finish();
                        } else {
                            uc.toastEvent.setValue(userInfoSBaseResponse.getMessage());
                        }
                    }, e -> {
                        e.printStackTrace();
                        dismissDialog();
                        uc.toastEvent.setValue("网络错误");
                    }));
        }

    }
}
