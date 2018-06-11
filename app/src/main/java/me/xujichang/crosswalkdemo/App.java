package me.xujichang.crosswalkdemo;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/7 - 11:21
 */
public class App extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
