/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.annotation.ExtContextMenuItem;
import com.sammbo.imdemo.entity.FileInfo;
import com.sammbo.imdemo.utils.UploadUtils;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class FileExt extends ChatExt {
    private static final int REQUEST_CODE = 125;

    @ExtContextMenuItem
    public void pickFile() {
        int extRequestCode = (REQUEST_CODE << 7) | ChatExtension.REQUEST_CODE_MIN;
        extRequestCode += index;
        String sdcardPath = Environment.getExternalStorageDirectory().getPath();
        new LFilePicker()
                .withActivity(activity)
                .withRequestCode(extRequestCode)
                .withStartPath(sdcardPath)//指定初始显示路径
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withFileSize(500 * 1024)//指定文件大小为500K
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> list = data.getStringArrayListExtra("paths");
            for (String path : list) {
                File file = new File(path);
                UploadUtils.getInstance().uploadFile(file)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (!TextUtils.isEmpty(result)) {
                                FileInfo fileInfo = FileInfo.builder()
                                        .filename(file.getName())
                                        .downUrl(result)
                                        .size(file.length())
                                        .build();
                                chatViewModel.sendFile(fileInfo);
                            } else {
                                Log.e("Tag", "upload fail");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("Tag", "upload error :" + throwable.getMessage());
                            }
                        });
            }
        }
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

    private static String getPathFromUri(Activity act, Uri uri, String selection) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = act.getContentResolver().query(uri, projection, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
