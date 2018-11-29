package base;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by HanTuo on 16/10/24.
 */

public class BaseWebView extends WebView {


    public BaseWebView(Context context) {
        super(context);
        initWebView();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");
        removeJavascriptInterface("searchBoxJavaBridge_");
        WebSettings webSettings = getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //这样你就可以在返回前一个页面的时候不刷新了
        webSettings.setDomStorageEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    if ("application/vnd.android.package-archive".equals(mimetype) || url.toLowerCase().contains(".apk")) {
                        final File file = new File(getContext().getExternalCacheDir(), url.hashCode() + ".apk");
                        if (file.exists()) {
                            Intent it = new Intent(Intent.ACTION_VIEW);
                            it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(it);
                        } else {
                            Toast.makeText(getContext(), "开始下载,下载完成后将提示您安装应用", Toast.LENGTH_SHORT).show();
                            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setDestinationUri(Uri.fromFile(file));
                            request.setTitle("下载中");
                            request.setDescription(contentDisposition);
                            final long downloadId = downloadManager.enqueue(request);
                            getContext().getApplicationContext().registerReceiver(new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    long requestId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                                    if (requestId == downloadId) {
                                        Intent it = new Intent(Intent.ACTION_VIEW);
                                        it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(it);
                                    }
                                    getContext().getApplicationContext().unregisterReceiver(this);
                                }
                            }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }
                    } else {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        getContext().startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
