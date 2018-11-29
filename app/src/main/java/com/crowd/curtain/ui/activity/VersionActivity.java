package com.crowd.curtain.ui.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import com.crowd.curtain.R;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;

@Layout(R.layout.activity_version)
public class VersionActivity extends AppBaseActivity {
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.iv_launch)
    ImageView ivLaunch;
    @Id(R.id.tv_version)
    TextView tvVersion;

    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), VersionActivity.class);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    public void initData(){
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("当前版本");
        tvVersion.setText(getLocalVersionName(this));
    }
}
