package com.sammbo.imdemo.data;

import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * @author xin.zhou4
 * @date 2020/11/2
 * email：xin.zhou4@geely.com
 * description：
 */
public class Global {
    public static String getAccount() {
        return SPUtils.getInstance().getString("account");
    }

    public static void setAccount(String account) {
        SPUtils.getInstance().put("account", account);
    }

}
