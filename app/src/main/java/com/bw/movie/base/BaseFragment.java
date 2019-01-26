package com.bw.movie.base;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.mvp.presenter.IpresenterImpl;
import com.bw.movie.mvp.view.Iview;
import com.bw.movie.view.CircularLoading;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @time 2019/01/23 14:04
 */
public abstract class BaseFragment extends Fragment implements Iview{

    private IpresenterImpl ipresenter;
    private Dialog loadDialog,failDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ipresenter=new IpresenterImpl(this);
        if (getLayoutResId()!=0) {
            return inflater.inflate(getLayoutResId(), container, false);
        }
        View view = getLayoutView();
        if (view!=null){
            return view;
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        initView(view);
        //添加数据
        initData();

    }

    /**
     * post请求数据
     * @param url  请求数据的路径
     * @param map  请求的参数
     * @param clas  转换数据的类
     */
    protected void onPostRequest(String url, Map<String,String> map, Class clas){
        if(map==null){
            map=new HashMap<>();
        }
        if(loadDialog==null){
            loadDialog = CircularLoading.showLoadDialog(getContext(),  true);
        }
        ipresenter.onPostStart(url,map,clas);
    }
    /**
     * get请求数据
     * @param url  请求数据的路径
     * @param clas 转换数据的类
     */
    protected void onGetRequest(String url,Class clas){
        if(loadDialog==null){
            loadDialog = CircularLoading.showLoadDialog(getContext(),  true);
        }
        ipresenter.onGetStart(url,clas);
    }

    @Override
    public void onSuccess(Object data) {
        CircularLoading.closeDialog(loadDialog);
        CircularLoading.closeDialog(failDialog);
        onNetSuccess(data);
    }

    @Override
    public void onFail(String error) {
        if(loadDialog!=null){
            CircularLoading.closeDialog(loadDialog);
        }
        if(failDialog!=null){
            CircularLoading.closeDialog(failDialog);
        }
        if(error.equals("当前网络不可用，请检查网络状态")){
            failDialog = CircularLoading.showFailDialog(getContext(), "糟糕，网络不给力呀！", true);
        }else{
            onNetFail(error);
        }
    }

    protected abstract int getLayoutResId();

    protected abstract View getLayoutView();

    protected abstract void initData();

    protected abstract void initView(View view);

    protected abstract void onNetSuccess(Object data);

    protected abstract void onNetFail(String error);

}
