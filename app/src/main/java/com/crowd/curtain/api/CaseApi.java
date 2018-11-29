package com.crowd.curtain.api;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.Map;

import base.http.Callback;
import base.http.net.HttpClient;
import base.http.net.HttpUploadFileHelp;
import com.crowd.curtain.common.config.ServerConfig;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zhangpeng on 2018/3/13.
 */

public class CaseApi {
    public static Call getCaseDate(int page,int count,String status,final Callback<JSONObject> callback){
        Request request = new Request.Builder().url(ServerConfig.getCaseList()+"?page="+page+"&per-page="+count+"&status="+status).build();
        return HttpClient.execute(request,callback);
    }
    public static Call postUploadCase(String[] keys, File[] files, Map<String,String> prames, final Callback<JSONObject> callback){
        Request request = HttpUploadFileHelp.postUploadFileRequest(ServerConfig.getUploadCase(),keys,files,prames);
        return HttpClient.execute(request, callback);
    }
    public static Call deleteCase(String caseId, final Callback<JSONObject> callback){
        Request request =new Request.Builder().url(ServerConfig.getCaseList()+"/"+caseId).delete().build();
        return HttpClient.execute(request, callback);
    }
}
