package com.sammbo.imdemo.data;

import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * @date 2020/11/2
 * description：
 */
public class Global {
    public static String getAccount() {
        return SPUtils.getInstance().getString("account");
    }

    public static void setAccount(String account) {
        SPUtils.getInstance().put("account", account);
    }

    public static void removeAccount(){
        SPUtils.getInstance().remove("account");
    }
}
