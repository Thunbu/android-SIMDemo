package com.sammbo.imdemo.ui.chat.msg;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.ui.chat.ChatViewModel;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.MultiItemViewModel;

public class BaseMsgViewModel extends MultiItemViewModel<ChatViewModel> {
    public ObservableField<MessageEntity> entity = new ObservableField<>();
    public ObservableField<Boolean> showTime = new ObservableField<>();

    public BaseMsgViewModel(@NonNull ChatViewModel viewModel, MessageEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }
}
