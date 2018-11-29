package com.crowd.curtain.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import base.BroadcastConstants;
import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.ToastUtil;
import com.crowd.curtain.R;
import com.crowd.curtain.api.UserApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.UserEntity;
import com.crowd.curtain.ui.customview.ExitDialog;
import com.crowd.curtain.utils.AppSharePerference;

/**
 *
 * @author zhangpeng
 * @date 2018/3/5
 */
@Layout(R.layout.activity_setting)
public class SettingActivity extends AppBaseActivity {
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.tv_account)
    TextView tvAccount;
    @Id(R.id.app_version)
    TextView appVersion;
    @Id(R.id.rl_account)
    RelativeLayout rlAccount;
    @Id(R.id.ll_unlogin)
    LinearLayout llUnlogin;
    @Id(R.id.btn_loginOut)
    TextView loginout;
    private UserEntity userEntity;
    private ExitDialog exitDialog;

    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), SettingActivity.class);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        exitDialog = new ExitDialog(mContext);
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("我的");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void  initData(){
        appVersion.setText("当前版本"+getLocalVersionName(this));
        if(TextUtils.isEmpty(AppSharePerference.getUserToken())){
            tvAccount.setText("未登录");
            llUnlogin.setVisibility(View.VISIBLE);
            rlAccount.setVisibility(View.GONE);
            loginout.setVisibility(View.GONE);
        }else{
            tvAccount.setText(AppSharePerference.getLastAccount());
            llUnlogin.setVisibility(View.GONE);
            rlAccount.setVisibility(View.VISIBLE);
            loginout.setVisibility(View.VISIBLE);
            UserApi.getUserInfo(new Callback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    if(jsonObject.containsKey("data")){
                        userEntity = JSON.parseObject(jsonObject.getString("data"),UserEntity.class);
                        tvAccount.setText(userEntity.username);
                    }
                }

                @Override
                public void onFail(ErrorModel httpError) {

                }
            });
        }
    }
    @Click(R.id.btn_tologin)
    private void toLogin(){
        toActivity(LoginActivity.newIntent());
    }
    @Click(R.id.ll_message)
    private void toMessage(){
        toActivity(MessageActivity.newIntent());
    }
    @Click(R.id.btn_loginOut)
    private void btnoQuit(){
        exitDialog.show();
    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    @Click(R.id.rl_account)
    private void toMine(){
        if(userEntity!=null){
            toActivity(UserInfoActivity.newIntent(userEntity));
        }
    }
    @Click(R.id.ll_about)
    private void toAbout(){
        toActivity(AboutUsActivity.newIntent());
    }
    @Click(R.id.ll_update)
    private void toUpdate(){
        toActivity(VersionActivity.newIntent());
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        if (BroadcastConstants.BROADCAST_REFRESH_TOKEN_TIME_OUT.equals(action)) {
            ToastUtil.showToast("登录超时请重新登录");
            AppSharePerference.saveUserToken("");
        }
    }
}
