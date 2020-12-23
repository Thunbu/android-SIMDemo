package com.sammbo.imdemo.ui.tab.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;

import me.goldze.mvvmhabit.base.BaseFragment;

public class MineFragment extends BaseFragment {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
