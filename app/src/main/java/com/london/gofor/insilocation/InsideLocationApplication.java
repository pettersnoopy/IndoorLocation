package com.london.gofor.insilocation;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.util.Stack;

import ImageCache.BitmapLruCache;
import database.DataHelper;
import database.SqliteHelper;

/**
 * Created by mac on 15/5/4.
 */
public class InsideLocationApplication extends Application {

    public static final String TAG = "InsideLocationApplication";

    private static BitmapLruCache mCache;

    private static Stack<Activity> activityStack = new Stack<Activity>();

    public void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    public void destroyAll() {
        if (!activityStack.isEmpty()) {
            Activity tmp = activityStack.pop();
            tmp.finish();
        }
    }

//    public static DataHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        File cacheLocation;

//        dbHelper = new DataHelper(this);
//        Log.d("database", "Database created!");

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cacheLocation = new File(Environment.getExternalStorageDirectory() + "/Inside-BitmapCache");
        } else {
            cacheLocation = new File(getFilesDir() + "/Inside-BitmapCache");
        }
        cacheLocation.mkdirs();

        BitmapLruCache.Builder builder = new BitmapLruCache.Builder(this);
        builder.setMemoryCacheEnabled(true).setMemoryCacheMaxSizeUsingHeapSize();
        builder.setDiskCacheEnabled(true).setDiskCacheLocation(cacheLocation);

        mCache = builder.build();

//        Log.d("TAG", "======== device ID " + getLocalMacAddress(this));
    }
}
