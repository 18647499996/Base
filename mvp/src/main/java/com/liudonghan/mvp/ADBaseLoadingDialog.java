package com.liudonghan.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
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

    private Dialog popupWindow;
    private View contentView;
    private Activity activity;

    @SuppressLint("StaticFieldLeak")
    private static volatile ADBaseLoadingDialog instance = null;

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
    public void init(Activity activity, String tip) {
        this.activity = activity;
        if (null != popupWindow) {
            if (popupWindow.isShowing()) {
                return;
            }
        }
        contentView = View.inflate(activity, R.layout.ad_dialog_loading, null);
        popupWindow = new Dialog(activity, R.style.AD_Base_Dialog);
        // 去除标题
        popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 添加一个自定义布局
        popupWindow.setContentView(contentView);
        //isUpload为true时关闭返回按钮
//        if (isUpload) {
//            popupWindow.setCancelable(false);
//            popupWindow.setCanceledOnTouchOutside(false);
//        }
        TextView textView = contentView.findViewById(R.id.tipTextView);
        ImageView spaceshipImage = contentView.findViewById(R.id.img);
        spaceshipImage.setBackgroundResource(R.drawable.ad_loading_progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage.getBackground();
        animationDrawable.start();
        if (TextUtils.isEmpty(tip)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(tip);
            textView.setVisibility(View.VISIBLE);
        }
        // 设置对话框的大小
        WindowManager wm = activity.getWindowManager();
        WindowManager.LayoutParams params = Objects.requireNonNull(popupWindow.getWindow()).getAttributes();
        // 获取屏幕宽高
        Display d = wm.getDefaultDisplay();
        // 设置高度为屏幕的0.6
        params.width = (int) d.getWidth();
        popupWindow.getWindow().setAttributes(params);
        popupWindow.getWindow().setDimAmount(0f);
        popupWindow.setCanceledOnTouchOutside(false);
        show();
    }


    private void show() {
        if (null != popupWindow) {
            if (popupWindow.isShowing()) {
                return;
            }
            popupWindow.show();
        }
    }

    public void dismiss() {
        try {
            if (null != popupWindow) {
                popupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁ac引用
     */
    public void destory() {
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        activity = null;
        contentView = null;
        popupWindow = null;
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
