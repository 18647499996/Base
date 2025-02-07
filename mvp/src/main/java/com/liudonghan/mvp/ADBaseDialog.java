package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Objects;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/10/23
 */
public abstract class ADBaseDialog<L extends ADBaseDialogListener, T, V extends ViewBinding> extends Dialog implements View.OnClickListener {

    protected Context context;
    //    public Unbinder bind;
    protected T data;
    protected L listener;
    protected V mViewBinding;
    private long lastClickTime = 0;
    private boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;

    /**
     * Dialog弹窗控件元素显示方向
     * GravityDirection.CENTER （ 居中 ）
     * GravityDirection.TOP    （ 顶部 ）
     * GravityDirection.LEFT   （ 左侧 ）
     * GravityDirection.RIGHT  （ 右侧 ）
     * GravityDirection.BOTTOM （ 底部 ）
     *
     * @return GravityDirection
     */
    protected abstract GravityDirection getGravityDirection();

    /**
     * 是否全屏显示
     *
     * @return true 全屏 false 包裹
     */
    protected abstract boolean isFullScreen();

    /**
     * Dialog显示动画
     *
     * @return style动画
     */
    protected abstract int getWindowAnimations();

    /**
     * 初始化数据
     *
     * @param savedInstanceState intent实例
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 设置点击事件
     *
     * @param v 视图控件
     * @throws RuntimeException 异常捕获
     */
    protected abstract void onClickDoubleListener(View v);

    public ADBaseDialog(@NonNull Context context) {
        super(context, R.style.AD_Base_Dialog);
        this.context = context;
    }

    public ADBaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(Objects.requireNonNull(mViewBinding).getRoot());
        initData(savedInstanceState);
        initListener();
        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

//    protected abstract V getDialogViewBinding();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RtlHardcoded")
    @Override
    public void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window != null) {
            // Dialog弹出方向
            switch (getGravityDirection()) {
                case TOP:
                    window.setGravity(Gravity.TOP);
                    break;
                case LEFT:
                    window.setGravity(Gravity.LEFT);
                    break;
                case RIGHT:
                    window.setGravity(Gravity.RIGHT);
                    break;
                case BOTTOM:
                    window.setGravity(Gravity.BOTTOM);
                    break;
                case CENTER:
                    window.setGravity(Gravity.CENTER);
                    break;
                default:
                    break;
            }
            // Dialog显示动画
            if (0 != getWindowAnimations()) {
                window.setWindowAnimations(getWindowAnimations());
            }
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, isFullScreen() ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private static <V extends ViewBinding> Method getMethod(ParameterizedType type) throws NoSuchMethodException {
        Type[] typeArguments = type.getActualTypeArguments();
        //获得泛型中的实际类型，可能会存在多个泛型，[0]也就是获得T的type
        Class<V> vClass = (Class<V>) typeArguments[2];
        //从 clazz 所代表的类中查找一个名为"inflate" 的公共方法，该方法接受一个 LayoutInflater 类型的参数，
        // 并返回一个 Method 对象，该对象代表了找到的这个方法。
        return vClass.getMethod("inflate", LayoutInflater.class);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

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
    public void dismiss() {
        super.dismiss();
        if (null != listener) {
            listener.onDismiss();
        }
    }

    /**
     * 设置返回键取消弹窗操作
     *
     * @param isCanceled 是否取消
     * @return ADBaseDialog
     */
    public ADBaseDialog<L, T, V> setDialogCancelable(boolean isCanceled) {
        this.cancelable = isCanceled;
        return this;
    }

    /**
     * 设置触摸取消弹窗操作
     *
     * @param isCanceledOnTouchOutside 是否取消
     * @return ADBaseDialog
     */
    public ADBaseDialog<L, T, V> setDialogCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        this.canceledOnTouchOutside = isCanceledOnTouchOutside;
        return this;
    }

    /**
     * 显示弹窗
     */
    public ADBaseDialog<L, T, V> showDialogFragment() {
        show();
        return this;
    }

    /**
     * 添加数据源
     *
     * @param data 数据源
     * @return ADBaseDialog<L, T>
     */
    public ADBaseDialog<L, T, V> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置回调事件
     *
     * @param listener 回调接口
     * @return DialogFragment
     */
    public ADBaseDialog<L, T, V> setOnDialogListener(L listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 布局弹窗方向
     */
    public enum GravityDirection {
        // 居中
        CENTER,
        // 居上
        TOP,
        // 居下
        BOTTOM,
        // 居左
        LEFT,
        // 居右
        RIGHT
    }
}
