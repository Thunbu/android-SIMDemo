package com.sammbo.imdemo.ui.register;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.app.AppViewModelFactory;
import com.sammbo.imdemo.databinding.ActivityRegisterBinding;
import com.sammbo.imdemo.ui.SBaseActivity;

import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/26
 * desc   :
 * version:
 */
public class RegisterActivity extends SBaseActivity<ActivityRegisterBinding, RegisterViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RegisterViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(RegisterViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.uc.toastEvent.observe(this, s -> ToastUtils.showShort(s));
    }
}
