package com.songhaoran.news.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.songhaoran.news.beans.NewsRequestParams;

import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class NewsApi {

    private static final String TAG = NewsApi.class.getSimpleName();

    private static final String URL_CHANNEL = "http://apis.baidu.com/showapi_open_bus/channel_news/channel_news";
    private static final String URL_NEWS = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";

    private Context context;
    private RequestQueue requestQueue;

    public NewsApi(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void getNewsList(NewsRequestParams params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String urlStringFormat = URL_NEWS + "?channelId=%s&channelName=%s&title=%s&page=%s&needContent=%s&needHtml=%s";
        String url = String.format(urlStringFormat, params.getChannelId(), params.getChannelName(),
                params.getTitle(), params.getPage(), params.getNeedContent(), params.getNeedHtml());
        NewsRequest request = new NewsRequest(context, url, responseListener, errorListener);
        requestQueue.add(request);
    }

    public void getChannelList(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        NewsRequest request = new NewsRequest(context, URL_CHANNEL, responseListener, errorListener);
        requestQueue.add(request);
    }

    public String getChannelListSync() {
        RequestFuture<String> requestFuture = RequestFuture.newFuture();
        NewsRequest request = new NewsRequest(context, URL_CHANNEL, requestFuture, requestFuture);
        requestQueue.add(request);
        try {
            return requestFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}