package me.xujichang.crosswalksdk.base;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.File;

import me.xujichang.crosswalksdk.R;
import me.xujichang.crosswalksdk.ability.IAbilityCallBack;
import me.xujichang.crosswalksdk.bean.ImageStatus;
import me.xujichang.crosswalksdk.bean.LocationData;
import me.xujichang.crosswalksdk.bean.LocationType;
import me.xujichang.crosswalksdk.bean.Status;
import me.xujichang.crosswalksdk.bean.WrapperResult;
import me.xujichang.crosswalksdk.room.CrosswalkResourceDatabase;
import me.xujichang.crosswalksdk.room.dao.ImageDao;
import me.xujichang.crosswalksdk.room.entity.ImageCache;
import me.xujichang.crosswalksdk.utils.FileUtil;
import me.xujichang.util.activity.SuperActivity;
import me.xujichang.util.tool.GlideTool;

import static me.xujichang.crosswalksdk.bean.Const.ALBUM_REQUEST_CODE;
import static me.xujichang.crosswalksdk.bean.Const.CAMERA_REQUEST_CODE;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/13 - 11:23
 */
public class BaseActivity extends SuperActivity implements IWrapperContext {
    private File cachedPhotoFile;
    private long fileStamp;
    protected RequestManager mRequestManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestManager = Glide.with(this).applyDefaultRequestOptions(GlideTool.getDefaultRequestOperations());
    }

    @Override
    protected long getActivityExitDuration() {
        return 1000;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public LifecycleOwner getOwner() {
        return this;
    }

    @Override
    public void onAlbumSelect(long stamp) {
        fileStamp = stamp;
        Intent intent = new Intent(
                Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    @Override
    public void onCameraSelect(long stamp) {
        fileStamp = stamp;
        //实例化Intent对象,使用MediaStore的ACTION_IMAGE_CAPTURE常量调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //开启相机，传入
        cachedPhotoFile = FileUtil.createImageFile(this, "crasswalk", stamp);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getUriFromFile(this, cachedPhotoFile));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * TODO  需子类实现
     *
     * @param callBack
     * @param type
     */
    @Override
    public void onGotLocation(IAbilityCallBack callBack, LocationType type) {
        LocationData locationData = new LocationData(120.12313456464, 30.156464);
        WrapperResult<LocationData> data = new WrapperResult<>(Status.SUCCESS);
        data.setData(locationData);
        callBack.onCallBack(data.toString());
//        if (type == LocationType.CACHED) {
//
//        } else {
//
//        }
    }

    @Override
    public void onExit(final String data) {
        if (Strings.isNullOrEmpty(data)) {
            finish();
        } else {
            showToast(data);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }
    }

    @Override
    public void showMessage(String data) {
        showToast(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE || requestCode == ALBUM_REQUEST_CODE) {

            //
            final ImageCache cache = new ImageCache(fileStamp);
            final ImageDao imageDao = CrosswalkResourceDatabase.getInstance(this).imageDao();
            if (resultCode == RESULT_CANCELED) {
                cache.setStatus(ImageStatus.CANCEL.ordinal());
                imageDao.updateImage(cache);
                return;
            }
            String path = null;
            File srcFile = null;
            if (requestCode == CAMERA_REQUEST_CODE) {
                //相机
                srcFile = cachedPhotoFile;
            } else {
                //相册
                Uri uri = data.getData();
                if (null != uri) {
                    path = FileUtil.getRealFilePath(getContext(), uri);
                    srcFile = new File(path);
                }
            }
            if (null == srcFile) {
                cache.setStatus(ImageStatus.NONE_DATA.ordinal());
                imageDao.updateImage(cache);
                return;
            }
            path = srcFile.getAbsolutePath();
            final String finalPath = path;
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .customView(R.layout.layout_accident_image_dialog, true)
                    .positiveText("确定")
                    .negativeText("取消")
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.POSITIVE) {
                                cache.setLocPath(finalPath);
                                cache.setStatus(ImageStatus.SUCCESS.ordinal());
                            } else {
                                cache.setStatus(ImageStatus.CANCEL.ordinal());
                            }
                            imageDao.updateImage(cache);
                            dialog.dismiss();
                        }
                    }).cancelable(false)
                    .autoDismiss(false)
                    .show();
            View view = dialog.getCustomView();
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            mRequestManager.load(path).into(ivImage);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
