package com.liudonghan.base.activity;

import com.liudonghan.mvp.ADBaseSubscription;

public class LoginPresenter extends ADBaseSubscription<LoginContract.View> implements LoginContract.Presenter {

    protected LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {

    }
}
