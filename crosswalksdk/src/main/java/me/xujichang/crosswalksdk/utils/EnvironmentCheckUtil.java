package me.xujichang.crosswalksdk.utils;

import android.content.Context;

import java.lang.ref.WeakReference;

import me.xujichang.crosswalksdk.base.IWrapperContext;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/12 - 10:22
 */
public class EnvironmentCheckUtil {
    public static boolean checkContext(WeakReference weakReference) {
        return weakReference.get() != null;
    }
}
