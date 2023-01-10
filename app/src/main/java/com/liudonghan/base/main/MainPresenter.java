package com.liudonghan.base.main;


import com.liudonghan.mvp.ADBaseSubscription;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainPresenter extends ADBaseSubscription<MainContract.View> implements MainContract.Presenter {


    MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {

    }

}