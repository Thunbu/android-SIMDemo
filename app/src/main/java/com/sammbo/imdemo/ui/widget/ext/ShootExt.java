/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.content.Context;
import android.content.Intent;

import com.sammbo.imdemo.R;
import com.sammbo.imdemo.annotation.ExtContextMenuItem;
import com.sammbo.imdemo.entity.VideoInfo;

public class ShootExt extends ChatExt {

    /**
     */
    @ExtContextMenuItem
    public void shoot() {
        VideoInfo videoInfo = VideoInfo.builder()
                .videoUrl("https://bossfs.sammbo.com/2/1/260392/2020/11/1604556149616.MOV")
                .videoSize(2492145)
                .videoType(VideoInfo.VIDEO_TYPE_AVI)
                .duration(5000)
                .coverUrl("https://bossfs.sammbo.com/2/1/260392/2020/11/1604556149509.jpg")
                .coverHeight(200)
                .coverWidth(300)
                .coverType(VideoInfo.COVER_TYPE_JPG)
                .coverSize(135455)
                .build();
        chatViewModel.sendVideo(videoInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
    @Override
    public int iconResId() {
        return R.mipmap.ic_func_shot;
    }

    @Override
    public String title(Context context) {
        return "拍摄";
    }

    @Override
    public String contextMenuTitle(Context context, String tag) {
        return title(context);
    }
}
