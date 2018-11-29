package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.widget.edittext.ClearEditText;
import com.crowd.curtain.R;
import com.crowd.curtain.api.UserApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.utils.AppSharePerference;

/**
 *
 * @author zhangpeng
 * @date 2018/3/5
 */
@Layout(R.layout.activity_login)
public class LoginActivity extends AppBaseActivity {
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.et_login_mobile)
    ClearEditText tvAccount;
    @Id(R.id.et_login_password)
    ClearEditText tvPwd;
    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), LoginActivity.class);
        return it;
    }

    public static Intent newClearIntent() {
        Intent it = new Intent(App.getContext(), LoginActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        tvCenterTitle.setText("登录");
    }
    @Click(R.id.backIcon)
    private void backUp(){
        finish();
    }

    @Click(R.id.tv_forgetpewd)
    private void forgetPwd(){
       toActivity(ForgetPwdActivity.newIntent());
    }

   @Click(R.id.login_toLogin)
    private  void loginUser(){
       String username = tvAccount.getText().toString().replace(" ","");
       String pwd = tvPwd.getText().toString().trim();
       UserApi.postLoginUser(username, pwd, new Callback<JSONObject>() {
           @Override
           public void onSuccess(JSONObject jsonObject) {
               if(jsonObject.containsKey("data")){
                   JSONObject dataObj = jsonObject.getJSONObject("data");
                   String token = dataObj.getString("token");
                   String longToken = dataObj.getString("longToken");

                   AppSharePerference.saveUserToken(token);
                   AppSharePerference.saveRefreshToken(longToken);
                   finish();
               }
           }

           @Override
           public void onFail(ErrorModel httpError) {

           }
       });
   }
}
