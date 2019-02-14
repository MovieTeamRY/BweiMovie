package com.bw.movie.mine.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.adapter.AttentFilmAdapter;
import com.bw.movie.mine.bean.AttentFilmBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AttentFilmFragment extends BaseFragment {
    @BindView(R.id.film_xrecyclerview)
    XRecyclerView filmXrecyclerview;
    Unbinder unbinder;
    private int page;
    private AttentFilmAdapter attentFilmAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.attent_film_fragment;
    }

    @Override
    protected void initData() {
        filmXrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        attentFilmAdapter = new AttentFilmAdapter(getContext());
        filmXrecyclerview.setAdapter(attentFilmAdapter);
        filmXrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                onGetRequest(String.format(Apis.URL_FIND_MOVIE_PAGE_LIST_GET,page),AttentFilmBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_MOVIE_PAGE_LIST_GET,page),AttentFilmBean.class);
            }
        });
        page=1;
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_PAGE_LIST_GET,page),AttentFilmBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof AttentFilmBean){
            AttentFilmBean filmBean= (AttentFilmBean) data;
            if(!filmBean.getMessage().equals("请先登陆")){
                List<AttentFilmBean.ResultBean> result = filmBean.getResult();
                if(result.size()>0){
                    if(result.size()<10){
                        ToastUtil.showToast("没有更多数据了");
                    }
                    if(page==1){
                        attentFilmAdapter.setResultBeanList(result);
                    }else{
                        attentFilmAdapter.addResultBeanList(result);
                    }
                    page++;
                }else{
                    ToastUtil.showToast("没有更多数据了");
                }
                filmXrecyclerview.loadMoreComplete();
                filmXrecyclerview.refreshComplete();
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
