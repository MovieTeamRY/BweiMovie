package com.bw.movie.mvp.model;

import com.bw.movie.mvp.mycallback.MyCallBack;

import java.util.Map;

public interface Imodel {
    void onPostRequest(String url, Map<String,String> map, Class clas, MyCallBack myCallBack);
    void onGetRequest(String url,Class clas,MyCallBack myCallBack);
}
