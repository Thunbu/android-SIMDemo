package com.sammbo.imdemo.data.http.service;

import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.ui.login.bean.LoginReponse;
import com.sammbo.imdemo.ui.login.bean.UploadAddress;
import com.sammbo.imdemo.ui.login.bean.UserInfo;
import com.sammbo.imdemo.ui.tab.address.bean.AddressEntity;
import com.sammbo.imdemo.ui.tab.address.bean.AddressResponse;

import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author xin.zhou4
 * @date 2020/10/30
 * email：xin.zhou4@geely.com
 * description：
 */
public interface AppService {
    @POST(RetrofitClient.path + "/home/userLogin")
    Observable<SBaseResponse<LoginReponse>> login(@Query("loginParam") String account, @Query("terminalCode") int terminal);

    @POST(RetrofitClient.path + "/home/register")
    Observable<SBaseResponse<UserInfo>> register(@Query("mobile") String mobile, @Query("userName") String userName, @Query("avatar") String avatar);

    @POST(RetrofitClient.path + "/home/getAddressBookList")
    Observable<SBaseResponse<AddressResponse>> getAddressList(@Query("appId") String appId, @Query("page") int page, @Query("rows") int rows);

    @GET(RetrofitClient.path + "/home/getPolicyEncrypt")
    Observable<SBaseResponse<UploadAddress>> getUploadHost();
}
