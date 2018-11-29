package com.crowd.curtain.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.crowd.curtain.R;
import com.crowd.curtain.base.AppBaseActivity;


/**
 * <p>{LaunchActivity}启动界面</p><img src="../../../{@docRoot}/img/launcher.webp" width="1440" height="2560"></img>
 * @author zhangpeng
 *
 */
public class LaunchActivity extends AppBaseActivity {
    private boolean isBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.newIntent(MainActivity.HOME));
                overridePendingTransition(R.anim.launch_in, R.anim.launch_out);// 淡出淡入动画效果
                finish();
                /*if (!isBackPressed) {
                    if (AppConfig.isFirstLaunch()){
                        startActivity(GuideActivity.newIntent(activity));
                        overridePendingTransition(R.anim.launch_in, R.anim.launch_out);
                    } else {
                        startActivity(MainActivity.newIntent(MainActivity.HOME));
                        overridePendingTransition(R.anim.launch_in, R.anim.launch_out);// 淡出淡入动画效果
                    }
                    finish();
                }*/
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackPressed = true;
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, LaunchActivity.class);
    }
}
