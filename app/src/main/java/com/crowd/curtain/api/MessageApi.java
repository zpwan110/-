package com.crowd.curtain.api;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.net.HttpClient;
import com.crowd.curtain.common.config.ServerConfig;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zhangpeng on 2018/4/6.
 */

public class MessageApi {
    public static Call getMessageDate(int page, int count, final Callback<JSONObject> callback){
        long lastTime = System.currentTimeMillis();
        Request request = new Request.Builder().url(ServerConfig.getMessages()+"?page="+page+"&per-page="+count+"&lastTime="+lastTime).build();
        return HttpClient.execute(request,callback);
    }
}
