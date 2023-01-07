package com.liudonghan.base.main;


import com.liudonghan.mvp.BaseSubscription;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainPresenter extends BaseSubscription<MainContract.View> implements MainContract.Presenter {


    MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {

    }

}