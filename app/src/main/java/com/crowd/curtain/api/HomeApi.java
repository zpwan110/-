package com.crowd.curtain.api;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.net.HttpClient;
import com.crowd.curtain.common.config.ServerConfig;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zpwan110 on 2017/4/8.
 */

public class HomeApi {

    /**
     * 获取首页分类数据
     * @param callback
     * @return
     */
    public static Call getHomeDate(final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getHomeData()).build();
        return HttpClient.execute(request,callback);
    }

    /**
     * 获取该类别下的窗帘列表
     * @param callback
     * @return
     */
    public static Call getTypeCurtain(String productCateId,final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getTypeCurtain()+productCateId).build();
        return HttpClient.execute(request,callback);
    }
}
