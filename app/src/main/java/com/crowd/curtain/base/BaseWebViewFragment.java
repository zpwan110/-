package com.crowd.curtain.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import base.BaseFragment;
import base.BaseWebView;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;

/**
 *
 * @author zpwan110
 * @date 2017/3/20
 */

@Layout(jingshu.com.base.R.layout.base_webview)
public class BaseWebViewFragment extends BaseFragment {
    private static final String METHOD_GET = "get";
    private static final String HTTP_REQUEST  = "http";
    private static final String HTTPS_REQUEST  = "https";
    private static final String BUNDLE_KEY_URL = "url";
    private static final String TELEPHONE_URI = "tel:";
    private HashMap<String, String> titleMap = new HashMap<>();
    @Id(jingshu.com.base.R.id.custom_toolbar)
    public RelativeLayout titleBar;
    @Id(jingshu.com.base.R.id.progressBar)
    public ProgressBar progressBar;
    @Id(jingshu.com.base.R.id.iv_nav_back)
    public ImageView navBackup;
    @Id(jingshu.com.base.R.id.webView)
    public BaseWebView webView;
    @Id(jingshu.com.base.R.id.tv_title)
    public TextView tvTitle;
    private boolean needClearHistory;
    private String originUrl;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY_URL, originUrl);
    }
    private void initFieldsFromBundle(Bundle bundle) {
        originUrl = bundle.getString(BUNDLE_KEY_URL);
    }

    public static BaseWebViewFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_URL, url);
        BaseWebViewFragment baseWebViewFragment = new BaseWebViewFragment();
        baseWebViewFragment.setArguments(bundle);
        return baseWebViewFragment;
    }
    public static BaseWebViewFragment newInstance(String url,String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_URL, url);
        BaseWebViewFragment baseWebViewFragment = new BaseWebViewFragment();
        baseWebViewFragment.setArguments(bundle);
        return baseWebViewFragment;
    }

    @Override
    protected void initViews() {
        initFieldsFromBundle(getArguments());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(titleBar.getLayoutParams());
        titleBar.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
//        webView.addJavascriptInterface(jsInterface==null?new JsInterface(mContext,webView):jsInterface,ANDROID_JSOBJECT);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        if(TextUtils.isEmpty(originUrl)){
            return;
        }
//        navBackup.setOnClickListener(view -> {
//            if(webView.canGoBack()){
//                webView.goBack();
//            }else{
//
//                getActivity().finish();
//            }
//        });
        if (originUrl.startsWith(HTTP_REQUEST)) {
            initCookie(originUrl);
            webView.loadUrl(originUrl);
        } else {
            handleUri(originUrl);
        }
    }

    private void initCookie(String url) {
        //得到CookieManager
        CookieManager cookieManager = CookieManager.getInstance();
//        TokenModel token = UserConfig.getUserToken();
        cookieManager.setAcceptCookie(true);
//        cookieManager.setCookie(url, "proUniId="+ AppConfig.getProUnildId());
//        cookieManager.setCookie(url, "token="+token.access_token);
//        cookieManager.setCookie(url, "refresh_token="+token.refresh_token);
//        cookieManager.setCookie(url, "VersionName="+App.getPackageInfo().versionName);
//        cookieManager.setCookie(url, "phone="+UserConfig.getLastAccount());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != webView.getUrl()) {
            webView.loadUrl("javascript:onResume()");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private WebViewClient webViewClient = new WebViewClient() {
        private boolean receiveError = false;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            tvTitle.setText(titleMap.get(url));
            if (receiveError) {
                receiveError = false;
            }
            setGone(jingshu.com.base.R.id.iv_nav_close);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
            final String url = request.getUrl().toString();
            if (request != null && request.getUrl() != null && METHOD_GET.equalsIgnoreCase(request.getMethod())) {
                String scheme = request.getUrl().getScheme().trim();

            }
            return super.shouldInterceptRequest(view,request);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (null != webView.getUrl() && webView.getUrl().equals(request.getUrl().toString())) {
                    receiveError = true;
                }
            }
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            if (needClearHistory) {
                needClearHistory = false;
                webView.clearHistory();//清除历史记录
            }
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (null != webView.getUrl() && webView.getUrl().equals(request.getUrl().toString())) {
                    receiveError = true;
                }
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (needClearHistory) {
                needClearHistory = false;
                webView.clearHistory();
            }
            progressBar.setVisibility(View.GONE);
        }
    };

    private boolean handleUri(String url) {
        if (url.startsWith(TELEPHONE_URI)){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(intent);
            return  true;
        }
        Uri uri = Uri.parse(url);
        return handleWebViewScheme(uri);
    }
    private boolean handleWebViewScheme(Uri uri) {

        return false;
    }
    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            String url = webView.getUrl();
            titleMap.put(url, title);
            tvTitle.setText(titleMap.get(url));
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return true;
        }

        // Andorid 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        }

        // Andorid 3.0 +
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }
    };

    public static void clearCookies(Context context) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
    @Override
    public boolean onBackpressed() {
        navBackup.performClick();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.clearHistory();
        webView.clearFormData();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.clearFormData();
        webView.removeAllViews();
        webView.destroy();
    }

    @Click(jingshu.com.base.R.id.iv_nav_close)
    public void close() {

    }
    @Click(jingshu.com.base.R.id.tv_right)
    public void toComponRule() {
        setGone(jingshu.com.base.R.id.tv_right);
    }

}
