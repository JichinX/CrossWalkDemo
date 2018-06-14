package me.xujichang.crosswalksdk.bean;

/**
 * Des:图片获取结果状态
 *
 * @author xujichang
 * created at 2018/6/13 - 16:38
 */
public enum ImageStatus {
    /**
     * 默认状态，即还未获取
     */DEFAULT,
    /**
     * 图片获取成功
     */SUCCESS,
    /**
     * 图片获取数据为空
     */NONE_DATA,
    /**
     * 图片获取被取消
     */CANCEL,
    /**
     * 图片获取出现错误
     */EXCEPTION;
}
