package com.sammbo.imdemo.utils;

/**
 * Created by zhouxin on 18/5/23.
 */

@FunctionalInterface
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);
}
