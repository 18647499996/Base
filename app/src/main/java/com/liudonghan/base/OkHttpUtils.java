package com.liudonghan.base;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/5/23
 */
public class OkHttpUtils {
    private static volatile OkHttpUtils instance = null;

    private OkHttpUtils() {
    }

    public static OkHttpUtils getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (OkHttpUtils.class) {
                // double checkout
                if (null == instance) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取鉴权中心配置
     * @return
     */
    public OkHttpClient getAuthServiceConfig() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new CodeInterceptor())
                .build();
    }

    public OkHttpClient getChatServiceConfig() {
        return null;
    }
}
