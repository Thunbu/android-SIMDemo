package com.sammbo.imdemo.utils;

import android.app.Application;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sammbo.imdemo.app.AppViewModelFactory;

import org.json.JSONObject;

import java.io.File;

import io.reactivex.Single;

/**
 * time   : 2021/01/27
 * desc   :
 * version:
 */
public class UploadUtils {
    private static volatile UploadUtils INSTANCE;
    private String token;
    private String host;

    private UploadUtils() {

    }

    public static UploadUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UploadUtils();
                }
            }
        }
        return INSTANCE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Single<String> uploadFile(File file) {
        return Single.create(emitter -> {
            Configuration config = new Configuration.Builder()
                    .connectTimeout(90)              // 链接超时。默认90秒
                    .useHttps(false)                  // 是否使用https上传域名
                    .responseTimeout(90)             // 服务器响应超时。默认90秒
                    .build();
            UploadManager uploadManager = new UploadManager(config);
            uploadManager.put(file, file.getName(), token, (key, info, response) -> {
                if (info.isOK()) {
                    emitter.onSuccess(host + key);
                }else {
                    emitter.onSuccess("");
                }
            }, null);
        });

    }
}
