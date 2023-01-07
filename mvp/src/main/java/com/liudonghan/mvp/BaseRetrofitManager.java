package com.liudonghan.mvp;

import android.util.SparseArray;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Description：Retrofit管理器（Retrofit + RxJava + OkHttp）
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public class BaseRetrofitManager {

    /**
     * retrofit配置
     */
    private Retrofit mRetrofit;

    private static SparseArray<Retrofit> retrofitManager = new SparseArray<>();
    private static SparseArray<Model> modelSparseArray = new SparseArray<>();

    private static volatile BaseRetrofitManager instance = null;


    private BaseRetrofitManager() {
    }

    public static BaseRetrofitManager getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (BaseRetrofitManager.class) {
                // double checkout
                if (null == instance) {
                    instance = new BaseRetrofitManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化retrofit（ 多域名配置 ）
     */
    public void initMultiRetrofit() {
        for (int i = 0; i < modelSparseArray.size(); i++) {
            int key = modelSparseArray.keyAt(i);
            Model model = modelSparseArray.get(key);
            initRetrofit(model.getBaseHttpUrl(), model.getBaseHttpUrlType(), model.getOkHttpClient());
        }
    }

    /**
     * 初始化Retrofit管理器配置
     *
     * @param baseHttpUrl     服务器地址
     * @param baseHttpUrlType 服务器连接类型
     * @param okHttpClient    OkHttp客户端
     */
    private void initRetrofit(String baseHttpUrl, int baseHttpUrlType, OkHttpClient okHttpClient) {
        mRetrofit = retrofitManager.get(baseHttpUrlType);
        if (mRetrofit == null) {
            // 初始化Retrofit配置
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseHttpUrl)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            retrofitManager.put(baseHttpUrlType, mRetrofit);
        }

    }

    /**
     * 初始化okHttp配置（ 单服务器）
     *
     * @return okHttp
     */
    public BaseRetrofitManager baseOkHttpClient(int baseHttpUrlType, String baseHttpUrl, OkHttpClient baseOkHttpClient) {
        // 初始化OkHttp配置
        initRetrofit(baseHttpUrl, baseHttpUrlType, baseOkHttpClient);
        return this;
    }

    /**
     * 添加客户端（ 群组模式 ）
     *
     * @param baseHttpUrl      服务器路径
     * @param baseHttpUrlType  服务器类型
     * @param baseOkHttpClient OkHttp客户端
     * @return BaseRetrofitManager
     */
    public BaseRetrofitManager addMultiOkHttpClient(String baseHttpUrl, int baseHttpUrlType, OkHttpClient baseOkHttpClient) {
        Model model = modelSparseArray.get(baseHttpUrlType);
        if (null == model) {
            modelSparseArray.put(baseHttpUrlType, new Model(baseHttpUrl, baseHttpUrlType, baseOkHttpClient));
        }
        return this;
    }

    /**
     * 切换服务
     *
     * @param tClass 服务器接口Api
     * @param <T>    接口泛型类
     * @return T
     */
    public <T> T transformService(int baseHttpUrlType, Class<T> tClass) {
        Model model = modelSparseArray.get(baseHttpUrlType);
        initRetrofit(model.getBaseHttpUrl(), model.getBaseHttpUrlType(), model.getOkHttpClient());
        return mRetrofit.create(tClass);
    }

    public static class Model {

        private String baseHttpUrl;
        private int baseHttpUrlType;
        private OkHttpClient okHttpClient;

        public String getBaseHttpUrl() {
            return baseHttpUrl;
        }

        public int getBaseHttpUrlType() {
            return baseHttpUrlType;
        }

        public OkHttpClient getOkHttpClient() {
            return okHttpClient;
        }

        public Model(String baseHttpUrl, int baseHttpUrlType, OkHttpClient okHttpClient) {
            this.baseHttpUrl = baseHttpUrl;
            this.baseHttpUrlType = baseHttpUrlType;
            this.okHttpClient = okHttpClient;
        }
    }


}
