package com.sammbo.imdemo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.app.AppViewModelFactory;
import com.sammbo.imdemo.databinding.ActivityLoginBinding;
import com.sammbo.imdemo.sdk.EnvService;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.SBaseActivity;
import com.sammbo.imdemo.ui.tab.MainActivity;

import me.goldze.mvvmhabit.utils.ToastUtils;


public class LoginActivity extends SBaseActivity<ActivityLoginBinding, LoginViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.toastEvent.observe(this, s -> ToastUtils.showShort(s));
        viewModel.uc.startAct.observe(this, v -> {
            //推送传值,跳会话前一定要先连接im
            Intent intent = new Intent(this, MainActivity.class);
            if (getIntent().getExtras() != null) {
                intent.putExtras(getIntent().getExtras());
            }
            startActivity(intent);
        });
    }

    @Override
    public void initData() {
        super.initData();
        //正式环境隐藏注册入口
        binding.regist.setVisibility(SDKManager.envService == EnvService.PRD? View.GONE:View.VISIBLE);
    }
}