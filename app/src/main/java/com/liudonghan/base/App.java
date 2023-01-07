package com.liudonghan.base;

import android.app.Application;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.liudonghan.mvp.BaseLoadingDialog;
import com.liudonghan.mvp.BaseRetrofitManager;

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
        BaseRetrofitManager
                .getInstance()
                .addMultiOkHttpClient("https://loginf.lawxp.com/",1,OkHttpUtils.getInstance().getAuthServiceConfig())
                .addMultiOkHttpClient("https://im.xinfushenghuo.cn/",2,OkHttpUtils.getInstance().getAuthServiceConfig())
                .initMultiRetrofit();
//        BaseLoadingDialog
//                .getInstance()
//                .setContentView(R.layout.dialog_loading)
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
