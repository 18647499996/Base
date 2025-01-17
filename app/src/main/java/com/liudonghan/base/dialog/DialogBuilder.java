package com.liudonghan.base.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.liudonghan.base.R;
import com.liudonghan.base.databinding.DialogBuilderBinding;
import com.liudonghan.mvp.ADBaseDialog;
import com.liudonghan.mvp.ADBaseDialogListener;
import com.liudonghan.view.calendar.ADCalendarEntity;
import com.liudonghan.view.city.ADCityModel;
import com.liudonghan.view.city.ADCitySelector;
import com.liudonghan.view.city.OnADItemClickListener;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/10/23
 */
public class DialogBuilder extends ADBaseDialog<DialogBuilder.OnDialogBuilderListener, ADCalendarEntity, DialogBuilderBinding> implements OnADItemClickListener {

    public DialogBuilder(@NonNull Context context) {
        super(context);
    }

    @Override
    protected DialogBuilderBinding getDialogViewBinding() {
        return DialogBuilderBinding.inflate(getLayoutInflater());
    }

    @Override
    public View getLayoutResourcesId() {
        return mViewBinding.getRoot();
    }

    @Override
    protected GravityDirection getGravityDirection() {
        return GravityDirection.TOP;
    }

    @Override
    protected int getWindowAnimations() {
        return R.style.AD_PopupWindow_Style_Top;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        mViewBinding.dialogBuilderTvContent.setText("Dialog弹窗ViewBinding");

    }

    @Override
    protected void initListener() {
        mViewBinding.dialogBuilderTvContent.setOnClickListener(v -> listener.itemClick("点击回调事件"));
    }

    @Override
    protected void onClickDoubleListener(View v) {

    }

    @Override
    public void itemClick(ADCitySelector addressSelector, ADCityModel city, int tabPosition, int position) {

    }

    public interface OnDialogBuilderListener extends ADBaseDialogListener {

        void itemClick(String log);
    }
}
