package com.liudonghan.mvp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Description：服务器返回结果定义协议
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public class BaseResult<T> implements Serializable {
    @SerializedName(value = "code", alternate = {"status", "httpCode", "errorCode","error_code"})
    private int code;
    @SerializedName(value = "msg", alternate = {"message", "errorMsg"})
    private String msg;
    @SerializedName(value = "data", alternate = {"result","value"})
    private T data;
    @SerializedName(value = "encrypted")
    private String encrypted;
    @SerializedName(value = "timestamp")
    private String timestamp;
    @SerializedName(value = "isSuccess")
    private boolean isSuccess;
    @SerializedName(value = "count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
