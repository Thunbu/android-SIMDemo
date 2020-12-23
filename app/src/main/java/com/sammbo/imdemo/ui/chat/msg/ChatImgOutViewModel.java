package com.sammbo.imdemo.ui.chat.msg;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.ImageInfo;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.VideoInfo;
import com.sammbo.imdemo.ui.chat.ChatViewModel;

import me.goldze.mvvmhabit.base.MultiItemViewModel;

public class ChatImgOutViewModel extends BaseMsgViewModel {
    public ObservableField<ImageInfo> imageInfo = new ObservableField<>();

    public ChatImgOutViewModel(@NonNull ChatViewModel viewModel, MessageEntity entity) {
        super(viewModel, entity);
        imageInfo.set((ImageInfo) entity.getData());
    }
}
