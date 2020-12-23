/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ChatExtManager {
    private static ChatExtManager instance;
    private List<ChatExt> chatExts;

    private ChatExtManager() {
        chatExts = new ArrayList<>();
        init();
    }

    public static synchronized ChatExtManager getInstance() {
        if (instance == null) {
            instance = new ChatExtManager();
        }
        return instance;
    }

    /**
     * 注册面板功能
     */
    private void init() {
        registerExt(ImageExt.class);
        registerExt(ShootExt.class);
        registerExt(FileExt.class);
    }

    public void registerExt(Class<? extends ChatExt> clazz) {
        Constructor constructor;
        try {
            constructor = clazz.getConstructor();
            ChatExt ext = (ChatExt) constructor.newInstance();
            chatExts.add(ext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ChatExt> getChatExts(String sessionId) {
        List<ChatExt> currentExts = new ArrayList<>();
        for (ChatExt ext : this.chatExts) {
            if (!ext.filter(sessionId)) {
                currentExts.add(ext);
            }
        }
        return currentExts;
    }
}
