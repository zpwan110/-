package base.http.net;

import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import base.http.DataConverter;
import base.http.ErrorCode;
import base.http.ErrorModel;
import base.http.ExceptionCode;
import base.http.HttpHelper;
import base.http.LogInterceptor;
import jingshu.com.base.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 *
 * @author HanTuo
 * @date 16/10/27
 */

public class HttpClient extends HttpHelper {

    public static void initOkHttpClient(List<Interceptor>  interceptors) {
        initDataConverter();
        OkHttpClient.Builder builder =  initBuild();
        if(interceptors!=null){
            for (Interceptor inteceptor : interceptors) {
            builder.addInterceptor(inteceptor);
            }
        }
        sOkHttpClient = builder.build();
    }
    public static OkHttpClient initOkHttpClient() {
        initDataConverter();
        OkHttpClient.Builder builder =  initBuild();
        return builder.build();
    }

    private static OkHttpClient.Builder initBuild() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new LogInterceptor());
        }
        return builder;
    }

    private static void initDataConverter() {
        dataConverter = new DataConverter() {
            @Override
            public Object convert(String data, Type type) {
                if (type == String.class) {
                    return data;
                } else {
                    return JSON.parseObject(data, type);
                }
            }

            @Override
            public ErrorModel convertError(int code, @Nullable String data) {
                try {
                    if (code == ExceptionCode.AUTHORIZE_FAIL) {
                        return ERROR_MODEL_AUTHORIZE_FAIL;
                    }
                    ServerError serverError =JSON.parseObject(data, ServerError.class);
                    switch (code) {
                        case ExceptionCode.CANCEL:
                            return ERROR_MODEL_CANCEL;
                        case ExceptionCode.CONNECTION_OFF:
                            return ERROR_MODEL_NET_ERROR;
                        case ExceptionCode.TIME_OUT:
                            return ERROR_MODEL_TIME_OUT;
                        default:
                            return new ErrorModel(ErrorCode.SERVER_ERROR, serverError.errorDetail.code, serverError.errorDetail.description);
                    }
                } catch (Exception e) {
                    return ERROR_MODEL_UNKNOWN;
                }

            }
        };
    }

    class ServerError {
        @JSONField(name="error")
        ErrorDetail errorDetail;
    }

    class ErrorDetail {
        @JSONField(name="code")
        int code;
        @JSONField(name="description")
        String description;
    }
}
