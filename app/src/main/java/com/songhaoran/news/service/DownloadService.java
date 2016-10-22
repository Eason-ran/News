package com.songhaoran.news.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.songhaoran.news.beans.Article;
import com.songhaoran.news.beans.Channel;
import com.songhaoran.news.beans.ImageUrls;
import com.songhaoran.news.beans.NewsRequestParams;
import com.songhaoran.news.utils.NewsDb;
import com.songhaoran.news.utils.NewsLoader;
import com.songhaoran.news.utils.ResourceStorage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class DownloadService extends Service {

    private NewsDb newsDb;
    private ExecutorService threadPool;
    private NewsLoader newsLoader;
    private RequestQueue requestQueue;
    private ResourceStorage resourceStorage;
    private int imgNumber;

    @Override
    public void onCreate() {
        super.onCreate();
        newsDb = new NewsDb(this);
        threadPool = Executors.newFixedThreadPool(5);
        newsLoader = new NewsLoader(this);
        requestQueue = Volley.newRequestQueue(this);
        resourceStorage = new ResourceStorage(this);
        imgNumber = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "开始下载!", Toast.LENGTH_LONG).show();
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获得订阅频道的Id
     */
    private List<String> getSubscribedChannelId() {
        List<String> channelIdList = new ArrayList<>();
        List<Channel> channels = newsDb.getSubscribedChannelList();
        for (Channel channel : channels) {
            channelIdList.add(channel.getChannelId());
        }
        return channelIdList;
    }

    /**
     * 开始下载
     */
    private void startDownload() {
        for (String channelId : getSubscribedChannelId()) {
            newsLoader.setOnLoadNewsDataListener(new NewsLoader.OnLoadNewsDataListener() {
                @Override
                public void onLoadNewsDataSuccess(final String channelId, final List<Article> articles) {
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            saveData(channelId, articles);
                        }
                    });
                    for (Article article : articles) {
                        imgNumber = imgNumber + article.getImgList().size();
                    }
                    for (Article article : articles) {
                        downloadImg(article.getImgList());
                    }
                }

                @Override
                public void onLoadNewsDataError(String error) {

                }
            });
            newsLoader.loadNewsDataOnline(new NewsRequestParams().setChannelId(channelId));
        }
    }

    private void saveData(String channelId, List<Article> articles) {
        Type srcType = new TypeToken<List<Article>>() {
        }.getType();
        String data = new Gson().toJson(articles, srcType);
        newsDb.setNewsData(channelId, data);
    }

    private void downloadImg(List<ImageUrls> imageUrls) {
        for (final ImageUrls img : imageUrls) {
            ImageRequest imageRequest = new ImageRequest(img.getUrl(),
                    new Response.Listener<Bitmap>() {

                        @Override
                        public void onResponse(final Bitmap response) {
                            threadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    resourceStorage.saveImg(resourceStorage.getFileName(img.getUrl()),
                                            response);
                                }
                            });
                            if (--imgNumber == 0) {
                                Toast.makeText(DownloadService.this, "下载完成!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(imageRequest);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
