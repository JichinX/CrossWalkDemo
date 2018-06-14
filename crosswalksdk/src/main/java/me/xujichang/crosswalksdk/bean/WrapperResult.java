package me.xujichang.crosswalksdk.bean;

import com.google.gson.Gson;

/**
 * Des:包装返给js的数据
 *
 * @author xujichang
 * created at 2018/6/12 - 19:11
 */
public class WrapperResult<T> {
    private int code;
    private String msg;
    private T data;

    public WrapperResult(Status status, T t) {
        this(status, t, status.getMsg());
    }

    public WrapperResult() {
        this(Status.SUCCESS, null, Status.SUCCESS.getMsg());
    }

    public WrapperResult(Status status) {
        this(status, (T) null);
    }

    public WrapperResult(Status status, String msg) {
        this(status, null, msg);
    }

    public WrapperResult(Status status, T data, String msg) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setStatus(Status status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
