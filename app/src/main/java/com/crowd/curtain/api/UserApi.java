package com.crowd.curtain.api;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.net.HttpClient;
import com.crowd.curtain.common.config.ServerConfig;
import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zpwan110 on 2017/4/8.
 */

public class UserApi {

    public static final String FORGET="forget";
    public static final String UPD_PHONE="updPhone";

    /**
     * 获取首页分类数据
     * @param callback
     * @return
     */
    public static Call postLoginUser(String username,String password,final Callback<JSONObject> callback){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("username",username);
        builder.addFormDataPart("password",password);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.postLogin()).post(body).build();
        return HttpClient.execute(request,callback);
    }

    public static Call getUserInfo(final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getUserInfo()).build();
        return HttpClient.execute(request,callback);
    }

    public static Call loginOut(final Callback<JSONObject> callback) {
        Request request = new Request.Builder().url(ServerConfig.getLoginOut()).build();
        return HttpClient.execute(request,callback);
    }
    public static Call changePwd(String oldPassword,String newPassword,final Callback<JSONObject> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("oldPwd",oldPassword);
        builder.addFormDataPart("newPwd",newPassword);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getChangePwd()).post(body).build();
        return HttpClient.execute(request,callback);
    }
    public static Call getYanCode(String phone,String codeType,final Callback<JSONObject> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("phone",phone);
        builder.addFormDataPart("use",codeType);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getYanCode()).post(body).build();
        return HttpClient.execute(request,callback);
    }
    public static Call postNewPhone(String newPhone,String code,final Callback<JSONObject> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("newPhone",newPhone);
        builder.addFormDataPart("code",code);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getBindPhone()).post(body).build();
        return HttpClient.execute(request,callback);
    }
    public static Call forgetPwd(String phone,String code,String newPwd,final Callback<JSONObject> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("phone",phone);
        builder.addFormDataPart("code",code);
        builder.addFormDataPart("newPwd",newPwd);
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(ServerConfig.getForgetPwd()).post(body).build();
        return HttpClient.execute(request,callback);
    }
}
