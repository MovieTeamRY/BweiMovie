package com.bw.movie.cinema.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.cinema.adapter.NearAdapter;
import com.bw.movie.cinema.bean.NearCinemaBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NearFragment extends BaseFragment {
    @BindView(R.id.near_cinema_recycler)
    RecyclerView nearCinemaRecycler;
    Unbinder unbinder;
    private NearAdapter nearAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.near_cinema_fragment;
    }

    @Override
    protected View getLayoutView() {
        return null;
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        nearCinemaRecycler.setLayoutManager(linearLayoutManager);
        nearAdapter = new NearAdapter(getContext());
        nearCinemaRecycler.setAdapter(nearAdapter);
        onGetRequest(String.format(Apis.URL_FIND_NEAR_BY_CINEMAS_GET,1),NearCinemaBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof NearCinemaBean){
            NearCinemaBean nearCinemaBean= (NearCinemaBean) data;
            nearAdapter.setList(nearCinemaBean.getResult());
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
