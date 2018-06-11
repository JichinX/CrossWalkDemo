package me.xujichang.crosswalksdk.expose;

import android.net.Uri;

import org.xwalk.core.XWalkJavascriptResult;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/11 - 11:37
 */
public interface IProtocolMethod {
    void invoke(Uri uri);

    void invoke(Uri uri, XWalkJavascriptResult result);
}
