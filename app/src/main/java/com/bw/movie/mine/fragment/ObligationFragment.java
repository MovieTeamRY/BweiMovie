package com.bw.movie.mine.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.ali.AliPayBean;
import com.bw.movie.ali.Result;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.activity.RecordActivity;
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

                if (pay ==1){
                    //TODO 去微信支付
                    Map<String, String> map=new HashMap<>();
                    map.put("payType",String.valueOf(pay));
                    map.put("orderId",resultBean.getOrderId());
                    onPostRequest(Apis.URL_PAY_POST,map,WXPayBean.class);
                } else if (pay ==2){
                    //支付宝支付
                    //ToastUtil.showToast(getString(R.string.zhi_pay_non_support));
                    Map<String, String> map=new HashMap<>();
                    map.put("payType",String.valueOf(pay));
                    map.put("orderId",resultBean.getOrderId());
                    onPostRequest(Apis.URL_PAY_POST,map,AliPayBean.class);
                }
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

                if(mpage==1){
                    obligationAdapter.setList(obligationBean.getResult());
                }else{
                    obligationAdapter.addList(obligationBean.getResult());
                }
                mpage++;
            }/*else{
                ToastUtil.showToast("没有更多数据了");
            }*/
            obligationRecycler.loadMoreComplete();
            obligationRecycler.refreshComplete();
        }else if (data instanceof WXPayBean){
            //微信支付
            WXPayBean wxPayBean = (WXPayBean) data;
            WeiXinUtil.weiXinPay(getActivity(), wxPayBean);
            if(obligationAdapter.popupWindow.isShowing()){
                obligationAdapter.popupWindow.dismiss();
            }
            getActivity().finish();
        }else if (data instanceof AliPayBean) {
            //支付宝支付
            AliPayBean aliPayBean = (AliPayBean) data;
            if (aliPayBean != null || aliPayBean.isSuccess()) {
                final String orderInfo = aliPayBean.getResult();
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(getActivity());
                        Map<String, String> result = alipay.payV2(orderInfo, true);

                        Message msg = new Message();
                        // msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();

                if(obligationAdapter.popupWindow.isShowing()){
                    obligationAdapter.popupWindow.dismiss();
                }
            }
        }
    }
    //接受支付宝返回的支付结果
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Result result = new Result((Map<String, String>) msg.obj);
            ToastUtil.showToast(result.getMemo());
            String resultStatus = result.getResultStatus();
            Log.i("TAG",resultStatus+"kahkdljaldjlajdlakjdkladj;laj;klj");
            //根据支付结果码跳转到相应的页面
            if (resultStatus.equals("9000")){
                ((RecordActivity)getActivity()).oBligation();
            }
        }
    };

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
