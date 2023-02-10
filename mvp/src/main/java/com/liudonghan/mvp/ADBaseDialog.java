package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/10/23
 */
public abstract class ADBaseDialog<L extends ADBaseDialogListener, T> extends Dialog {

    public Context context;
    public Unbinder bind;
    public boolean isCancelable, isCanceledOnTouchOutside;
    public T data;
    public L listener;

    public abstract int getLayoutResourcesId();

    protected abstract GravityDirection getGravityDirection();

    protected abstract int getWindowAnimations();

    protected abstract void initData(View view);

    protected abstract void initListener();

    public ADBaseDialog(@NonNull Context context) {
        super(context, R.style.Base_Dialog);
        this.context = context;
    }

    public ADBaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = View.inflate(context, getLayoutResourcesId(), null);
        setContentView(inflate);
        bind = ButterKnife.bind(this);
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        initData(inflate);
        initListener();
    }


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
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != bind) {
            bind.unbind();
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
    public ADBaseDialog<L, T> setDialogCancelable(boolean isCanceled) {
        this.isCancelable = isCanceled;
        return this;
    }

    /**
     * 设置触摸取消弹窗操作
     *
     * @param isCanceledOnTouchOutside 是否取消
     * @return ADBaseDialog
     */
    public ADBaseDialog<L, T> setDialogCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
        return this;
    }

    /**
     * 显示弹窗
     */
    public void showDialogFragment() {
        show();
    }

    /**
     * 添加数据源
     *
     * @param data 数据源
     * @return ADBaseDialog<L, T>
     */
    public ADBaseDialog<L, T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置回调事件
     *
     * @param listener 回调接口
     * @return DialogFragment
     */
    public ADBaseDialog<L, T> setOnDialogListener(L listener) {
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