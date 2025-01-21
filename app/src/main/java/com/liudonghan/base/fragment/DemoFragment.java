package com.liudonghan.base.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.liudonghan.base.dialog.DialogBuilder;
import com.liudonghan.base.R;
import com.liudonghan.base.databinding.FragmentDemoBinding;
import com.liudonghan.mvp.ADBaseFragment;
import com.liudonghan.view.calendar.ADCalendarEntity;
import com.liudonghan.view.snackbar.ADSnackBarManager;
import com.liudonghan.view.title.ADTitleBuilder;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class DemoFragment extends ADBaseFragment<DemoPresenter, FragmentDemoBinding> implements DemoContract.View {

    @Override
    protected View getViewBindingLayout() {
        return mViewBinding.getRoot();
    }

    @Override
    protected FragmentDemoBinding getFragmentViewBinding() {
        return FragmentDemoBinding.inflate(getLayoutInflater());
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
        Log.i("Mac_Liu","创建Home");
        mViewBinding.fragmentDemoTvContent.setText("Demo碎片ViewBinding");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onClickDoubleListener(View paramView) throws RuntimeException {
        if (paramView.getId() == R.id.fragment_demo_tv_content) {
            new DialogBuilder(requireActivity())
                    .setDialogCancelable(true)
                    .setDialogCanceledOnTouchOutside(true)
                    .setData(new ADCalendarEntity())
                    .setOnDialogListener(new DialogBuilder.OnDialogBuilderListener() {
                        @Override
                        public void itemClick(String log) {
                            ADSnackBarManager.getInstance().show(getActivity(), log,R.color.color_7c7c7c);
                        }

                        @Override
                        public void onDismiss() {

                        }
                    })
                    .showDialogFragment();
        }
    }

    @Override
    protected void setListener() throws RuntimeException {
        mViewBinding.fragmentDemoTvContent.setOnClickListener(this);
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