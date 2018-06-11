package me.xujichang.crosswalksdk.view;

import android.net.Uri;

import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.util.Map;

import me.xujichang.crosswalksdk.expose.IProtocol;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/11 - 09:57
 */
class SimpleUIClient extends XWalkUIClient {
    private Map<String, IProtocol> schemesMap;

    public SimpleUIClient(XWalkView view, Map<String, IProtocol> schemes) {
        super(view);
        this.schemesMap = schemes;
    }

    @Override
    public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult
            result) {
        LogTool.d("shouldOverrideUrlLoading:" + url);
        Uri uri = Uri.parse(message);
        //自定义协议
        if (schemesMap.containsKey(uri.getScheme())) {
            schemesMap.get(uri.getScheme()).analysis(uri, result);
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
