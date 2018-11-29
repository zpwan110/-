package com.crowd.curtain.api;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.net.HttpClient;
import com.crowd.curtain.common.config.ServerConfig;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zhangpeng on 2018/4/5.
 */

public class ProductApi {
    public static Call getProductById(int bannerId,String proId, final Callback<JSONObject> callback) {;
        String path ="";
        if(bannerId==-1){
            path=path+proId;
        }else{
            path =proId+"?bannerId="+bannerId;
        }
        Request request = new Request.Builder().url(ServerConfig.getProductWithSize() +path).build();
        return HttpClient.execute(request, callback);
    }
    public static Call getProductByBanner(int bannerId, final Callback<JSONObject> callback) {
        Request request = new Request.Builder().url(ServerConfig.getProduct() + "?=" + bannerId).build();
        return HttpClient.execute(request, callback);
    }
}
