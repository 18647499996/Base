package com.liudonghan.mvp;

import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Description：Retrofit管理器（Retrofit + RxJava + OkHttp）
 *
 * 🔧 2026 架构优化版：
 * 1. 彻底消灭全局 mRetrofit 成员变量，根治多线程并发请求时的域名交织/串流 Bug。
 * 2. 对静态 SparseArray 进行同步锁保护，确保多线程初始化域名时的内存安全。
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public class ADBaseRetrofitManager {

    /**
     * 域名配置
     */
    private String baseHttpUrl;

    /**
     * OkHttp配置
     */
    private OkHttpClient baseOkHttpClient;

    // 💡 核心优化点 1：彻底删除这里的 private Retrofit mRetrofit; 全局变量！
    // 理由：多线程并发时，这个变量会被交替覆盖，导致 A 业务错调 B 域名的接口。

    // 💡 核心优化点 2：为两个静态容器提供一把专用的线程锁对象
    private static final Object LOCK = new Object();
    private static final SparseArray<Retrofit> retrofitManager = new SparseArray<>();
    private static final SparseArray<Model> modelSparseArray = new SparseArray<>();

    private static volatile ADBaseRetrofitManager instance = null;

    public ADBaseRetrofitManager() {
    }

    public static ADBaseRetrofitManager getInstance() {
        if (null == instance) {
            synchronized (ADBaseRetrofitManager.class) {
                if (null == instance) {
                    instance = new ADBaseRetrofitManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化retrofit（ 多域名配置 ）
     */
    public void initMultiRetrofit() {
        // 💡 优化点 3：读取或遍历静态 SparseArray 时加锁，防止遍历中途其他线程执行 put() 导致数组越界或崩溃
        synchronized (LOCK) {
            Log.i("Mac_Liu", "Multiple Request Size：" + modelSparseArray.size());
            for (int i = 0; i < modelSparseArray.size(); i++) {
                Model model = modelSparseArray.valueAt(i); // 比 get(key) 效率更高，直接拿 value
                initRetrofit(model.getBaseHttpUrl(), model.getBaseHttpUrlType(), model.getOkHttpClient());
            }
        }
    }

    /**
     * 初始化Retrofit配置并将其返回
     *
     * @param baseHttpUrl     服务器地址
     * @param baseHttpUrlType 服务器连接类型
     * @param okHttpClient    OkHttp客户端
     * @return Retrofit 实例
     */
    private Retrofit initRetrofit(String baseHttpUrl, int baseHttpUrlType, OkHttpClient okHttpClient) {
        // 💡 优化点 4：局部变量化 + 线程同步锁
        synchronized (LOCK) {
            Retrofit retrofit = retrofitManager.get(baseHttpUrlType);
            if (retrofit == null) {
                // 初始化Retrofit配置
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseHttpUrl)
                        .client(okHttpClient)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                retrofitManager.put(baseHttpUrlType, retrofit);
            }
            return retrofit; // 直接返回当前类型的专用实例，拒绝全局共享
        }
    }

    /**
     * 添加客户端（ 群组模式 ）
     *
     * @param baseHttpUrl      服务器路径
     * @param baseHttpUrlType  服务器类型
     * @param baseOkHttpClient OkHttp客户端
     * @return BaseRetrofitManager
     */
    public ADBaseRetrofitManager addMultiOkHttpClient(String baseHttpUrl, int baseHttpUrlType, OkHttpClient baseOkHttpClient) {
        synchronized (LOCK) { // 💡 优化点 5：写操作加锁保护
            Model model = modelSparseArray.get(baseHttpUrlType);
            if (null == model) {
                modelSparseArray.put(baseHttpUrlType, new Model(baseHttpUrl, baseHttpUrlType, baseOkHttpClient));
            }
        }
        return this;
    }

    /**
     * 切换服务 ( 群组模式 ）
     *
     * @param tClass 服务器接口Api
     * @param <T>    接口泛型类
     * @return T
     */
    public <T> T transformService(int baseHttpUrlType, Class<T> tClass) {
        Model model;
        synchronized (LOCK) {
            model = modelSparseArray.get(baseHttpUrlType);
        }
        if (model == null) {
            throw new IllegalArgumentException("请先通过 addMultiOkHttpClient 注册 baseHttpUrlType: " + baseHttpUrlType);
        }
        // 💡 优化点 6：通过局部变量接收专门的 Retrofit 实例，多线程并发时各自独立，绝不串号
        Retrofit targetRetrofit = initRetrofit(model.getBaseHttpUrl(), model.getBaseHttpUrlType(), model.getOkHttpClient());
        return targetRetrofit.create(tClass);
    }

    /**
     * 初始化okHttp配置（ 单一配置 ）
     *
     * @return okHttp
     */
    public ADBaseRetrofitManager baseOkHttpClient(OkHttpClient baseOkHttpClient) {
        this.baseOkHttpClient = baseOkHttpClient;
        return this;
    }

    /**
     * 配置服务器域名地址（ 单一配置 ）
     *
     * @param baseHttpUrl 域名地址
     * @return ADBaseRetrofitManager
     */
    public ADBaseRetrofitManager baseHttpUrl(String baseHttpUrl) {
        this.baseHttpUrl = baseHttpUrl;
        return this;
    }

    /**
     * 构建Retrofit管理器（ retrofit引用 ）
     *
     * @return Retrofit
     */
    public Retrofit baseRetrofitManager() {
        return new Retrofit.Builder()
                .baseUrl(baseHttpUrl)
                .client(null == baseOkHttpClient ? ADBaseOkHttpClient.getInstance().getOkHttpClient().build() : baseOkHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 构建Retrofit管理器（ 接口Api ）
     *
     * @param tClass 接口类
     * @param <T>    泛型
     * @return T 接口Api
     */
    public <T> T baseRetrofitManager(Class<T> tClass) {
        return baseRetrofitManager().create(tClass);
    }

    /**
     * 清空缓存
     */
    public void clear() {
        synchronized (LOCK) {
            retrofitManager.clear();
            modelSparseArray.clear();
        }
    }

    public static class Model {
        private final String baseHttpUrl;
        private final int baseHttpUrlType;
        private final OkHttpClient okHttpClient;

        public String getBaseHttpUrl() { return baseHttpUrl; }
        public int getBaseHttpUrlType() { return baseHttpUrlType; }
        public OkHttpClient getOkHttpClient() { return okHttpClient; }

        public Model(String baseHttpUrl, int baseHttpUrlType, OkHttpClient okHttpClient) {
            this.baseHttpUrl = baseHttpUrl;
            this.baseHttpUrlType = baseHttpUrlType;
            this.okHttpClient = okHttpClient;
        }
    }
}