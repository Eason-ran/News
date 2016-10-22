package com.songhaoran.news.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.songhaoran.news.R;
import com.songhaoran.news.beans.Channel;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ItemViewHolder> {

    private List<Channel> channels;
    private OnCheckedChangeListener onCheckedChangeListener;

    public ChannelListAdapter(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.textView.setText(channels.get(position).getName());
        holder.checkBox.setChecked(channels.get(position).getSubscribed()==1);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckedChangeListener!=null){
                    onCheckedChangeListener.onCheckedChanged(channels.get(holder.getAdapterPosition()).getChannelId(),isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (channels!=null){
            return channels.size();
        }
        return 0;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener{
        void onCheckedChanged(String channelId,boolean isChecked);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private CheckBox checkBox;

        public ItemViewHolder(View itemView){
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }


}
