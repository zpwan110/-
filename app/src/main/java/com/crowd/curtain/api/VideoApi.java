package com.crowd.curtain.api;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.net.HttpClient;
import com.crowd.curtain.common.config.ServerConfig;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zhangpeng on 2018/3/25.
 */

public class VideoApi {
    public static Call getVideoDate(int pageNum, int size, Callback<JSONObject> callback) {
        Request request = new Request.Builder().url(ServerConfig.getVideoList()+"?page="+pageNum+"&per-page="+size).build();
        return HttpClient.execute(request,callback);
    }
}
