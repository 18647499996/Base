package com.liudonghan.mvp;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description：RxJava线程调度器
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public class ADBaseTransformerManager {

    /**
     * todo RxJava线程调度器（线程切换）
     *
     * @param <T> 可变参数
     * @return Observable.Transformer<BaseResult < T>, T>
     */
    public static <T> Observable.Transformer<ADBaseResult<T>, T> defaultSchedulers() {
        return baseResultObservable -> baseResultObservable
                .map(new ServerResultFunc<>())
                .onErrorResumeNext(new BaseFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    /**
     * todo RxJava线程调度器（线程切换）下载文件
     *
     * @param downloadFile 下载文件File
     * @param <T>          可变参数
     * @return Observable.Transformer<BaseResult < T>, T>
     */
    public static <T> Observable.Transformer<ResponseBody, File> defaultSchedulers(File downloadFile) {
        return requestBodyObservable -> requestBodyObservable
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(requestBody -> ADBaseFileDownloadInterceptor.writeFile(requestBody.byteStream(), downloadFile))
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * todo MediaType application/json 格式
     *
     * @param t   序列化数据模型
     * @param <T> 泛型类
     * @return RequestBody
     */
    public static <T> RequestBody transformJson(T t) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(t));
    }

    /**
     * todo MediaType application/json 格式
     *
     * @param map 参数集合
     * @return RequestBody
     */
    public static RequestBody transformJson(Map<String, String> map) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), gson.toJson(map));
    }

    /**
     * todo 上传文件
     *
     * @param file File文件
     * @return MultipartBody.Part 请求类型
     */
    public static MultipartBody.Part transformUploadFile(File file) {
        return transformUploadFile(file, file.getName());
    }

    /**
     * todo 上传文件
     *
     * @param file     File文件
     * @param fileName File文件名称
     * @return MultipartBody.Part
     */
    public static MultipartBody.Part transformUploadFile(File file, String fileName) {
        return transformUploadFile(file, fileName, "file");
    }

    /**
     * todo 上传文件
     *
     * @param file     File文件
     * @param fileName 文件名称Ï
     * @param name     key值Name
     * @return MultipartBody.Part
     */
    public static MultipartBody.Part transformUploadFile(File file, String fileName, String name) {
        return MultipartBody.Part.createFormData(name, fileName, RequestBody.create(MediaType.parse("application/octet-stream"), file));
    }

    /**
     * todo MediaType multipart/form-data
     *
     * @param map 参数集合
     * @return MultipartBody.Builder
     */
    public static MultipartBody.Builder transformData(Map<String, String> map) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> next : map.entrySet()) {
            builder.addFormDataPart(next.getKey(), next.getValue());
        }
        return builder;
    }

    /**
     * 异常信息抓取器
     *
     * @param <T>
     */
    public static class BaseFunction<T> implements Func1<Throwable, Observable<T>> {

        @Override
        public Observable<T> call(Throwable throwable) {
            return Observable.error(ADBaseExceptionManager.getInstance().throwableUtils(throwable));
        }
    }

    /**
     * 服务器HttpCode编码识别器
     *
     * @param <T>
     */
    public static class ServerResultFunc<T> implements Func1<ADBaseResult<T>, T> {

        @Override
        public T call(ADBaseResult<T> baseResult) {
            // 判断服务器code编码负数统一处理
            if (baseResult.getCode() < 0) {
                throw new ServerException(baseResult.getCode(), baseResult.getMsg());
            }
            return baseResult.getData();
        }


    }

    /**
     * 服务器异常信息
     */
    public static class ServerException extends RuntimeException {

        private int code;
        private String msg;
        private String url;
        private String params;
        private String data;
        private String headers;

        public ServerException() {

        }

        public ServerException(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public ServerException(int code, String msg, String url) {
            this.code = code;
            this.msg = msg;
            this.url = url;
        }

        public ServerException(int code, String msg, String url, String params) {
            this.code = code;
            this.msg = msg;
            this.url = url;
            this.params = params;
        }

        public ServerException(int code, String msg, String url, String params, String data) {
            this.code = code;
            this.msg = msg;
            this.url = url;
            this.params = params;
            this.data = data;
        }

        public ServerException(int code, String msg, String url, String params, String data, String headers) {
            this.code = code;
            this.msg = msg;
            this.url = url;
            this.params = params;
            this.data = data;
            this.headers = headers;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getHeaders() {
            return headers;
        }

        public void setHeaders(String headers) {
            this.headers = headers;
        }


    }

    /**
     * token异常信息
     */
    public static class TokenException extends RuntimeException {

        private int code;
        private String msg;
        private String url;
        private String params;
        private String data;
        private String headers;

        public TokenException(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public TokenException(int code, String msg, String url) {
            this.code = code;
            this.msg = msg;
            this.url = url;
        }

        public TokenException(int code, String msg, String url, String params) {
            this.code = code;
            this.msg = msg;
            this.url = url;
            this.params = params;
        }

        public TokenException(int code, String msg, String url, String params, String data) {
            this.code = code;
            this.msg = msg;
            this.url = url;
            this.params = params;
            this.data = data;
        }

        public TokenException(int code, String msg, String url, String params, String data, String headers) {
            this.code = code;
            this.msg = msg;
            this.url = url;
            this.params = params;
            this.data = data;
            this.headers = headers;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getHeaders() {
            return headers;
        }

        public void setHeaders(String headers) {
            this.headers = headers;
        }
    }
}
