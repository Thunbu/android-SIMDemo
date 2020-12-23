package com.sammbo.imdemo.ui.chat.msg;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.ui.chat.ChatViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;

public class ChatTxtOutViewModel extends BaseMsgViewModel {
    public ObservableField<String> content = new ObservableField<>();

    public ChatTxtOutViewModel(@NonNull ChatViewModel viewModel, MessageEntity entity) {
        super(viewModel, entity);
        content.set((String)entity.getData());
    }
}
