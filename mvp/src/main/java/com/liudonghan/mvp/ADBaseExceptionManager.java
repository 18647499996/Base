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

    private static final String TAG = "Mac_Liu";

    private static volatile ADBaseExceptionManager instance = null;

    private ADBaseExceptionManager() {
    }

    public static ADBaseExceptionManager getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADBaseExceptionManager.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADBaseExceptionManager();
                }
            }
        }
        return instance;
    }

    /**
     * todo 服务器异常 默认："网络连接失败，请稍后连接"
     *
     * @param netWorkConnectError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setNetWorkConnectError(String netWorkConnectError) {
        STR_HTTP_ERROR = netWorkConnectError;
        return this;
    }

    /**
     * todo 服务器异常 默认："服务器连接异常，请稍后连接"
     *
     * @param serverError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setServerError(String serverError) {
        STR_SERVER_ERROR = serverError;
        return this;
    }

    /**
     * todo token异常 默认："用户身份过期，请重新登录"
     *
     * @param tokenError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setTokenError(String tokenError) {
        STR_TOKEN_ERROR = tokenError;
        return this;
    }

    /**
     * todo json异常 默认："网络数据获取失败，请稍后重试"
     *
     * @param jsonError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setJsonError(String jsonError) {
        STR_JSON_ERROR = jsonError;
        return this;
    }

    /**
     * todo 长链接异常 默认："无网络连接，请检查您的网络"
     *
     * @param socketError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setSocketError(String socketError) {
        STR_SOCKET_ERROR = socketError;
        return this;
    }

    /**
     * todo 超时异常 默认："网络连接超时，请稍后连接"
     *
     * @param timeOutError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setTimeOutError(String timeOutError) {
        STR_TIME_OUT_ERROR = timeOutError;
        return this;
    }

    /**
     * todo 中断异常 默认："网络连接中断，请稍后连接"
     *
     * @param interruptedError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setInterruptedError(String interruptedError) {
        STR_REQUEST_INTERRUPTED_ERROR = interruptedError;
        return this;
    }

    /**
     * todo 未知异常 默认："未知错误"
     *
     * @param unknownError 异常描述
     * @return ADBaseExceptionManager
     */
    public ADBaseExceptionManager setUnknownError(String unknownError) {
        STR_UNKNOWN = unknownError;
        return this;
    }

    /**
     * 异常信息处理
     *
     * @param throwable 处理的异常信息
     * @return apiException 异常信息
     */
    public ApiException throwableUtils(Throwable throwable) {
        ApiException apiException;
        if (throwable instanceof EOFException || throwable instanceof ConnectException || throwable instanceof SSLHandshakeException) {
            apiException = new ApiException(throwable, HTTP_ERROR);
            apiException.setErrorMessage(STR_HTTP_ERROR);
            Log.w(TAG, "网络异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof ADBaseTransformerManager.ServerException) {
            ADBaseTransformerManager.ServerException resultException = (ADBaseTransformerManager.ServerException) throwable;
            apiException = new ApiException(resultException, resultException.getCode());
            apiException.setErrorMessage(null == resultException.getMsg() ? STR_SERVER_ERROR : resultException.getMsg());
            Log.w(TAG, apiException.getErrorMessage() + "\n异常类型：服务器异常" + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage() + "\n异常地址：" + resultException.getUrl() + "\n异常参数：" + resultException.getParams());
            return apiException;
        } else if (throwable instanceof ADBaseTransformerManager.TokenException) {
            ADBaseTransformerManager.TokenException tokenException = (ADBaseTransformerManager.TokenException) throwable;
            apiException = new ApiException(tokenException, tokenException.getCode());
            apiException.setErrorMessage(STR_TOKEN_ERROR);
            Log.w(TAG, apiException.getErrorMessage() + " \n异常类型：Token异常" + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage() + "\n异常地址：" + tokenException.getUrl() + "\n异常参数：" + tokenException.getParams());
            return apiException;
        } else if (throwable instanceof JsonParseException ||
                throwable instanceof JSONException ||
                throwable instanceof ParseException) {
            apiException = new ApiException(throwable, PARSE_ERROR);
            apiException.setErrorMessage(STR_JSON_ERROR);
            Log.w(TAG, "解析异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
            //环信无网络判断
        } else if (throwable instanceof SocketException || throwable instanceof UnknownHostException || throwable instanceof ApiException) {
            apiException = new ApiException(throwable, NO_NET_ERROR);
            apiException.setErrorMessage(STR_SOCKET_ERROR);
            Log.w(TAG, "连接异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof SocketTimeoutException) {
            apiException = new ApiException(throwable, TIME_OUT_ERROR);
            apiException.setErrorMessage(STR_TIME_OUT_ERROR);
            Log.w(TAG, "超时异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else if (throwable instanceof InterruptedException) {
            apiException = new ApiException(throwable, REQUEST_INTERRUPTED_ERROR);
            apiException.setErrorMessage(STR_REQUEST_INTERRUPTED_ERROR);
            Log.w(TAG, "中断异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
            return apiException;
        } else {
            apiException = new ApiException(throwable, UNKNOWN);
            apiException.setErrorMessage(STR_UNKNOWN);
            Log.w(TAG, "其他异常：" + apiException.getErrorMessage() + "\n异常编码：" + apiException.getCode() + "\n异常信息：" + apiException.getThrowable().getMessage());
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

    public String STR_HTTP_ERROR = "网络连接失败，请稍后连接";

    public String STR_SERVER_ERROR = "服务器连接异常，请稍后连接";

    public String STR_SOCKET_ERROR = "无网络连接，请检查您的网络";

    public String STR_REQUEST_INTERRUPTED_ERROR = "网络连接中断，请稍后连接";

    public String STR_TIME_OUT_ERROR = "网络连接超时，请稍后连接";

    public String STR_TOKEN_ERROR = "用户身份过期，请重新登录";

    public String STR_JSON_ERROR = "网络数据获取失败，请稍后重试";

    public String STR_UNKNOWN = "未知错误";

}
