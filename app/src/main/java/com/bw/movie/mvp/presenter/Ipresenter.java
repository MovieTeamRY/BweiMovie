package com.bw.movie.mvp.presenter;

import com.bw.movie.mvp.mycallback.MyCallBack;

import java.util.Map;

public interface Ipresenter {
    void onPostStart(String url, Map<String,String> map,Class clas);
    void onGetStart(String url,Class clas);
    void onpostFileRequest(String url, Map<String,String> map, Class clas);
}
