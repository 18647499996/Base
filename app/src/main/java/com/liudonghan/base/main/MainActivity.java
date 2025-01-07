package com.liudonghan.base.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.liudonghan.base.R;
import com.liudonghan.mvp.ADBaseActivity;
import com.liudonghan.view.title.ADTitleBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainActivity extends ADBaseActivity<MainPresenter> implements MainContract.View {

    private OkHttpClient client;
    private WebSocket webSocket;
    private WebView webView;

    @Override
    protected int getLayout() throws RuntimeException {
        return R.layout.activity_main;
    }

    @Override
    protected ADTitleBuilder initBuilderTitle() throws RuntimeException {
        return null;
    }

    @Override
    protected MainPresenter createPresenter() throws RuntimeException {
        return (MainPresenter) new MainPresenter(this).builder(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        client = new OkHttpClient();
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://8.152.213.121:55556/signalr");
    }

    @Override
    protected void addListener() throws RuntimeException {

    }

    @Override
    protected void onClickDoubleListener(View view) throws RuntimeException {

    }

    @Override
    protected void onDestroys() throws RuntimeException {
        if (webSocket != null) {
            webSocket.close(1000, "Closing WebSocket");
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = (MainPresenter) checkNotNull(presenter);
    }

    @Override
    public void showErrorMessage(String msg) {

    }


}
