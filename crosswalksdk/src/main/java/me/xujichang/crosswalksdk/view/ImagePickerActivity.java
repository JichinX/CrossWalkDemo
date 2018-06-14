package me.xujichang.crosswalksdk.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.xujichang.crosswalksdk.R;
import me.xujichang.crosswalksdk.base.BaseActivity;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/13 - 11:22
 */
public class ImagePickerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_picker);
    }
}
