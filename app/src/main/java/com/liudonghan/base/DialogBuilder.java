package com.liudonghan.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.liudonghan.mvp.ADBaseDialog;
import com.liudonghan.mvp.ADBaseDialogListener;
import com.liudonghan.view.city.ADCityView;

import butterknife.BindView;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/10/23
 */
public class DialogBuilder extends ADBaseDialog<ADBaseDialogListener, String> {

    @BindView(R.id.dialog_builder)
    ADCityView dialogBuilder;

    public DialogBuilder(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutResourcesId() {
        return R.layout.dialog_builder;
    }

    @Override
    protected GravityDirection getGravityDirection() {
        return GravityDirection.CENTER;
    }

    @Override
    protected int getWindowAnimations() {
        return 0;
    }

    @Override
    protected void initData(View view) {
        dialogBuilder.getAdCitySelector().setTabAmount(4);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onClickDoubleListener(View v) {

    }
}
