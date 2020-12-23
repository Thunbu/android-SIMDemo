/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.lqr.emoji.IEmotionExtClickListener;
import com.lqr.emoji.IEmotionSelectedListener;
import com.lqr.emoji.LQREmotionKit;
import com.lqr.emoji.MoonUtils;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.databinding.ChatInputPanelBinding;
import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.ui.chat.ChatViewModel;
import com.sammbo.imdemo.ui.widget.ext.ChatExtension;
import com.sammbo.imdemo.ui.widget.keyboard.InputAwareLayout;

public class ChatInputPanel extends FrameLayout implements IEmotionSelectedListener, TextWatcher {

    ChatExtension extension;
    private SessionEntity sessionEntity;
    private ChatViewModel chatViewmodel;
    private InputAwareLayout rootLinearLayout;
    private Activity activity;

    private static final int MAX_EMOJI_PER_MESSAGE = 50;
    private int messageEmojiCount = 0;
    ChatInputPanelBinding mBinding;

    private OnChatInputPanelStateChangeListener onChatInputPanelStateChangeListener;

    public ChatInputPanel(@NonNull Context context) {
        super(context);
    }

    public ChatInputPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public ChatInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatInputPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void setOnChatInputPanelStateChangeListener(OnChatInputPanelStateChangeListener onChatInputPanelStateChangeListener) {
        this.onChatInputPanelStateChangeListener = onChatInputPanelStateChangeListener;
    }

    public void bind(FragmentActivity activity, InputAwareLayout rootInputAwareLayout) {

    }

    public void setupChat(String sessionId) {
        this.extension.bind(activity, sessionId);
    }

    public void onDestroy() {
        this.extension.onDestroy();
    }

    public void init(Activity activity, ChatViewModel chatViewModel, InputAwareLayout rootInputAwareLayout) {
        mBinding = ChatInputPanelBinding.inflate(LayoutInflater.from(getContext()), this, true);

        this.activity = activity;
        this.chatViewmodel = chatViewModel;
//        this.fragment = fragment;
        this.rootLinearLayout = rootInputAwareLayout;
        this.extension = new ChatExtension(activity, chatViewModel, mBinding.chatExtViewPager);

        mBinding.editText.addTextChangedListener(this);
        // emotion
        mBinding.emotionLayout.setEmotionAddVisiable(true);
        mBinding.emotionLayout.setEmotionSettingVisiable(true);

        // emotion
        mBinding.emotionLayout.setEmotionSelectedListener(this);
        mBinding.emotionLayout.setEmotionExtClickListener(new IEmotionExtClickListener() {
            @Override
            public void onEmotionAddClick(View view) {
                Toast.makeText(activity, "add", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmotionSettingClick(View view) {
                Toast.makeText(activity, "setting", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.emotionImageView.setOnClickListener(v -> {
            if (rootLinearLayout.getCurrentInput() == mBinding.emotionContainerFrameLayout) {
                hideEmotionLayout();
                rootLinearLayout.showSoftkey(mBinding.editText);
            } else {
                showEmotionLayout();
            }
        });

        mBinding.extImageView.setOnClickListener(v -> {
            if (rootLinearLayout.getCurrentInput() == mBinding.extContainerContainerLayout) {
                hideChatExtension();
                rootLinearLayout.showSoftkey(mBinding.editText);
            } else {
                mBinding.emotionImageView.setImageResource(R.mipmap.ic_cheat_emo);
                showChatExtension();
            }
        });

        mBinding.send.setOnClickListener(v -> {
            chatViewModel.sendText(mBinding.editText.getText().toString());
            mBinding.editText.setText("");
        });

    }

    public void onKeyboardShown() {
        hideEmotionLayout();
    }

    public void onKeyboardHidden() {
        // do nothing
    }

    public void setInputText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mBinding.editText.setText(text);
        mBinding.editText.setSelection(text.length());
        mBinding.editText.requestFocus();
        rootLinearLayout.showSoftkey(mBinding.editText);
    }

    private void showEmotionLayout() {
//        audioButton.setVisibility(View.GONE);
        mBinding.emotionImageView.setImageResource(R.mipmap.ic_cheat_keyboard);
        rootLinearLayout.show(mBinding.editText, mBinding.emotionContainerFrameLayout);
        if (onChatInputPanelStateChangeListener != null) {
            onChatInputPanelStateChangeListener.onInputPanelExpanded();
        }
    }

    private void hideEmotionLayout() {
        mBinding.emotionImageView.setImageResource(R.mipmap.ic_cheat_emo);
        if (onChatInputPanelStateChangeListener != null) {
            onChatInputPanelStateChangeListener.onInputPanelCollapsed();
        }
    }

    private void showChatExtension() {
        rootLinearLayout.show(mBinding.editText, mBinding.extContainerContainerLayout);
        if (onChatInputPanelStateChangeListener != null) {
            onChatInputPanelStateChangeListener.onInputPanelExpanded();
        }
    }

    private void hideChatExtension() {
        if (onChatInputPanelStateChangeListener != null) {
            onChatInputPanelStateChangeListener.onInputPanelCollapsed();
        }
    }

    public void closeChatInputPanel() {
        mBinding.emotionImageView.setImageResource(R.mipmap.ic_cheat_emo);
        rootLinearLayout.hideAttachedInput(true);
        rootLinearLayout.hideCurrentInput(mBinding.editText);
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable editable = mBinding.editText.getText();
        if (key.equals("/DEL")) {
            messageEmojiCount--;
            messageEmojiCount = messageEmojiCount < 0 ? 0 : messageEmojiCount;
            mBinding.editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            if (messageEmojiCount >= MAX_EMOJI_PER_MESSAGE) {
                Toast.makeText(activity, "最多允许输入" + MAX_EMOJI_PER_MESSAGE + "个表情符号", Toast.LENGTH_SHORT).show();
                return;
            }
            messageEmojiCount++;
            int code = Integer.decode(key);
            char[] chars = Character.toChars(code);
            String value = Character.toString(chars[0]);
            for (int i = 1; i < chars.length; i++) {
                value += Character.toString(chars[i]);
            }

            int start = mBinding.editText.getSelectionStart();
            int end = mBinding.editText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            editable.replace(start, end, value);

            int editEnd = mBinding.editText.getSelectionEnd();
            MoonUtils.replaceEmoticons(LQREmotionKit.getContext(), editable, 0, editable.toString().length());
            mBinding.editText.setSelection(editEnd);
        }
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
        // 动态表情
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mBinding.editText.getText().toString().trim().length() > 0) {
            if (activity.getCurrentFocus() == mBinding.editText) {
//                notifyTyping(TypingMessageContent.TYPING_TEXT);
            }
            mBinding.extSwitcher.setDisplayedChild(1);
        } else {
            mBinding.extSwitcher.setDisplayedChild(0);
        }
    }


    public interface OnChatInputPanelStateChangeListener {
        /**
         * 输入面板展开
         */
        void onInputPanelExpanded();

        /**
         * 输入面板关闭
         */
        void onInputPanelCollapsed();
    }
}
