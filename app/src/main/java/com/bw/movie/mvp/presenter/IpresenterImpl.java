package com.bw.movie.mvp.presenter;

import com.bw.movie.mvp.model.Imodelmpl;
import com.bw.movie.mvp.mycallback.MyCallBack;
import com.bw.movie.mvp.view.Iview;

import java.util.Map;

public class IpresenterImpl implements Ipresenter{

    Iview iview;
    Imodelmpl imodelmpl;

    public IpresenterImpl(Iview iview) {
        this.iview = iview;
        imodelmpl=new Imodelmpl();
    }

    @Override
    public void onPostStart(String url, Map<String, String> map, Class clas) {
        imodelmpl.onPostRequest(url, map, clas, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                iview.onSuccess(data);
            }

            @Override
            public void onFail(String error) {
                iview.onFail(error);
            }
        });
    }

    @Override
    public void onGetStart(String url, Class clas) {
        imodelmpl.onGetRequest(url, clas, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                iview.onSuccess(data);
            }

            @Override
            public void onFail(String error) {
                iview.onFail(error);
            }
        });
    }

    public void onDetach(){
        if(imodelmpl!=null){
            imodelmpl=null;
        }
        if(iview!=null){
            iview=null;
        }
    }
}
