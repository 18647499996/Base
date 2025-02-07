package com.liudonghan.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Objects;


/**
 * Description：Activity通用管理
 *
 * @author Created by: Li_Min
 * Time:2018/8/4
 */
public abstract class ADBaseActivity<P extends ADBasePresenter, V extends ViewBinding> extends AppCompatActivity implements View.OnClickListener {

    public ImmersionBar immersionBar;
    private long lastClickTime = 0;

    protected P mPresenter;
    protected V mViewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            // 初始化布局
            setContentView(Objects.requireNonNull(mViewBinding).getRoot());
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

    private static <V extends ViewBinding> Method getMethod(ParameterizedType type) throws NoSuchMethodException {
        Type[] typeArguments = type.getActualTypeArguments();
        //获得泛型中的实际类型，可能会存在多个泛型，[0]也就是获得T的type
        Class<V> vClass = (Class<V>) typeArguments[1];
        //从 clazz 所代表的类中查找一个名为"inflate" 的公共方法，该方法接受一个 LayoutInflater 类型的参数，
        // 并返回一个 Method 对象，该对象代表了找到的这个方法。
        return vClass.getMethod("inflate", LayoutInflater.class);
    }


//    /**
//     * 构建ActivityViewBinding实例
//     *
//     * @return V
//     * @throws RuntimeException
//     */
//    protected abstract V getActivityBinding() throws RuntimeException;
//
//    /**
//     * 加载ViewBinding布局
//     *
//     * @return View
//     */
//    protected abstract View getViewBindingLayout() throws RuntimeException;


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
