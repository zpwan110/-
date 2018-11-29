package com.crowd.curtain.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.CookieManager;

import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.base.BaseWebViewFragment;

/**
 * @author zhangpeng
 */
public class WebViewActivity extends AppBaseActivity {

    public static final String INTENT_KEY_URL = "URL";
    public static final String INTENT_KEY_SHOW_MORE = "show_more";
    private int code = 0;
    private String mPageName;
    private CookieManager cookieManager;

    public static Intent newIntent(Context context, String url) {
        Intent it = new Intent(context, WebViewActivity.class);
        it.putExtra(INTENT_KEY_URL, url);
        return it;
    }
    public static Intent newIntent(Activity activity,String url) {
        Intent it = new Intent(activity, WebViewActivity.class);
        it.putExtra(INTENT_KEY_URL, url);
        return it;
    }

    public static Intent newIntent(Context context,String url, int code) {
        Intent it = new Intent(context, WebViewActivity.class);
        it.putExtra(INTENT_KEY_URL, url);
        it.putExtra(INTENT_KEY_SHOW_MORE, code);
        return it;
    }

    public static Intent newIntent(Context context,String url, String pageName) {
        Intent it = new Intent(context, WebViewActivity.class);
        it.putExtra(INTENT_KEY_URL, url);
        it.putExtra("PageName", pageName);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageName = getIntent().getStringExtra("PageName");
        code = getIntent().getIntExtra(INTENT_KEY_SHOW_MORE,0);
        getSupportFragmentManager().beginTransaction().add(Window.ID_ANDROID_CONTENT, code != 0
                        ? BaseWebViewFragment.newInstance(getIntent().getStringExtra(INTENT_KEY_URL), code+"")
                        : BaseWebViewFragment.newInstance(getIntent().getStringExtra(INTENT_KEY_URL), mPageName),
                BaseWebViewFragment.class.getName())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
