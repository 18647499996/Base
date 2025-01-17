package com.liudonghan.base.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.liudonghan.base.R;
import com.liudonghan.base.databinding.FragmentDemoBinding;
import com.liudonghan.base.databinding.FragmentFansBinding;
import com.liudonghan.base.dialog.FansDialogBuilder;
import com.liudonghan.mvp.ADBaseFragment;
import com.liudonghan.view.snackbar.ADSnackBarManager;
import com.liudonghan.view.title.ADTitleBuilder;

import java.util.Objects;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class FansFragment extends ADBaseFragment<DemoPresenter, FragmentFansBinding> implements DemoContract.View {

    @Override
    protected View getViewBindingLayout() {
        return mViewBinding.getRoot();
    }

    @Override
    protected FragmentFansBinding getFragmentViewBinding() {
        return FragmentFansBinding.inflate(getLayoutInflater());
    }


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

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        mViewBinding.fragmentFansTvContent.setText("Fans碎片ViewBinding");
    }

    @Override
    protected void onClickDoubleListener(View paramView) throws RuntimeException {

    }

    @Override
    protected void setListener() throws RuntimeException {
        mViewBinding.fragmentFansTvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FansDialogBuilder()
                        .showDialogFragment(requireActivity().getSupportFragmentManager(), "tag");
            }
        });
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