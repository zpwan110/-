package com.crowd.curtain.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.HashSet;

import base.BaseFragment;
import base.util.LogUtil;
import okhttp3.Call;

/**
 * Created by HanTuo on 16/10/27.
 */

public class AppBaseFragment extends BaseFragment {
    private String TAG = getClass().getName();
    private HashSet<Call> callbackSet;
    protected String mPageName = "";
    protected boolean isCreated;
    protected Context mContext;
    private InputMethodManager imm;

    public void req(Call call) {
        if (null == callbackSet) callbackSet = new HashSet(1);
        callbackSet.add(call);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.isCreated = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != callbackSet) for (Call call : callbackSet) call.cancel();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    protected void onNavBack() {
        FragmentManager fragmentManager = getFragmentManager();
        if (0 == fragmentManager.getBackStackEntryCount()) {
            getActivity().finish();
        } else {
            fragmentManager.popBackStack();
        }
    }

    /**
     * hide Input 隐藏键盘
     */
    public void hideInput() {
        try {
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getActivity().getCurrentFocus() != null && getActivity().getCurrentFocus().getWindowToken() != null && null != imm) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
