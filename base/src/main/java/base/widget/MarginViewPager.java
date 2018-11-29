package base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/1/18.
 */
public class MarginViewPager extends ViewPager {
    private boolean fromEdge;

    public MarginViewPager(Context context) {
        super(context);
    }

    public MarginViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (event.getX() < 3 * ViewConfiguration.get(getContext()).getScaledEdgeSlop() || getMeasuredWidth() - event.getX() < 3 * ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
//                fromEdge = true;
//            } else {
//                fromEdge = false;
//            }
            fromEdge=false;
        }
        return fromEdge && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return fromEdge && super.onTouchEvent(ev);
    }
}
