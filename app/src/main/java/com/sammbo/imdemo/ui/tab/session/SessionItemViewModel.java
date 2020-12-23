package com.sammbo.imdemo.ui.tab.session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.ui.chat.ChatActivity;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class SessionItemViewModel extends ItemViewModel<SessioinViewModel> {
    public ObservableField<SessionEntity> entity = new ObservableField<>();

    public SessionItemViewModel(@NonNull SessioinViewModel viewModel, SessionEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand startChatClick = new BindingCommand(() -> viewModel.startActivity(ChatActivity.class, ChatActivity.startParams(entity.get().getSessionId(), entity.get().getSessionName())));
}
