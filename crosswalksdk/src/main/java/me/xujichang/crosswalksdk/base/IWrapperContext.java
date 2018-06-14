package me.xujichang.crosswalksdk.base;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import me.xujichang.crosswalksdk.ability.IAbilityCallBack;
import me.xujichang.crosswalksdk.bean.LocationType;

/**
 * Des: 对上下文环境做封装
 *
 * @author xujichang
 * created at 2018/6/13 - 13:42
 */
public interface IWrapperContext {
    Context getContext();

    Activity getActivity();

    LifecycleOwner getOwner();

    void onAlbumSelect(long stamp);

    void onCameraSelect(long stamp);

    void onGotLocation(IAbilityCallBack callBack, LocationType type);
}
