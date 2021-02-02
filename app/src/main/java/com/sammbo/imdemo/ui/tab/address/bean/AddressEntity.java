package com.sammbo.imdemo.ui.tab.address.bean;

import androidx.databinding.BaseObservable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/26
 * desc   :
 * version:
 */
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class AddressEntity extends BaseObservable {

    /**
     * userId : 2694027299647970309
     * account : A_8589934606
     * appId : 1000000217
     * userNickname : 杨小黑
     * nicknamePy : yangxiaohei
     * petName :
     * userStatus : 0
     * avatar : http://bossfs.sammbo.com/0/1/head/penguin.png
     * createTime : 2021-01-07T02:54:15.000+0000
     * definition :
     * disablePush : 0
     */

    private String userId;
    private String account;
    private String appId;
    private String userNickname;
    private String nicknamePy;
    private String petName;
    private int userStatus;
    private String avatar;
    private String createTime;
    private String definition;
    private int disablePush;

}
