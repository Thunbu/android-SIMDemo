package com.sammbo.imdemo.ui.login;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.data.repository.AppRepository;
import com.sammbo.imdemo.entity.SpinnerItemData;
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
    public List<IKeyAndValue> defaultAccounts;

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, AppRepository model) {
        super(application, model);
    }

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(() -> login());

    public BindingCommand<IKeyAndValue> onAccountSelectorCommand = new BindingCommand<>(iKeyAndValue -> account.set(iKeyAndValue.getValue()));

    @Override
    public void onCreate() {
        super.onCreate();
        defaultAccounts = new ArrayList<>();
        defaultAccounts.add(new SpinnerItemData("掏钱猛", "A_8589934620"));
        defaultAccounts.add(new SpinnerItemData("梅菜饼", "A_8589934619"));
        defaultAccounts.add(new SpinnerItemData("张大葱", "A_8589934618"));
        defaultAccounts.add(new SpinnerItemData("炫小银", "A_8589934617"));
        defaultAccounts.add(new SpinnerItemData("汪大称", "A_8589934616"));
        defaultAccounts.add(new SpinnerItemData("兔小虾", "A_8589934615"));
    }

    private void login() {
        if (!TextUtils.isEmpty(account.get())) {
            addSubscribe(model.login(account.get(), 0)
                    .compose(SRxUtils.schedulersTransformer()) //线程调度
                    .doOnSubscribe(disposable -> showDialog())
                    .subscribe(response -> {
                        dismissDialog();
                        if (response.isOk()) {
                            model.saveAccount(account.get());
                            model.loginIM(account.get(), response.getData());
                            startActivity(MainActivity.class);
                            finish();
                        } else {
                            uc.toastEvent.setValue(response.getMessage());
                        }
                    }, e -> {
                        e.printStackTrace();
                        dismissDialog();
                        uc.toastEvent.setValue("网络错误");
                    }));
        }
    }
}
