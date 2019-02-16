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
import com.bw.movie.film.bean.CancalFollowMovieBean;
import com.bw.movie.film.bean.FollowMovieBean;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 附近影院页面
 * @author YU
 * @date 2019.01.27
 */
public class NearFragment extends BaseFragment {
    @BindView(R.id.near_cinema_recycler)
    RecyclerView nearCinemaRecycler;
    Unbinder unbinder;
    private NearAdapter nearAdapter;
    private String longtitude="";
    private String latitude="";
    private List<NearCinemaBean.Result> results;
    private int index;
    @Override
    protected int getLayoutResId() {
        return R.layout.near_cinema_fragment;
    }

    @Override
    protected void initData() {
        results=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        nearCinemaRecycler.setLayoutManager(linearLayoutManager);
        nearAdapter = new NearAdapter(getContext());
        nearCinemaRecycler.setAdapter(nearAdapter);
        nearAdapter.setPriseClick(new NearAdapter.PriseClick() {
            @Override
            public void onClick(int position, List<NearCinemaBean.Result> list, int followCinema) {
                results.addAll(list);
                index=position;
                if(followCinema==1){
                    //已经关注过 点击 就取消关注
                    onGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_CINEAM_GET,list.get(position).getId()),CancalFollowMovieBean.class);
                }else{
                    //没有关注过  点击 就关注
                    onGetRequest(String.format(Apis.URL_FOLLOW_CINEAM_GET,list.get(position).getId()),FollowMovieBean.class);
                }
            }
        });
        //onGetRequest(String.format(Apis.URL_FIND_NEAR_BY_CINEMAS_GET,116.30551391385724,40.04571807462411,1),NearCinemaBean.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getLoacation(MessageBean messageBean){
        if(messageBean.getId().equals("location")){
            double[] location= (double[]) messageBean.getObject();
            //请求数据
            onGetRequest(String.format(Apis.URL_FIND_NEAR_BY_CINEMAS_GET,location[0],location[1],1),NearCinemaBean.class);
        }else if(messageBean.getId().equals("recomm")){
            onGetRequest(String.format(Apis.URL_FIND_NEAR_BY_CINEMAS_GET,116.30551391385724,40.04571807462411,1),NearCinemaBean.class);
        }
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof NearCinemaBean){
            //给适配器添加数据
            NearCinemaBean nearCinemaBean= (NearCinemaBean) data;
            if(nearCinemaBean.getResult().size()>0){
                nearAdapter.setList(nearCinemaBean.getResult());
            }
        }else if(data instanceof FollowMovieBean){
            FollowMovieBean followMovieBean= (FollowMovieBean) data;
            ToastUtil.showToast(followMovieBean.getMessage());
            results.get(index).setFollowCinema(1);
            nearAdapter.setList(results);
            EventBus.getDefault().postSticky(new MessageBean("near",null));
        }else if(data instanceof CancalFollowMovieBean){
            CancalFollowMovieBean cancalFollowMovieBean= (CancalFollowMovieBean) data;
            ToastUtil.showToast(cancalFollowMovieBean.getMessage());
            results.get(index).setFollowCinema(2);
            nearAdapter.setList(results);
            EventBus.getDefault().postSticky(new MessageBean("near",null));
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
        EventBus.getDefault().unregister(this);
    }
}
