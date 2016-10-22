package com.songhaoran.news.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.songhaoran.news.beans.Channel;
import com.songhaoran.news.beans.ChannelListData;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = DbOpenHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 1;
    private Context context;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE newsdata(channelId TEXT,data TEXT)");
        db.execSQL("CREATE TABLE channel(id TEXT,name TEXT,subscribed INT)");
        initTable(db);
    }

    private void initTable(SQLiteDatabase db) {
        NewsApi newsApi = new NewsApi(context);
        ChannelListData data = new Gson().fromJson(newsApi.getChannelListSync(), ChannelListData.class);
        if (data != null) {
            if (data.getCode() == 0) {
                List<Channel> channels = data.getBody().getChannelList();
                String insertChannel = "INSERT INTO channel(id,name,subscribed) VALUES('%s','%s','%s')";
                int i = 0;
                for (Channel channel : channels) {
                    if (i < 5) {
                        //设置前5个是已订阅的，subscribed为1
                        db.execSQL(String.format(insertChannel, channel.getChannelId(), channel.getName(), 1));
                    } else {
                        //为订阅的channel，subscribed为0
                        db.execSQL(String.format(insertChannel, channel.getChannelId(), channel.getName(), 0));
                    }
                    i++;
                }
            }else {
                Log.e(TAG,data.getError());
                Looper.prepare();
                Toast.makeText(context,data.getError(),Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
