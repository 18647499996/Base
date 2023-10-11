package com.liudonghan.base;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:10/9/23
 */
public class WallpaperInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response proceed = chain.proceed(chain.request());
        Log.i("Mac_Liu","随机图片路径：" + proceed.request().url());
        return proceed;
    }
}
