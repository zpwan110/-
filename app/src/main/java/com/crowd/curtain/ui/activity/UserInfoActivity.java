package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import com.crowd.curtain.R;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.UserEntity;
import com.crowd.curtain.utils.AppSharePerference;

/**
 *
 * @author zhangpeng
 * @date 2018/3/5
 */
@Layout(R.layout.activity_user)
public class UserInfoActivity extends AppBaseActivity {
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.tv_account)
    RelativeLayout rlAccount;
    @Id(R.id.ll_unlogin)
    LinearLayout llUnlogin;

    @Id(R.id.user_account)
    TextView userAccount;
    @Id(R.id.user_name)
    TextView userName;
    @Id(R.id.user_phone)
    TextView userPhone;
    @Id(R.id.user_sexy)
    TextView userSexy;
    @Id(R.id.user_address)
    TextView userAddress;
    private UserEntity userEntity;

    public static Intent newIntent(UserEntity userEntity) {
        Intent it = new Intent(App.getContext(), UserInfoActivity.class);
        it.putExtra("user",userEntity);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null){
            userEntity = getIntent().getParcelableExtra("user");
        }
        initView();
        initMine();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(AppSharePerference.getUserToken())){
            finish();
        }
    }

    private void initMine() {
        if(userEntity==null){return;}
        userAccount.setText(userEntity.username);
        userName.setText(userEntity.name);
        userPhone.setText(userEntity.phone);
        userSexy.setText(userEntity.sex);
        userAddress.setText(userEntity.address);
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("个人资料");
    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    @Click(R.id.user_change_pwd)
    private void changPwd(){
        toActivity(UpdatePwdActivity.newIntent());
    }
    @Click(R.id.user_change_mobile)
    private void changeMobile(){
        toActivity(BindPhoneActivity.newIntent(userEntity.phone));
    }
}
