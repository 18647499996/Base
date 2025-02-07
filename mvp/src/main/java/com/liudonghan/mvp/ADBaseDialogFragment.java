package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Objects;

/**
 * Description：DialogFragment基类
 *
 * @author Created by: Li_Min
 * Time:12/3/21
 */
public abstract class ADBaseDialogFragment<T extends ADBaseDialogListener, P, V extends ViewBinding> extends DialogFragment implements View.OnClickListener {

    private long lastClickTime = 0;
    protected T onDialogFragmentListener;
    protected P data;
    protected V mViewBinding;
    protected Dialog dialog;
    private boolean isCancelable = true, isCanceledOnTouchOutside = true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(requireActivity(), 0 == getDialogStyle() ? R.style.AD_Base_Dialog : getDialogStyle());
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        initData(view);
        initListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RtlHardcoded")
    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
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
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != onDialogFragmentListener) {
            onDialogFragmentListener.onDismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

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
     * 获取Dialog弹窗样式
     *
     * @return style样式
     */
    protected abstract int getDialogStyle();

    /**
     * 初始化数据
     *
     * @param view view引用
     */
    protected abstract void initData(View view);

    /**
     * 初始化监听
     */
    protected abstract void initListener();


    /**
     * 设置点击事件
     *
     * @param view view
     * @throws RuntimeException 异常捕获
     */
    protected abstract void onClickDoubleListener(View view);


    @Override
    public void onClick(View v) {
        // 防止快速点击（1秒响应一次）
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > 1000) {
            lastClickTime = currentTime;
            onClickDoubleListener(v);
        }
    }

    /**
     * 设置回调事件
     *
     * @param t 回调接口
     * @return DialogFragment
     */
    public ADBaseDialogFragment<T, P, V> setOnDialogFragmentListener(T t) {
        this.onDialogFragmentListener = t;
        return this;
    }

    /**
     * 显示弹窗
     *
     * @param manager Fragment管理器
     * @param tag     弹窗Tag
     * @return DialogFragment
     */
    public ADBaseDialogFragment<T, P, V> showDialogFragment(FragmentManager manager, String tag) {
        show(manager, tag);
        return this;
    }

    /**
     * 设置数据源
     *
     * @param data 数据源
     * @return BaseDialogFragment<T, P>
     */
    public ADBaseDialogFragment<T, P, V> setDialogFragmentData(P data) {
        this.data = data;
        return this;
    }

    /**
     * 设置返回键取消弹窗操作
     *
     * @param isCanceled 是否取消
     * @return BaseDialogFragment<T, P>
     */
    public ADBaseDialogFragment<T, P, V> setDialogCancelable(boolean isCanceled) {
        this.isCancelable = isCanceled;
        return this;
    }

    /**
     * 设置触摸取消弹窗操作
     *
     * @param isCanceledOnTouchOutside 是否取消
     * @return BaseDialogFragment<T, P>
     */
    public ADBaseDialogFragment<T, P, V> setDialogCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
        return this;
    }


    /**
     * 关闭弹窗
     */
    public void dismissDialogFragment() {
        if (null != getDialog()) {
            dismiss();
        }
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
