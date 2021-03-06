package com.songhaoran.news.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.songhaoran.news.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class NewsRequest extends StringRequest {

    private Context context;

    public NewsRequest(Context context,String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> headers = new HashMap<>();
        headers.put("apikey",context.getResources().getString(R.string.api_key));
        return headers;
    }
}
