package com.liudonghan.base.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liudonghan.base.databinding.FragmentMineBinding;
import com.liudonghan.mvp.ADBaseFragment;
import com.liudonghan.view.snackbar.ADSnackBarManager;
import com.liudonghan.view.title.ADTitleBuilder;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MineFragment extends ADBaseFragment<DemoPresenter, FragmentMineBinding> implements DemoContract.View {

//    @Override
//    protected View getViewBindingLayout() {
//        return mViewBinding.getRoot();
//    }
//
//    @Override
//    protected FragmentMineBinding getFragmentViewBinding() {
//        return FragmentMineBinding.inflate(getLayoutInflater());
//    }

    @Override
    protected ADTitleBuilder initBuilderTitle(View view) throws RuntimeException {
        return null;
    }

    @Override
    protected DemoPresenter createPresenter() {
        return (DemoPresenter) new DemoPresenter(this).builder(getActivity());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        Log.i("Mac_Liu","创建Mine");
        mViewBinding.fragmentMineTvContent.setText("Mine碎片ViewBinding");
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