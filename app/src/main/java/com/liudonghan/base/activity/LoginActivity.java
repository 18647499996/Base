package com.liudonghan.base.activity;

import android.os.Bundle;
import android.view.View;

import com.liudonghan.base.activity.photo.AddPhotoActivity;
import com.liudonghan.base.databinding.ActivityLoginBinding;
import com.liudonghan.mvp.ADBaseActivity;
import com.liudonghan.utils.ADIntentManager;

import java.io.IOException;

public class LoginActivity extends ADBaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginContract.View {
    @Override
    protected Object initBuilderTitle() throws RuntimeException {
        return null;
    }

    @Override
    protected LoginPresenter createPresenter() throws RuntimeException {
        return (LoginPresenter) new LoginPresenter(this).builder(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {

    }

    @Override
    protected void addListener() throws RuntimeException {
        mViewBinding.activityLoginBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADIntentManager.getInstance()
                        .from(LoginActivity.this)
                        .startClass(AddPhotoActivity.class)
                        .builder();
            }
        });
    }

    @Override
    protected void onClickDoubleListener(View view) throws RuntimeException, IOException {

    }

    @Override
    protected void onDestroys() throws RuntimeException {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = (LoginPresenter) checkNotNull(presenter);
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
