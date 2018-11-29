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
import base.util.ToastUtil;
import base.widget.edittext.ClearEditText;
import com.crowd.curtain.R;
import com.crowd.curtain.api.UserApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.utils.AppSharePerference;

/**
 * Created by zhangpeng on 2018/3/8.
 */
@Layout(R.layout.activity_updatepwd)
public class UpdatePwdActivity extends AppBaseActivity{

    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;

    @Id(R.id.et_old_pwd)
    ClearEditText oldPwd;
    @Id(R.id.et_new_pwd)
    ClearEditText newPwd;
    @Id(R.id.et_again_pwd)
    ClearEditText againPwd;


    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), UpdatePwdActivity.class);
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
        tvCenterTitle.setText("修改密码");


    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    @Click(R.id.btn_confirm)
    private void changePwd(){
        String oldpwd = oldPwd.getText().toString().replace(" ","");
        String newpwd = newPwd.getText().toString().replace(" ","");
        String againpwd = againPwd.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(oldpwd)){
            ToastUtil.showToastFailPic("请输入原始密码");
            return;
        }
        if(TextUtils.isEmpty(newpwd)){
            ToastUtil.showToastFailPic("请输入新密码");
            return;
        }
        if(!againpwd.equals(newpwd)){
            ToastUtil.showToastFailPic("两次输入不一致");
            return;
        }
        UserApi.changePwd(oldpwd, newpwd, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String token = dataObj.getString("token");
                    String longToken = dataObj.getString("longToken");
                    toActivity(LoginActivity.newClearIntent());
                    AppSharePerference.saveUserToken("");
                    AppSharePerference.saveRefreshToken("");
                    ToastUtil.showToastOkPic("修改成功");
                    finish();
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });

    }

}
