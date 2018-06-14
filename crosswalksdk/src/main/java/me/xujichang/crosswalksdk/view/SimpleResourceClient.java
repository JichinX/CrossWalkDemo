package me.xujichang.crosswalksdk.view;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.common.base.Strings;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import me.xujichang.crosswalksdk.expose.DefaultIProtocol;
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

    @Override
    public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
        LogTool.d("shouldInterceptLoadRequest:" + request.getUrl() + " auth:" + request.getUrl().getAuthority() + "  path:" + request.getUrl().getPath());
        String scheme = request.getUrl().getScheme();
        if (DefaultIProtocol.PROTOCOL_NAME.equals(scheme)) {
            try {
                String path = request.getUrl().getPath();
                String auth = request.getUrl().getAuthority();
                File file = new File(path);
                FileInputStream inputStream = null;
                inputStream = new FileInputStream(file);
                return createXWalkWebResourceResponse(getMimeType(file), "UTF-8", inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return super.shouldInterceptLoadRequest(view, request);
            }
        }
        return super.shouldInterceptLoadRequest(view, request);
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!Strings.isNullOrEmpty(type)) {
            return type;
        }
        return "file/*";
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if ("".equals(fileName) || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }
}
