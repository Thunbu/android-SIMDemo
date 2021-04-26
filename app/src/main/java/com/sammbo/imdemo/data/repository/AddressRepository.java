package com.sammbo.imdemo.data.repository;

import com.geely.imsdk.client.bean.group.GroupType;
import com.geely.imsdk.client.listener.SIMValueCallBack;
import com.geely.imsdk.client.manager.group.send.SIMGroupManager;
import com.geely.imsdk.client.manager.group.send.SIMGroupManagerImpl;
import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.data.http.service.AppService;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.tab.address.bean.AddressEntity;
import com.sammbo.imdemo.ui.tab.address.bean.AddressResponse;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/26
 * desc   :
 * version:
 */
public class AddressRepository extends BaseModel {

    private volatile static AddressRepository INSTANCE = null;
    private AppService appService;

    private AddressRepository() {
        appService = RetrofitClient.getInstance().create(AppService.class);
    }

    public static AddressRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (AppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AddressRepository();
                }
            }
        }
        return INSTANCE;
    }

    public Observable<SBaseResponse<AddressResponse>> getAddressBookList(int page, int rows) {
        return appService.getAddressList( SDKManager.envService.getAppId(), page, rows);
    }

    public void createGroup(String groupName,List<String> menmbers, SIMValueCallBack<String> callBack) {
        SIMGroupManager.Factory.create().createGroup(groupName, GroupType.INSIDE, menmbers, callBack);
    }
}
