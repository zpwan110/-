package com.crowd.curtain.api;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.InputStream;

import base.http.Callback;
import base.http.net.HttpClient;
import com.crowd.curtain.common.config.ServerConfig;
import com.crowd.curtain.utils.AppSharePerference;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zpwan110 on 2017/4/8.
 */

public class ApplicationApi {
    /**
     *
     * 同步刷新token
     * @param refreshToken
     * @return
     */
    public static Call postSyncRefrenshToken(String refreshToken){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("longToken",refreshToken);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getRefreshToken()).post(body).build();
        Call call = HttpClient.initOkHttpClient().newCall(addToken(request));
        return call;
    }

    /**
     * @param productId 产品ID
     * @param pause 停留时间（单位：秒）
     *        model 机型，0为安卓，1为IOS
     * @return
     */
    public static Call postProductTime(String productId, long pause,Callback<JSONObject> callback){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("productId",productId);
        builder.addFormDataPart("pause",pause+"");
        builder.addFormDataPart("model",0+"");
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getPauseTime()).post(body).build();
        return HttpClient.execute(request,callback);
    }

    public static Call postAppData(String device,Callback<JSONObject> callback){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("device",device);
        builder.addFormDataPart("model",0+"");
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getAppTime()).post(body).build();
        return HttpClient.execute(request,callback);
    }

    /**
     *
     * @param request
     * @return
     */
    private static Request addToken(Request request) {
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
     * 获取首页征信报告
     * @param callback
     * @return
     *//*
    public static Call getReportList(final Callback<ResponseBodyJson> callback){
        Request request = new Request.Builder().url(ServerConfig.getReportList()).build();
        return HttpClient.execute(request,callback);
    }
    *//**
     * Alpha计算
     * @param callback
     * @return
     *//*
    public static Call getCalculateAlpha(final Callback<ResponseBodyJson> callback){
        Request request = new Request.Builder().url(ServerConfig.getAlphaCalculate()).build();
        return HttpClient.execute(request,callback);
    }
    public static Call getAlphaReport(final Callback<ResponseBodyJson> callback){
        Request request = new Request.Builder().url(ServerConfig.getAlphaReport()).build();
        return HttpClient.execute(request,callback);
    }
    public static Call getReportStatue(String auId, String reportId, final Callback<ResponseBodyJson> callback){
        Request request = new Request.Builder().url(ServerConfig.getReportStatue()+"?auId="+auId+"&reportId="+reportId).build();
        return HttpClient.execute(request,callback);
    }*/

    /**
     * 获取本地Json文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readLocalJson(Context context, String fileName) {
        String jsonString = "";
        String resultString = "";
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer, "utf-8");
        } catch (Exception e) {
        }
        return resultString;
    }
    /*public static Call alphaCount(final Callback<ResponseBodyJson> callback){
        Request request = new Request.Builder().url(ServerConfig.getAlphaCount()).build();
        return HttpClient.execute(request,callback);
    }
    public static Call updateContacts(String json, boolean isUpdate, final Callback<ResponseBodyJson> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("addressBook",json);
        builder.addFormDataPart("update",isUpdate+"");
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getUpdateContactsList()).post(body).build();
        return HttpClient.execute(request, callback);
    }*/
}
