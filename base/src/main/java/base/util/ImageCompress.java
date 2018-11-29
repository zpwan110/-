package base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.IOException;

import base.http.net.HttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 图片压缩
 *
 * @author XiaoSai
 * @version V1.0.0
 */
public class ImageCompress implements Handler.Callback{
    private Builder mBuilder;
    private final int DEFAULT_SIZE = 150;
    private Handler mHandler;
    private static final int MSG_COMPRESS_SUCCESS = 0;
    private static final int MSG_COMPRESS_START = 1;
    private static final int MSG_COMPRESS_ERROR = 2;

    private ImageCompress(Builder builder) {
        this.mBuilder = builder;
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public static Builder with(Context context){
        return new Builder(context);
    }

    private void httpLaunch(){
        if (TextUtils.isEmpty(mBuilder.fileUrl) && mBuilder.listener != null) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, new NullPointerException("image file cannot be null")));
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(mBuilder.fileUrl).build();
                try {
                    Response response = HttpClient.initOkHttpClient().newCall(request).execute();
                    Bitmap mBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, mBitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, new RuntimeException(e)));
                }
            }
        }).start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (mBuilder.listener == null&&mBuilder.httpListener==null) {
            return false;
        }
        if(mBuilder.listener!=null){
            switch (msg.what) {
                case MSG_COMPRESS_START:
                    mBuilder.listener.onStart();

                    break;
                case MSG_COMPRESS_SUCCESS:
                    if(msg.obj instanceof Bitmap){

                    }else{
                        mBuilder.listener.onSuccess(msg.obj+"");
                    }
                    break;
                case MSG_COMPRESS_ERROR:
                    mBuilder.listener.onError((Throwable) msg.obj);
                    break;
            }
        }else if(mBuilder.httpListener!=null){
            switch (msg.what) {
                case MSG_COMPRESS_SUCCESS:
                        mBuilder.httpListener.onSuccess((Bitmap) msg.obj);
                    break;
                case MSG_COMPRESS_ERROR:
                    mBuilder.httpListener.onError((Throwable) msg.obj);
                    break;
            }
        }

        return false;
    }

    public String checkSuffix(String path) {
        if (TextUtils.isEmpty(path)||!path.contains(".")) {
            return ".jpg";
        }
        return path.substring(path.lastIndexOf("."), path.length());
    }

    private String getImageCacheFile(String suffix) {
        return mBuilder.targetDir + "/" +
                System.currentTimeMillis() +
                (int) (Math.random() * 1000) +
                (TextUtils.isEmpty(suffix) ? ".jpg" : suffix);
    }


    public static class Builder{
        private Context context;
        private String filePath;
        private String fileUrl;
        private Bitmap bitmap;
        private int ignoreSize;
        private String targetDir;
        private OnCompressListener listener;
        private OnHttpListener httpListener;

        private Builder(Context context){
            this.context = context;
        }

        private ImageCompress build(){
            return new ImageCompress(this);
        }

        public Builder load(String localPath) {
            this.filePath = localPath;
            return this;
        }
        public Builder loadHttp(String httpPath) {
            this.fileUrl = httpPath;
            return this;
        }
        public Builder load(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder ignoreBy(int size) {
            this.ignoreSize = size;
            return this;
        }

        public Builder setTargetDir(String targetDir) {
            this.targetDir = targetDir;
            return this;
        }

        public Builder setOnCompressListener(OnCompressListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setOnHttpListener(OnHttpListener listener) {
            this.httpListener = listener;
            return this;
        }

       /* public void launch(){
            build().launch();
        }*/

        public void httpLaunch() {
                build().httpLaunch();
        }
    }

    public interface OnCompressListener{
        void onStart();
        void onSuccess(String filePath);
        void onError(Throwable e);
    }
    public interface OnHttpListener{
        void onSuccess(Bitmap bitmap);
        void onError(Throwable e);
    }
}