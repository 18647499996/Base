package com.liudonghan.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liudonghan.base.R;
import com.liudonghan.base.main.MainActivity;
import com.liudonghan.mvp.ADBaseFragment;
import com.liudonghan.mvp.ADBaseLoadingDialog;
import com.liudonghan.view.snackbar.ADSnackBarManager;
import com.liudonghan.view.title.ADTitleBuilder;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class DemoFragment extends ADBaseFragment<DemoPresenter, MainActivity> implements DemoContract.View {

    public String currentFragment = "currentFragment";

    @Override
    protected int loadViewLayout() {
        return R.layout.fragment_demo;
    }

    @Override
    protected ADTitleBuilder initBuilderTitle(View view) throws RuntimeException {
        return null;
    }

    @Override
    protected DemoPresenter createPresenter() {
        return (DemoPresenter) new DemoPresenter(this).builder(getActivity());
    }

    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {

    }

    @Override
    protected void onClickDoubleListener(View paramView) throws RuntimeException {

    }

    @Override
    protected void setListener() throws RuntimeException {

    }

    @Override
    public void setPresenter(DemoContract.Presenter presenter) {
        mPresenter = (DemoPresenter) checkNotNull(presenter);
    }

    @Override
    public void showErrorMessage(String msg) {
        ADSnackBarManager.getInstance().showError(getActivity(), msg);
    }
}