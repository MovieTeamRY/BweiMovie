package com.bw.movie.mine.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.adapter.CompletedAdapter;
import com.bw.movie.mine.bean.ObligationBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CompletedFragment extends BaseFragment {

    @BindView(R.id.completed_recycler)
    XRecyclerView completedXrecycler;
    Unbinder unbinder;
    private int mpage;
    private int status;
    private CompletedAdapter completedAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.mine_record_completed_fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        completedAdapter = new CompletedAdapter(getContext());
        completedXrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        completedXrecycler.setAdapter(completedAdapter);
        completedXrecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage = 1;
                onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET, status, mpage), ObligationBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET, status, mpage), ObligationBean.class);
            }
        });
        mpage = 1;
        status=2;
        onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET, status, mpage), ObligationBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof ObligationBean){
            ObligationBean obligationBean= (ObligationBean) data;
            if(obligationBean.getMessage().equals("请求成功")){
                if(obligationBean.getResult().size()<10){
                    ToastUtil.showToast("没有更多数据了");
                }
                if(mpage==1){
                    completedAdapter.setList(obligationBean.getResult());
                }else{
                    completedAdapter.addList(obligationBean.getResult());
                }
                mpage++;
            }else{
                ToastUtil.showToast(obligationBean.getMessage());
            }
            completedXrecycler.loadMoreComplete();
            completedXrecycler.refreshComplete();
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
