package com.sammbo.imdemo.ui.chat.msg;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.entity.FileInfo;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.VideoInfo;
import com.sammbo.imdemo.ui.chat.ChatViewModel;

public class FileInfoViewModel extends BaseMsgViewModel {
    public ObservableField<FileInfo> fileInfo = new ObservableField<>();
    public FileInfoViewModel(@NonNull ChatViewModel viewModel, MessageEntity entity) {
        super(viewModel, entity);
        fileInfo.set((FileInfo)entity.getData());
    }
}
