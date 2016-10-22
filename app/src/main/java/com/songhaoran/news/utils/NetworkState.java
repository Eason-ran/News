package com.songhaoran.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public class NetworkState {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info:infos){
            if (info.isConnected()){
                return true;
            }
        }
        return false;
    }
}
