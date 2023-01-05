package com.liudonghan.base;

import com.liudonghan.mvp.BaseResult;

import retrofit2.http.POST;
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
    Observable<BaseResult<UserModel>> getUserInfo();
}
