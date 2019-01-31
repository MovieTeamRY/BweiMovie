package com.bw.movie.cinema.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.cinema.bean.CinemaInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CinemaDetailFragment extends BaseFragment {
    @BindView(R.id.text_loc)
    TextView textLoc;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_route)
    TextView textRoute;
    @BindView(R.id.subway_way)
    TextView subwayWay;
    Unbinder unbinder;
    private int cinemaId;

    @Override
    protected int getLayoutResId() {
        return R.layout.cinema_detail_fragment;
    }

    @Override
    protected void initData() {
        Intent intent=getActivity().getIntent();
        cinemaId = intent.getIntExtra("id", 0);
        onGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET, cinemaId), CinemaInfoBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    public void setCinemaId(int cinemaId){
        this.cinemaId=cinemaId;
        Log.i("TAG",cinemaId+"====");
        onGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET, cinemaId), CinemaInfoBean.class);
    }


    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof CinemaInfoBean) {
            CinemaInfoBean cinemaInfoBean= (CinemaInfoBean) data;
            CinemaInfoBean.ResultBean result = cinemaInfoBean.getResult();
            textLoc.setText(result.getAddress());
            textPhone.setText(result.getPhone());
            subwayWay.setText(result.getVehicleRoute());
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG", error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
