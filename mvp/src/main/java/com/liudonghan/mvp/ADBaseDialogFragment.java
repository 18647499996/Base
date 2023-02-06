package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description：DialogFragment基类
 *
 * @author Created by: Li_Min
 * Time:12/3/21
 */
public abstract class ADBaseDialogFragment<T extends ADBaseDialogListener, P> extends DialogFragment implements View.OnClickListener {

    private Unbinder bind;
    private long lastClickTime = 0;
    protected T onDialogFragmentListener;
    protected P data;
    protected Dialog dialog;
    private boolean isCancelable, isCanceledOnTouchOutside;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(requireActivity(), 0 == getDialogStyle() ? R.style.Base_Dialog : getDialogStyle());
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourcesId(), container, false);
        bind = ButterKnife.bind(this, view);
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        initData(view);
        initListener();
        return view;
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
            WindowManager windowManager = requireActivity().getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (display.getWidth());
            window.setAttributes(params);
        }
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
        if (null != bind) {
            bind.unbind();
        }
    }

    /**
     * 添加Dialog弹窗布局
     *
     * @return 布局ID
     */
    protected abstract int getLayoutResourcesId();

    /**
     * 弹出方向
     *
     * @return GravityDirection
     */
    protected abstract GravityDirection getGravityDirection();

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
    public ADBaseDialogFragment<T, P> setOnDialogFragmentListener(T t) {
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
    public ADBaseDialogFragment<T, P> showDialogFragment(FragmentManager manager, String tag) {
        show(manager, tag);

        return this;
    }

    /**
     * 设置数据源
     *
     * @param data 数据源
     * @return BaseDialogFragment<T, P>
     */
    public ADBaseDialogFragment<T, P> setDialogFragmentData(P data) {
        this.data = data;
        return this;
    }

    /**
     * 设置返回键取消弹窗操作
     *
     * @param isCanceled 是否取消
     * @return BaseDialogFragment<T, P>
     */
    public ADBaseDialogFragment<T, P> setDialogCancelable(boolean isCanceled) {
        this.isCancelable = isCanceled;
        return this;
    }

    /**
     * 设置触摸取消弹窗操作
     *
     * @param isCanceledOnTouchOutside 是否取消
     * @return BaseDialogFragment<T, P>
     */
    public ADBaseDialogFragment<T, P> setDialogCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
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
