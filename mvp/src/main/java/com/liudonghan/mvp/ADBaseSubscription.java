package com.liudonghan.mvp;

import android.content.Context;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Description：RxJava取消订阅及Context获取
 *
 * @author Created by: Li_Min
 * Time:2018/8/4
 */
public abstract class ADBaseSubscription<T extends ADBaseView> implements ADBasePresenter {

    protected CompositeSubscription subscriptions = new CompositeSubscription();

    private Context context;

    protected T view;

    protected ADBaseSubscription(T view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDestroy() {
        if (subscriptions != null) {
            // 销毁时统一清空页面请求链接，防止空引用操作
            subscriptions.clear();
        }
        subscriptions = null;
    }

    @Override
    public void getNetworkListData(int page, boolean refreshType, Object... o) {

    }

    @Override
    public void getNetworkListData(int page, int limit, boolean refreshType, Object... o) {

    }

    /**
     * 这里的Context根据自己项目需求调用，由于该项目将请求结果回调类（BaseRequestResult）当中统一添加了Loading加载
     *
     * @param context 上下文
     * @return BaseSubscription
     */
    public ADBaseSubscription builder(Context context) {
        this.context = context;
        return this;
    }


    public Context getContext() {
        return context;
    }

    /**
     * 移除请求订阅
     *
     * @param subscription 订阅器
     */
    public void addSubscription(Subscription subscription) {
        if (null != subscriptions) {
            subscriptions.add(subscription);
        }
    }


}
