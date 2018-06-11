package me.xujichang.crosswalksdk.expose;

import android.net.Uri;
import android.util.Log;

import org.xwalk.core.XWalkJavascriptResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Des:默认的协议 codvision://方法名?参数
 * 提供默认的方法
 * 测试使用的sayHello方法
 *
 * @author xujichang
 * created at 2018/6/8 - 11:13
 */
public class DefaultIProtocol implements IProtocol {
    public static final String PROTOCOL_NAME = "codvision";
    private static String TAG;
    private static Map<String, IProtocolMethod> mMethodMap;

    static {
        TAG = "DefaultIProtocol";
        mMethodMap = new HashMap<>();
        IProtocolMethod sayHello = new IProtocolMethod() {
            @Override
            public void invoke(Uri uri) {

            }

            @Override
            public void invoke(Uri uri, XWalkJavascriptResult result) {

            }
        };
        mMethodMap.put("sayHello", sayHello);
    }

    @Override
    public void analysis(Uri uri) {
        analysis(uri, null);
    }

    @Override
    public void analysis(Uri uri, XWalkJavascriptResult result) {
        String methodName = uri.getAuthority();

        if (mMethodMap.containsKey(methodName)) {
            mMethodMap.get(methodName).invoke(uri, null);
        } else {
            throw new RuntimeException("No such method :" + methodName);
        }
    }

    @Override
    public Map<String, IProtocolMethod> getProtocols() {
        return mMethodMap;
    }

    @Override
    public void addProtocols(Map<String, IProtocolMethod> protocols) {
        mMethodMap.putAll(protocols);
    }
}
