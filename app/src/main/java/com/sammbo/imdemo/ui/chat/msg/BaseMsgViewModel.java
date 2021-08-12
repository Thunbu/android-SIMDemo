package com.sammbo.imdemo.ui.chat.msg;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.ImageInfo;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.ui.chat.ChatViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;

public class BaseMsgViewModel extends MultiItemViewModel<ChatViewModel> {
    public ObservableField<MessageEntity> entity = new ObservableField<>();
    public ObservableField<Boolean> showTime = new ObservableField<>();
    public ObservableField<Boolean> showSign = new ObservableField<>(false);

    public BaseMsgViewModel(@NonNull ChatViewModel viewModel, MessageEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }


    public ChatViewModel getChatViewModel(){
        return viewModel;
    }
}
