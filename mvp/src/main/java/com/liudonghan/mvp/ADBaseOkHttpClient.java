package com.liudonghan.mvp;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:10/11/23
 */
public class ADBaseOkHttpClient {
    private static volatile ADBaseOkHttpClient instance = null;

    private ADBaseOkHttpClient() {
    }

    public static ADBaseOkHttpClient getInstance() {
        //single checkout
        if (null == instance) {
            synchronized (ADBaseOkHttpClient.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADBaseOkHttpClient();
                }
            }
        }
        return instance;
    }

    /**
     * todo 构建OkHttpClient
     *
     * @param interceptors 拦截器
     * @return OkHttpClient.Builder
     */
    public OkHttpClient.Builder getOkHttpClient(Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.callTimeout(60, TimeUnit.SECONDS);
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        return builder;
    }

    /**
     * todo 构建OkHttpClient
     *
     * @return OkHttpClient.Builder
     */
    public OkHttpClient.Builder getOkHttpClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS);
    }
}
