package com.crowd.curtain.ui.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zhangpeng on 2018/4/9.
 */

public class HorizontalRefreshLayout extends LinearLayout {
    private Handler mHandler;
    private LinearLayout mHeaderView;

    public HorizontalRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public HorizontalRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mHandler = new Handler(Looper.getMainLooper());
        initHeaderView();
    }

    private void initHeaderView() {
        mHeaderView = new LinearLayout(getContext());
        mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mHeaderView.setOrientation(LinearLayout.VERTICAL);
        addView(mHeaderView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
