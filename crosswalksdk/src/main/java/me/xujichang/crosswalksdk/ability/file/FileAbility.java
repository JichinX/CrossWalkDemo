package me.xujichang.crosswalksdk.ability.file;

import android.content.Context;

import me.xujichang.crosswalksdk.ability.IAbilityCallBack;
import me.xujichang.crosswalksdk.base.IWrapperContext;

/**
 * Des:文件功能的中转站
 *
 * @author xujichang
 * created at 2018/6/12 - 10:25
 */
public class FileAbility {
    /**
     * 文件类型 默认为Image
     */
    private FileType mFileType;
    /**
     * 处理结果回调 无默认值
     */
    private IAbilityCallBack mIAbilityCallBack;
    /**
     * 上下文 Context
     */
    private IWrapperContext mContext;

    private FileAbility(Builder builder) {
        mFileType = builder.mFileType;
        mIAbilityCallBack = builder.mIAbilityCallBack;
        mContext = builder.mContext;
    }

    /**
     * 开始按配置进行 调用
     */
    public void execute() {
        if (mFileType == FileType.AUDIO) {
            //音频
        } else if (mFileType == FileType.TXT) {
            //文本
        } else if (mFileType == FileType.VIDEO) {
            //视频
        } else if (mFileType == FileType.IMAGE) {
            //图片
            ImageProvider.selectImage(mContext, mIAbilityCallBack);
        } else if (mFileType == FileType.TYPE_NONE) {
            //未匹配
        }
    }

    public static final class Builder {
        private FileType mFileType;
        private IAbilityCallBack mIAbilityCallBack;
        private IWrapperContext mContext;

        public Builder() {
        }

        public Builder mFileType(FileType val) {
            mFileType = val;
            return this;
        }

        public Builder mIAbilityCallBack(IAbilityCallBack val) {
            mIAbilityCallBack = val;
            return this;
        }

        public Builder mContext(IWrapperContext val) {
            mContext = val;
            return this;
        }

        public FileAbility build() {
            return new FileAbility(this);
        }
    }
}
