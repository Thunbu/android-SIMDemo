package com.sammbo.imdemo.ui.tab;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.geely.imsdk.client.listener.SIMCallBack;
import com.geely.imsdk.client.manager.device.SIMDeviceManager;
import com.geely.libpush.MyPushManager;
import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.databinding.ActivityMainBinding;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.SBaseActivity;
import com.sammbo.imdemo.ui.chat.ChatActivity;
import com.sammbo.imdemo.ui.tab.address.AddressFragment;
import com.sammbo.imdemo.ui.tab.session.SessionFragment;
import com.sammbo.imdemo.ui.tab.mine.MineFragment;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends SBaseActivity<ActivityMainBinding, BaseViewModel> {

    private List<Fragment> mFragments;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //初始化Fragment
        initFragment();
        //初始化底部Button
        initBottomTab();
        checkPush();
    }

    /**
     * 解析推送
     * */
    private void checkPush() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fromType = bundle.getString("fromType");
            String title = bundle.getString("title");
            String session = bundle.getString("session");
            //推送通知栏过来的跳转
            if (!TextUtils.isEmpty(session) && TextUtils.equals("push", fromType)) {
                viewModel.startActivity(ChatActivity.class, ChatActivity.startParams(session, title));
                getIntent().putExtra("fromType", "");
                getIntent().putExtra("session", "");
            }
        }
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new SessionFragment());
        mFragments.add(new AddressFragment());
//        mFragments.add(new MineFragment());
        //默认选中第一个
        commitAllowingStateLoss(0);
    }

    private void initBottomTab() {
        NavigationController navigationController = binding.pagerBottomTab.material()
                .addItem(R.drawable.ic_baseline_message_24, "消息")
                .addItem(R.drawable.ic_baseline_people_24, "通讯录")
//                .addItem(R.drawable.ic_baseline_info_24, "我的")
                .build();
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frameLayout, mFragments.get(index));
//                transaction.commitAllowingStateLoss();
                commitAllowingStateLoss(index);
            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }
    private void commitAllowingStateLoss(int position) {
        hideAllFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(position + "");
        if (currentFragment != null) {
            transaction.show(currentFragment);
        } else {
            currentFragment = mFragments.get(position);
            transaction.add(R.id.frameLayout, currentFragment, position + "");
        }
        transaction.commitAllowingStateLoss();
    }

    //隐藏所有Fragment
    private void hideAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(i + "");
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
