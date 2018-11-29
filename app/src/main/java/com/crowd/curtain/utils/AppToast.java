package com.crowd.curtain.utils;

import base.util.ToastUtil;

/**
 * Created by zhangpeng on 2017/7/13.
 */

public class AppToast extends ToastUtil {
    public static void showOk(String message){
        showToastOkPic(message);
    }
    public static void showFail(String message){
        showToastFailPic(message);
    }
}
