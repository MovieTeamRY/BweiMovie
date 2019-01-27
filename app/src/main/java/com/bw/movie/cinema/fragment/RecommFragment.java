package com.bw.movie.cinema.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.cinema.adapter.NearAdapter;
import com.bw.movie.cinema.adapter.RecommAdapter;
import com.bw.movie.cinema.bean.NearCinemaBean;
import com.bw.movie.cinema.bean.RecommCinemaBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecommFragment extends BaseFragment {
    @BindView(R.id.recomm_cinema_recycler)
    RecyclerView recommCinemaRecycler;
    Unbinder unbinder;
    private RecommAdapter recommAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.recomm_cinema_fragment;
    }

    @Override
    protected View getLayoutView() {
        return null;
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommCinemaRecycler.setLayoutManager(linearLayoutManager);
        recommAdapter = new RecommAdapter(getContext());
        recommCinemaRecycler.setAdapter(recommAdapter);
        onGetRequest(String.format(Apis.URL_FIND_RECOMMEND_CINEMAS_GET, 1), RecommCinemaBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof RecommCinemaBean){
            RecommCinemaBean recommCinemaBean= (RecommCinemaBean) data;
            if(recommCinemaBean.getResult().size()>0){
                recommAdapter.setList(recommCinemaBean.getResult());
            }
        }
    }

    @Override
    protected void onNetFail(String error) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
