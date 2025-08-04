package com.liudonghan.base;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
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
