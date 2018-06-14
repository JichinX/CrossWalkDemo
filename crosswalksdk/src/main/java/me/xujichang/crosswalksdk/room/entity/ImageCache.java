package me.xujichang.crosswalksdk.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.common.base.Strings;

import me.xujichang.crosswalksdk.bean.ImageStatus;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/13 - 10:56
 */
@Entity(tableName = "tbl_cache_image")
public class ImageCache {
    /**
     * 时间戳
     */
    @PrimaryKey
    private long timeStamp;
    /**
     * 图片文件本地路径
     */
    @ColumnInfo(name = "loc_path")
    private String locPath;
    /**
     * 图片本地Uri
     */
    @ColumnInfo(name = "loc_uri")
    private String locUri;
    /**
     * 图片在远程的地址
     */
    @ColumnInfo(name = "remote_url")
    private String remoteUrl;
    /**
     * 此时间戳对应文件的状态
     * 0 默认
     * 1 成功
     * 2 取消
     * 3 为空
     */
    @ColumnInfo(name = "status")
    private int status = 0;

    public ImageCache() {
    }

    @Ignore
    public ImageCache(long stamp) {
        timeStamp = stamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLocPath() {
        return locPath;
    }

    public void setLocPath(String locPath) {
        this.locPath = locPath;
    }

    public String getLocUri() {
        return locUri;
    }

    public void setLocUri(String locUri) {
        this.locUri = locUri;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Ignore
    public String getUsefulPath() {
        if (!Strings.isNullOrEmpty(locPath)) {
            return locPath;
        }
        if (!Strings.isNullOrEmpty(locUri)) {
            return locUri;
        }
        if (!Strings.isNullOrEmpty(remoteUrl)) {
            return remoteUrl;
        }
        return null;
    }

    @Ignore
    public boolean isUseful() {
        return status == ImageStatus.SUCCESS.ordinal();
    }

    @Ignore
    @Override
    public String toString() {
        return "ImageCache{" +
                "timeStamp=" + timeStamp +
                ", locPath='" + locPath + '\'' +
                ", locUri='" + locUri + '\'' +
                ", remoteUrl='" + remoteUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public boolean isDefault() {
        return status == ImageStatus.DEFAULT.ordinal();
    }
}
