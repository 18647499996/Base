package com.liudonghan.base;

import android.util.Log;

import androidx.annotation.NonNull;


import com.liudonghan.utils.LogUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Description：拦截器（ 日志 ）
 *
 * @author Created by: Li_Min
 * Time:3/9/22
 */
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String bodyStr = bodyToString(request);
//        LogUtils.json(this.getClass().getName(), String.format("请求参数 %s: body=   %s", URLDecoder.decode(String.valueOf(request.url()), "utf-8"), bodyStr));
        Log.d(this.getClass().getName(),String.format("请求参数 %s: body=   %s", URLDecoder.decode(String.valueOf(request.url()), "utf-8"), bodyStr));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        if (response.body() != null) {
            ResponseBody body = response.peekBody(1024 * 1024);
            Log.d(this.getClass().getName(),String.format(Locale.getDefault(), "返回数据 %s in %.1fms%n   %s", URLDecoder.decode(URLDecoder.decode(String.valueOf(response.request().url()), "utf-8"), "utf-8"), (t2 - t1) / 1e6d, body.string()));
//            LogUtils.json(this.getClass().getName(), String.format(Locale.getDefault(), "返回数据 %s in %.1fms%n   %s", URLDecoder.decode(URLDecoder.decode(String.valueOf(response.request().url()), "utf-8"), "utf-8"), (t2 - t1) / 1e6d, body.string()));
        } else {
            Log.i(this.getClass().getName(), "body null");
            LogUtils.e("服务器数据异常" + request.url());

        }
        return response;

    }

    /**
     * 字符串输出
     *
     * @param request 请求数据
     * @return buffer
     */
    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy == null || copy.body() == null) {
                return "";
            }
            Objects.requireNonNull(copy.body()).writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}