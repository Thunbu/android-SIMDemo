package com.sammbo.imdemo.ui.tab.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.app.AppViewModelFactory;
import com.sammbo.imdemo.app.SApplication;
import com.sammbo.imdemo.databinding.FragmentSessionBindingImpl;

import me.goldze.mvvmhabit.base.BaseFragment;

public class SessionFragment extends BaseFragment<FragmentSessionBindingImpl, SessioinViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_session;
    }

    @Override
    public SessioinViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(SApplication.getInstance());
        return ViewModelProviders.of(this, factory).get(SessioinViewModel.class);
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
