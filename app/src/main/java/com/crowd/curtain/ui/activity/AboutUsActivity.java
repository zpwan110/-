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

@Layout(R.layout.activity_aboutus)
public class AboutUsActivity extends AppBaseActivity {
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.tv_app_about)
    TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("关于我们");
        tvVersion.setText(R.string.app_about);
    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), AboutUsActivity.class);
        return it;
    }
}
