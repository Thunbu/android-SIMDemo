/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sammbo.imdemo.R;
import com.sammbo.imdemo.annotation.ExtContextMenuItem;
import com.sammbo.imdemo.entity.ImageInfo;

public class ImageExt extends ChatExt {

    /**
     */
    @ExtContextMenuItem
    public void pickImage() {
        ImageInfo imageInfo = ImageInfo.builder()
                .thumb(new ImageInfo.ImageExt("https://bossfs.sammbo.com/0/1/head/yellowcat.png?x-oss-process=image/resize,l_198", 300, 300, 256))
                .large(new ImageInfo.ImageExt("https://bossfs.sammbo.com/0/1/head/yellowcat.png?x-oss-process=image/resize,l_720", 400, 400, 512))
                .origin(new ImageInfo.ImageExt("https://bossfs.sammbo.com/0/1/head/yellowcat.png", 600, 600, 1024))
                .type(ImageInfo.IMAGE_TYPE_JPG)
                .build();
        chatViewModel.sendImage(imageInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
        }
    }

    @Override
    public int iconResId() {
        return R.mipmap.ic_func_pic;
    }

    @Override
    public String title(Context context) {
        return "照片";
    }

    @Override
    public String contextMenuTitle(Context context, String tag) {
        return title(context);
    }
}
