package com.bw.movie.film.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.film.adapter.RelaeseAdapter;
import com.bw.movie.film.bean.RelaeseBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

public class FilmFragment extends BaseFragment {
    @BindView(R.id.recycler_flow)
    RecyclerCoverFlow recyclerFlow;
    Unbinder unbinder;
    private int current;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            /*recyclerFlow.scrollToPosition(current);
            current++;
            handler.sendEmptyMessageDelayed(0,2000);*/
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.file_fragment;
    }

    @Override
    protected View getLayoutView() {
        return null;
    }

    @Override
    protected void initData() {
        onGetRequest(String.format(Apis.URL_FIND_RELEASE_MOVIE_LIST_GET,1),RelaeseBean.class);
        recyclerFlow.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            //滑动监听
            @Override
            public void onItemSelected(int position) {

            }
        });
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof RelaeseBean){
            RelaeseBean relaeseBean= (RelaeseBean) data;
            if(relaeseBean.getMessage().equals("查询成功")){
                if(relaeseBean.getResult().size()>0){
                    recyclerFlow.setAdapter(new RelaeseAdapter(relaeseBean.getResult(),getContext()));
                    current=99990*100000;
                    handler.sendEmptyMessage(0);
                }
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
        handler.removeMessages(0);
    }
}
