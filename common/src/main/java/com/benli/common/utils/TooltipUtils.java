package com.benli.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by wangzhengji on 2015/8/25.
 */
public class TooltipUtils {
    /**
     * 通用创建无message的dialog，默认支持点击外部取消、支持按返回取消
     *
     * @param context
     * @param title            弹框的标题
     * @param positiveListener 确定按钮的实例化监听
     * @param negativeListener 取消按钮的实例化监听
     * @param positiveText     确定按钮的文字显示
     * @param negativeText     取消按钮的文字显示
     */
    public synchronized static void showDialog(Context context, String title,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener,
                                  String positiveText, String negativeText) {
        showDialog(context, title, positiveListener, negativeListener, positiveText, negativeText, true);
    }

    /**
     * 通用创建dialog，默认支持点击外部取消、支持按返回取消
     *
     * @param context
     * @param title            弹框的标题
     * @param message          弹框的内容
     * @param positiveListener 确定按钮的实例化监听
     * @param negativeListener 取消按钮的实例化监听
     * @param positiveText     确定按钮的文字显示
     * @param negativeText     取消按钮的文字显示
     */
    public synchronized static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener,
                                  String positiveText, String negativeText) {
        showDialog(context, title, message, positiveListener, negativeListener ,positiveText, negativeText, true);
    }

    /**
     * 通用创建无message的dialog，默认支持按返回取消
     *
     * @param context
     * @param title            弹框的标题
     * @param positiveListener 确定按钮的实例化监听
     * @param negativeListener 取消按钮的实例化监听
     * @param positiveText     确定按钮的文字显示
     * @param negativeText     取消按钮的文字显示
     * @param touchOutside     是否支持点击外部取消
     */
    public synchronized static void showDialog(Context context, String title,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener,
                                  String positiveText, String negativeText, boolean touchOutside) {
        showDialog(context, title, null, positiveListener, negativeListener, positiveText, negativeText, touchOutside);
    }

    /**
     * 通用创建dialog，默认支持按返回取消
     * @param context
     * @param title            弹框的标题
     * @param message          弹框的内容
     * @param positiveListener 确定按钮的实例化监听
     * @param negativeListener 取消按钮的实例化监听
     * @param positiveText     确定按钮的文字显示
     * @param negativeText     取消按钮的文字显示
     * @param touchOutside     是否支持点击外部取消
     */
    public synchronized static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener positiveListener,
                                  DialogInterface.OnClickListener negativeListener,
                                  String positiveText, String negativeText, boolean touchOutside) {
        showDialog(context, title, message, positiveListener, negativeListener, positiveText, negativeText, touchOutside, true);
    }

    /**
     * 通用创建dialog
     * @param context
     * @param title            弹框的标题
     * @param message          弹框的内容
     * @param positiveListener 确定按钮的实例化监听
     * @param negativeListener 取消按钮的实例化监听
     * @param positiveText     确定按钮的文字显示
     * @param negativeText     取消按钮的文字显示
     * @param touchOutside     是否支持点击外部取消
     * @param cancelable       是否支持按返回取消
     */
    public synchronized static void showDialog(Context context, String title, String message,
                                               DialogInterface.OnClickListener positiveListener,
                                               DialogInterface.OnClickListener negativeListener,
                                               String positiveText, String negativeText, boolean touchOutside, boolean cancelable) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setPositiveButton(positiveText, positiveListener);
        alertDialog.setNegativeButton(negativeText, negativeListener);

        //是否包含标题，设置Title
        boolean hasTitle = !TextUtils.isEmpty(title);
        if (hasTitle) {
            alertDialog.setTitle(title);
        }

        //包含内容的时候，设置Message
        boolean hasMsg = !TextUtils.isEmpty(message);
        if (hasMsg){
            alertDialog.setMessage(message);
        }

        //只要标题和内容有一个不是空就显示Dialog
        if (hasTitle || hasMsg){
            AlertDialog dialog = alertDialog.create();
            dialog.setCanceledOnTouchOutside(touchOutside);
            dialog.setCancelable(cancelable);
            dialog.show();
        }
    }

    /**
     * 短时间显示Toast消息，并保证运行在UI线程中
     *
     * @param activity Activity
     * @param message  消息内容
     */
    public static void showToastS(final Activity activity, final Object ...message) {
        showToast(activity, false, 1500, message);
    }

    /**
     * 长时间显示Toast消息，并保证运行在UI线程中
     *
     * @param activity Activity
     * @param message  消息内容
     */
    public static void showToastL(final Activity activity, final Object ...message) {
        showToast(activity, false, 3000, message);
    }

    public interface MessageFilter {
        String filter(String msg);
    }

    public static MessageFilter msgFilter;

    /**
     * Toast消息
     *
     * @param activity
     * @param message  消息内容
     * @param center   是否居中
     * @param time     显示时间
     */
    public static void showToast(final Activity activity, final boolean center, final int time, final Object ...message) {
        String msg1 = "";
        if (ArrayUtils.isEmpty(message)) {
            msg1 = "null or empty msg!";
        }else{
            msg1 = String.format(message[0].toString(), ArrayUtils.subarray(message, 1, message.length));
        }
        final String msg = msgFilter != null ? msgFilter.filter(msg1) : msg1;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (currentToast == null) {
                    currentToast = Toast.makeText(activity.getApplication(), msg, time);
                }
                currentToast.setText(msg);
                currentToast.setDuration(time);
                if (center) currentToast.setGravity(Gravity.CENTER, 0, 0);
                currentToast.show();
            }
        });
    }

    /**
     * 关闭Toast
     */
    public static void cancelAllToast(){
        try {
            if (currentToast != null)
                currentToast.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Toast currentToast = null;
}
