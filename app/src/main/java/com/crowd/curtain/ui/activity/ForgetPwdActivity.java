package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.CountDownTimerUtils;
import base.util.ToastUtil;
import base.widget.edittext.ClearEditText;
import com.crowd.curtain.R;
import com.crowd.curtain.api.UserApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;

/**
 * Created by zhangpeng on 2018/3/8.
 */
@Layout(R.layout.activity_forgetpwd)
public class ForgetPwdActivity extends AppBaseActivity{

    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.btn_yan)
    TextView btnYan;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;

    @Id(R.id.et_mobile)
    ClearEditText clMobile;
    @Id(R.id.et_yan_code)
    ClearEditText clYancode;
    @Id(R.id.et_new_pwd)
    ClearEditText clNewpwd;


    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), ForgetPwdActivity.class);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("忘记密码");
    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    @Click(R.id.btn_yan)
    private void getCode(){
        String mobile = clMobile.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(mobile)){
            ToastUtil.showToastFailPic("手机号不能为空");
            return;
        }
        UserApi.getYanCode(mobile, UserApi.FORGET, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("message")){
                    String message = jsonObject.getString("message");
                    ToastUtil.showToast(message);
                    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(btnYan, 60000, 1000);
                    countDownTimerUtils.startTime();
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }
    @Click(R.id.btn_confirm)
    private void changePwd(){
        String phone = clMobile.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToastFailPic("手机号不能为空");
            return;
        }
        String yan = clYancode.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(yan)){
            ToastUtil.showToastFailPic("验证码不能为空");
            return;
        }
        String newPwd = clNewpwd.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(newPwd)){
            ToastUtil.showToastFailPic("请输入新密码");
            return;
        }
        UserApi.forgetPwd(phone,yan,newPwd,new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                finish();
                ToastUtil.showToast("密码已重置");
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

}
