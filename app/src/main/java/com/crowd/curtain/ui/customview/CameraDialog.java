package com.crowd.curtain.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.ErrorModel;
import base.util.ToastUtil;
import com.crowd.curtain.R;
import com.crowd.curtain.api.UserApi;
import com.crowd.curtain.base.AppBaseActivity;

/**
 * Created by zpwan110 on 2017/4/24.
 */

public class CameraDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private AppBaseActivity mActivity;
    Window window;
    private WindowManager.LayoutParams wlp;

    public CameraDialog(Context context) {
        super(context, R.style.CameraDialog);
        this.context = context;
        this.mActivity = (AppBaseActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_dialog);
        this.setCanceledOnTouchOutside(true);
        window =this.getWindow();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.tv_camera).setOnClickListener(this);
        findViewById(R.id.tv_album).setOnClickListener(this);
        findViewById(R.id.exit_cancel).setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        window.getDecorView().setPadding(0, 0, 0,0);
    }

    /**
     * 退出登录
     */
    private void userExit(){
        UserApi.loginOut(new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                dismiss();
            }

            @Override
            public void onFail(ErrorModel httpError) {
                ToastUtil.showToastFailPic(httpError.msg);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_camera:
                mActivity.takePicForCamera();
                break;
            case R.id.tv_album:
                mActivity.takePicForAlbum();
                break;
            case R.id.exit_cancel:
                break;
        }
        dismiss();
    }
}
