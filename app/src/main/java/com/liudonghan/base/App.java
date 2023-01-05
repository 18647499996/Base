package com.liudonghan.base;

import android.app.Application;

import com.liudonghan.mvp.BaseRetrofitManager;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/5/23
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseRetrofitManager
                .getInstance()
                .addMultiOkHttpClient("https://loginf.lawxp.com/",1,OkHttpUtils.getInstance().getAuthServiceConfig())
                .addMultiOkHttpClient("https://im.xinfushenghuo.cn/",2,OkHttpUtils.getInstance().getAuthServiceConfig())
                .initMultiRetrofit();
    }
}
