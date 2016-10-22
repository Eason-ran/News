package com.songhaoran.news.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class NewsListData {

    @SerializedName("showapi_res_code")
    private int code;

    @SerializedName("showapi_res_error")
    private String error;

    @SerializedName("showapi_res_body")
    private NewsListBody mNewsListBody;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public NewsListBody getNewsListBody() {
        return mNewsListBody;
    }

    public void setNewsListBody(NewsListBody newsListBody) {
        this.mNewsListBody = newsListBody;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
