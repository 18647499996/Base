package com.liudonghan.base;

import com.liudonghan.mvp.ADBaseResult;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/5/23
 */
public interface ChatService {
    /**
     * 获取用户信息
     *
     * @return Observable<BaseResult < UserModel>>
     */
    @POST("j_user/getUserInfo?")
    Observable<ADBaseResult<UserModel>> getUserInfo();

    @Streaming
    @GET("/")
    Observable<ResponseBody> getWallpaper();
}
