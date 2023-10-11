package com.liudonghan.mvp;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:10/11/23
 */
public class ADBaseCodeInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (ADBaseExceptionManager.RESPONSE_CODE == response.code()) {
            ResponseBody body = response.peekBody(Integer.MAX_VALUE);
            String bodyStr = body.string();
            int code;
            String msg;

            if (!TextUtils.isEmpty(bodyStr)) {
                ADBaseResult baseResult = fromJson(bodyStr, ADBaseResult.class);
                if (null == baseResult) {
                    Log.w("Mac_Liu", "remote service data error ：" + bodyStr);
                    throw new ADBaseTransformerManager.ServerException();
                }
                code = baseResult.getCode();
                msg = baseResult.getMsg();
            } else {
                throw new ADBaseTransformerManager.ServerException();
            }

            switch (code) {
                // code编码主要根据服务器返回识别
                case 0:
                case 1:
                case 200:
                case 10000:
                    return response;
                default:
                    throw new ADBaseTransformerManager.ServerException(code, msg, URLDecoder.decode(String.valueOf(request.url()), "utf-8"), ADBaseLogInterceptor.bodyToString(request));
            }
        } else if (ADBaseExceptionManager.AUTH_CODE == response.code()) {
            throw new ADBaseTransformerManager.TokenException(response.code(), "用户授权失败，请重新登录", URLDecoder.decode(String.valueOf(request.url()), "utf-8"), ADBaseLogInterceptor.bodyToString(request));
        } else {
            throw new ADBaseTransformerManager.ServerException(response.code(), response.message(), URLDecoder.decode(String.valueOf(request.url()), "utf-8"), ADBaseLogInterceptor.bodyToString(request));
        }
    }


    /**
     * 序列化json对象
     *
     * @param json json数据
     * @param t    序列化对象T
     * @return T
     */
    public <T> T fromJson(String json, Class<T> t) throws RuntimeException {
        T t1;
        try {
            t1 = new GsonBuilder().disableHtmlEscaping().create().fromJson(json, t);
        } catch (RuntimeException e) {
            Log.e("Mac_Liu", "JsonSyntax Error：" + json);
            e.printStackTrace();
            return null;
        }
        return t1;

    }
}
