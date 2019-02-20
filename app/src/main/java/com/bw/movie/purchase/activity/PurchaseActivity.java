package com.bw.movie.purchase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.base.MyApplication;
import com.bw.movie.purchase.adapter.PurchseAdapter;
import com.bw.movie.purchase.bean.FilmSchedulingBean;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseActivity extends BaseActivty {

    @BindView(R.id.movieNmae)
    TextView movieNmae;
    @BindView(R.id.fill_recyclerview)
    RecyclerView fillRecyclerview;
    @BindView(R.id.return_image)
    ImageView return_image;
    private PurchseAdapter purchseAdapter;
    private int movieId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_purchase_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getApplication());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fillRecyclerview.setLayoutManager(linearLayoutManager);
        //创建适配器
        purchseAdapter = new PurchseAdapter(this);
        fillRecyclerview.setAdapter(purchseAdapter);
        //点击事件
        purchseAdapter.setOnClickListener(new PurchseAdapter.Click() {
            @Override
            public void onClick(FilmSchedulingBean.ResultBean resultBean) {
                Bundle bundle = new Bundle();
                bundle.putInt("movieId",movieId);
                bundle.putParcelable("resultBean",resultBean);
                IntentUtils.getInstence().intent(PurchaseActivity.this,PlayVideoCinemaActivity.class,bundle);
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        movieId = bundle.getInt("movieId");
        String name = bundle.getString("name");
        movieNmae.setText(name);
        //根据电影ID查询当前排片该电影的影院列表
        onGetRequest(String.format(Apis.URL_FIND_CINEMAS_LIST_BY_MOVIE_ID_GET, movieId), FilmSchedulingBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof FilmSchedulingBean) {
            FilmSchedulingBean filmSchedulingBean = (FilmSchedulingBean) data;
            if (filmSchedulingBean != null && filmSchedulingBean.isSuccess()) {
                //传值到根据电影ID查询当前排片该电影的影院列表适配器
                purchseAdapter.setList(filmSchedulingBean.getResult());
            }
            ToastUtil.showToast(filmSchedulingBean.getMessage());
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
