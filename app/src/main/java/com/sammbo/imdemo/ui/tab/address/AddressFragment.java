package com.sammbo.imdemo.ui.tab.address;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.app.AppViewModelFactory;
import com.sammbo.imdemo.databinding.FragmentAddressBinding;
import com.sammbo.imdemo.ui.login.LoginViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

public class AddressFragment extends BaseFragment<FragmentAddressBinding, AddressViewModel> {

    public static AddressFragment newsIntance(boolean hasCheckBox) {
        AddressFragment fragment = new AddressFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("hasCheckBox", hasCheckBox);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_address;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewModel.hasCheckBox.set(bundle.getBoolean("hasCheckBox", false));
        }
    }

    @Override
    public AddressViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(AddressViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.initRefreshListener(binding.swipeRefreshLayout);
        binding.swipeRefreshLayout.autoRefresh();
    }

}
