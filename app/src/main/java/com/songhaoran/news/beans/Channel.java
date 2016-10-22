package com.songhaoran.news.beans;

/**
 * channel类
 */
public class Channel{

    private String channelId;
    private String name;
    private int subscribed;//表示已订阅

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(int subscribed) {
        this.subscribed = subscribed;
    }
}
