package com.songhaoran.news.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public class NewsListBody {

    @SerializedName("pagebean")
    private Page page;
    private int ret_code;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}