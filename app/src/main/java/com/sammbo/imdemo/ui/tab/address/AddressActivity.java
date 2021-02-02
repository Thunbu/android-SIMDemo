package com.sammbo.imdemo.ui.tab.address;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.ui.SBaseActivity;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/29
 * desc   :
 * version:
 */
public class AddressActivity extends SBaseActivity {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_fragment_container;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, AddressFragment.newsIntance(true));
        transaction.commitAllowingStateLoss();
    }
}
