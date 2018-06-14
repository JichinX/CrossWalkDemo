package me.xujichang.crosswalksdk.bean;

import com.google.common.base.Strings;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/14 - 10:34
 */
public enum ResourceType {
    /**
     * 图片资源
     */IMAGE("img"),
    /**
     * s视频资源
     */VIDEO("video"),
    /**
     * 文本资源
     */TXT("txt"),
    /**
     * 音频
     */AUDIO("audio");
    private String auth;

    ResourceType(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }
}
