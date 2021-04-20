package com.sammbo.imdemo.data.http.service;

import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.ui.login.bean.LoginReponse;
import com.sammbo.imdemo.ui.login.bean.UploadAddress;
import com.sammbo.imdemo.ui.login.bean.UserInfo;
import com.sammbo.imdemo.ui.tab.address.bean.AddressResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author xin.zhou4
 * @date 2020/10/30
 * email：xin.zhou4@geely.com
 * description：
 */
public interface AppService {
    @POST
    Observable<SBaseResponse<LoginReponse>> login(@Url String url, @Query("loginParam") String account, @Query("terminalCode") int terminal);

    @POST
    Observable<SBaseResponse<String>> loginPrd(@Url String url, @Query("userId") String account, @Query("terminalCode") int terminal);

    @POST
    Observable<SBaseResponse<UserInfo>> register(@Url String url, @Query("mobile") String mobile, @Query("userName") String userName, @Query("avatar") String avatar);

    @POST
    Observable<SBaseResponse<AddressResponse>> getAddressList(@Url String url, @Query("appId") String appId, @Query("page") int page, @Query("rows") int rows);

    @GET
    Observable<SBaseResponse<UploadAddress>> getUploadHost(@Url String url);


}
