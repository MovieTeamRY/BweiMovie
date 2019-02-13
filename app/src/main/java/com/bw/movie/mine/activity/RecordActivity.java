package com.bw.movie.mine.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.adapter.ObligationAdapter;
import com.bw.movie.mine.bean.ObligationBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RecordActivity extends BaseActivty {

    @BindView(R.id.obligation_radio)
    RadioButton obligationRadio;
    @BindView(R.id.completed_radio)
    RadioButton completedRadio;
    @BindView(R.id.record_group)
    RadioGroup recordGroup;
    @BindView(R.id.record_recycler)
    XRecyclerView recordRecycler;
    @BindView(R.id.user_return)
    ImageView userReturn;
    private Unbinder bind;
    private int status;
    private int mpage;
    private ObligationAdapter obligationAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        status=1;
        loadData();
    }

    public void loadData(){
        obligationAdapter = new ObligationAdapter(status, this);
        recordRecycler.setLayoutManager(new LinearLayoutManager(this));
        recordRecycler.setAdapter(obligationAdapter);
        recordRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage=1;
                onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET,mpage),ObligationBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET,mpage),ObligationBean.class);
            }
        });
        mpage=1;
        onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET,mpage),ObligationBean.class);
        if(status==1){
            obligationAdapter.setOnClick(new ObligationAdapter.PayClick() {
                @Override
                public void onClick(ObligationBean.ResultBean resultBean) {
                    //TODO 去支付创建订单

                }
            });
        }
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
                    obligationAdapter.setList(obligationBean.getResult());
                }else{
                    obligationAdapter.addList(obligationBean.getResult());
                }
                mpage++;
            }else{
                ToastUtil.showToast(obligationBean.getMessage());
            }
            recordRecycler.loadMoreComplete();
            recordRecycler.refreshComplete();
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG", error);
    }

    @OnClick({R.id.obligation_radio, R.id.completed_radio, R.id.user_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.obligation_radio:
                status=1;
                loadData();

                break;
            case R.id.completed_radio:
                status=2;
                loadData();
                break;
            case R.id.user_return:
                finish();
                break;
            default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

}
