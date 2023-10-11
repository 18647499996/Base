package com.liudonghan.mvp;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2021/3/3
 */
public class ADBaseFileDownloadInterceptor implements Interceptor {
    public static DownloadListener downloadListener;

    public ADBaseFileDownloadInterceptor(DownloadListener downloadListener) {
        ADBaseFileDownloadInterceptor.downloadListener = downloadListener;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new DownloadResponseBody(Objects.requireNonNull(response.body()), downloadListener)).build();
    }

    /**
     * 将输入流写入文件
     *
     * @param inputString 输入流
     * @param file        文件
     */
    public static File writeFile(InputStream inputString, File file) {
        if (file.exists()) {
            boolean delete = file.delete();
            Log.i("Mac_Liu", "delete repeat file " + delete);
        }

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }
            inputString.close();
            fileOutputStream.close();
            if (null != downloadListener) {
                downloadListener.onSucceed(file);
            }
        } catch (FileNotFoundException e) {
            if (null != downloadListener) {
                downloadListener.onFail(e.getMessage());
            }
        } catch (IOException e) {
            if (null != downloadListener) {
                downloadListener.onFail("IOException");
            }
        }
        return file;
    }

    public interface DownloadListener {
        /**
         * 开始下载
         *
         * @param length 大小
         */
        void onStartDownload(long length);

        /**
         * 正在下载
         *
         * @param progress  进度
         * @param read      百分比
         * @param totalSize 文件大小
         */
        void onProgress(int progress, int read, long totalSize);

        /**
         * 下载失败
         *
         * @param errorInfo 异常信息
         */
        void onFail(String errorInfo);

        /**
         * 下载成功
         *
         * @param file 下载文件
         */
        void onSucceed(File file);
    }

    private static class DownloadResponseBody extends ResponseBody {
        private ResponseBody responseBody;
        private DownloadListener downloadListener;
        // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
        private BufferedSource bufferedSource;

        public DownloadResponseBody(ResponseBody responseBody, DownloadListener downloadListener) {
            this.responseBody = responseBody;
            this.downloadListener = downloadListener;
            downloadListener.onStartDownload(responseBody.contentLength());
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }


        @NonNull
        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    Log.i("Mac_Liu", "download read: " + (int) (totalBytesRead * 100 / responseBody.contentLength()));
                    if (null != downloadListener) {
                        if (bytesRead != -1) {
                            downloadListener.onProgress((int) (totalBytesRead), (int) (totalBytesRead * 100 / responseBody.contentLength()), responseBody.contentLength());
                        }
                    }
                    return bytesRead;
                }
            };
        }
    }
}
