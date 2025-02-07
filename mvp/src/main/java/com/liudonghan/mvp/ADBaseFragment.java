package com.liudonghan.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Objects;

/**
 * Fragment基类
 *
 * @author liudonghan 2015-11-29
 */
public abstract class ADBaseFragment<P extends ADBasePresenter, V extends ViewBinding> extends Fragment implements OnClickListener {
    private View view;
    private long lastClickTime;
    protected ImmersionBar immersionBar;

    protected P mPresenter;
    protected V mViewBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            // 返回当前类的父类的Type，也就是BaseActivity
            // getGenericSuperclass() 返回的是 Type 对象，它可以包含泛型信息
            Type type = this.getClass().getGenericSuperclass();
            // ParameterizedType 对象 :它代表一个具有实际类型参数的泛型类型
            if (type instanceof ParameterizedType) {
                Method method = getMethod((ParameterizedType) type);
                // 方法调用，获得viewBinding实例
                mViewBinding = (V) method.invoke(null, getLayoutInflater());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(mViewBinding).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBuilderTitle(view);
        init(savedInstanceState);
        setListener();
    }

    private static <V extends ViewBinding> Method getMethod(ParameterizedType type) throws NoSuchMethodException {
        Type[] typeArguments = type.getActualTypeArguments();
        //获得泛型中的实际类型，可能会存在多个泛型，[0]也就是获得T的type
        Class<V> vClass = (Class<V>) typeArguments[1];
        //从 clazz 所代表的类中查找一个名为"inflate" 的公共方法，该方法接受一个 LayoutInflater 类型的参数，
        // 并返回一个 Method 对象，该对象代表了找到的这个方法。
        return vClass.getMethod("inflate", LayoutInflater.class);
    }

//    /**
//     * 加载ViewBinding布局
//     *
//     * @return View
//     */
//    protected abstract View getViewBindingLayout();
//
//    /**
//     * 构建FragmentViewBinding布局
//     *
//     * @return V
//     */
//    protected abstract V getFragmentViewBinding();

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
        mViewBinding = null;
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
