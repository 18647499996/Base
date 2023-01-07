package com.liudonghan.mvp;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.liudonghan.view.loading.LoadingDialogView;

import rx.Subscriber;

/**
 * Description：网络请求返回结果回调监听器
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public abstract class BaseRequestResult<T> extends Subscriber<T> {

    private static final int ERROR = 10000;
    private Context context;

    protected BaseRequestResult() {

    }

    protected BaseRequestResult(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != context) {
            BaseLoadingDialog.getInstance().init(context,"请求中..");
        }
    }

    @Override
    public void onCompleted() {
        onCompletedListener();
        if (null != context) {
            BaseLoadingDialog.getInstance().dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (null != context) {
            BaseLoadingDialog.getInstance().dismiss();
        }
        if (e instanceof BaseExceptionManager.ApiException) {
            onErrorListener((BaseExceptionManager.ApiException) e);
        } else {
            onErrorListener(new BaseExceptionManager.ApiException(e, ERROR, "未知错误"));
        }
    }

    @Override
    public void onNext(T t) {
        if (null != context) {
            LoadingDialogView.getInstance().dismiss();
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
    protected abstract void onErrorListener(BaseExceptionManager.ApiException e);

    /**
     * 请求成功回调
     *
     * @param t 返回泛型Model
     */
    protected abstract void onNextListener(T t);
}
