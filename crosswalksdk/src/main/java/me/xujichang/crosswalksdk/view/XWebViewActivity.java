package me.xujichang.crosswalksdk.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import me.xujichang.crosswalksdk.R;
import me.xujichang.crosswalksdk.widget.XWebView;

/**
 * Des:包装好的WebView Activity
 *
 * @author xujichang
 * created at 2018/6/7 - 09:54
 */
public class XWebViewActivity extends XWebViewBaseActivity {
    private ProgressBar mProgressBar;
    private XWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_webview);
        mProgressBar = findViewById(R.id.bar_progress);
        mWebView = findViewById(R.id.web_view);
        mWebView.requestFocus();
        setWebView(mWebView);
        initActionBar();
        loadUrl(url);
    }

    private void initActionBar() {
        setActionBarTitle("加载中...");
        showBackArrow();
        setRightImg(R.drawable.ic_refresh_24dp);
    }

    @Override
    protected void onRightAreaClick() {
        onRefresh();
    }

    @Override
    protected XWalkResourceClient onSetResourceClient() {
        SimpleResourceClient resourceClient = new SimpleResourceClient(mWebView, getSchemesMap()) {
            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                super.onProgressChanged(view, progressInPercent);
                mProgressBar.setProgress(progressInPercent);
            }
        };
        return resourceClient;
    }

    @Override
    protected XWalkUIClient onSetUIClient() {
        SimpleUIClient uiClient = new SimpleUIClient(mWebView, getSchemesMap(), this) {
            @Override
            public void onReceivedTitle(XWalkView view, String title) {
                super.onReceivedTitle(view, title);
                setActionBarTitle(title);
            }

            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }, 500);
            }
        };
        return uiClient;
    }
}
