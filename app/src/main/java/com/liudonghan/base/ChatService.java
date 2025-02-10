package com.liudonghan.base;

import com.liudonghan.mvp.ADBaseResult;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
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
    @GET("/th?id=OHR.BoxingDaySunrise_EN-US9951041123_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp")
    Observable<ResponseBody> getWallpaper();

    @POST("app/news/newsDetails?")
    Observable<ADBaseResult<WHBNewDetailsBean>> getWHBNewDetails(@Header("Whb-Client-Type") String headers, @Body RequestBody requestBody);

    @POST("api/sns/web/v1/homefeed")
    Observable<Object> getXhsHomeFeed(@HeaderMap Map<String,String> headerMap,@Body RequestBody requestBody);
}
