package com.liudonghan.mvp;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLHandshakeException;

/**
 * Description：自定义异常捕获及信息处理类
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public class ADBaseExceptionManager {

    private static volatile ADBaseExceptionManager instance = null;

    private ADBaseExceptionManager(){}

    public static ADBaseExceptionManager getInstance(){
     //single chcekout
     if(null == instance){
        synchronized (ADBaseExceptionManager.class){
            // double checkout
            if(null == instance){
                instance = new ADBaseExceptionManager();
            }
        }
     }
     return instance;
    }

    /**
     * 异常信息处理
     * @param throwable 处理的异常信息
     * @return apiException 异常信息
     */
    public ApiException throwableUtils(Throwable throwable) {
        ApiException apiException;
        if (throwable instanceof EOFException || throwable instanceof ConnectException || throwable instanceof SSLHandshakeException) {
            apiException = new ApiException(throwable, Error.HTTP_ERROR);
            apiException.setErrorMessage(Error.STR_HTTP_ERROR);
            Log.e(this.getClass().getName(), "网络异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof ADBaseTransformerManager.ServerException) {
            ADBaseTransformerManager.ServerException resultException = (ADBaseTransformerManager.ServerException) throwable;
            apiException = new ApiException(resultException, resultException.getCode());
            apiException.setErrorMessage(null == resultException.getMsg()? Error.STR_PARSE_ERROR : resultException.getMsg());
            Log.e(this.getClass().getName(), "服务器异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof ADBaseTransformerManager.TokenException){
            ADBaseTransformerManager.TokenException tokenException = (ADBaseTransformerManager.TokenException) throwable;
            apiException = new ApiException(tokenException,tokenException.getCode());
            apiException.setErrorMessage(tokenException.getMsg());
            Log.e(this.getClass().getName(), "Token异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        }else if (throwable instanceof JsonParseException ||
                throwable instanceof JSONException ||
                throwable instanceof ParseException) {
            apiException = new ApiException(throwable, Error.PARSE_ERROR);
            apiException.setErrorMessage(Error.STR_PARSE_ERROR);
            Log.e(this.getClass().getName(), "解析异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
            //环信无网络判断
        } else if (throwable instanceof SocketException || throwable instanceof UnknownHostException || throwable instanceof ApiException) {
            apiException = new ApiException(throwable, Error.NO_NET_ERROR);
            apiException.setErrorMessage(Error.STR_NO_NET_ERROR);
            Log.e(this.getClass().getName(), "连接异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof SocketTimeoutException) {
            apiException = new ApiException(throwable, Error.TIME_OUT_ERROR);
            apiException.setErrorMessage(Error.STR_TIME_OUT_ERROR);
            Log.e( this.getClass().getName(), "超时异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof InterruptedException) {
            apiException = new ApiException(throwable, Error.REQUEST_INTERRUPTED_ERROR);
            apiException.setErrorMessage(Error.STR_REQUEST_INTERRUPTED_ERROR);
            Log.e(this.getClass().getName(), "中断异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else {
            apiException = new ApiException(throwable, Error.UNKNOWN);
            apiException.setErrorMessage(Error.STR_UNKNOWN);
            Log.e(this.getClass().getName(), "其他异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        }

    }

    /**
     * 自定义异常抓捕器
     */
    public static class ApiException extends Exception {

        private final Throwable throwable;
        private int code;
        private String errorMessage;

       public ApiException(Throwable throwable, int code, String errorMessage) {
            this.throwable = throwable;
            this.code = code;
            this.errorMessage = errorMessage;
        }

        public ApiException(Throwable throwable, int errorCode) {
            this.throwable = throwable;
            this.code = errorCode;
        }


        public Throwable getThrowable() {
            return throwable;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        private void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    /**
     * 异常协议信息
     */
    public static class Error {
        /**
         * 未知错误
         */
        static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        static final int PARSE_ERROR = 1001;
        /**
         * 协议出错
         */
        static final int HTTP_ERROR = 1003;

        /**
         * 无网络
         */
        static final int NO_NET_ERROR = 1004;
        /**
         * 请求超时
         */
        static final int TIME_OUT_ERROR = 1005;
        /**
         * 请求取消
         */
        static final int REQUEST_INTERRUPTED_ERROR = 1006;

        static final String STR_HTTP_ERROR = "网络连接失败，请稍后连接";

        static final String STR_PARSE_ERROR = "网络连接异常，请稍后连接";

        static final String STR_NO_NET_ERROR = "无网络连接，请检查您的网络";

        static final String STR_REQUEST_INTERRUPTED_ERROR = "网络连接中断，请稍后连接";

        static final String STR_TIME_OUT_ERROR = "网络连接超时，请稍后连接";

        static final String STR_UNKNOWN = "未知错误";

    }
}
