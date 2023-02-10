package com.liudonghan.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.liudonghan.mvp.ADBaseDialog;
import com.liudonghan.mvp.ADBaseDialogListener;

import butterknife.BindView;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2/10/23
 */
public class DialogBuilder extends ADBaseDialog<ADBaseDialogListener, String> {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tipTextView)
    TextView tipTextView;

    public DialogBuilder(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutResourcesId() {
        return R.layout.ad_dialog_loading;
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
        tipTextView.setText(data);
    }

    @Override
    protected void initListener() {

    }
}
