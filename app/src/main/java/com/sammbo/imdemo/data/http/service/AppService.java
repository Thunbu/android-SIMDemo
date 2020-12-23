package com.sammbo.imdemo.data.http.service;

import com.sammbo.imdemo.data.http.RetrofitClient;
import com.sammbo.imdemo.data.http.SBaseResponse;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.http.BaseResponse;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author xin.zhou4
 * @date 2020/10/30
 * email：xin.zhou4@geely.com
 * description：
 */
public interface AppService {
    @POST(RetrofitClient.path + "/home/userSig")
    Observable<SBaseResponse<String>> login(@Query("userId")String userid, @Query("terminalCode")int terminal);
}
