package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import base.injectionview.Id;
import base.injectionview.Layout;
import com.crowd.curtain.R;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 *
 * @author zhangpeng
 * @date 2018/3/5
 */
@Layout(R.layout.activity_video_player
)
public class VideoPlayerActivity extends AppBaseActivity {

    @Id(R.id.videoplayer)
    private JCVideoPlayerStandard jcvVideo;
    private String videoUrl;

    public static Intent newInstant(String videoUrl) {
        Intent it = new Intent(App.getContext(), VideoPlayerActivity.class);
        it.putExtra("videoUrl",videoUrl);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoUrl = getIntent().getStringExtra("videoUrl");
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        initView();
    }
    private void initView() {
        jcvVideo.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST,"");
        jcvVideo.startVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPause() {
        super.onPause();
        jcvVideo.stopNestedScroll();
    }

    public void  initData(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jcvVideo.release();
        finish();
    }
}
