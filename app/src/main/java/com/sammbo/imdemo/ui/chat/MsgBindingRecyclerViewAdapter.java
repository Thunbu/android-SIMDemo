package com.sammbo.imdemo.ui.chat;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.ui.chat.menu.ChatItemMenu;
import com.sammbo.imdemo.ui.chat.msg.BaseMsgViewModel;
import com.sammbo.imdemo.utils.Consumer;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * 为了规避gradle高版本对泛型的约束导致databinding编译失败
 */
public class MsgBindingRecyclerViewAdapter extends BindingRecyclerViewAdapter<BaseMsgViewModel> {
    private View chatWindow;

    public void setChatWindow(View chatWindow) {
        this.chatWindow = chatWindow;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        BaseMsgViewModel adapterItem = getAdapterItem(position);
        MessageEntity messageEntity = adapterItem.entity.get();
        ChatItemMenu.createDefault(chatWindow, holder.itemView, messageEntity, false, false, false, menuCode -> {
            adapterItem.getChatViewModel().itemLongClick(menuCode,position,messageEntity,adapterItem);
        });
    }
}
