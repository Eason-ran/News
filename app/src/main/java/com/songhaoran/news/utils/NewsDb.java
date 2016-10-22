package com.songhaoran.news.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.songhaoran.news.beans.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class NewsDb {

    private SQLiteDatabase db;

    public NewsDb(Context context) {
        this.db = new DbOpenHelper(context).getWritableDatabase();
    }

    //获取某个搜索条件下的频道
    private List<Channel> getChannelList(String selection) {
        List<Channel> channels = new ArrayList<>();
        Cursor cursor = db.query("channel", new String[]{"id", "name", "subscribed"},
                selection, null, null, null, null);
        while (cursor.moveToNext()) {
            Channel channel = new Channel();
            channel.setChannelId(cursor.getString(cursor.getColumnIndex("id")));
            channel.setName(cursor.getString(cursor.getColumnIndex("name")));
            channel.setSubscribed(cursor.getInt(cursor.getColumnIndex("subscribed")));
            channels.add(channel);
        }
        cursor.close();
        return channels;
    }

    //获取数据库中被订阅的频道列表
    public List<Channel> getSubscribedChannelList() {
        return getChannelList("subscribed=1");
    }

    //获取所有频道
    public List<Channel> getAllChannelList() {
        return getChannelList(null);
    }

    //自定义被订阅的频道
    public void setChannelSubscribed(String channelId, boolean subscribed) {
        ContentValues cv = new ContentValues();
        if (subscribed) {
            cv.put("subscribed", 1);
        } else {
            cv.put("subscribed", 0);
        }
        db.update("channel", cv, "id='" + channelId + "'", null);
    }

    /**
     * 保存离线数据
     */
    public void setNewsData(String channelId, String data) {
        ContentValues cv = new ContentValues();
        cv.put("data", data);
        db.update("newsdata", cv, "channelId='" + channelId + "'", null);
    }

    /**
     * 获取离线数据
     */
    public String getNewsData(String channelId) {
        Cursor cursor = db.query("newsdata", new String[]{"data"}, "channelId='" + channelId + "", null, null, null, null);
        cursor.moveToFirst();
        String data = cursor.getString(cursor.getColumnIndex("data"));
        cursor.close();
        return data;
    }
}
