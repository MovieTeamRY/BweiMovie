package com.bw.movie.mvp.model;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bw.movie.Apis;
import com.bw.movie.base.MyApplication;
import com.bw.movie.mvp.mycallback.MyCallBack;
import com.bw.movie.mvp.network.RetrofitManager;
import com.bw.movie.view.CircularLoading;
import com.google.gson.Gson;

import java.util.Map;

public class Imodelmpl implements Imodel{



    @Override
    public void onPostRequest(String url, Map<String, String> map, final Class clas, final MyCallBack myCallBack) {
        if(!isNetWork()){
            myCallBack.onFail(Apis.NOTNETWORK);

        }else{

            RetrofitManager.getInstance().post(url, map, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    Object o = new Gson().fromJson(data, clas);
                    myCallBack.onSuccess(o);

                }

                @Override
                public void onFail(String error) {
                    myCallBack.onFail(error);
                }
            });
        }
    }

    @Override
    public void onGetRequest(String url, final Class clas, final MyCallBack myCallBack) {
        if(!isNetWork()){
            myCallBack.onFail(Apis.NOTNETWORK);
        }else{
            RetrofitManager.getInstance().get(url, new RetrofitManager.HttpListener() {
                @Override
                public void onSuccess(String data) {
                    Object o = new Gson().fromJson(data, clas);
                    myCallBack.onSuccess(o);
                }

                @Override
                public void onFail(String error) {
                    myCallBack.onFail(error);
                }
            });
        }
    }

    public static boolean isNetWork(){
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isAvailable();
    }
}
