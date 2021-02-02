package com.sammbo.imdemo.entity.busevent;

import lombok.Data;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/29
 * desc   :
 * version:
 */
@Data
public class EventMessage<T> {
    private int code;
    private T data;

}
