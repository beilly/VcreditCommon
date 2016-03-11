package com.vcredit.common.base;

import android.support.multidex.MultiDexApplication;

import com.vcredit.common.BuildConfig;

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
        return getApplicationInfo().name;
    }

    public  String getEncoding(){
        return "UTF-8";
    }
}
