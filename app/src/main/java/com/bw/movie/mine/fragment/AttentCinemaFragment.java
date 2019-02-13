package com.bw.movie.mine.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.adapter.AttentCinemaAdapter;
import com.bw.movie.mine.bean.AttentCinemaBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AttentCinemaFragment extends BaseFragment {
    @BindView(R.id.cinema_xrecyclerview)
    XRecyclerView cinemaXrecyclerview;
    Unbinder unbinder;
    private int mpage;
    private AttentCinemaAdapter attentCinemaAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.attent_cinema_fragment;
    }

    @Override
    protected void initData() {
        cinemaXrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        attentCinemaAdapter = new AttentCinemaAdapter(getContext());
        cinemaXrecyclerview.setAdapter(attentCinemaAdapter);
        cinemaXrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage=1;
                onGetRequest(String.format(Apis.URL_FIND_CINEAM_PAGE_LIST_GET,mpage),AttentCinemaBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_CINEAM_PAGE_LIST_GET,mpage),AttentCinemaBean.class);
            }
        });
        mpage=1;
        onGetRequest(String.format(Apis.URL_FIND_CINEAM_PAGE_LIST_GET,mpage),AttentCinemaBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof AttentCinemaBean){
            AttentCinemaBean cinemaBean= (AttentCinemaBean) data;
            if(!cinemaBean.getMessage().equals("请先登陆")){
                List<AttentCinemaBean.ResultBean> result = cinemaBean.getResult();
                if(result.size()>0){
                    if(result.size()<10){
                        ToastUtil.showToast("没有更多数据了");
                    }
                    if(mpage==1){
                        attentCinemaAdapter.setResultBeanList(result);
                    }else{
                        attentCinemaAdapter.addResultBeanList(result);
                    }
                    mpage++;
                }else{
                    ToastUtil.showToast("没有更多数据了");
                }
                cinemaXrecyclerview.refreshComplete();
                cinemaXrecyclerview.loadMoreComplete();
            }
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
        ToastUtil.showToast(error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
