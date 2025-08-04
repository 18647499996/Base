package com.liudonghan.base.activity;

import com.liudonghan.mvp.ADBasePresenter;
import com.liudonghan.mvp.ADBaseView;

public interface LoginContract {
    interface View extends ADBaseView<Presenter> {

    }

    interface Presenter extends ADBasePresenter {

    }
}
