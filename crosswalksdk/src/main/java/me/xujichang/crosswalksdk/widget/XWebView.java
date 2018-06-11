package me.xujichang.crosswalksdk.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import org.xwalk.core.XWalkView;

/**
 * Des:自定义WebView 方便以后扩展
 *
 * @author xujichang
 * created at 2018/6/7 - 09:52
 */
public class XWebView extends XWalkView {
    public XWebView(Context context) {
        super(context);
    }

    public XWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XWebView(Context context, Activity activity) {
        super(context, activity);
    }
}
