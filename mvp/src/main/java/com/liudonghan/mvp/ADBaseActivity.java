package com.liudonghan.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

import java.io.IOException;
import java.util.Calendar;

import butterknife.ButterKnife;


/**
 * Description：Activity通用管理
 *
 * @author Created by: Li_Min
 * Time:2018/8/4
 */
public abstract class ADBaseActivity<P extends ADBasePresenter> extends AppCompatActivity implements View.OnClickListener {

    public ImmersionBar immersionBar;
    private long lastClickTime = 0;

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // 初始化布局
            setContentView(getLayout());
            // 初始化ButterKnife
            ButterKnife.bind(this);
            // 初始化标题
            initBuilderTitle();
            // 初始化数据
            initDatas(savedInstanceState);
            // 设置监听事件
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化布局
     *
     * @return 布局文件资源id
     * @throws RuntimeException 异常捕获
     */
    protected abstract int getLayout() throws RuntimeException;

    /**
     * 初始化标题
     *
     * @return TitleBuilder 实例
     * @throws RuntimeException 异常捕获
     */
    protected abstract Object initBuilderTitle() throws RuntimeException;

    /**
     * 初始化Presenter
     *
     * @return 返回Presenter
     * @throws RuntimeException 异常捕获
     */
    protected abstract P createPresenter() throws RuntimeException;

    /**
     * 初始化数据
     *
     * @param savedInstanceState bundle属性
     * @throws RuntimeException 异常捕获
     */
    protected abstract void initData(Bundle savedInstanceState) throws RuntimeException;

    /**
     * 添加监听事件
     *
     * @throws RuntimeException 异常捕获
     */
    protected abstract void addListener() throws RuntimeException;

    /**
     * 设置点击事件
     *
     * @param view view
     * @throws RuntimeException 异常捕获
     */
    protected abstract void onClickDoubleListener(View view) throws RuntimeException, IOException;

    /**
     * activity销毁
     *
     * @throws RuntimeException 异常捕获
     */
    protected abstract void onDestroys() throws RuntimeException;


    @Override
    public void onClick(View v) {
        // 防止快速点击（1秒响应一次）
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > 1000) {
            lastClickTime = currentTime;
            try {
                onClickDoubleListener(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * presenter 空指针异常捕获
     *
     * @param reference presenter
     * @return presenter
     */
    public static ADBasePresenter checkNotNull(ADBasePresenter reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    /**
     * 初始化数据逻辑
     *
     * @param savedInstanceState Bundle数据
     */
    public void initDatas(Bundle savedInstanceState) {
        // 初始化沉浸式
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init();

        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.onSubscribe();
        }
        initData(savedInstanceState);

    }

    /**
     * 启动activity页面
     *
     * @param context 上下文
     * @param c       启动页面
     */
    public static void startActivity(Context context, Class<?> c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    /**
     * 获取Presenter引用
     *
     * @return P
     */
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ADBaseLoadingDialog.getInstance().destroy();
        // 页面销毁时取消presenter绑定
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        onDestroys();
    }


}
