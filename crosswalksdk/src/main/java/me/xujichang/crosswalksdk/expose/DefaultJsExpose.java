package me.xujichang.crosswalksdk.expose;

import org.xwalk.core.JavascriptInterface;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/7 - 10:09
 */
public class DefaultJsExpose {
    public static final String ExposeName = "DefaultInterface";

    @JavascriptInterface
    public String sayHello() {
        return "Hello from Native";
    }
}
