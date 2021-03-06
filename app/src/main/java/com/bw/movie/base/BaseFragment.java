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
//import com.squareup.leakcanary.RefWatcher;

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
        }else{
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        initView(view);
        //添加数据
        /*if(getUserVisibleHint()){*/
            initData();
        /*}*/
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
            loadDialog = CircularLoading.getInstance().showLoadDialog(getContext(),  true);
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
            loadDialog = CircularLoading.getInstance().showLoadDialog(getContext(),  true);
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
        if(error.equals("当前网络不可用，请检查网络状态")){
            if(failDialog==null){
                failDialog = CircularLoading.getInstance().showFailDialog(getContext(), "糟糕，网络不给力呀！", true);
            }
        }else{
            if(failDialog!=null){
                CircularLoading.closeDialog(failDialog);
            }
            onNetFail(error);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ipresenter.onDetach();
        if(loadDialog!=null){
            CircularLoading.closeDialog(loadDialog);
        }
        if(failDialog!=null){
            CircularLoading.closeDialog(failDialog);
        }
        /*//检测内存险漏
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }

    protected abstract int getLayoutResId();

    protected abstract void initData();

    protected abstract void initView(View view);

    protected abstract void onNetSuccess(Object data);

    protected abstract void onNetFail(String error);

}
