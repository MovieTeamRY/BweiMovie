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
import com.bw.movie.cinema.adapter.RecommAdapter;
import com.bw.movie.cinema.bean.NearCinemaBean;
import com.bw.movie.cinema.bean.RecommCinemaBean;
import com.bw.movie.film.bean.CancalFollowMovieBean;
import com.bw.movie.film.bean.FollowMovieBean;
import com.bw.movie.login.LoginActivity;
import com.bw.movie.utils.IntentUtils;
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
 * 推荐影院页面
 * @author YU
 * @date 2019.01.27
 */
public class RecommFragment extends BaseFragment {
    @BindView(R.id.recomm_cinema_recycler)
    RecyclerView recommCinemaRecycler;
    Unbinder unbinder;
    private RecommAdapter recommAdapter;
    private List<RecommCinemaBean.Result> resultList;
    private int index;

    @Override
    protected int getLayoutResId() {
        return R.layout.recomm_cinema_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //请求数据
        onGetRequest(String.format(Apis.URL_FIND_RECOMMEND_CINEMAS_GET, 1), RecommCinemaBean.class);
    }

    @Override
    protected void initData() {
        resultList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommCinemaRecycler.setLayoutManager(linearLayoutManager);
        recommAdapter = new RecommAdapter(getContext());
        recommCinemaRecycler.setAdapter(recommAdapter);
        recommAdapter.setPriseClick(new RecommAdapter.PriseClick() {
            @Override
            public void onClick(int position, List<RecommCinemaBean.Result> list, int followCinema) {
                resultList.addAll(list);
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
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getMessage(MessageBean messageBean){
        if(messageBean.getId().equals("near")){
            onGetRequest(String.format(Apis.URL_FIND_RECOMMEND_CINEMAS_GET, 1), RecommCinemaBean.class);
        }
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof RecommCinemaBean){
            RecommCinemaBean recommCinemaBean= (RecommCinemaBean) data;
            if(recommCinemaBean.getResult().size()>0){
                //给适配器设置数据
                recommAdapter.setList(recommCinemaBean.getResult());
            }
        }else if(data instanceof FollowMovieBean){
            FollowMovieBean followMovieBean= (FollowMovieBean) data;
            if(followMovieBean.getMessage().equals("请先登陆")){
                IntentUtils.getInstence().intent(getContext(),LoginActivity.class);
            }
            ToastUtil.showToast(followMovieBean.getMessage());
            resultList.get(index).setFollowCinema(1);
            recommAdapter.setList(resultList);
            EventBus.getDefault().postSticky(new MessageBean("recomm",null));
        }else if(data instanceof CancalFollowMovieBean){
            CancalFollowMovieBean cancalFollowMovieBean= (CancalFollowMovieBean) data;
            ToastUtil.showToast(cancalFollowMovieBean.getMessage());
            if(cancalFollowMovieBean.getMessage().equals("请先登陆")){
                IntentUtils.getInstence().intent(getContext(),LoginActivity.class);
            }
            resultList.get(index).setFollowCinema(2);
            recommAdapter.setList(resultList);
            EventBus.getDefault().postSticky(new MessageBean("recomm",null));
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
