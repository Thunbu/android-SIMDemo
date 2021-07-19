package com.sammbo.imdemo.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;

/**
 * @date 2020/12/14
 * description：
 */
@Data
@Builder
public class ImageInfo {
    public static final int IMAGE_TYPE_JPG = 0;
    public static final int IMAGE_TYPE_PNG = 1;
    public static final int IMAGE_TYPE_UNKNOWN = 255;
    // 缩略图
    private ImageExt thumb;
    // 大图
    private ImageExt large;
    // 原图
    private ImageExt origin;
    private int type;

    public static class ImageExt {
        public String url;
        public int height;
        public int width;
        public int size;
        public ImageExt(String url, int height, int width, int size) {
            this.url = url;
            this.height = height;
            this.width = width;
            this.size = size;
        }
    }
}
