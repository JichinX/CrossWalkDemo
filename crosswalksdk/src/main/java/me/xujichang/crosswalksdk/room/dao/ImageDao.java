package me.xujichang.crosswalksdk.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import me.xujichang.crosswalksdk.room.entity.ImageCache;

/**
 * Des:对图片表(tbl_cache_image)进行操作
 *
 * @author xujichang
 * created at 2018/6/13 - 10:48
 */
@Dao
public interface ImageDao {
    /**
     * 插入
     *
     * @param cache
     */
    @Insert
    void addImage(ImageCache cache);

    /**
     * 查询
     *
     * @param stamp
     */
    @Query("SELECT * FROM tbl_cache_image WHERE timeStamp = :stamp")
    LiveData<List<ImageCache>> getImage(long stamp);

    /**
     * 更新信息
     *
     * @param cache
     */
    @Update
    void updateImage(ImageCache cache);
}
