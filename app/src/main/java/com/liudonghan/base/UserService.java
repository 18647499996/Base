package com.liudonghan.base;

import com.liudonghan.mvp.BaseResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/5/23
 */
public interface UserService {

    @FormUrlEncoded
    @POST("F_UserInfo/GetUserInfoByToken?")
    Observable<BaseResult<UserModel>> getUserInfo(@Field("token") String token);
}
