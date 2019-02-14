package com.bw.movie.mine.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.adapter.ObligationAdapter;
import com.bw.movie.mine.bean.ObligationBean;
import com.bw.movie.utils.ToastUtil;
import com.bw.movie.utils.WeiXinUtil;
import com.bw.movie.wxapi.bean.WXPayBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ObligationFragment extends BaseFragment {
    @BindView(R.id.obligation_recycler)
    XRecyclerView obligationRecycler;
    Unbinder unbinder;
    private int mpage;
    private int status;
    private ObligationAdapter obligationAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.mine_record_obligation_fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        obligationAdapter = new ObligationAdapter(getContext());
        obligationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        obligationRecycler.setAdapter(obligationAdapter);
        obligationRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        status=1;
        onGetRequest(String.format(Apis.URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET, status, mpage), ObligationBean.class);
        obligationAdapter.setOnClick(new ObligationAdapter.PayClick() {
            @Override
            public void onClick(ObligationBean.ResultBean resultBean,int pay) {
                //TODO 去支付
                Map<String, String> map=new HashMap<>();
                map.put("payType",String.valueOf(pay));
                map.put("orderId",resultBean.getOrderId());
                onPostRequest(Apis.URL_PAY_POST,map,WXPayBean.class);
            }
        });
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
                    obligationAdapter.setList(obligationBean.getResult());
                }else{
                    obligationAdapter.addList(obligationBean.getResult());
                }
                mpage++;
            }else{
                ToastUtil.showToast(obligationBean.getMessage());
            }
            obligationRecycler.loadMoreComplete();
            obligationRecycler.refreshComplete();
        }else if (data instanceof WXPayBean){
            //微信支付
            WXPayBean wxPayBean = (WXPayBean) data;
            WeiXinUtil.weiXinPay(getActivity(), wxPayBean);
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
