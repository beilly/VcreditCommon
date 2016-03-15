package com.benli.common.base;

import android.support.multidex.MultiDexApplication;

import com.benli.common.BuildConfig;
import com.benli.common.R;
import com.benli.common.utils.StorageUtils;
import com.litesuits.common.io.FilenameUtils;

import java.io.File;

/**
 * Created by shibenli on 2016/3/8.
 */
public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    /** 获得Application对象 */
    private static BaseApp appInstance;
    public static BaseApp getInstance() {
        return appInstance;
    }

    public  boolean getDebugMode(){
        return BuildConfig.DEBUG;
    }

    public  String getFilePath(){
        File file = StorageUtils.getOwnCacheDirectory(this, getString(R.string.app_name));
        if (file == null)
            file = getFilesDir();
        return file.getAbsolutePath();
    }

    public  String getEncoding(){
        return "UTF-8";
    }
}
