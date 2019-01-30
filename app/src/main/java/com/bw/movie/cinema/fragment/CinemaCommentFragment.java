package com.bw.movie.cinema.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.cinema.adapter.CinemaCommentAdapter;
import com.bw.movie.cinema.bean.CinemaCommentBean;
import com.bw.movie.cinema.bean.CinemaInfoBean;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CinemaCommentFragment extends BaseFragment {
    @BindView(R.id.comment_xrecycler)
    XRecyclerView commentXrecycler;
    Unbinder unbinder;
    private int page;
    private CinemaCommentAdapter cinemaCommentAdapter;
    private int id;

    @Override
    protected int getLayoutResId() {
        return R.layout.cinema_comment_fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getCinemaId(MessageBean messageBean) {
        if (messageBean.getId().equals("detail_cinemaId")) {
            id = (int) messageBean.getObject();
            page=1;
            onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET, id, page), CinemaCommentBean.class);
        }
    }

    @Override
    protected void initData() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        commentXrecycler.setLayoutManager(linearLayoutManager);
        cinemaCommentAdapter = new CinemaCommentAdapter(getContext());
        commentXrecycler.setAdapter(cinemaCommentAdapter);
        commentXrecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET,id, page), CinemaCommentBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET, id,page), CinemaCommentBean.class);
            }
        });

    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof CinemaCommentBean){
            CinemaCommentBean cinemaCommentBean= (CinemaCommentBean) data;
            if(cinemaCommentBean.getResult().size()>0){
                if(page==1){
                    cinemaCommentAdapter.setList(cinemaCommentBean.getResult());
                }else{
                    cinemaCommentAdapter.addList(cinemaCommentBean.getResult());
                }
                commentXrecycler.refreshComplete();
                commentXrecycler.loadMoreComplete();
                page++;
            }else{
                ToastUtil.showToast(cinemaCommentBean.getMessage());
            }
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
