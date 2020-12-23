/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.content.Context;
import android.content.Intent;

import com.sammbo.imdemo.R;
import com.sammbo.imdemo.annotation.ExtContextMenuItem;

public class FileExt extends ChatExt {

    @ExtContextMenuItem
    public void pickFile() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public int iconResId() {
        return R.mipmap.ic_func_file;
    }

    @Override
    public String title(Context context) {
        return "文件";
    }

    @Override
    public String contextMenuTitle(Context context, String tag) {
        return title(context);
    }
}
