package com.crowd.curtain.ui.customview;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.ErrorModel;
import base.util.ToastUtil;
import com.crowd.curtain.R;
import com.crowd.curtain.api.UserApi;
import com.crowd.curtain.ui.activity.SettingActivity;
import com.crowd.curtain.utils.AppSharePerference;

/**
 * Created by zpwan110 on 2017/4/24.
 */

public class ExitDialog extends Dialog implements View.OnClickListener{
    View dialogView;
    ImageView ivClose;
    private Context context;
    private SettingActivity settingActivity;
    Window window;
    private TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
    private long dialogTranDuration = 2000;
    private int titleHeight;
    private WindowManager.LayoutParams wlp;
    private int padingDialog;
    private AnimatorSet animatorSetClose;

    public ExitDialog(Context context) {
        super(context, R.style.ExitDialog);
        this.context = context;
        this.settingActivity = (SettingActivity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_dialog);
        this.setCanceledOnTouchOutside(true); //点击外部会消失
        window =this.getWindow();
        initViews();
    }

    private void initViews() {
        findViewById(R.id.exit_conmit).setOnClickListener(this);
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
                ToastUtil.showToastOkPic("退出成功");
                AppSharePerference.deleteUserToken();
                settingActivity.initData();
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
            case R.id.exit_conmit:
                userExit();
                break;
            case R.id.exit_cancel:
                ToastUtil.showToastFailPic("取消");
                dismiss();
                break;
        }
    }
}
