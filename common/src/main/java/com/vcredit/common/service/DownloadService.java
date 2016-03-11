package com.vcredit.common.service;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.webkit.MimeTypeMap;

import com.vcredit.common.base.BaseApp;
import com.vcredit.common.utils.CommonUtils;

/**
 * 下载服务
 *
 * @author zhuofeng
 */
public class DownloadService extends Service {

    private DownloadManager downloadManager = null;
    private OnCompleteReceiver onComplete;
    private long downloadId;
    private String fileName;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        onComplete = new OnCompleteReceiver();
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getStringExtra("downloadUrl");
            fileName = intent.getStringExtra("fileName");
            Environment.getExternalStoragePublicDirectory(BaseApp.getInstance().getFilePath()).mkdirs();
            download(url, fileName);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 下载
    @SuppressLint("NewApi")
    private void download(String url, String fileNameTmp) {
        if (isDownloading(url)){
            return;
        }
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        // 根据文件后缀设置mime
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        int startIndex = fileNameTmp.lastIndexOf(".");
        String tmpMimeString = fileNameTmp.substring(startIndex + 1).toLowerCase();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(tmpMimeString);
        request.setMimeType(mimeString);
        CommonUtils.LOG_D(getClass(), "--------mimeTypeMap =" + mimeString);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileNameTmp);
        request.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        request.setDescription("更新下载");
        request.setDestinationInExternalPublicDir(BaseApp.getInstance().getFilePath(), fileNameTmp);
        downloadId = downloadManager.enqueue(request);
    }

    // 下载通知点击的监听器
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            showDownloadManagerView();
        }
    };

    // 下载完成的接收器
    private class OnCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // 通过intent获取发广播的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(id));
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (id == downloadId && c.getInt(columnIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                    String localFilePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    CommonUtils.installApkByGuide(DownloadService.this, localFilePath);
                    stopSelf();
                }
            }
            c.close();
        }
    }

    // 跳转到系统下载界面
    private void showDownloadManagerView() {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isDownloading(String url) {
        Cursor c = downloadManager.query(new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING));
        if (c.moveToFirst()) {
            String tmpURI = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
            if (tmpURI.equals(url)){
                return true;
            }
        }
        return false;
    }
}
