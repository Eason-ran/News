package com.songhaoran.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.songhaoran.news.R;
import com.songhaoran.news.beans.Channel;
import com.songhaoran.news.service.DownloadService;
import com.songhaoran.news.ui.adapter.NewsPagerAdapter;
import com.songhaoran.news.utils.NewsDb;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int CODE_DATA_CHANGED = 1000;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * 绑定tabLayout和viewPager
         */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 赋值数据
     */
    private void initData(){
        final List<String> titles = new ArrayList<>();
        final List<NewsListFragment> fragments = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                NewsDb newsDb = new NewsDb(MainActivity.this);
                List<Channel> channels = newsDb.getSubscribedChannelList();

                for (Channel channel : channels){
                    Bundle args = new Bundle();
                    args.putString("channelId",channel.getChannelId());
                    NewsListFragment fragment = new NewsListFragment();
                    fragment.setArguments(args);
                    fragments.add(fragment);
                    titles.add(channel.getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter
                                (getSupportFragmentManager(),fragments,titles);
                        viewPager.setAdapter(newsPagerAdapter);
                    }
                });
            }
        }.start();
    }

    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("title",query);
                startActivity(intent);
                searchView.onActionViewCollapsed();//关闭searchView
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                break;
            case R.id.download:
                Intent intent = new Intent(this, DownloadService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                break;
            case R.id.manage:
                Intent intent1 = new Intent(MainActivity.this,ChannelActivity.class);
                startActivityForResult(intent1,CODE_DATA_CHANGED);//当订阅的channel改变时，返回结构
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 订阅的频道发生改变时，ChannelActivity会返回一个resultCode回来，
     * 这里是收到resultCode时重新刷新主页数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CODE_DATA_CHANGED){
            initData();
        }
    }
}
