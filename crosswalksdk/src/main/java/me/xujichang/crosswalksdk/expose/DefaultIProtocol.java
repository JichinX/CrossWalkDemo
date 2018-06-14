package me.xujichang.crosswalksdk.expose;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import org.xwalk.core.XWalkJavascriptResult;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.xujichang.crosswalksdk.ability.SimpleAbilityCallBack;
import me.xujichang.crosswalksdk.ability.file.FileAbility;
import me.xujichang.crosswalksdk.ability.file.FileType;
import me.xujichang.crosswalksdk.ability.location.LocationAbility;
import me.xujichang.crosswalksdk.base.IWrapperContext;
import me.xujichang.crosswalksdk.bean.LocationType;
import me.xujichang.crosswalksdk.utils.EnvironmentCheckUtil;

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
    private static WeakReference<IWrapperContext> sWeakReference;

    static {
        TAG = "DefaultIProtocol";
        mMethodMap = new HashMap<>();
        IProtocolMethod sayHello = new SimpleProtocolMethod() {
            @Override
            public void invoke(Uri uri, XWalkJavascriptResult result) {
                //测试
                if (EnvironmentCheckUtil.checkContext(sWeakReference)) {
                    IWrapperContext context = sWeakReference.get();
                    if (null != result) {
                        String query = uri.getQuery();
                        result.confirmWithResult("this str is from Java,and the query from js is :" + query);
                    } else {
                        context.showMessage(uri.getQueryParameter("data"));
                    }
                }
            }
        };
        //文件
        IProtocolMethod obtainFile = new SimpleProtocolMethod() {
            @Override
            public void invoke(Uri uri, XWalkJavascriptResult result) {
                //获取照片
                if (EnvironmentCheckUtil.checkContext(sWeakReference)) {
                    IWrapperContext context = sWeakReference.get();
                    if (null == result) {
                        context.showMessage(uri.getQueryParameter("data"));
                        return;
                    }
                    String type = uri.getQueryParameter("type");
                    String mimeType = uri.getQueryParameter("mime_type");
                    new FileAbility.Builder()
                            .mContext(context)
                            .mFileType(FileType.obtainType(type, mimeType))
                            .mIAbilityCallBack(new SimpleAbilityCallBack<XWalkJavascriptResult>(result) {
                                @Override
                                protected void onCallBackWithSelf(XWalkJavascriptResult selfCallBack, String result) {
                                    selfCallBack.confirmWithResult(result);
                                }
                            })
                            .build()
                            .execute();
                }
            }
        };
        //位置
        IProtocolMethod obtainLocation = new SimpleProtocolMethod() {
            @Override
            public void invoke(Uri uri, XWalkJavascriptResult result) {
                if (EnvironmentCheckUtil.checkContext(sWeakReference)) {
                    IWrapperContext context = sWeakReference.get();
                    String type = uri.getQueryParameter("type");
                    new LocationAbility.Builder()
                            .mType(LocationType.obtainType(type))
                            .mContext(context)
                            .mIAbilityCallBack(new SimpleAbilityCallBack<XWalkJavascriptResult>(result) {
                                @Override
                                protected void onCallBackWithSelf(XWalkJavascriptResult selfCallBack, String result) {
                                    selfCallBack.confirmWithResult(result);
                                }
                            })
                            .build()
                            .execute();
                }
            }
        };
        //退出
        IProtocolMethod exit = new SimpleProtocolMethod() {
            @Override
            public void invoke(Uri uri, XWalkJavascriptResult result) {
                if (EnvironmentCheckUtil.checkContext(sWeakReference)) {
                    IWrapperContext context = sWeakReference.get();
                    String data = uri.getQueryParameter("data");
                    context.onExit(data);
                }
            }
        };
        mMethodMap.put("exit", exit);
        mMethodMap.put("sayHello", sayHello);
        mMethodMap.put("obtainFile", obtainFile);
        mMethodMap.put("obtainLocation", obtainLocation);
    }

    public DefaultIProtocol(IWrapperContext context) {
        sWeakReference = new WeakReference<>(context);
    }

    @Override
    public void analysis(Uri uri) {
        analysis(uri, null);
    }

    @Override
    public void analysis(Uri uri, XWalkJavascriptResult result) {
        String methodName = uri.getAuthority();

        if (mMethodMap.containsKey(methodName)) {
            mMethodMap.get(methodName).invoke(uri, result);
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
