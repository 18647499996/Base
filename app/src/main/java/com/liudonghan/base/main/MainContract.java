package com.liudonghan.base.main;


import com.liudonghan.mvp.BasePresenter;
import com.liudonghan.mvp.BaseView;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}