package com.vcredit.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.vcredit.common.service.DownloadService;
import com.vcredit.common.base.BaseApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wangzhengji on 2016/1/26.
 */
public class CommonUtils {
    // 自定义log参数
    private static final String LOG_TAG = "Face";
    private static final int LOG_SIZE_LIMIT = 3500;
    /**
     * 统一自定义log，建议使用
     *
     * @param paramClass  getClass()或xxx.class
     * @param paramString 需要打印string
     */
    public static void LOG_D(Class<?> paramClass, String paramString) {
        // 只有debug模式才打印log
        if (BaseApp.getInstance().getDebugMode()) {
            String str = paramClass.getName();
            if (str != null) {
                str = str.substring(1 + str.lastIndexOf("."));
            }
            int i = paramString.length();
            if (i > LOG_SIZE_LIMIT) {
                int j = 0;
                int k = 1 + i / LOG_SIZE_LIMIT;
                while (j < k + -1) {
                    Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT,
                            LOG_SIZE_LIMIT * (j + 1)));
                    j++;
                }
                Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT, i));
            } else {
                Log.d(LOG_TAG, str + " -> " + paramString);
            }
        }
    }

    /**
     * 获取安装包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context)  {
        PackageInfo packageInfo = new PackageInfo();
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 判断Sdcard是否存在
     *
     * @return
     */
    public static boolean detectSdcardIsExist() {
        String extStorageState = Environment.getExternalStorageState();
        File file = Environment.getExternalStorageDirectory();
        if (!Environment.MEDIA_MOUNTED.equals(extStorageState)
                || !file.exists() || !file.canWrite()
                || file.getFreeSpace() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断存储空间大小是否满足条件
     *
     * @param sizeByte
     * @return
     */
    public static boolean isAvaiableSpace(float sizeByte) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            float availableSpare = blocks * blockSize;
            if (availableSpare > (sizeByte + 1024 * 1024)) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 开始安装apk文件
     *
     * @param context
     * @param localFilePath
     */
    public static void installApkByGuide(Context context, String localFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + localFilePath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 下载
     *
     * @param url      下载链接
     * @param context
     * @param fileName 文件名
     * @param size     文件大小（b）
     */
    public static void startDownload(String url, Context context,
                                     String fileName, Float size) {
        if (CommonUtils.detectSdcardIsExist()) {
            if (CommonUtils.isAvaiableSpace(size)) {
                Intent intent = new Intent(context, DownloadService.class);
                intent.putExtra("downloadUrl", url);
                intent.putExtra("fileName", fileName);
                context.startService(intent);
            } else {
                TooltipUtils.showToastS((Activity) context, "存储卡空间不足");
            }
        } else {
            TooltipUtils.showToastS((Activity) context, "请检查存储卡是否安装");
        }
    }

    /**
     * 获得当前时间
     * @return
     */
    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /** 获取IMEI
     *  手机唯一设别号码
     *  */
    public static String getIMEI(Context context) {
        if (null == context) {
            return null;
        }
        String imei = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
        }
        return imei;
    }

    /**
     * 判断字符串是否为空或NULL
     * @param str
     * @return
     */
    public static boolean isEmptyOrNull(String str){
        return str == null || str.length() == 0;
    }

    /**
     * 判断集合对象是否为空或NULL
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isEmptyOrNull(List<T> list){
        return list == null || list.size() == 0;
    }

    /**
     * InputStream to String
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 获得视口
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }
}
