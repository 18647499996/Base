package com.liudonghan.base.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.liudonghan.base.R;
import com.liudonghan.base.databinding.ActivityMainBinding;
import com.liudonghan.base.fragment.DemoFragment;
import com.liudonghan.base.fragment.FansFragment;
import com.liudonghan.base.fragment.MineFragment;
import com.liudonghan.mvp.ADBaseActivity;
import com.liudonghan.view.tabhost.ADFragmentTabHost;
import com.liudonghan.view.tabhost.ADNavigationEntity;
import com.liudonghan.view.tabhost.FragmentTabHost;
import com.liudonghan.view.tabhost.TabHostAdapter;
import com.liudonghan.view.title.ADTitleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainActivity extends ADBaseActivity<MainPresenter, ActivityMainBinding> implements MainContract.View, ADFragmentTabHost.OnADFragmentTabHostListener {

//    @Override
//    protected View getViewBindingLayout() throws RuntimeException {
//        return mViewBinding.getRoot();
//    }
//
//    @Override
//    protected ActivityMainBinding getActivityBinding() throws RuntimeException {
//        return ActivityMainBinding.inflate(getLayoutInflater());
//    }

    @Override
    protected ADTitleBuilder initBuilderTitle() throws RuntimeException {
        return null;
    }

    @Override
    protected MainPresenter createPresenter() throws RuntimeException {
        return (MainPresenter) new MainPresenter(this).builder(this);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        List<ADNavigationEntity> tabs = new ArrayList<>();
        tabs.add(new ADNavigationEntity("首页", R.color.color_eb2525, R.color.color_7c7c7c, new DemoFragment(), true));
        tabs.add(new ADNavigationEntity("发现", R.color.color_eb2525, R.color.color_7c7c7c, new FansFragment(), false));
        tabs.add(new ADNavigationEntity("推荐", R.color.color_eb2525, R.color.color_7c7c7c, new MineFragment(), false));
        mViewBinding.activityMainTabHost.setData(tabs);
    }

    @Override
    protected void addListener() throws RuntimeException {
        mViewBinding.activityMainTabHost.setOnADFragmentTabHostListener(this);
    }

    @Override
    protected void onClickDoubleListener(View view) throws RuntimeException {

    }

    @Override
    protected void onDestroys() throws RuntimeException {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = (MainPresenter) checkNotNull(presenter);
    }

    @Override
    public void showErrorMessage(String msg) {

    }


    @Override
    public void onTabHost(ADNavigationEntity item, int position, FragmentTabHost fragmentTabHost, TabHostAdapter tabHostAdapter) {

    }
}
