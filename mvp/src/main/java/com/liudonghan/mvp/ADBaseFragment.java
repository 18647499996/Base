package com.liudonghan.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

import java.util.Calendar;

/**
 * Fragment基类
 *
 * @author liudonghan 2015-11-29
 */
public abstract class ADBaseFragment<P extends ADBasePresenter, V> extends Fragment implements OnClickListener {
    private View view;
    private long lastClickTime;
    protected ImmersionBar immersionBar;

    protected P mPresenter;
    protected V mViewBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewBinding = getFragmentViewBinding();
        return getViewBindingLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBuilderTitle(view);
        init(savedInstanceState);
        setListener();
    }

    /**
     * 加载布局
     *
     * @return 布局文件
     */
    protected abstract int loadViewLayout();

    /**
     * 加载ViewBinding布局
     *
     * @return View
     */
    protected abstract View getViewBindingLayout();

    /**
     * 构建FragmentViewBinding布局
     *
     * @return V
     */
    protected abstract V getFragmentViewBinding();

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
     */
    public void init(Bundle savedInstanceState) {
        // 初始化沉浸式
        immersionBar = ImmersionBar.with(this);
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.onSubscribe();
        }
        initData(savedInstanceState);
    }
}
