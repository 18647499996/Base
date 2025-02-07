package com.liudonghan.base.dialog;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.liudonghan.base.R;
import com.liudonghan.base.databinding.DialogFansBinding;
import com.liudonghan.mvp.ADBaseDialogFragment;
import com.liudonghan.mvp.ADBaseDialogListener;
import com.liudonghan.utils.ADScreenUtils;

import java.util.Objects;

public class FansDialogBuilder extends ADBaseDialogFragment<FansDialogBuilder.OnFansDialogBuilderListener, String, DialogFansBinding> {

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    protected GravityDirection getGravityDirection() {
        return GravityDirection.CENTER;
    }

    @Override
    protected int getWindowAnimations() {
        return 0;
    }

    @Override
    protected int getDialogStyle() {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(View view) {
        mViewBinding.dialogFansTvContent.setText("DialogFragmentViewBinding弹窗");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onClickDoubleListener(View view) {

    }

    public interface OnFansDialogBuilderListener extends ADBaseDialogListener {

    }
}
