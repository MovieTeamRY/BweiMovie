package com.bw.movie.cinema.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.cinema.adapter.CinemaCommentAdapter;
import com.bw.movie.cinema.bean.CinemaCommentBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CinemaCommentFragment extends BaseFragment {
    @BindView(R.id.comment_xrecycler)
    XRecyclerView commentXrecycler;
    Unbinder unbinder;
    private int page;
    private CinemaCommentAdapter cinemaCommentAdapter;
    private int cinemaId;

    @Override
    protected int getLayoutResId() {
        return R.layout.cinema_comment_fragment;
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }
    @Override
    protected void initData() {
        Intent intent=getActivity().getIntent();
        cinemaId = intent.getIntExtra("id", 0);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        commentXrecycler.setLayoutManager(linearLayoutManager);
        cinemaCommentAdapter = new CinemaCommentAdapter(getContext());
        commentXrecycler.setAdapter(cinemaCommentAdapter);
        commentXrecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET,cinemaId, page), CinemaCommentBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET, cinemaId,page), CinemaCommentBean.class);
            }
        });
        Log.i("TAG",cinemaId+"===");
        page=1;
        onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET, cinemaId, page), CinemaCommentBean.class);
    }

    public void setCinemaId(int cinemaId){
        this.cinemaId=cinemaId;
        Log.i("TAG",cinemaId+"===");
        page=1;
        onGetRequest(String.format(Apis.URL_FIND_ALL_CINEAM_COMMENT_GET, cinemaId, page), CinemaCommentBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof CinemaCommentBean){
            CinemaCommentBean cinemaCommentBean= (CinemaCommentBean) data;
            if(cinemaCommentBean.getMessage().equals(getString(R.string.none_data))){
                ToastUtil.showToast(getString(R.string.not_has_more));
            }else if(cinemaCommentBean.getResult().size()>0){
                if(page==1){
                    cinemaCommentAdapter.setList(cinemaCommentBean.getResult());
                }else{
                    cinemaCommentAdapter.addList(cinemaCommentBean.getResult());
                }
            }else{
                ToastUtil.showToast(cinemaCommentBean.getMessage());
            }
            page++;
            commentXrecycler.refreshComplete();
            commentXrecycler.loadMoreComplete();
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
