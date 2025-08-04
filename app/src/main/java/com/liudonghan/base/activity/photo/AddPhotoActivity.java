package com.liudonghan.base.activity.photo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.liudonghan.base.databinding.ActivityPhotoBinding;
import com.liudonghan.mvp.ADBaseActivity;
import com.liudonghan.utils.ADPicturePhotoUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AddPhotoActivity extends ADBaseActivity<AddPhotoPresenter, ActivityPhotoBinding> implements AddPhotoContract.View, ADPicturePhotoUtils.ADImageFileCallback {

    protected ADPicturePhotoUtils picturePhotoUtils;
//    protected PicturePhotoUtil picturePhotoUtils;

    @Override
    protected Object initBuilderTitle() throws RuntimeException {
        return null;
    }

    @Override
    protected AddPhotoPresenter createPresenter() throws RuntimeException {
        return (AddPhotoPresenter) new AddPhotoPresenter(this).builder(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) throws RuntimeException {
        picturePhotoUtils = ADPicturePhotoUtils.getInstance().init(this,new ADPicturePhotoUtils.Config(
                false,false,Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
                new Date().getTime() + ".jpg"
        ));
    }

    @Override
    protected void addListener() throws RuntimeException {
        picturePhotoUtils.onCallBack(this);
        mViewBinding.activityAddPhotoImgAdd.setOnClickListener(v -> {
            picturePhotoUtils.takePicture();
        });
    }

    @Override
    protected void onClickDoubleListener(View view) throws RuntimeException, IOException {

    }

    @Override
    protected void onDestroys() throws RuntimeException {

    }

    @Override
    public void setPresenter(AddPhotoContract.Presenter presenter) {
        mPresenter = (AddPhotoPresenter) checkNotNull(presenter);
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    public void handleResult(File file) {
        Log.i("TAG", "文件地址：" + file.getAbsolutePath() );
        mViewBinding.activityAddPhotoImgAdd.setImageURI(Uri.parse(file.getAbsolutePath()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picturePhotoUtils.onActivityResult(requestCode, resultCode, data);
    }
}
