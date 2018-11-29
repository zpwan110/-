package base.http.net;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/12/14.
 */

public class HttpUploadFileHelp {
    /**
     * 上传文件
     * @param url 上传路径
     * @param keyfile 文件对应的key
     * @param file 文件
     * @param maps 附带的参数
     * @return
     */
    public static Request postUploadFileRequest(String url, String keyfile, File file, Map<String, String> maps){
        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
        String fileName = file.getName();
        if(maps==null){
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+keyfile+"\";filename=\""+fileName+"\""), RequestBody.create(MediaType.parse("image/png"),file)
            );
        }else{
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+keyfile+"\";filename=\""+fileName+"\""), RequestBody.create(MediaType.parse("image/png"),file));
        }
        RequestBody body=builder.build();
        return   new Request.Builder().url(url).post(body).build();
    }

    /**
     * 上传通讯录
     * @param url
     * @param file
     * @param json
     * @return
     */
    public static Request postUploadFileJsonRequest(String fileKey, String url, File file, String json){
        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
        String fileName = file.getName();
        if(TextUtils.isEmpty(json)){
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+fileKey+"\";filename=\""+fileName+"\""), RequestBody.create(MediaType.parse("text/x-vcard"),file)
            );
        }else{
//            builder.addPart(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json));
            builder.addFormDataPart("map",json);
            builder.addFormDataPart("fileName",fileName);
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\""+fileKey+"\";filename=\""+fileName+"\""), RequestBody.create(MediaType.parse("text/x-vcard"),file));
        }
        RequestBody body=builder.build();
        return   new Request.Builder().url(url).post(body).build();
    }
    public static Request postUploadFileRequest(String url, String[] keyfiles, File[] files, Map<String, String> maps){
        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
        String fileName;
        if(maps!=null){
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
        }
        for (int i = 0; i < keyfiles.length; i++) {
            fileName = files[i].getName();
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+keyfiles[i]+"\";filename=\""+fileName+"\""), RequestBody.create(MediaType.parse("image/png"),files[i]));
        }
        RequestBody body=builder.build();
        return   new Request.Builder().url(url).post(body).build();
    }

    /**
     * 上传bitmap 格式文件
     * @param url 上传路径
     * @param keyfile 文件对应的key
     * @param file bitmap 图片
     * @param maps 附带的参数
     * @return
     */
    public static Request postUploadFileRequest(String url, String keyfile, byte[] file, Map<String, String> maps){
        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(maps==null){
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+keyfile+"\";"), RequestBody.create(MediaType.parse("image/png"),file)
            ).build();
        }else{
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+keyfile+"\";"), RequestBody.create(MediaType.parse("image/png"),file)
            );
        }
        RequestBody body=builder.build();
        return   new Request.Builder().url(url).post(body).build();
    }
    public static Request postUploadFileRequest(String url, Map<String,byte[]> images, Map<String, String> maps){
        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(maps!=null){
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
        }
        for (String key:images.keySet()) {
            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\""+key+"\";"), RequestBody.create(MediaType.parse("image/png"),images.get(key)));
        }
        RequestBody body=builder.build();
        return   new Request.Builder().url(url).post(body).build();
    }

    /**
     * bitmap 转 byte[]
     * @param bm bitmap
     * @return
     */
    private static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
