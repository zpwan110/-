package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
@Layout(R.layout.activity_bindphone)
public class BindPhoneActivity extends AppBaseActivity{

    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;

    @Id(R.id.tv_tips)
    TextView tvTips;
    @Id(R.id.btn_code)
    TextView btnCode;
    @Id(R.id.et_new_phone)
    ClearEditText newPhone;
    @Id(R.id.et_new_code)
    ClearEditText newCode;
    private String mobile;


    public static Intent newIntent(String mobile) {
        Intent it = new Intent(App.getContext(), BindPhoneActivity.class);
        it.putExtra("mobile",mobile);
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
        tvCenterTitle.setText("更改绑定手机");
        if(getIntent()!=null){
            mobile = getIntent().getStringExtra("mobile");
            tvTips.setText("更改绑定手机号后，下次登录可以使用新手机号登录当前手机号:"+mobile);
        }

    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    @Click(R.id.btn_code)
    private void getCode(){
        String phone = newPhone.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToastFailPic("新手机号不能为空");
            return;
        }
        UserApi.getYanCode(phone, UserApi.UPD_PHONE, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("message")){
                    String message = jsonObject.getString("message");
                    ToastUtil.showToast(message);
                    CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(btnCode, 60000, 1000);
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
        String phone = newPhone.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToastFailPic("新手机号不能为空");
            return;
        }
        String code = newCode.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(code)){
            ToastUtil.showToastFailPic("验证码不能为空");
            return;
        }
        UserApi.postNewPhone(phone, code, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("message")){
                    String message = jsonObject.getString("message");
                    ToastUtil.showToast(message);
                    finish();
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
