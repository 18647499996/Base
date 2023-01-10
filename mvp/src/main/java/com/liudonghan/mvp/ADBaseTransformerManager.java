package com.liudonghan.mvp;


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
     * RxJava线程调度器（线程切换）
     *
     * @param <T> 可变参数
     * @return Observable.Transformer<BaseResult < T>, T>
     */
    public static <T> Observable.Transformer<ADBaseResult<T>, T> defaultSchedulers() {
        return baseResultObservable -> baseResultObservable
                .map(new ServerResultFunc<>())
                .onErrorResumeNext(new BaseFunction<>())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异常信息抓取器
     *
     * @param <T>
     */
    private static class BaseFunction<T> implements Func1<Throwable, Observable<T>> {

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
    private static class ServerResultFunc<T> implements Func1<ADBaseResult<T>, T> {

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

        public ServerException() {

        }

        public ServerException(int code, String msg) {
            this.code = code;
            this.msg = msg;
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
    }

    /**
     * token异常信息
     */
    public static class TokenException extends RuntimeException {

        private int code;
        private String msg;

        public TokenException(int code, String msg) {
            this.code = code;
            this.msg = msg;
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

    }
}
