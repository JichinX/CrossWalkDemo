package me.xujichang.crosswalksdk.ability;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/12 - 10:32
 */
public abstract class SimpleAbilityCallBack<T> implements IAbilityCallBack {
    private T selfCallBack;

    public SimpleAbilityCallBack(T selfCallBack) {
        this.selfCallBack = selfCallBack;
    }

    @Override
    public void onCallBack(String result) {
        onCallBackWithSelf(selfCallBack, result);
    }

    protected abstract void onCallBackWithSelf(T selfCallBack, String result);
}
