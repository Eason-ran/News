package com.songhaoran.news.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.songhaoran.news.beans.Article;
import com.songhaoran.news.beans.NewsListData;
import com.songhaoran.news.beans.NewsRequestParams;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class NewsLoader {

    private Context context;
    private NewsApi newsApi;
    private NewsDb newsDb;

    private OnLoadNewsDataListener onLoadNewsDataListener;

    public NewsLoader(Context context) {
        this.context = context;
        this.newsApi = new NewsApi(context);
        this.newsDb = new NewsDb(context);
    }

    /**
     * 当activity使用newsLoader加载数据的时候，activity并不关心数据是从网络加载还是从本地加载，
     * 所以我们只对activity暴露出一个loadNewsData方法，然后在newsLoader中对网络进行判断，如果联网调用
     * newsDataOnLine，如果不联网调用newsDataOffLine
     */
    public void loadNewsData(NewsRequestParams params) {
        if (NetworkState.isNetworkConnected(context)) {
            loadNewsDataOnline(params);
        } else {
            loadNewsDataOffline(params);
            Log.i("使用离线方法","iiiiii");
        }
    }

    /**
     * 在线加载数据
     */
    public void loadNewsDataOnline(final NewsRequestParams params) {
        newsApi.getNewsList(params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (onLoadNewsDataListener != null) {
                    NewsListData newsListData = new Gson().fromJson(response, NewsListData.class);
                    if (newsListData.getCode() == 0) {
                        onLoadNewsDataListener.onLoadNewsDataSuccess(params.getChannelId(), newsListData.getNewsListBody().getPage().getArticles());
                    } else {
                        onLoadNewsDataListener.onLoadNewsDataError(newsListData.getError());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (onLoadNewsDataListener != null) {
                    onLoadNewsDataListener.onLoadNewsDataError(error.getMessage());
                }
            }
        });
    }

    /**
     * 离线加载数据
     */
    private void loadNewsDataOffline(NewsRequestParams params) {
        String channelId = params.getChannelId();
        Type dataType = new TypeToken<List<Article>>() {
        }.getType();
        List<Article> articles = new Gson().fromJson(newsDb.getNewsData(channelId), dataType);
        if (onLoadNewsDataListener != null) {
            onLoadNewsDataListener.onLoadNewsDataSuccess(channelId, articles);
        }
    }

    public void setOnLoadNewsDataListener(OnLoadNewsDataListener onLoadNewsDataListener) {
        this.onLoadNewsDataListener = onLoadNewsDataListener;
    }

    /**
     * 因为是异步，所以我们要一个接口，将数据返回给activity
     */
    public interface OnLoadNewsDataListener {
        void onLoadNewsDataSuccess(String channelId, List<Article> articles);

        void onLoadNewsDataError(String error);
    }

}
