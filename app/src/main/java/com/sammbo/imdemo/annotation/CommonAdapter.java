package com.sammbo.imdemo.annotation;

import com.geely.imsdk.client.bean.message.SIMMsgType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xin.zhou4
 * @since 2020/4/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommonAdapter {
    /**
     * 对应的本地Message类型
     */
    int[] local() default {};

    /**
     * 对应的SIMMessage类型
     */
    SIMMsgType[] remote() default {};
}
