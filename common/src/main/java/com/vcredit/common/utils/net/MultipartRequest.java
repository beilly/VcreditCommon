package com.vcredit.common.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.vcredit.common.base.BaseApp;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by shibenli on 2016/3/9.
 */
public class MultipartRequest extends Request<JSONObject> {
    private MultipartEntity entity = new MultipartEntity();

    private final Response.Listener<JSONObject> mListener;
    private final Map<String, Object> files;
    private final Map<String, String> params;


    public MultipartRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, Object> files, Map<String, String> params) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        this.files = files;
        this.params = params;
        setRetryPolicy(new DefaultRetryPolicy(30 *1000, 0, 1.0f));
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        try {
            for (String fileName: files.keySet()) {
                Object obj = files.get(fileName);
                if (obj instanceof  File) {
                    entity.addPart(fileName, new FileBody((File)obj, fileName));
                }else if (obj instanceof  byte[]){
                    entity.addPart(fileName, new ByteArrayBody((byte[]) obj, fileName));
                }
            }

            for (String key : params.keySet()) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    /** 重写参数编码方法 */
    @Override
    protected String getParamsEncoding() {
        return BaseApp.getInstance().getEncoding();
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, BaseApp.getInstance().getEncoding());
            return Response.success(new JSONObject(parsed), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}