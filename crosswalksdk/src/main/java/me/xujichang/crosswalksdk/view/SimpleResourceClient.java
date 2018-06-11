package me.xujichang.crosswalksdk.view;

import android.net.Uri;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

import java.util.Map;

import me.xujichang.crosswalksdk.expose.IProtocol;
import me.xujichang.util.tool.LogTool;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/11 - 09:52
 */
public class SimpleResourceClient extends XWalkResourceClient {
    private Map<String, IProtocol> schemesMap;

    public SimpleResourceClient(XWalkView view, Map<String, IProtocol> schemes) {
        super(view);
        schemesMap = schemes;
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        LogTool.d("shouldOverrideUrlLoading:" + url);
        Uri uri = Uri.parse(url);
        //自定义协议
        if (schemesMap.containsKey(uri.getScheme())) {
            schemesMap.get(uri.getScheme()).analysis(uri);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }
}
