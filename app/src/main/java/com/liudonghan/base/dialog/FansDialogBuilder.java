package com.liudonghan.base.dialog;

import android.annotation.SuppressLint;
import android.view.View;

import com.liudonghan.base.R;
import com.liudonghan.base.databinding.DialogFansBinding;
import com.liudonghan.mvp.ADBaseDialogFragment;
import com.liudonghan.mvp.ADBaseDialogListener;

public class FansDialogBuilder extends ADBaseDialogFragment<FansDialogBuilder.OnFansDialogBuilderListener, String, DialogFansBinding> {
    @Override
    protected DialogFansBinding getDialogFragmentViewBinding() {
        return DialogFansBinding.inflate(getLayoutInflater());
    }

    @Override
    protected View getLayoutResourcesId() {
        return mViewBinding.getRoot();
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
