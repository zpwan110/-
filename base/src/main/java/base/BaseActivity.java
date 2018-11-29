package base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import base.injectionview.InjectionUtil;


/**
 * Created by HanTuo on 16/10/24.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener,FragmentManager.OnBackStackChangedListener{
    protected Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        InjectionUtil.load(this, false);
        activity = this;
    }

    protected void setText(View v, CharSequence content) {
        ((TextView) v).setText(content);
    }
    protected void setBackgroudColor(int viewId,int color) {
        (findViewById(viewId)).setBackgroundColor(color);
    }
    protected void setBackgroudColor(int viewId,Drawable color) {
        (findViewById(viewId)).setBackground(color);
    }
    protected void setText(int viewId, CharSequence content) {
       TextView tv = ((TextView) findViewById(viewId));
        tv.setText(content);
        tv.setVisibility(View.VISIBLE);
    }
    protected void setTextDrawable(int viewId, String name,int drawablweId) {
        TextView textView =   findViewById(viewId);
        textView.setText(name);
        Drawable drawable = getResources().getDrawable(drawablweId);
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
    }
    protected void post(Runnable runnable){
        getWindow().getDecorView().post(runnable);
    }

    protected void postDelayed(Runnable runnable, long delay){
        getWindow().getDecorView().postDelayed(runnable, delay);
    }

    /**
     * 调用(Clazz)v.findViewById(id)
     *
     * @param id
     * @param v
     */
    public final <T extends View> T f(int id, View v) {
        return (T) v.findViewById(id);
    }

    /**
     * 调用 findViewById(id)
     *
     * @param id
     */
    public final <T extends View> T f(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }

    protected void setGone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    protected void setGone(int... ids) {
        for (int id : ids) {
            findViewById(id).setVisibility(View.GONE);
        }
    }

    protected void setVisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    protected void setVisiable(int... ids) {
        for (int id : ids) {
            findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    protected void setInvisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    protected void setInvisiable(int... ids) {
        for (int id : ids) {
            findViewById(id).setVisibility(View.INVISIBLE);
        }
    }
    /**
     *
     * @param intent
     */
    public void toActivity(final Intent intent) {
        toActivity(intent, true);
    }

    /**
     * 打开新的Activity
     *
     * @param intent
     * @param showAnimation
     */
    public void toActivity(final Intent intent, final boolean showAnimation) {
        toActivity(intent, -1, showAnimation);
    }

    /**
     * 打开新的Activity，向左滑入效果
     *
     * @param intent
     * @param requestCode
     */
    public void toActivity(final Intent intent, final int requestCode) {
        toActivity(intent, requestCode, true);
    }
    /**
     * 打开新的Activity
     *
     * @param intent
     * @param requestCode
     * @param showAnimation
     */
    public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {

        if (intent == null) {
            return;
        }
        //fragment中使用context.startActivity会导致在fragment中不能正常接收onActivityResult
        if (requestCode < 0) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
    }




    @Override
    public void onBackPressed() {
        try {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (null != fragments && !fragments.isEmpty()) {
                for (Fragment fragment : fragments) {
                    if (null != fragment) {
                        if (fragment.isResumed() && fragment.getUserVisibleHint()) {
                            if (fragment instanceof BaseFragment) {
                                BaseFragment f = (BaseFragment) fragment;
                                if (f.onBackpressed()) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackStackChanged() {

    }
}
