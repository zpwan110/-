package com.crowd.curtain.interceptor;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import base.BroadcastConstants;
import base.http.net.HttpClient;
import base.util.BroadCastReceiverUtil;
import base.util.ToastUtil;
import com.crowd.curtain.api.ApplicationApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.common.config.ServerConfig;
import com.crowd.curtain.utils.AppSharePerference;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Token 失效时拦截
 * Created by zpwan110 on 2017/4/8.
 */

public class TokenInterceptor implements Interceptor {

    /**
     * 200:请求成功
     * 401:授权未通过（Token过期或错误）
     * 404:未找到改接口或未找到指定对象
     * 412:为满足前提条件（多指传参未通过验证）
     * 500:系统内部错误
     */
    private static final int SUCCESS_CODE = 200;
    private static final int TOKEN_ERROR = 401;
    private static final int REFRESH_TOKEN_ERROR = 407;
    private static final int PATH_ERROR = 404;
    private static final int PARAME_ERROR = 412;
    private static final int SERVER_ERROR = 500;
    private static final int NULL_ERROR = 600;
    private static Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(addToken(request));
        return checkToken(response, request, chain);
    }

    /**
     * 添加token
     *
     * @param request
     * @return
     */
    private Request addToken(Request request) {
        String headers = request.header("Authorization");
        if (TextUtils.isEmpty(headers) && !TextUtils.isEmpty(AppSharePerference.getUserToken())) {
            Request authorised = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + AppSharePerference.getUserToken())
                    .addHeader("Accept-Encoding", "")
                    .build();
            return authorised;
        }
        Request authorised = request.newBuilder()
                .addHeader("Accept-Encoding", "")
                .build();
        return authorised;
    }

    /**
     * token 检查token是否过期
     *
     * @param response
     * @param chain
     */
    private Response checkToken(Response response, final Request request, final Chain chain) throws IOException {
        ResponseBody responseBody = response.body();
        String dataJson = responseBody.string();
        if (response.isSuccessful()) {
            JSONObject obj = JSONObject.parseObject(dataJson);
            if (obj.containsKey("code")) {
                switch (obj.getIntValue("code")) {
                    case TOKEN_ERROR:
                       return refToken(request, response,dataJson);
                    case SUCCESS_CODE:
                        return response.newBuilder()
                                .body(ResponseBody.create(responseBody.contentType(),dataJson))
                                .build();
                    case PARAME_ERROR:
                        handler.post(() -> {
                            if(obj.containsKey("message")){
                                ToastUtil.showToastFailPic(obj.getString("message"));
                            }
                        });
                        return response.newBuilder()
                                .body(ResponseBody.create(responseBody.contentType(),dataJson)).code(PARAME_ERROR)
                                .build();
                    case SERVER_ERROR:
                        handler.post(() -> {
                            if(obj.containsKey("message")){
                                ToastUtil.showToastFailPic(obj.getString("message"));
                            }
                        });
                        return response.newBuilder()
                            .body(ResponseBody.create(responseBody.contentType(),dataJson)).code(SERVER_ERROR)
                            .build();
                    case PATH_ERROR:
                        handler.post(() -> {
                            if(obj.containsKey("message")){
                                ToastUtil.showToastFailPic(obj.getString("message"));
                            }
                        });
                        return response.newBuilder()
                                .body(ResponseBody.create(responseBody.contentType(),dataJson)).code(PATH_ERROR)
                                .build();
                }
            }
        }
        return response.newBuilder()
                .body(ResponseBody.create(responseBody.contentType(),dataJson)).code(NULL_ERROR)
                .build();
    }

    /**
     * 刷新Token
     *
     * @param request
     * @return
     * @throws IOException
     */
    private Response refToken(Request request, Response oldResponse,String responseString) throws IOException {
        //token失效，需重新刷新token
        String reqUrl = ServerConfig.BASE_URL+String.valueOf(request.url().uri().getPath());
        String relPath  = ServerConfig.getRefreshToken();
        if ((reqUrl).equals(relPath)) {
            ResponseBody oldBody = oldResponse.body();
            return oldResponse.newBuilder()
                    .body(ResponseBody.create(oldBody.contentType(), responseString))
                    .build();
        }
        Response responseRef = ApplicationApi.postSyncRefrenshToken(AppSharePerference.getRefreshToken()).execute();
        ResponseBody responseRefBody = responseRef.body();
        String newResponString = responseRefBody.string();
        if (responseRef.isSuccessful()) {
            JSONObject jsonObject = JSON.parseObject(newResponString);
            if (jsonObject.containsKey("code")) {
                if(jsonObject.getIntValue("code") == SUCCESS_CODE){
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String token = dataObj.getString("token");
                    String longToken = dataObj.getString("longToken");

                    AppSharePerference.saveUserToken(token);
                    AppSharePerference.saveRefreshToken(longToken);
                    return againRequest(request);
                }else if(jsonObject.getIntValue("code") == REFRESH_TOKEN_ERROR){
                    BroadCastReceiverUtil.sendBroadcast(App.getContext(),
                            BroadcastConstants.BROADCAST_REFRESH_TOKEN_TIME_OUT);
                    AppSharePerference.saveUserToken("");
                }
            }
        }
        return responseRef.newBuilder()
                .body(ResponseBody.create(responseRefBody.contentType(), newResponString))
                .build();
    }

    /**
     * 重新请求
     *
     * @param request
     * @return
     * @throws IOException
     */
    private Response againRequest(Request request) throws IOException {
        return HttpClient.getHttpClient().newCall(addToken(request)).execute();
    }
}
