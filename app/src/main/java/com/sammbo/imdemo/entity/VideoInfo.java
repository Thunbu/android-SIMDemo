package com.sammbo.imdemo.entity;

import com.geely.imsdk.client.bean.message.elem.video.SIMVideoFormat;
import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

/**
 * @author xin.zhou4
 * @date 2020/12/14
 * email：xin.zhou4@geely.com
 * description：
 */
@Data
@Builder
public class VideoInfo {
    public static final int VIDEO_TYPE_AVI = 0;
    public static final int VIDEO_TYPE_RM = 1;
    public static final int VIDEO_TYPE_RMVB = 2;
    public static final int VIDEO_TYPE_WMV = 3;
    public static final int VIDEO_TYPE_MP4 = 4;
    public static final int VIDEO_TYPE_UNKNOWN = 255;

    public static final int COVER_TYPE_JPG = 0;
    public static final int COVER_TYPE_PNG = 1;
    public static final int COVER_TYPE_UNKNOWN = 255;

    private String videoUrl;
    private int videoType;
    private int duration;
    private int videoSize;

    private String coverUrl;
    private int coverSize;
    private int coverHeight;
    private int coverWidth;
    private int coverType;
}
