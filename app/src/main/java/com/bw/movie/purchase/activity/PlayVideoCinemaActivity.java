package com.bw.movie.purchase.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.activity.SeatActivity;
import com.bw.movie.cinema.adapter.CinemaSchedulAdapter;
import com.bw.movie.cinema.bean.FilmSchedulBean;
import com.bw.movie.film.bean.FilmDetailsBean;
import com.bw.movie.purchase.bean.FilmSchedulingBean;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayVideoCinemaActivity extends BaseActivty {

    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.cinema_line)
    ImageView cinemaLine;
    @BindView(R.id.film_image)
    SimpleDraweeView filmImage;
    @BindView(R.id.film_name)
    TextView filmName;
    @BindView(R.id.film_class)
    TextView filmClass;
    @BindView(R.id.film_director)
    TextView filmDirector;
    @BindView(R.id.film_data)
    TextView filmData;
    @BindView(R.id.film_address)
    TextView filmAddress;
    @BindView(R.id.film_recyclerview)
    RecyclerView filmRecyclerview;
    @BindView(R.id.return_image)
    ImageView returnImage;
    private CinemaSchedulAdapter cinemaSchedulAdapter;
    private Bundle bundle;
    private ArrayList<String> list;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_play_video_cinema;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bundle = new Bundle();
        list = new ArrayList<>();
        ButterKnife.bind(this);
        //创建布局
        LinearLayoutManager schedulLayoutManager=new LinearLayoutManager(this);
        schedulLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        filmRecyclerview.setLayoutManager(schedulLayoutManager);
        //创建适配器
        cinemaSchedulAdapter = new CinemaSchedulAdapter(this);
        filmRecyclerview.setAdapter(cinemaSchedulAdapter);

        cinemaSchedulAdapter.setOnClickListener(new CinemaSchedulAdapter.Click() {
            @Override
            public void onClick(FilmSchedulBean.ResultBean resultBean) {
                bundle.putStringArrayList("list",list);
                bundle.putParcelable("resultBean",resultBean);
                IntentUtils.getInstence().intent(PlayVideoCinemaActivity.this,SeatActivity.class,bundle);
            }
        });
    }

    @Override
    protected void initData() {
        //接收传值
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int movieId = bundle.getInt("movieId");
        FilmSchedulingBean.ResultBean resultBean = bundle.getParcelable("resultBean");
        Log.i("TAG", resultBean.getAddress() + "+++++++++++++");
        Log.i("TAG", movieId + "---------------");
        //赋值影院信息
        cinemaName.setText(resultBean.getName());
        cinemaAddress.setText(resultBean.getAddress());
        list.add(resultBean.getName());
        list.add(resultBean.getAddress());
        //请求电影详情接口
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET, movieId), FilmDetailsBean.class);
        //根据电影ID和影院ID查询电影排期列表
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST,resultBean.getId(),movieId),FilmSchedulBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof FilmDetailsBean) {
            FilmDetailsBean detailsBean = (FilmDetailsBean) data;
            if (detailsBean.isSuccess() && detailsBean != null) {
                list.add(detailsBean.getResult().getName());
                //展示数据
                Uri uri = Uri.parse(detailsBean.getResult().getImageUrl());
                filmImage.setImageURI(uri);
                filmName.setText(detailsBean.getResult().getName());
                filmClass.setText("类型："+detailsBean.getResult().getMovieTypes());
                filmDirector.setText("导演："+detailsBean.getResult().getDirector());
                filmData.setText("时长："+detailsBean.getResult().getDuration());
                filmAddress.setText("产地："+detailsBean.getResult().getPlaceOrigin());

            }
            ToastUtil.showToast(detailsBean.getMessage());
        }else if(data instanceof FilmSchedulBean){
            FilmSchedulBean schedulBean= (FilmSchedulBean) data;
            if(schedulBean.isSuccess()&&schedulBean!=null){
                cinemaSchedulAdapter.setList(schedulBean.getResult());
            }
            ToastUtil.showToast(schedulBean.getMessage());
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }


    @OnClick({R.id.cinema_line, R.id.return_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cinema_line:
                break;
            case R.id.return_image:
                finish();
                break;
            default:break;
        }
    }
}
