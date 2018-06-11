package me.xujichang.crosswalksdk.expose;

import android.net.Uri;

import org.xwalk.core.XWalkJavascriptResult;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/8 - 11:14
 */
public interface IProtocol {
    /**
     * 单方面执行
     *
     * @param uri
     */
    void analysis(Uri uri);

    /**
     * 需要result
     *
     * @param uri
     * @param result
     */
    void analysis(Uri uri, XWalkJavascriptResult result);

    /**
     * 获取协议
     *
     * @return
     */
    Map<String, IProtocolMethod> getProtocols();

    /**
     * 添加协议
     *
     * @param protocols
     */
    void addProtocols(Map<String, IProtocolMethod> protocols);

}
