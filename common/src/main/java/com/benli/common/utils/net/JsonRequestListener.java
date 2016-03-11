package com.benli.common.utils.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.benli.common.utils.CommonUtils;

import org.json.JSONObject;

/**
 * Created by shibenli on 2016/3/9.
 */
public class JsonRequestListener implements Response.Listener<JSONObject>, Response.ErrorListener {
    protected RequestListener listener;
    public JsonRequestListener(RequestListener listener){
        this.listener = listener;
    }
    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonUtils.LOG_D(getClass(), volleyError.toString());
        listener.onError(volleyError.toString());
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        String result = jsonObject.toString();
        CommonUtils.LOG_D(getClass(), result);

        if (listener != null) {
            listener.onSuccess(result);
        }
    }
}
