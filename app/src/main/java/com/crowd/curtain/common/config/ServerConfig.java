package com.crowd.curtain.common.config;

/**
 * Created by zhangpeng on 2018/3/1.
 */

public class ServerConfig {
    public static String BASE_URL = ServerConfig.BASE_URL_RELEASE;
    public static String BASE_H5_URL = ServerConfig.BASE_H5_URL_RELEASE;

    public static void initserver() {
        switch (AppConfig.PORT_TYPE) {
            case AppConfig.RELEASE:
                ServerConfig.BASE_URL = BASE_URL_RELEASE;
                ServerConfig.BASE_H5_URL = ServerConfig.BASE_H5_URL_RELEASE;
                break;
            case AppConfig.TEST:
                ServerConfig.BASE_URL = BASE_URL_TEST;
                ServerConfig.BASE_H5_URL = ServerConfig.BASE_H5_URL_TEST;
                break;
            case AppConfig.DEBUG:
                ServerConfig.BASE_URL = BASE_URL_DEBUG;
                ServerConfig.BASE_H5_URL = ServerConfig.BASE_H5_URL_DEBUG;
                break;
            default:
                break;
        }
    }

    public static final String BASE_URL_DEBUG = "http://cl.gunaimu.com";
    public static final String BASE_URL_TEST = "http://192.168.16.174:3001";
    public static final String BASE_URL_RELEASE = "https://gxqfz.omclml.com";

    public static final String BASE_H5_URL_RELEASE = "https://gxqfz.omclml.com";
    public static final String BASE_H5_URL_DEBUG = "https://gxqfz.omclml.com";
    public static final String BASE_H5_URL_TEST = "http://192.168.16.174:3001/wallet";

    public static String getHomeData() {
        return BASE_URL+"/homes";
    }
    public static String getAppTime() {
        return BASE_URL+"/apps";
    }
    public static String getPauseTime() {
        return BASE_URL+"/pauses";
    }
    public static String postLogin() {
        return BASE_URL+"/users";
    }
    public static String getYanCode() {
        return BASE_URL+"/sms";
    }
    public static String getBindPhone() {
        return BASE_URL+"/phones";
    }
    public static String getUserInfo() {
        return BASE_URL+"/personals";
    }
    public static String getTypeCurtain() {
        return BASE_URL+"/homes/";
    }
    public static String getLoginOut() {
        return BASE_URL+"/logouts";
    }
    public static String getForgetPwd() {
        return BASE_URL+"/forgets";
    }
    public static String getChangePwd() {
        return BASE_URL+"/update-passwords";
    }
    public static String getCaseList() {
        return BASE_URL+"/shows";
    }
    public static String getUploadCase() {
        return BASE_URL+"/shows";
    }
    public static String getUploadFile() {
        return BASE_URL+"/files";
    }
    public static String getProduct() {
        return BASE_URL+"/products/";
    }

    public static String getProductWithSize() {
        return BASE_URL+"/products/with-size/";
    }

    public static String getRefreshToken() {
        return BASE_URL+"/tokens";
    }

    public static String getCates() {
        return BASE_URL + "/cates";
    }
    public static String getMessages() {
        return BASE_URL + "/notices";
    }
    public static String getHots() {
        return BASE_URL + "/hots";
    }
    public static String getTypeCate() {
        return BASE_URL + "/products";
    }

    public static String getVideoList() {
        return BASE_URL+"/videos";
    }

    public static String getMater() {
        return BASE_URL+"/materials";
    }
}
