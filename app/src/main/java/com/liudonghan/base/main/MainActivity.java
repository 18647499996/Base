package com.liudonghan.base.main;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;

import com.liudonghan.base.ChatService;
import com.liudonghan.base.DialogBuilder;
import com.liudonghan.base.OkHttpUtils;
import com.liudonghan.base.R;
import com.liudonghan.base.UserModel;
import com.liudonghan.base.UserService;
import com.liudonghan.base.WHBNewDetailsBean;
import com.liudonghan.base.WallpaperInterceptor;
import com.liudonghan.base.fragment.DemoFragment;
import com.liudonghan.multi_image.permission.ADPermission;
import com.liudonghan.multi_image.permission.OnPermission;
import com.liudonghan.multi_image.permission.Permission;
import com.liudonghan.mvp.ADBaseActivity;
import com.liudonghan.mvp.ADBaseCodeInterceptor;
import com.liudonghan.mvp.ADBaseDialogListener;
import com.liudonghan.mvp.ADBaseExceptionManager;
import com.liudonghan.mvp.ADBaseFileDownloadInterceptor;
import com.liudonghan.mvp.ADBaseLoadingDialog;
import com.liudonghan.mvp.ADBaseLogInterceptor;
import com.liudonghan.mvp.ADBaseOkHttpClient;
import com.liudonghan.mvp.ADBasePopupWindow;
import com.liudonghan.mvp.ADBaseRequestResult;
import com.liudonghan.mvp.ADBaseRetrofitManager;
import com.liudonghan.mvp.ADBaseTransformerManager;
import com.liudonghan.utils.ADParamsUtils;
import com.liudonghan.view.snackbar.ADSnackBarManager;
import com.liudonghan.view.title.ADTitleBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainActivity extends ADBaseActivity<MainPresenter> implements MainContract.View {

    public String main = "currentActivity";
    private Button button;

    @Override
    protected int getLayout() throws RuntimeException {
        return R.layout.activity_main;
    }

    @Override
    protected ADTitleBuilder initBuilderTitle() throws RuntimeException {
        return new ADTitleBuilder(this).setMiddleTitleBgRes("首页").setLeftImageRes(R.drawable.ad_back_black).setLeftRelativeLayoutListener(this);
    }

    @Override
    protected MainPresenter createPresenter() throws RuntimeException {
        return (MainPresenter) new MainPresenter(this).builder(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        button = (Button) findViewById(R.id.btn3);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, new DemoFragment(), "Demo");
        fragmentTransaction.commit();
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADBaseLoadingDialog.getInstance().init(MainActivity.this, "点击加载");
                ADBaseRetrofitManager.getInstance()
                        .baseHttpUrl("https://bing.icodeq.com/")
                        .baseOkHttpClient(ADBaseOkHttpClient.getInstance()
                                .getOkHttpClient(
                                        new WallpaperInterceptor(),
                                        new ADBaseFileDownloadInterceptor(new ADBaseFileDownloadInterceptor.DownloadListener() {
                                            @Override
                                            public void onStartDownload(long length) {
                                                Log.i("Mac_Liu", "start download " + length);
                                            }

                                            @Override
                                            public void onProgress(int progress, int read, long totalSize) {
                                                Log.i("Mac_Liu", "progress " + progress + "  total " + totalSize);
                                            }

                                            @Override
                                            public void onFail(String errorInfo) {
                                                Log.i("Mac_Liu", "download error" + errorInfo);
                                            }

                                            @Override
                                            public void onSucceed(File file) {
                                                Log.i("Mac_Liu", "download succeed" + file);
                                            }
                                        })).build())
                        .baseRetrofitManager(ChatService.class)
                        .getWallpaper()
                        .compose(ADBaseTransformerManager.defaultSchedulers(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/", "dog_" + System.currentTimeMillis() + ".jpg")))
                        .subscribe(new ADBaseRequestResult<File>(MainActivity.this) {
                            @Override
                            protected void onCompletedListener() {

                            }

                            @Override
                            protected void onErrorListener(ADBaseExceptionManager.ApiException e) {

                            }

                            @Override
                            protected void onNextListener(File file) {
                                Log.i("Mac_Liu", "file path :" + file.toString());
                            }
                        });
            }
        });

        ADBaseRetrofitManager
                .getInstance()
                .transformService(2, ChatService.class)
                .getUserInfo()
                .compose(ADBaseTransformerManager.defaultSchedulers())
                .subscribe(new ADBaseRequestResult<UserModel>(this) {
                    @Override
                    protected void onCompletedListener() {

                    }

                    @Override
                    protected void onErrorListener(ADBaseExceptionManager.ApiException e) {

                    }

                    @Override
                    protected void onNextListener(UserModel userModel) {

                    }
                });
        ADBaseRetrofitManager
                .getInstance()
                .baseHttpUrl("https://loginf.lawxp.com/")
                .baseOkHttpClient(OkHttpUtils.getInstance().getAuthServiceConfig())
                .baseRetrofitManager(UserService.class)
                .getUserInfo("eyJhbGciOiJSUzI1NiIsImtpZCI6IjcyRENCNzE2RTE3NzAzMjQxQjM5QzU4NTlCQjNDNDI5IiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2NzI3MTc3MjIsImV4cCI6MTY3MzU4MTcyMiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5sYXd4cC5jb20iLCJjbGllbnRfaWQiOiJhcHAiLCJzdWIiOiI3MThfaXN3ZWJvYTpUcnVlX2lzd2VzYWxlOkZhbHNlX2lzYWdlbnQ6RmFsc2UiLCJhdXRoX3RpbWUiOjE2NzI3MTc3MjIsImlkcCI6ImxvY2FsIiwiVXNlcklkIjoiMjEwMjk2MTExMCIsIm5hbWUiOiIxODY0NzQ5OTk5NiIsImdpdmVuX25hbWUiOiLliJjlhqzmtrUiLCJlbWFpbCI6ImxpdWRvbmdoYW5AbGF3eHAuY29tIiwianRpIjoiREJEQUNFNzlBRDk1NTRCMThBMzAyNTkzMEM2N0I4MTEiLCJpYXQiOjE2NzI3MTc3MjIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsiY3VzdG9tIl19.jeJTt0xI0dJ9VHuvl8sBhAWiek0JdMeL7oDAQFMB_VN2S2KY_HGiSXFd_QdpAR9CNc0Uf9P-QLJFgZA86LvxPS2yvQjt6vPIiBkGk_BAuovKdu3cZ6qrBSCcdvJyD9gVaP4OUuH9FlzOMOf669h6fMkizKsRIwsGVFiUsvjIhOI37i2WZoM-HPvfIIvnzAtaO1aWyiL9ja8a6FIqO4aKj3STsMc2FYQ4WMWaY7LdrivJhrYohg0OLDKo8wWt4fwwwR7H0gU5f4F4S-_HYRMk3DZ-KZ-8GwwI_0440BFedIzFj7bQ1MlnPoIXdw4_ZOKjwShhxEKQuWulSIUtb1-DmA")
                .compose(ADBaseTransformerManager.defaultSchedulers())
                .subscribe(new ADBaseRequestResult<UserModel>() {
                    @Override
                    protected void onCompletedListener() {

                    }

                    @Override
                    protected void onErrorListener(ADBaseExceptionManager.ApiException e) {

                    }

                    @Override
                    protected void onNextListener(UserModel userModel) {

                    }
                });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogBuilder(MainActivity.this)
                        .setData("")
                        .setDialogCancelable(false)
                        .setDialogCanceledOnTouchOutside(false)
                        .setOnDialogListener(new ADBaseDialogListener() {
                            @Override
                            public void onDismiss() {

//                                DemoFragment demoFragment = (DemoFragment) getSupportFragmentManager().findFragmentById(R.id.frame);

                            }
                        }).showDialogFragment();
            }
        });
        button.setOnClickListener(view -> {
            UserModel userModel = new UserModel();
            userModel.setNickname("我是popupWindow");
            new ADBasePopupWindow<UserModel>(MainActivity.this, button)
                    .setTouchModal(false)
                    .setWidthHeight(0, 0)
                    .setContentView(R.layout.popup_main)
                    .setData(userModel)
                    .findViewById((helper, userModel1) -> helper.setText(R.id.popup_tv_title, userModel1.getNickname()))
                    .builder()
                    .show();
        });

    }

    @Override
    protected void addListener() throws RuntimeException {
        ADBaseRetrofitManager.getInstance()
                .baseHttpUrl("https://api.whb.cn")
                .baseOkHttpClient(ADBaseOkHttpClient.getInstance().getOkHttpClient(new ADBaseLogInterceptor(), new ADBaseCodeInterceptor()).build())
                .baseRetrofitManager(ChatService.class)
                .getWHBNewDetails("02", ADBaseTransformerManager.transformJson(
                        new ADParamsUtils()
                                .put("contId", "545254")
                                .params()))
                .compose(ADBaseTransformerManager.defaultSchedulers())
                .doOnNext(whbNewDetailsBean -> {
                    whbNewDetailsBean.getTextInfo().getImages();
                }).subscribe(new ADBaseRequestResult<WHBNewDetailsBean>() {
            @Override
            protected void onCompletedListener() {

            }

            @Override
            protected void onErrorListener(ADBaseExceptionManager.ApiException e) {

            }

            @Override
            protected void onNextListener(WHBNewDetailsBean whbNewDetailsBean) {

            }
        });
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
        ADSnackBarManager.getInstance().showError(this, msg);
    }


}
