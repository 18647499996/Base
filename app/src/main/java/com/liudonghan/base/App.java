package com.liudonghan.base;

import android.app.Application;

import com.liudonghan.mvp.ADBaseExceptionManager;
import com.liudonghan.mvp.ADBaseRetrofitManager;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/5/23
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ADBaseRetrofitManager
                .getInstance()
                .addMultiOkHttpClient("https://loginf.lawxp.com/", 1, OkHttpUtils.getInstance().getAuthServiceConfig())
                .addMultiOkHttpClient("https://im.xinfushenghuo.cn/", 2, OkHttpUtils.getInstance().getAuthServiceConfig())
                .initMultiRetrofit();
        ADBaseExceptionManager
                .getInstance()
                .setTokenError("token过期");
//        BaseLoadingDialog
//                .getInstance()
//                .setContentView(R.layout.ad_dialog_loading)
//                .setCreateLoadingDialogData((view, tip) -> {
//                    TextView textView = view.findViewById(R.id.tipTextView);
//                    ImageView spaceshipImage = view.findViewById(R.id.img);
//                    textView.setText(tip);
//                    spaceshipImage.setBackgroundResource(R.drawable.progress_pull);
//                    AnimationDrawable animationDrawable = (AnimationDrawable) spaceshipImage.getBackground();
//                    animationDrawable.start();
//                });
    }
}
