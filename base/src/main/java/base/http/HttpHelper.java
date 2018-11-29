package base.http;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import base.util.LogUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HanTuo on 16/10/9.
 */

public class HttpHelper {
    public static final ErrorModel ERROR_MODEL_CANCEL = new ErrorModel(ErrorCode.REQUEST_CANCELED, 0, "请求已取消");
    public static final ErrorModel ERROR_MODEL_NET_ERROR = new ErrorModel(ErrorCode.NETWORK_ERROR, 0, "网络连接失败");
    public static final ErrorModel ERROR_MODEL_TIME_OUT = new ErrorModel(ErrorCode.TIME_OUT, 0, "网络连接失败");
    public static final ErrorModel ERROR_MODEL_AUTHORIZE_FAIL = new ErrorModel(ErrorCode.AUTHORIZE_FAIL, 0, "身份认证失败");
    public static final ErrorModel ERROR_MODEL_UNKNOWN = new ErrorModel(ErrorCode.TIME_OUT, 0, "未知错误");

    protected static OkHttpClient sOkHttpClient;
    private static Handler handler = new Handler(Looper.getMainLooper());
    protected static DataConverter dataConverter;

    private static void postSuccessToMainThread(final Callback callback, final Object result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onSuccess(result);
                } catch (Exception e) {
                    LogUtil.e(e);
                }
            }
        });
    }

    private static void postFailToMainThread(final Callback callback, final ErrorModel httpError) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onFail(httpError);
                } catch (Exception e) {
                    LogUtil.e(e);
                }
            }
        });
    }

    public static <T> Call execute(Request request, final Callback<T> callback) {
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    postFailToMainThread(callback, dataConverter.convertError(ExceptionCode.CANCEL, null));
                } else if (e instanceof SocketTimeoutException || e instanceof java.net.ConnectException) {
                    postFailToMainThread(callback, dataConverter.convertError(ExceptionCode.TIME_OUT, null));
                } else {
                    postFailToMainThread(callback, dataConverter.convertError(ExceptionCode.UNKNOWN, null));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 if (response.isSuccessful()) {
                    String dataJson = null;
                    try {
                        dataJson = response.body().string();
                    } catch (Exception e) {
                        postFailToMainThread(callback, new ErrorModel(0, 0, "网络异常"));
                        LogUtil.e(e);
                        return;
                    }
                    try {
                        Type type = callback.getClass().getGenericInterfaces()[0];
                        type = ((ParameterizedType) type).getActualTypeArguments()[0];
                        Object obj = dataConverter.convert(dataJson, type);
                        postSuccessToMainThread(callback, obj);
                    } catch (Exception e) {
                        LogUtil.e(e);
                    }
                } else {
                    try {
                        String errorBody = response.body().string();
                        if (response.code() == 401) {
                            postFailToMainThread(callback, dataConverter.convertError(ExceptionCode.AUTHORIZE_FAIL, errorBody));
                        } else {
                            postFailToMainThread(callback, dataConverter.convertError(ExceptionCode.UNKNOWN, errorBody));
                        }
                    } catch (Exception e) {
                        LogUtil.e(e);
                    }

                }
            }
        });
        return call;
    }

    public static OkHttpClient getHttpClient() {
        return sOkHttpClient;
    }
}
