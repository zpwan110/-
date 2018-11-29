package com.crowd.curtain.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import base.injectionview.Id;
import base.injectionview.Layout;
import base.widget.Indicator;
import com.crowd.curtain.R;
import com.crowd.curtain.base.AppBaseActivity;

/**
 * <p>{GuideActivity}启动界面</p><img src="../../../{@docRoot}/img/guide01.webp" width="1440" height="2560"></img>
 * <p>{GuideActivity}启动界面</p><img src="../../../{@docRoot}/img/guide02.webp" width="1440" height="2560"></img>
 * <p>{GuideActivity}启动界面</p><img src="../../../{@docRoot}/img/guide03.webp" width="1440" height="2560"></img>
 * @author zhangpeng
 * @date 2017/8/11
 */

@Layout(R.layout.activity_guide)
public class GuideActivity extends AppBaseActivity implements
        View.OnTouchListener, GestureDetector.OnGestureListener {
    @Id(R.id.viewPager)
    private ViewPager viewPager;
    @Id(R.id.indicator)
    private Indicator indicator;

    private int[] imgResourceID = {R.mipmap.guide01, R.mipmap.guide02, R.mipmap.guide03};
    private PagerAdapter adapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return imgResourceID.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            FrameLayout layout = new FrameLayout(GuideActivity.this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(params);
            imageView.setLayoutParams(params);
            Picasso.with(mContext).load(imgResourceID[position]).into(imageView);
            layout.addView(imageView);
            if (position ==(getCount()-1)) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(MainActivity.newIntent(MainActivity.HOME));
                        overridePendingTransition(R.anim.launch_in, R.anim.launch_out);// 淡出淡入动画效果
                        finish();
                    }
                });
            }
            container.addView(layout);
            return layout;
        }
    };
    @Override
    protected void onCreate(Bundle bundle) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//通知栏透明
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        viewPager.setAdapter(adapter);
        indicator.setUpWithViewPager(viewPager);
        indicator.setCount(adapter.getCount());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public static Intent newIntent(Context activity) {
        Intent intent = new Intent(activity, GuideActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }
}