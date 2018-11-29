package base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import base.injectionview.InjectionUtil;


/**
 * Created by HanTuo on 16/10/24.
 */

public class BaseFragment extends Fragment {
    protected View rootView;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getView();
        if (null == rootView) {
            rootView = InjectionUtil.loadView(inflater, container, this, false);
            if (null != rootView) {
                rootView.setClickable(true);
                initViews();
            }
        }
        return rootView;
    }

    protected void initViews() {

    }

    protected void setImageResource(int viewId, int resources) {
        ((ImageView) rootView.findViewById(viewId)).setImageResource(resources);
    }

    protected void setText(View v, Object content) {
        ((TextView) v).setText(null == content ? null : String.valueOf(content));
    }

    protected void setText(int viewId, Object content) {
        ((TextView) rootView.findViewById(viewId)).setText(null == content ? null : String.valueOf(content));
    }
    protected void setText(int viewId, Spanned content) {
        ((TextView) rootView.findViewById(viewId)).setText(null == content ? null : content);
    }

    protected void setIvBackgrund(int viewId,boolean isReal) {
        ImageView v =   rootView.findViewById(viewId);
        Drawable drawable = v.getDrawable();
        if(!isReal){
            drawable.setColorFilter(new LightingColorFilter(0x00000000,Color.GRAY));
            v.setImageDrawable(drawable);
        }else{
            drawable.clearColorFilter();
            v.setImageDrawable(drawable);
        }
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
        return (T) rootView.findViewById(id);
    }

    protected void setGone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    protected void setGone(int... ids) {
        for (int id : ids) {
            rootView.findViewById(id).setVisibility(View.GONE);
        }
    }

    protected void setVisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    protected void setVisiable(int... ids) {
        for (int id : ids) {
            rootView.findViewById(id).setVisibility(View.VISIBLE);
        }
    }

    protected void setVisiableOrGone(int ids,int visible) {
        rootView.findViewById(ids).setVisibility(visible);
    }

    protected void setInvisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    protected void setInvisiable(int... ids) {
        for (int id : ids) {
            rootView.findViewById(id).setVisibility(View.INVISIBLE);
        }
    }


    public boolean onBackpressed() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (null != fragments && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment.isResumed() && fragment.getUserVisibleHint()) {
                    if (fragment instanceof BaseFragment) {
                        BaseFragment f = (BaseFragment) fragment;
                        if (f.onBackpressed()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
