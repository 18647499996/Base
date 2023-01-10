package com.liudonghan.mvp;

import android.content.Context;

import rx.Subscriber;

/**
 * Description：网络请求返回结果回调监听器
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public abstract class ADBaseRequestResult<T> extends Subscriber<T> {

    private static final int ERROR = 10000;
    private Context context;

    protected ADBaseRequestResult() {

    }

    protected ADBaseRequestResult(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != context) {
            ADBaseLoadingDialog.getInstance().init(context,"请求中..");
        }
    }

    @Override
    public void onCompleted() {
        onCompletedListener();
        if (null != context) {
            ADBaseLoadingDialog.getInstance().dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (null != context) {
            ADBaseLoadingDialog.getInstance().dismiss();
        }
        if (e instanceof ADBaseExceptionManager.ApiException) {
            onErrorListener((ADBaseExceptionManager.ApiException) e);
        } else {
            onErrorListener(new ADBaseExceptionManager.ApiException(e, ERROR, "未知错误"));
        }
    }

    @Override
    public void onNext(T t) {
        if (null != context) {
            ADBaseLoadingDialog.getInstance().dismiss();
        }
        onNextListener(t);
    }


    /**
     * 请求完成回调
     */
    protected abstract void onCompletedListener();

    /**
     * 请求异常回调
     *
     * @param e 异常信息
     */
    protected abstract void onErrorListener(ADBaseExceptionManager.ApiException e);

    /**
     * 请求成功回调
     *
     * @param t 返回泛型Model
     */
    protected abstract void onNextListener(T t);
}
