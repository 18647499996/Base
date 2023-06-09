package com.liudonghan.base;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.liudonghan.mvp.ADBaseResult;
import com.liudonghan.mvp.ADBaseTransformerManager;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Description：拦截器（ code码 ）
 * 服务器返回code编码拦截器
 * 根据需求处理统一拦截
 * 比如token异常处理 与服务器定义好统一code编码
 *
 * @author Created by: Li_Min
 * Time:3/9/22
 */
public class CodeInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (200 == response.code()) {
            ResponseBody body = response.peekBody(1024 * 1024);
            String bodyStr = body.string();
            int code;
            String msg;

            if (!TextUtils.isEmpty(bodyStr)) {
                ADBaseResult baseResult = GsonUtils.fromJson(bodyStr, ADBaseResult.class);
                if (null == baseResult) {
                    Log.e("服务器数据：" , bodyStr);
                    throw new ADBaseTransformerManager.ServerException();
                }
                code = baseResult.getCode();
                msg = baseResult.getMsg();
            } else {
                throw new ADBaseTransformerManager.ServerException();
            }

            switch (code) {
                //code编码主要根据服务器返回识别
                case 0:
                case 1:
                case 200:
                case 10000:
                    return response;
                case 2:
                case 401:
//                    RxBus.get().post(Constant.User.TOKEN_OVERDUE);
                    throw new ADBaseTransformerManager.TokenException(code, msg,URLDecoder.decode(String.valueOf(request.url()), "utf-8"),bodyToString(request));
                case 1000:
                    // 可以通过发送RxBus通知进入登录页面
                    throw new ADBaseTransformerManager.TokenException(code, msg,URLDecoder.decode(String.valueOf(request.url()), "utf-8"),bodyToString(request));
                case 400:
                case 190:
                default:
                    throw new ADBaseTransformerManager.ServerException(code, msg, URLDecoder.decode(String.valueOf(request.url()), "utf-8"),bodyToString(request));
            }
        } else if (401 == response.code()) {
//            RxBus.get().post(Constant.User.TOKEN_OVERDUE);
            throw new ADBaseTransformerManager.TokenException(response.code(), "用户授权失败，请重新登录",URLDecoder.decode(String.valueOf(request.url()), "utf-8"),bodyToString(request));
        } else if (401 == response.code()) {
            throw new ADBaseTransformerManager.TokenException(response.code(), "无网络连接，请检查您的网络");
        } else {
            throw new ADBaseTransformerManager.ServerException(response.code(), response.message());
        }
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