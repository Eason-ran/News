package com.songhaoran.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songhaoran.news.R;
import com.songhaoran.news.beans.Article;
import com.songhaoran.news.beans.NewsRequestParams;
import com.songhaoran.news.ui.adapter.NewsListAdapter;
import com.songhaoran.news.utils.NewsLoader;

import java.util.List;

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        NewsListAdapter.OnItemViewClickListener {

    private View view;
    private int page;
    private String channelId;
    private String title;
    private NewsLoader newsLoader;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private NewsListAdapter newsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        init();
        return view;
    }

    private void init() {
        //默认参数
        page = 1;
        channelId = getArguments().getString("channelId", "");
        title = getArguments().getString("title", "");
        newsLoader = new NewsLoader(getContext());

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (linearLayoutManager.getItemCount() == linearLayoutManager.findLastVisibleItemPosition() + 1) {
                        loadNextPage();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        newsListAdapter = new NewsListAdapter();
        newsListAdapter.setOnItemViewClickListener(this);
        recyclerView.setAdapter(newsListAdapter);

        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        newsLoader.setOnLoadNewsDataListener(new NewsLoader.OnLoadNewsDataListener() {
            @Override
            public void onLoadNewsDataSuccess(String channelId, List<Article> articles) {
                newsListAdapter.setArticles(articles);//传入articles
//                Log.i("传入了articles","它的长度"+articles.size());
                newsListAdapter.notifyDataSetChanged();//通知数据中心
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onLoadNewsDataError(String error) {

            }
        });
        newsLoader.loadNewsData(new NewsRequestParams().setChannelId(channelId).setTitle(title).setPage(1));
        Log.i("fragment导入了参数", channelId + "和" + title + page);
    }

    private void loadNextPage() {
        newsLoader.setOnLoadNewsDataListener(new NewsLoader.OnLoadNewsDataListener() {
            @Override
            public void onLoadNewsDataSuccess(String channelId, List<Article> articles) {
                newsListAdapter.addArticles(articles);//传入articles
                newsListAdapter.notifyDataSetChanged();//通知数据中心
                page++;
            }

            @Override
            public void onLoadNewsDataError(String error) {

            }
        });
        newsLoader.loadNewsData(new NewsRequestParams().setChannelId(channelId).setTitle(title).setPage(page + 1));
    }

    /**
     * ItemView点击进入新闻详情页
     */
    @Override
    public void onItemViewClick(int position, View itemView) {
        Intent intent = new Intent(getContext(), NewsActivity.class);
        intent.putExtra("title", newsListAdapter.getArticles().get(position).getTitle());
        intent.putExtra("html", newsListAdapter.getArticles().get(position).getHtml());
        startActivity(intent);
    }
}
