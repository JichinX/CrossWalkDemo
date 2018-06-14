package me.xujichang.crosswalksdk.ability.file;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.xujichang.crosswalksdk.ability.IAbilityCallBack;
import me.xujichang.crosswalksdk.base.IWrapperContext;
import me.xujichang.crosswalksdk.bean.Status;
import me.xujichang.crosswalksdk.bean.WrapperResult;
import me.xujichang.crosswalksdk.room.CrosswalkResourceDatabase;
import me.xujichang.crosswalksdk.room.dao.ImageDao;
import me.xujichang.crosswalksdk.room.entity.ImageCache;
import me.xujichang.crosswalksdk.utils.FileUtil;
import me.xujichang.crosswalksdk.view.ImagePickerActivity;
import me.xujichang.util.tool.LogTool;

import static me.xujichang.crosswalksdk.bean.Const.ALBUM_REQUEST_CODE;
import static me.xujichang.crosswalksdk.bean.Const.CAMERA_REQUEST_CODE;

/**
 * Des:文件-图片处理
 *
 * @author xujichang
 * created at 2018/6/12 - 11:13
 */
public class ImageProvider {
    private static ImageDao mImageDao;

    public static void selectImage(final IWrapperContext context, final IAbilityCallBack iAbilityCallBack) {
        checkImageDao(context.getContext());
        ArrayList<String> items = new ArrayList<>();
        items.add("相机");
        items.add("相册");
        new MaterialDialog.Builder(context.getContext())
                .title("图片来源")
                .content("请选择拍照或者从相册中获取")
                .items(items)
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        iAbilityCallBack.onCallBack(new Gson().toJson(new WrapperResult<ImageCache>(Status.CANCEL)));
                    }
                })
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position,
                                            CharSequence text) {
                        final long stamp = System.currentTimeMillis();
                        //插入数据库
                        ImageCache cache = new ImageCache(stamp);
                        mImageDao.addImage(cache);
                        //根据stamp进行监听状态
                        final LiveData<List<ImageCache>> liveData = mImageDao.getImage(stamp);
                        final Observer<List<ImageCache>> observer = new Observer<List<ImageCache>>() {

                            @Override
                            public void onChanged(@Nullable List<ImageCache> caches) {
                                LogTool.d("Image changed... ...");
                                ImageCache cache = null;
                                WrapperResult<ImageCache> result = new WrapperResult<ImageCache>();
                                if (null != caches && caches.size() > 0) {
                                    cache = caches.get(0);
                                    LogTool.d("ImageCache changed: " + cache);
                                    if (cache.isDefault()) {
                                        LogTool.d("ImageCache changed: default " + cache);
                                        return;
                                    }
                                    if (cache.isUseful()) {
                                        LogTool.d("ImageCache changed: useful " + cache);
                                        result.setData(cache);
                                        result.setStatus(Status.SUCCESS);
                                    } else {
                                        LogTool.d("ImageCache changed: not useful " + cache);
                                        result.setStatus(Status.NONE);
                                        result.setData(cache);
                                    }
                                } else {
                                    LogTool.d("ImageCache changed: none " + cache);
                                    result.setStatus(Status.NONE);
                                    result.setData(cache);
                                }

                                liveData.removeObserver(this);
                                iAbilityCallBack.onCallBack(new Gson().toJson(result));
                            }
                        };
                        liveData.observe(context.getOwner(), observer);
                        if (position == 1) {
                            context.onAlbumSelect(stamp);

                        } else {
                            context.onCameraSelect(stamp);
                        }
                        dialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .cancelable(false)
                .show();
    }

    private static void checkImageDao(Context context) {
        if (null == mImageDao) {
            mImageDao = CrosswalkResourceDatabase.getInstance(context).imageDao();
        }
    }
}
