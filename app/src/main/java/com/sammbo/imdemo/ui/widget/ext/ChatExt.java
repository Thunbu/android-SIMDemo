/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sammbo.imdemo.ui.chat.ChatViewModel;

/**
 * 扩展面板里的每一项功能，如"照片","拍摄","文件"等
 */
public abstract class ChatExt {
    protected Activity activity;
    protected ChatViewModel chatViewModel;
    protected ChatExtension extension;
    protected int index;

//    /**
//     * ext 优先级
//     *
//     * @return
//     */
//    public abstract int priority();

    /**
     * ext icon资源id
     *
     * @return
     */
    public abstract int iconResId();

    /**
     * ext 标题
     *
     * @param context
     * @return
     */
    public abstract String title(Context context);

    /**
     * 长按Ext，弹出的 context menu 的标题
     *
     * @param tag
     * @return
     */
    public abstract String contextMenuTitle(Context context, String tag);

    /**
     * 当前会话是否显示此扩展
     *
     * @param sessionId
     * @return 返回true，表示不显示
     */
    public boolean filter(String sessionId) {
        return false;
    }

    /**
     * 和会话界面绑定之后调用
     *
     */
    protected void onBind(Activity activity, ChatViewModel chatViewModel, ChatExtension extension, int index) {
        this.activity = activity;
        this.chatViewModel = chatViewModel;
        this.extension = extension;
        this.index = index;
    }

    protected void onDestroy() {
        this.activity = null;
        this.chatViewModel = null;
    }

    protected final void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        throw new IllegalStateException("show override this method");
    }

    /**
     * @param intent
     * @param requestCode 必须在0-256范围之内, 扩展{@link ChatExt}内部唯一即可
     */
    protected final void startActivityForResult(Intent intent, int requestCode) {
        if (requestCode < 0 || requestCode > 256) {
            throw new IllegalArgumentException("request code should in [0, 256]");
        }
        extension.startActivityForResult(intent, requestCode, index);
    }

}
