package com.benli.common.utils.net;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.benli.common.R;
import com.benli.common.utils.CommonUtils;

import org.json.JSONObject;

/**
 * 响应监听类，对正常返回进行后续处理（Listener<String>子类） 对返回信息进行预处理
 */
public class IMJsonListener implements Response.Listener<JSONObject> {

    private RequestListener requestListener;
    private Context mContext;

    public IMJsonListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public IMJsonListener(RequestListener requestListener, Context context) {
        this.requestListener = requestListener;
        mContext = context;
    }

    @Override
    public void onResponse(JSONObject arg0) {
        try {
            CommonUtils.LOG_D(getClass(), arg0.toString());
            Toast.makeText(mContext, arg0.toString(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            this.requestListener.onError(mContext.getResources().getString(R.string.net_error_ununited));
        }
    }
}
