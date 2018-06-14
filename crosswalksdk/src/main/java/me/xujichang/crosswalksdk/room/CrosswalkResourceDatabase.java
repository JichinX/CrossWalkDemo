package me.xujichang.crosswalksdk.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import me.xujichang.crosswalksdk.room.dao.ImageDao;
import me.xujichang.crosswalksdk.room.entity.ImageCache;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/13 - 10:46
 */
@Database(entities = {ImageCache.class}, version = 1, exportSchema = false)
public abstract class CrosswalkResourceDatabase extends RoomDatabase {
    public abstract ImageDao imageDao();

    private static volatile CrosswalkResourceDatabase INSTANCE;


    public static CrosswalkResourceDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CrosswalkResourceDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CrosswalkResourceDatabase.class, "crosswalk_resource.db")
//                            .addMigrations()
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
