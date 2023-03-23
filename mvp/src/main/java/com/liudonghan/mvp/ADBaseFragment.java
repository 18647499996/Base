package com.liudonghan.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 *
 * @author liudonghan 2015-11-29
 */
public abstract class ADBaseFragment<P extends ADBasePresenter, A> extends Fragment implements OnClickListener {
    private Unbinder unbinder;
    private View view;
    private long lastClickTime;
    protected ImmersionBar immersionBar;

    protected P mPresenter;

    protected A currentActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = initView(inflater, container, savedInstanceState);
        } else {
            container = (ViewGroup) view.getParent();
        }
        if (container != null) {
            container.removeView(view);
        }
        return view;

    }

    /**
     * 初始化布局
     */
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            View pView = inflater.inflate(loadViewLayout(), container, false);
            unbinder = ButterKnife.bind(this, pView);
            initBuilderTitle(pView);
            initDatas(savedInstanceState, pView);
            setListener();
            return pView;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加载布局
     *
     * @return 布局文件
     */
    protected abstract int loadViewLayout();

    /**
     * 初始化标题
     *
     * @param view 布局View
     * @return TitleBuilder 实例
     * @throws RuntimeException 异常捕获
     */
    protected abstract Object initBuilderTitle(View view) throws RuntimeException;

    /**
     * 初始化Presenter
     *
     * @return p 泛型类Presenter
     */
    protected abstract P createPresenter();

    /**
     * 初始化数据
     *
     * @param savedInstanceState 初始化数据
     * @throws RuntimeException 异常捕获
     */
    protected abstract void initData(Bundle savedInstanceState) throws RuntimeException;

    /**
     * 处理点击事件
     *
     * @param paramView 发生点击事件的组件
     * @throws RuntimeException 异常捕获
     */
    protected abstract void onClickDoubleListener(View paramView) throws RuntimeException;

    /**
     * 设置监听器
     *
     * @throws RuntimeException 异常捕获
     */
    protected abstract void setListener() throws RuntimeException;

    @Override
    public void onClick(View v) {
        // 防止快速点击（1秒响应一次）
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > 1000) {
            lastClickTime = currentTime;
            onClickDoubleListener(v);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
//        if (null != immersionBar) {
//            immersionBar.destroy();
//        }
        //页面销毁时取消presenter绑定
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public static ADBasePresenter checkNotNull(ADBasePresenter reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    /**
     * 初始化数据（沉浸式状态栏）
     *
     * @param savedInstanceState intent数据
     * @param pView              view视图
     */
    public void initDatas(Bundle savedInstanceState, View pView) {
        // 初始化沉浸式
        immersionBar = ImmersionBar.with(this);
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.onSubscribe();
        }
        initData(savedInstanceState);
    }

    public A getCurrentActivity() {
        return currentActivity = (A) getActivity();
    }
}
