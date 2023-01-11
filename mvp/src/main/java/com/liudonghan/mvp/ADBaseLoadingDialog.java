package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/7/23
 */
public class ADBaseLoadingDialog {

    private Dialog dialog;
    private View contentView;
    private Activity activity;
    private CreateLoadingDialogData createLoadingDialogData;

    @SuppressLint("StaticFieldLeak")
    private static volatile ADBaseLoadingDialog instance = null;
    private int layoutId;

    private ADBaseLoadingDialog() {
    }

    public static ADBaseLoadingDialog getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (ADBaseLoadingDialog.class) {
                // double checkout
                if (null == instance) {
                    instance = new ADBaseLoadingDialog();
                }
            }
        }
        return instance;
    }

    /**
     * 创建PopupWindow
     */
    public void init(Context context, String tip) {
        this.activity = (Activity) context;
        if (null != dialog) {
            if (dialog.isShowing()) {
                return;
            }
        }
        initView();
        initData(tip);
        initDialog();
        show();
    }

    private void initView() {
        contentView = View.inflate(activity, 0 == layoutId ? R.layout.ad_dialog_loading : layoutId, null);
    }

    /**
     * 设置dialog属性
     */
    private void initDialog() {
        dialog = new Dialog(activity, R.style.Base_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        // 设置对话框的大小
        WindowManager wm = activity.getWindowManager();
        WindowManager.LayoutParams params = Objects.requireNonNull(dialog.getWindow()).getAttributes();
        Display d = wm.getDefaultDisplay();
        params.width = (int) d.getWidth();
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setDimAmount(0f);
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 初始化数据
     *
     * @param tip 加载描述
     */
    private void initData(String tip) {
        if (0 == layoutId) {
            TextView textView = contentView.findViewById(R.id.tipTextView);
            ImageView spaceshipImage = contentView.findViewById(R.id.img);
            spaceshipImage.setBackgroundResource(R.drawable.ad_loading_progress);
            AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage.getBackground();
            animationDrawable.start();
            textView.setText(tip);
        } else {
            createLoadingDialogData.initData(contentView, tip);
        }
    }

    public ADBaseLoadingDialog setContentView(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public void setCreateLoadingDialogData(CreateLoadingDialogData createLoadingDialogData) {
        this.createLoadingDialogData = createLoadingDialogData;
    }


    private void show() {
        if (null != dialog) {
            if (dialog.isShowing()) {
                return;
            }
            dialog.show();
        }
    }

    public void dismiss() {
        try {
            if (null != dialog) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁ac引用
     */
    public void destroy() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        activity = null;
        contentView = null;
        dialog = null;
    }


    public interface CreateLoadingDialogData {

        /**
         * 初始化
         *
         * @param view view引用
         * @param tip 描述
         */
        void initData(View view, String tip);
    }
}
