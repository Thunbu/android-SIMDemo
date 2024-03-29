package com.sammbo.imdemo.ui.tab.session;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.ui.chat.ChatActivity;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class SessionItemViewModel extends ItemViewModel<SessioinViewModel> {
    public ObservableField<SessionEntity> entity = new ObservableField<>();

    public ObservableField<SessionEntity> getEntity() {
        return entity;
    }

    public void setEntity(SessionEntity entity) {
        this.entity.set(entity);
        this.entity.notifyChange();
    }

    public SessionItemViewModel(@NonNull SessioinViewModel viewModel, SessionEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand startChatClick = new BindingCommand(() -> viewModel.startActivity(ChatActivity.class, ChatActivity.startParams(entity.get().getSessionId(), entity.get().getSessionName())));
    public BindingCommand<View> longClick = new BindingCommand<View>(view -> {
        viewModel.showMenu(view, this);
    });
}
