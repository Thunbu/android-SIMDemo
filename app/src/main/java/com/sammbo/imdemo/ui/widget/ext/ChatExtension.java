/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.IntRange;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sammbo.imdemo.annotation.ExtContextMenuItem;
import com.sammbo.imdemo.ui.chat.ChatViewModel;
import com.sammbo.imdemo.ui.widget.keyboard.ViewPagerFixed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ChatExtension {
    private Activity activity;
    private ChatViewModel chatViewModel;
    private ViewPagerFixed extViewPager;
    private List<ChatExt> exts;
    private String sessionId;

    private boolean hideOnScroll = true;

    /**
     * @param extViewPager         用于展示{@link ChatExtPageView}, 每个ChatExtPageView包含8个{@link ChatExt}
     */
    public ChatExtension(Activity activity, ChatViewModel chatViewModel, ViewPagerFixed extViewPager) {
        this.activity = activity;
        this.chatViewModel = chatViewModel;
        this.extViewPager = extViewPager;
    }

    private void onChatExtClick(ChatExt ext) {
        List<ExtMenuItemWrapper> extMenuItems = new ArrayList<>();
        Method[] allMethods = ext.getClass().getDeclaredMethods();
        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(ExtContextMenuItem.class)) {
                ExtContextMenuItem item = method.getAnnotation(ExtContextMenuItem.class);
                extMenuItems.add(new ExtMenuItemWrapper(item, method));
            }
        }
        if (extMenuItems.size() > 0) {
            if (extMenuItems.size() == 1) {
                try {
                    extMenuItems.get(0).method.invoke(ext);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                List<String> titles = new ArrayList<>(extMenuItems.size());
                for (ExtMenuItemWrapper itemWrapper : extMenuItems) {
                    titles.add(ext.contextMenuTitle(activity, itemWrapper.extContextMenuItem.tag()));
                }
                // TODO sort
                new MaterialDialog.Builder(activity).items(titles).itemsCallback((dialog, v, position, text) -> {
                    try {
                        extMenuItems.get(position).method.invoke(ext);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }).show();
            }
        }
    }


    public void bind(Activity activity, String sessionId) {
        this.sessionId = sessionId;
        setupExtViewPager(extViewPager);
        for (int i = 0; i < exts.size(); i++) {
            exts.get(i).onBind(activity, chatViewModel, this, i);
        }
    }

    public void onDestroy() {
        for (int i = 0; i < exts.size(); i++) {
            exts.get(i).onDestroy();
        }
    }

    private void setupExtViewPager(ViewPagerFixed viewPager) {
        exts = ChatExtManager.getInstance().getChatExts(sessionId);
        if (exts.isEmpty()) {
            return;
        }
        viewPager.setAdapter(new ChatExtPagerAdapter(exts, index -> {
            onChatExtClick(exts.get(index));
        }));
    }

    public boolean canHideOnScroll() {
        return hideOnScroll;
    }

    public void disableHideOnScroll() {
        this.hideOnScroll = false;
    }

    public static final int REQUEST_CODE_MIN = 0x8000;


    /**
     * 低16位是合法的request code
     * <p>
     * 第15位强制置1，表示从ChatExtension发起的
     * <p>
     * 第14-7位，共8个位，是{@link ChatExt}可用所有request code, 即request code只能在0-256
     * <p>
     * 第6-0位，共7个位，是index
     *
     * @param intent
     * @param requestCode
     * @param index
     */
    public void startActivityForResult(Intent intent, @IntRange(from = 0, to = 256) int requestCode, int index) {
        int extRequestCode = (requestCode << 7) | ChatExtension.REQUEST_CODE_MIN;
        extRequestCode += index;
        activity.startActivityForResult(intent, extRequestCode);
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if(exts == null || exts.isEmpty()){
            return false;
        }
        int index = requestCode & 0x7F;
        ChatExt chatExt = exts.get(index);
        if(chatExt == null){
            return false;
        }
        chatExt.onActivityResult((requestCode >> 7) & 0xFF, resultCode, data);
        return true;
    }

    private static class ExtMenuItemWrapper {
        ExtContextMenuItem extContextMenuItem;
        Method method;

        ExtMenuItemWrapper(ExtContextMenuItem extContextMenuItem, Method method) {
            this.extContextMenuItem = extContextMenuItem;
            this.method = method;
        }
    }
}
