package com.songhaoran.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.songhaoran.news.R;
import com.songhaoran.news.beans.Article;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12 0012.
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private List<Article> articles;
    private OnItemViewClickListener onItemViewClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            //类型为ITEM时，解析ItemViewHolder
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false));
        } else if (viewType == VIEW_TYPE_FOOTER) {
            //类型为FOOTER时，解析FooterViewHolder
            return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder && position < articles.size()) {
            final ItemViewHolder mHolder = (ItemViewHolder) holder;
            Article article = articles.get(position);
            mHolder.titleTv.setText(article.getTitle());
            mHolder.descTv.setText(article.getDesc());
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemViewClickListener != null) {
                        onItemViewClickListener.onItemViewClick(position, mHolder.itemView);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (articles != null) {
            return articles.size() + 1;  //还要加一个footer
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == articles.size()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView titleTv;
        private TextView descTv;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            descTv = (TextView) itemView.findViewById(R.id.descTv);
            if (titleTv != null && descTv != null) {
                titleTv.setText("标题");
                descTv.setText("描述");
            }
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {

        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public interface OnItemViewClickListener {
        void onItemViewClick(int position, View itemView);
    }
}
