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

public class SearchApi {


    private static Object material;

    /**
     * 获取分类数据
     * @param callback
     * @return
     */
    public static Call getSearchCurtain(int page,int count,String num,String keywords,final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getTypeCate()+"?cateId="+num+"&keywords="+keywords+"&page="+page+"&per-page="+count).build();
        return HttpClient.execute(request,callback);
    }

    public static Call getSearchCurtain(int page,int count,String keywords,final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getTypeCate()+"?keywords="+keywords+"&page="+page+"&per-page="+count).build();
        return HttpClient.execute(request,callback);
    }

    public static Call getCateList(final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getCates()).build();
        return HttpClient.execute(request,callback);
    }


    public static Call getHotSearch(final Callback<JSONObject> callback) {
        Request request = new Request.Builder().url(ServerConfig.getHots()).build();
        return HttpClient.execute(request,callback);
    }

    public static Call getMaterial(int page,int parentId,String num,Callback<JSONObject> callback) {
        Request request = new Request.Builder().url(ServerConfig.getMater()+"?parentCateId="+parentId+"&number="+num+"&page="+page+"&per-page="+10).build();
        return HttpClient.execute(request,callback);
    }
}
