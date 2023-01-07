package com.liudonghan.base.main;

import android.os.Bundle;
import android.view.View;

import com.liudonghan.base.ChatService;
import com.liudonghan.base.R;
import com.liudonghan.base.UserModel;
import com.liudonghan.base.UserService;
import com.liudonghan.mvp.BaseActivity;
import com.liudonghan.mvp.BaseExceptionManager;
import com.liudonghan.mvp.BaseLoadingDialog;
import com.liudonghan.mvp.BaseRequestResult;
import com.liudonghan.mvp.BaseRetrofitManager;
import com.liudonghan.mvp.BaseTransformerManager;
import com.liudonghan.view.snackbar.SnackBarManager;
import com.liudonghan.view.title.TitleBuilder;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @Override
    protected int getLayout() throws RuntimeException {
        return R.layout.activity_main;
    }

    @Override
    protected TitleBuilder initBuilderTitle() throws RuntimeException {
        return new TitleBuilder(this).setMiddleTitleBgRes("首页").setLeftImageRes(R.drawable.nav_fanhui_hei).setLeftRelativeLayoutListener(this);
    }

    @Override
    protected MainPresenter createPresenter() throws RuntimeException {
        return (MainPresenter) new MainPresenter(this).builder(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showErrorMessage("错误");
                BaseLoadingDialog.getInstance().init(MainActivity.this,"点击加载");
                BaseRetrofitManager
                        .getInstance()
                        .transformService(1, UserService.class)
                        .getUserInfo("eyJhbGciOiJSUzI1NiIsImtpZCI6IjcyRENCNzE2RTE3NzAzMjQxQjM5QzU4NTlCQjNDNDI5IiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2NzI3MTc3MjIsImV4cCI6MTY3MzU4MTcyMiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5sYXd4cC5jb20iLCJjbGllbnRfaWQiOiJhcHAiLCJzdWIiOiI3MThfaXN3ZWJvYTpUcnVlX2lzd2VzYWxlOkZhbHNlX2lzYWdlbnQ6RmFsc2UiLCJhdXRoX3RpbWUiOjE2NzI3MTc3MjIsImlkcCI6ImxvY2FsIiwiVXNlcklkIjoiMjEwMjk2MTExMCIsIm5hbWUiOiIxODY0NzQ5OTk5NiIsImdpdmVuX25hbWUiOiLliJjlhqzmtrUiLCJlbWFpbCI6ImxpdWRvbmdoYW5AbGF3eHAuY29tIiwianRpIjoiREJEQUNFNzlBRDk1NTRCMThBMzAyNTkzMEM2N0I4MTEiLCJpYXQiOjE2NzI3MTc3MjIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsiY3VzdG9tIl19.jeJTt0xI0dJ9VHuvl8sBhAWiek0JdMeL7oDAQFMB_VN2S2KY_HGiSXFd_QdpAR9CNc0Uf9P-QLJFgZA86LvxPS2yvQjt6vPIiBkGk_BAuovKdu3cZ6qrBSCcdvJyD9gVaP4OUuH9FlzOMOf669h6fMkizKsRIwsGVFiUsvjIhOI37i2WZoM-HPvfIIvnzAtaO1aWyiL9ja8a6FIqO4aKj3STsMc2FYQ4WMWaY7LdrivJhrYohg0OLDKo8wWt4fwwwR7H0gU5f4F4S-_HYRMk3DZ-KZ-8GwwI_0440BFedIzFj7bQ1MlnPoIXdw4_ZOKjwShhxEKQuWulSIUtb1-DmA")
                        .compose(BaseTransformerManager.defaultSchedulers())
                        .subscribe(new BaseRequestResult<UserModel>() {
                            @Override
                            protected void onCompletedListener() {

                            }

                            @Override
                            protected void onErrorListener(BaseExceptionManager.ApiException e) {

                            }

                            @Override
                            protected void onNextListener(UserModel userModel) {

                            }
                        });
            }
        });

        BaseRetrofitManager
                .getInstance()
                .transformService(2, ChatService.class)
                .getUserInfo()
                .compose(BaseTransformerManager.defaultSchedulers())
                .subscribe(new BaseRequestResult<UserModel>(this) {
                    @Override
                    protected void onCompletedListener() {

                    }

                    @Override
                    protected void onErrorListener(BaseExceptionManager.ApiException e) {

                    }

                    @Override
                    protected void onNextListener(UserModel userModel) {

                    }
                });
//        BaseRetrofitManager
//                .getInstance()
//                .baseOkHttpClient(1,"https://loginf.lawxp.com/",OkHttpUtils.getInstance().getAuthServiceConfig())
//                .transformService(1,UserService.class)
//                .getUserInfo("eyJhbGciOiJSUzI1NiIsImtpZCI6IjcyRENCNzE2RTE3NzAzMjQxQjM5QzU4NTlCQjNDNDI5IiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2NzI3MTc3MjIsImV4cCI6MTY3MzU4MTcyMiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5sYXd4cC5jb20iLCJjbGllbnRfaWQiOiJhcHAiLCJzdWIiOiI3MThfaXN3ZWJvYTpUcnVlX2lzd2VzYWxlOkZhbHNlX2lzYWdlbnQ6RmFsc2UiLCJhdXRoX3RpbWUiOjE2NzI3MTc3MjIsImlkcCI6ImxvY2FsIiwiVXNlcklkIjoiMjEwMjk2MTExMCIsIm5hbWUiOiIxODY0NzQ5OTk5NiIsImdpdmVuX25hbWUiOiLliJjlhqzmtrUiLCJlbWFpbCI6ImxpdWRvbmdoYW5AbGF3eHAuY29tIiwianRpIjoiREJEQUNFNzlBRDk1NTRCMThBMzAyNTkzMEM2N0I4MTEiLCJpYXQiOjE2NzI3MTc3MjIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsiY3VzdG9tIl19.jeJTt0xI0dJ9VHuvl8sBhAWiek0JdMeL7oDAQFMB_VN2S2KY_HGiSXFd_QdpAR9CNc0Uf9P-QLJFgZA86LvxPS2yvQjt6vPIiBkGk_BAuovKdu3cZ6qrBSCcdvJyD9gVaP4OUuH9FlzOMOf669h6fMkizKsRIwsGVFiUsvjIhOI37i2WZoM-HPvfIIvnzAtaO1aWyiL9ja8a6FIqO4aKj3STsMc2FYQ4WMWaY7LdrivJhrYohg0OLDKo8wWt4fwwwR7H0gU5f4F4S-_HYRMk3DZ-KZ-8GwwI_0440BFedIzFj7bQ1MlnPoIXdw4_ZOKjwShhxEKQuWulSIUtb1-DmA")
//                .compose(BaseTransformerManager.defaultSchedulers())
//                .subscribe(new BaseRequestResult<UserModel>() {
//                    @Override
//                    protected void onCompletedListener() {
//
//                    }
//
//                    @Override
//                    protected void onErrorListener(BaseExceptionManager.ApiException e) {
//
//                    }
//
//                    @Override
//                    protected void onNextListener(UserModel userModel) {
//
//                    }
//                });
    }

    @Override
    protected void addListener() throws RuntimeException {

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
        SnackBarManager.getInstance().showError(this, msg);
    }


}
