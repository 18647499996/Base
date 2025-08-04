package com.liudonghan.base.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

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
import java.util.Objects;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainActivity extends ADBaseActivity<MainPresenter, ActivityMainBinding> implements MainContract.View, ADFragmentTabHost.OnADFragmentTabHostListener {

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
//        if (getPackageManager().getLaunchIntentForPackage("cn.gov.chinatax.gt4.app") != null) {
//            // App已安装
//
//            Log.i("已下载","s");
//        } else {
//            // 引导用户下载（跳转至应用市场）
//            Log.i("未下载","Ï");
//        }
        Intent intent = new Intent();
        intent.setPackage("cn.gov.chinatax.gt4.app"); // 税务App包名（需确认实际包名）
//        intent.setAction("android.intent.action.VIEW");
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("gt4app://taxapp/qrcode/scan?defaultHandle=true")); // 假设的Deep Link格式
        startActivityForResult(intent, 200);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            String authResult = Objects.requireNonNull(data).getStringExtra("auth_result");
            // 处理认证成功/失败逻辑
            Log.i("~~~~~~",authResult);
        }
    }
}
