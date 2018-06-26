package me.xujichang.crosswalksdk.bean;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/12 - 19:07
 */
public enum Status {
    /**
     * 数据请求 成功
     */SUCCESS(200, "成功"),
    /**
     * 数据请求 失败
     */FAILED(500, "失败"),
    /**
     * 数据返回为空
     */NULL(400, "数据为空"),
    /**
     * 数据返回为空对象或空集合
     */NONE(300, "对象或集合为空"),
    /**
     * 取消
     */CANCEL(100, "请求的操作被取消"), NO_SUCH_METHOD(0, "没有对应解析方法");
    private int code;
    private String msg;

    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
