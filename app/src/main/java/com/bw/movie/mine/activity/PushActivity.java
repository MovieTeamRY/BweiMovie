package com.bw.movie.mine.activity;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.adapter.PushAdapter;
import com.bw.movie.mine.bean.MassageBean;
import com.bw.movie.mine.bean.MessageNumBean;
import com.bw.movie.mine.bean.UpdateMessageBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PushActivity extends BaseActivty {

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.push_recyclerview)
    XRecyclerView pushRecyclerview;
    private int page;
    private final int COUNT=10;
    private PushAdapter pushAdapter;
    private int count;
    private int position;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_push;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        //.查询用户当前未读消息数量
        onGetRequest(Apis.URL_FIND_UNREAD_MESSAGE_COUNT_GET,MessageNumBean.class);
        page=1;
        //创建布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PushActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pushRecyclerview.setLayoutManager(linearLayoutManager);
        //TODO 创建适配器
        pushAdapter = new PushAdapter(this);
        pushRecyclerview.setAdapter(pushAdapter);
        //点击事件
        pushAdapter.setOnClickListener(new PushAdapter.Click() {
            @Override
            public void onClick(int i, int id) {
                position=i;
                onGetRequest(String.format(Apis.URL_CHABGE_SYS_MSG_STATUS_GET,id),UpdateMessageBean.class);
            }
        });
        pushRecyclerview.setPullRefreshEnabled(true);
        pushRecyclerview.setLoadingMoreEnabled(true);
        pushRecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                onGetRequest(String.format(Apis.URL_FIND_ALL_SYS_MSG_LIST,page,COUNT),MassageBean.class);
            }

            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_ALL_SYS_MSG_LIST,page,COUNT),MassageBean.class);
            }
        });
        onGetRequest(String.format(Apis.URL_FIND_ALL_SYS_MSG_LIST,page,COUNT),MassageBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof MassageBean){
            MassageBean massageBean  = (MassageBean) data;
            if (massageBean.getResult().size()<10){
                ToastUtil.showToast("没有更多数据");
            }
            if (massageBean.isSuccess()||massageBean!=null){
                if (page==1){
                // TODO 传值到适配器
                    pushAdapter.setList(massageBean.getResult());
                }else{
                    pushAdapter.addList(massageBean.getResult());
                }
                page++;
                pushRecyclerview.loadMoreComplete();
                pushRecyclerview.refreshComplete();
            }
        }else if (data instanceof MessageNumBean) {
            MessageNumBean messageNumBean = (MessageNumBean) data;
            if (messageNumBean!=null||messageNumBean.isSuccess()){
                count = messageNumBean.getCount();
                message.setText("系统消息（"+count+"条未读）");
            }
        }else if (data instanceof UpdateMessageBean) {
            UpdateMessageBean updateMessageBean = (UpdateMessageBean) data;
            if (updateMessageBean.isSuccess()||updateMessageBean!=null) {
                count--;
                message.setText("系统消息（"+count+"条未读）");
                pushAdapter.notif(position);
            }
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }

    @OnClick(R.id.return_image)
    public void onViewClicked() {
        finish();
    }
}
