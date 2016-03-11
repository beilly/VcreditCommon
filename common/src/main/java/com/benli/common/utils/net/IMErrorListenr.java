package com.benli.common.utils.net;

import android.content.res.Resources;

import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.benli.common.R;
import com.benli.common.base.BaseApp;
import com.benli.common.utils.CommonUtils;

/**
 * 响应监听类，对错误返回进行处理（ErrorListener子类）
 */
public class IMErrorListenr implements ErrorListener {

    private RequestListener requestListener;

    public IMErrorListenr(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    @Override
    public void onErrorResponse(VolleyError arg0) {
        CommonUtils.LOG_D(getClass(), "arg0=" + arg0.toString());

        Resources resources = BaseApp.getInstance().getResources();

        this.requestListener.onError(arg0 instanceof TimeoutError ?
                resources.getString(R.string.net_error_timeout) :  resources.getString(R.string.net_error_ununited));

        this.requestListener.onError(arg0 instanceof NoConnectionError ?
                resources.getString(R.string.net_error_noConnection) : resources.getString(R.string.net_error_ununited) );

        if (BaseApp.getInstance().getDebugMode() && arg0 != null && arg0.networkResponse != null && arg0.networkResponse.data != null) {
            byte[] htmlBodyBytes = arg0.networkResponse.data;
            if (htmlBodyBytes != null) {
                CommonUtils.LOG_D(getClass(), "VolleyError=" + new String(htmlBodyBytes));
            }
        }

    }
}
