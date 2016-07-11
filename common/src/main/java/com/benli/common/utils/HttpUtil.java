package com.benli.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.benli.common.R;
import com.benli.common.utils.net.FakeX509TrustManager;
import com.benli.common.utils.net.GetJsonRequest;
import com.benli.common.utils.net.IMErrorListenr;
import com.benli.common.utils.net.IMJsonListener;
import com.benli.common.utils.net.JsonRequestListener;
import com.benli.common.utils.net.MultipartRequest;
import com.benli.common.utils.net.PostJsonRequest;
import com.benli.common.utils.net.RequestListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * 网络通讯类
 */
public class HttpUtil {

    /** 请求队列 */
    protected RequestQueue queue;
    /** 上下文对象 */
    protected Context context;
    /** 是否显示进度条 */
    public static boolean isOpenProgressbar = true;
    /** 会话识别号 */
    public static String sessionId;

    /**
     * 构造函数
     * @param context
     */
    protected HttpUtil(Context context) {
        queue = Volley.newRequestQueue(context);
        //设置允许所有的SSL验证，使用https自带的SSL加密逻辑
        FakeX509TrustManager.allowAllSSL();
        this.context = context;
        isOpenProgressbar = true;
    }

    protected static HttpUtil httpUtil;

    public static HttpUtil getInstance(Context context){
        if(httpUtil == null) {
            httpUtil = new HttpUtil(context);
        }
        if(context != httpUtil.context){
            httpUtil.cancelAllRequestQueue();
            httpUtil = new HttpUtil(context);
        }

        return httpUtil;
    }

    /**
     * postJson请求
     *
     * @param url
     * @param params Map
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doPostByJson(String url, Map<String, Object> params,
                                            RequestListener requestListener) {
        JSONObject jsonObject = new JSONObject(params);
        return doPostByJson(url, jsonObject, requestListener);
    }


    /**
     * postJson请求
     *
     * @param url
     * @param jsonObject JSONObject
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doPostByJson(String url, JSONObject jsonObject,
                                            RequestListener requestListener) {
        // 网络检查
        if (!checkNetState(context)) {
            Toast.makeText(context, R.string.net_error_check, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        isShowProgressDialog();

        CommonUtils.LOG_D(getClass(), "params = " + jsonObject.toString());
        Request<JSONObject> request = queue.add(new PostJsonRequest(url, jsonObject,
                new IMJsonListener(requestListener, context),
                new IMErrorListenr(requestListener)));

        // 为请求添加context标记
        request.setTag(context);
        return request;
    }

    /**
     * getJson请求
     *
     * @param url
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doGetByJson(String url, RequestListener requestListener) {
        // 网络检查
        if (!checkNetState(context)) {
            Toast.makeText(context, R.string.net_error_check, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        isShowProgressDialog();

        // 加入请求队列
        Request<JSONObject> request = queue.add(new GetJsonRequest(url, new JSONObject(),
                new IMJsonListener(requestListener, context),
                new IMErrorListenr(requestListener)));

        // 为请求添加context标记
        request.setTag(context);
        return request;
    }


    public Request<JSONObject> doPostMultipart(String url, RequestListener requestListener,
                                               Map<String, Object> files, Map<String, String> parms) {
        // 网络检查
        if (!checkNetState(context)) {
            Toast.makeText(context, R.string.net_error_check, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        isShowProgressDialog();

        JsonRequestListener jsonRequestListener = new JsonRequestListener(requestListener);

        // 加入请求队列
        MultipartRequest mr = new MultipartRequest(url,
                jsonRequestListener, jsonRequestListener,
                files,
                parms
        );
        mr.setShouldCache(false);
        Request<JSONObject> request = queue.add(mr);

        // 为请求添加context标记
        request.setTag(context);
        return request;
    }

    /**
     * 清除当前Activity的请求队列
     */
    protected void cancelAllRequestQueue() {
        if (queue != null && context != null) {
            queue.cancelAll(context);
            queue.stop();
            queue = null;
        }
    }

    /**
     * 设置ProgressDialog的显示模式
     * true 为自动模式（ProgressDialog的显示消失和请求的开始结束关联）默认
     * false 为手动模式（ProgressDialog的显示消失由开发者自己控制）
     *
     * @param isOpen
     */
    public void setIsOpenProgressbar(boolean isOpen) {
        isOpenProgressbar = isOpen;
    }

    /**
     * 检测网络状态（true、可用 false、不可用）
     */
    public static boolean checkNetState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    private void isShowProgressDialog() {
        // 是否显示进度条
        if (isOpenProgressbar) {

        }
    }
}
