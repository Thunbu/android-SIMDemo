/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.sammbo.imdemo.ui.widget.ext.ChatExt;

/**
 * 用于注解聊天扩展{@link ChatExt}, 点击扩展之后的相应，若扩展{@link ChatExt}只有一个菜单{@link ExtContextMenuItem}，
 * 那么直接响应；如果有多个，则弹出菜单，让进一步选择
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtContextMenuItem {
    String tag() default "";
}
