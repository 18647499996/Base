package com.liudonghan.base.activity.photo;

import com.liudonghan.mvp.ADBasePresenter;
import com.liudonghan.mvp.ADBaseSubscription;

public class AddPhotoPresenter extends ADBaseSubscription<AddPhotoContract.View> implements AddPhotoContract.Presenter {

    protected AddPhotoPresenter(AddPhotoContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {

    }
}
