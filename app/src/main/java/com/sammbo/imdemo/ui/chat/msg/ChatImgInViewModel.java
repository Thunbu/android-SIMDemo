package com.sammbo.imdemo.ui.chat.msg;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.ImageInfo;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.ui.chat.ChatViewModel;

public class ChatImgInViewModel extends BaseMsgViewModel {
    public ObservableField<ImageInfo> imageInfo = new ObservableField<>();

    public ChatImgInViewModel(@NonNull ChatViewModel viewModel, MessageEntity entity) {
        super(viewModel, entity);
        imageInfo.set((ImageInfo) entity.getData());
    }
}
