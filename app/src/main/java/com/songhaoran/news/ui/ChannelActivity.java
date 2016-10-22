package com.songhaoran.news.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.songhaoran.news.R;
import com.songhaoran.news.ui.adapter.ChannelListAdapter;
import com.songhaoran.news.utils.NewsDb;

/**
 * Created by Administrator on 2016/10/11 0011.
 */

public class ChannelActivity extends AppCompatActivity {

    private NewsDb newsDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        init();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置左上角有一个返回箭头图标
            getSupportActionBar().setTitle("订阅频道");
        }

        newsDb = new NewsDb(this);
        ChannelListAdapter channelListAdapter = new ChannelListAdapter(newsDb.getAllChannelList());
        channelListAdapter.setOnCheckedChangeListener(new ChannelListAdapter.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(String channelId, boolean isChecked) {
                newsDb.setChannelSubscribed(channelId,isChecked);
                setResult(MainActivity.CODE_DATA_CHANGED);
                Log.i("返回结果","啦啦啦啦");
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.channel_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(channelListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
}
