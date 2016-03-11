package com.benli.common.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.benli.common.base.BaseApp;
import com.benli.common.utils.CommonUtils;
import com.benli.common.utils.HttpUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuofeng on 2015/4/8.
 *
 * Volley自定义GET请求
 */
public class GetJsonRequest extends JsonObjectRequest {

    /** 构造函数，初始化请求对象 */
    public GetJsonRequest(String url, JSONObject object, Listener<JSONObject> listener,
                          ErrorListener errorListener) {
        super(Method.GET,  url, object, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30 *1000, 0, 1.0f));
    }

    /** 重写参数编码方法 */
    @Override
    protected String getParamsEncoding() {
        return BaseApp.getInstance().getEncoding();
    }

    /** 重写请求头获取方法 */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Charsert", getParamsEncoding());
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip,deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        if (!CommonUtils.isEmptyOrNull(HttpUtil.sessionId))
            headers.put("x-auth-token", HttpUtil.sessionId);
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Response<JSONObject> superResponse = super.parseNetworkResponse(response);
        String authToken = response.headers.get("x-auth-token");
        if (!CommonUtils.isEmptyOrNull(authToken))
            HttpUtil.sessionId = authToken;
        //CommonUtils.LOG_D(getClass(), "authToken = " + HttpUtil.sessionId);
        return superResponse;
    }

}
