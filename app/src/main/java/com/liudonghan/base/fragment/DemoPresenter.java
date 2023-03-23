package com.liudonghan.base.fragment;


import com.liudonghan.mvp.ADBaseSubscription;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class DemoPresenter extends ADBaseSubscription<DemoContract.View> implements DemoContract.Presenter {


    DemoPresenter(DemoContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {

    }

}