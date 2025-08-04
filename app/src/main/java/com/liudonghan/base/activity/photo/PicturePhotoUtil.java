package com.liudonghan.base.activity.photo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * 拍照、选择照片
 *
 * @author Administrator
 */
public class PicturePhotoUtil {

    private Context mContext;
    public Button btnDelete;
    private boolean isCrop = false, isSquare = true;
    private boolean photoFlag = false;//判断是否是拍照，拍照时true，相册是FALSE

    /**
     * 调用系统拍照、图片浏览、图片裁剪状态
     */
    private static final int VIDEO_CAPTURE = 0;
    private static final int TAKE_CAMERA = 1;
    private static final int FETCH_PHOTO = 2;
    private static final int CROP_PHOTO = 3;

    /**
     * 返回图片的URI
     */
    private Uri mUri = null;
    /**
     * 图片默认保存路径
     */
    private File mFile = null;

    /**
     * 从相册中选择图片
     */
    private File sdcardTempFile;

    private FetchImageCallback mCallBack = null;

    /**
     * 获取图片的回调接口
     */
    public interface FetchImageCallback {

        void handleResult(File file);

    }

    private Uri getPictureUri() {
        return mUri;
    }

    /**
     * @param context
     * @param corp     是否裁切
     * @param isSquare 是否是需要方形裁切框
     * @param callBack 返回图片路径的回调
     */
    public PicturePhotoUtil(Context context, boolean corp, boolean isSquare, FetchImageCallback callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
        this.isCrop = corp;
        this.isSquare = isSquare;
        Date date = new Date();
        mFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.d("打印一下-----------" , mFile.getAbsolutePath());
        String name = date.getTime() + "";
        if (null != mFile && mFile.exists()) {
            mFile = new File(mFile, name + ".jpg");
        }
    }

    /**
     * 拍照
     */
    public void takePicture() {
        photoFlag = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /***
         * 以下程序是采用相机拍照，拍照后的图片存放在相册中，采用ContentValues方式保存的是拍照后的原图， 其它方式会不太清晰
         */
        ContentValues values = new ContentValues();
        mUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (isCrop) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            mCallBack.handleResult(mFile);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri.fromFile(mFile));
        }
        ((Activity) mContext).startActivityForResult(intent, TAKE_CAMERA);
    }

    /**
     * 录像的方法
     */
    public void videotape() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        mCallBack.handleResult(mFile);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 7);//限制录制时间10秒
        ((Activity) mContext).startActivityForResult(intent, VIDEO_CAPTURE);
    }

    /**
     * 相册
     */
    public void getPicture() {
        photoFlag = false;
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("image/*");
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // ((Activity) mContext).startActivityForResult(intent, FETCH_PHOTO);
        Date date = new Date();
        sdcardTempFile = mContext.getExternalFilesDir("");
        String name = date.getTime() + "";
        if (null != sdcardTempFile && sdcardTempFile.exists()) {
            sdcardTempFile = new File(sdcardTempFile, name + ".jpg");
        }

        // Intent intent = new Intent("android.intent.action.PICK");
        // intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
        // "image/*");

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }

        // if (isCrop) {
        // intent.putExtra("output", Uri.fromFile(sdcardTempFile));
        // intent.putExtra("crop", "true");
        // intent.putExtra("aspectX", 1);// 裁剪框比例
        // intent.putExtra("aspectY", 1);
        // // intent.putExtra("outputX", 200);// 输出图片大小
        // // intent.putExtra("outputY", 200);
        // }

        ((Activity) mContext).startActivityForResult(intent, FETCH_PHOTO);
    }

    /**
     * 传递裁剪参数
     *
     * @param uri 图片URI
     */
    public void cropImageUri(Uri uri, File file) {
        if (null == file) {
            // CommonUtils.showToast(mContext, "启动相机异常");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        // 裁切信号
        intent.putExtra("crop", "true");
        if (isSquare) {
            // 裁切框 的宽高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        } else {
            // 裁切框 的宽高比
            intent.putExtra("aspectX", 5);
            intent.putExtra("aspectY", 2);
        }

        // // 裁切后图片的尺寸
        // intent.putExtra("outputX", mCropWidth);
        // intent.putExtra("outputY", mCropHeight);

        // 自动缩放
        intent.putExtra("scale", true);
        // 裁切后图片的输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 不启用面部识别
        intent.putExtra("noFaceDetection", true);

        // 若"return-data"为false,则须要设置输出目录，裁切好的图片会存储在指定的目录中而不会从Intent返回
        // 若"return-data"为true,则不必设置输出目录，图片会在Intent中返回，通过(Bitmap)
        // intent.getExtras().get("data")获取
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        ((Activity) mContext).startActivityForResult(intent, CROP_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            Log.i("chenliangsen", "result_ok");
            Uri photoUri = null;
            switch (requestCode) {
                case TAKE_CAMERA:
                    if (isCrop) {
                        photoUri = getPictureUri();
                        cropImageUri(photoUri, mFile);
                    } else {
                        mCallBack.handleResult(mFile);
                    }
                    break;
                case FETCH_PHOTO:
                    if (data != null && data.getData() != null) {
                        if (isCrop) {
                            cropImageUri(data.getData(), sdcardTempFile);
                        } else {
                            sendPicByUri(data.getData());
                        }
                    }
                    break;
                case CROP_PHOTO:
                    Log.d("TAG","打印一下CROP_PHOTO");
                    if (sdcardTempFile != null && !photoFlag) {
                        Log.d("TAG","打印一下sdcardTempFile");
                        mCallBack.handleResult(sdcardTempFile);
                    } else {
                        Log.d("打印一下mFile" , mFile.getAbsolutePath());
                        mCallBack.handleResult(mFile);
                    }
                    break;
            }
        }
    }

    /**
     * 不剪切图片
     *
     * @param selectedImage
     */
    public void sendPicByUri(Uri selectedImage) {
        Log.e("chenliangsen", "sendPicByUri");
        Cursor cursor = mContext.getContentResolver().query(selectedImage, null, null, null, null);
        String st8 = "找不到图片";
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(mContext, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sdcardTempFile = new File(picturePath);
            mCallBack.handleResult(sdcardTempFile);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(mContext, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            mCallBack.handleResult(file);
        }

    }
}
