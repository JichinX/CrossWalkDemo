package me.xujichang.crosswalksdk.bean;

import com.google.common.base.Strings;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/14 - 14:15
 */
public enum LocationType {
    /**
     * 取缓存中的位置信息
     */CACHED,
    /**
     * 现在请求
     */NEW_REQUEST;

    public static LocationType obtainType(String type) {
        if (Strings.isNullOrEmpty(type)) {
            return NEW_REQUEST;
        }
        try {
            int typeCode = Integer.parseInt(type);
            if (typeCode == CACHED.ordinal()) {
                return CACHED;
            }
            if (typeCode == NEW_REQUEST.ordinal()) {
                return NEW_REQUEST;
            }
            throw new RuntimeException("No Type with code :" + type);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return NEW_REQUEST;
        }
    }
}
