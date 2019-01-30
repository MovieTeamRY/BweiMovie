package com.bw.movie.cinema.fragment;

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
import com.bw.movie.utils.MessageBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.cinema_detail_fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getCinemaId(MessageBean messageBean) {
        if (messageBean.getId().equals("detail_cinemaId")) {
            int id = (int) messageBean.getObject();
            onGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET, id), CinemaInfoBean.class);
        }
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof CinemaInfoBean) {
            CinemaInfoBean cinemaInfoBean= (CinemaInfoBean) data;
            CinemaInfoBean.ResultBean result = cinemaInfoBean.getResult();
            textLoc.setText(result.getAddress());
            textPhone.setText(result.getPhone());
            textRoute.setText(result.getVehicleRoute());
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG", error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
