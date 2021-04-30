package com.sammbo.imdemo.entity.busevent;

import lombok.Data;

/**
 * time   : 2021/01/29
 * desc   :
 * version:
 */
@Data
public class EventMessage<T> {
    private int code;
    private T data;

}
