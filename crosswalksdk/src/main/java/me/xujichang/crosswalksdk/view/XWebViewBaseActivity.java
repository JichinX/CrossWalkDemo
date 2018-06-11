package me.xujichang.crosswalksdk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import me.xujichang.crosswalksdk.expose.DefaultIProtocol;
import me.xujichang.crosswalksdk.expose.DefaultJsExpose;
import me.xujichang.crosswalksdk.expose.IProtocol;
import me.xujichang.crosswalksdk.widget.XWebView;
import me.xujichang.util.activity.SuperActivity;


/**
 * Des: 集成使用CrossWalk内核的简单封装，
 * TODO 后续考虑独立出来，不再依赖Activity
 *
 * @author xujichang
 * created at 2018/6/7 - 09:57
 */
public abstract class XWebViewBaseActivity extends SuperActivity {
    private static XWebView mWebView;
    private static final Map<String, IProtocol> schemesMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加对javascript支持
        XWalkPreferences.setValue(XWalkPreferences.ENABLE_JAVASCRIPT, true);
        //开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        //JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        // enable multiple windows.
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
        generateWalkPreferences();
        CookieManager cookieManager = new CookieManager();
        onGotCookieManager(cookieManager);
    }


    @Override
    protected long getActivityExitDuration() {
        return 1000;
    }

    //==============================loadUrl=====================
    public void loadUrl(String url, boolean useCache, boolean clear) {
        mWebView.loadUrl(url);
        mWebView.setDrawingCacheEnabled(useCache);
        if (clear) {
            clearHistoryAndCache();
        }
    }

    public void loadUrl(String url) {
        loadUrl(url, true, false);
    }

    public void loadUrl(String url, boolean userCache) {
        loadUrl(url, userCache, false);
    }

    private void clearHistoryAndCache() {
        //清除历史记录
        mWebView.getNavigationHistory().clear();
        //清除磁盘缓存
        mWebView.clearCache(true);
    }
    //================================setWebView===========================

    protected void setWebView(XWebView webView) {
        setWebView(webView, null);
    }

    protected void setWebView(XWebView webView, Map<String, IProtocol> schemes) {
        setWebView(webView, schemes, null);
    }

    /**
     * 子类将WebView对象反馈上来,进行设置
     *
     * @param webView
     */
    protected void setWebView(XWebView webView, Map<String, IProtocol> schemes, Map<Object, String> jsInterface) {
        mWebView = webView;
        //=====================通信协议设置=======================
        //添加协议
        schemesMap.put(DefaultIProtocol.PROTOCOL_NAME, new DefaultIProtocol());
        //添加自定义的协议
        if (null != schemes && schemes.size() > 0) {
            for (Map.Entry<String, IProtocol> entry : schemes.entrySet()) {
                String key = entry.getKey();
                if (schemesMap.containsKey(key)) {
                    schemesMap.get(key).addProtocols(entry.getValue().getProtocols());
                } else {
                    schemesMap.put(key, entry.getValue());
                }

            }
        }
        //设置默认的js入口
        mWebView.addJavascriptInterface(new DefaultJsExpose(), DefaultJsExpose.ExposeName);
        if (null != jsInterface && jsInterface.size() > 0) {
            for (Map.Entry<Object, String> entry : jsInterface.entrySet()) {
                mWebView.addJavascriptInterface(entry.getKey(), entry.getValue());
            }
        }
        //==========================对WebView的设置======================
        XWalkSettings settings = mWebView.getSettings();
        onWebViewSetting(mWebView, settings);
        //资源的变化
        mWebView.setResourceClient(onSetResourceClient());
        //UI的变化
        mWebView.setUIClient(onSetUIClient());
    }


    /**
     * 可以使用webview 和setting对象 进行设置
     *
     * @param webView
     * @param settings
     */
    protected void onWebViewSetting(XWebView webView, XWalkSettings settings) {
        webView.setHapticFeedbackEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setScrollBarStyle(XWalkView.SCROLLBARS_OUTSIDE_INSET);
        webView.setScrollbarFadingEnabled(true);
        //获取Setting
        settings.setSupportSpatialNavigation(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
    }


    //===============================生命周期=================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebView) {
            mWebView.onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWebView) {
            mWebView.pauseTimers();
            mWebView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWebView) {
            mWebView.resumeTimers();
            mWebView.onShow();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != mWebView) {
            mWebView.onNewIntent(intent);
        }
    }

    protected void onRefresh() {
        mWebView.loadUrl(mWebView.getUrl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.getNavigationHistory().canGoBack()) {
                //返回上一页面
                mWebView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
            } else {
                /*finish();*/
                super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//======================================一些方法或者回调====================

    /**
     * 获取到Cookie对象，可以对cookie进行操作
     *
     * @param cookieManager
     */
    protected void onGotCookieManager(CookieManager cookieManager) {

    }


    /**
     * 对XWalkPreference进行设置
     */
    protected void generateWalkPreferences() {

    }

    protected XWalkResourceClient onSetResourceClient() {
        return new SimpleResourceClient(mWebView, getSchemesMap());
    }

    protected Map<String, IProtocol> getSchemesMap() {
        return schemesMap;
    }

    protected XWalkUIClient onSetUIClient() {
        return new SimpleUIClient(mWebView, getSchemesMap());
    }
}
