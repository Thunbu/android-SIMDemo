package com.sammbo.imdemo.ui.chat;

import com.sammbo.imdemo.ui.chat.msg.BaseMsgViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * 为了规避gradle高版本对泛型的约束导致databinding编译失败
 */
public class MsgBindingRecyclerViewAdapter extends BindingRecyclerViewAdapter<BaseMsgViewModel> {
}
