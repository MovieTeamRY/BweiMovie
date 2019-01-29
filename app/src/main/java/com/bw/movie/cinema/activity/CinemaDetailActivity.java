package com.bw.movie.cinema.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.adapter.CinemaFilmAdapter;
import com.bw.movie.cinema.adapter.CinemaSchedulAdapter;
import com.bw.movie.cinema.bean.CinemaFilmBean;
import com.bw.movie.cinema.bean.CinemaInfoBean;
import com.bw.movie.cinema.bean.FilmSchedulBean;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

public class CinemaDetailActivity extends BaseActivty {

    @BindView(R.id.cinema_logo)
    SimpleDraweeView cinemaLogo;
    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.cinema_navigation)
    ImageView cinemaNavigation;
    @BindView(R.id.cinema_film)
    RecyclerCoverFlow cinemaFilm;
    @BindView(R.id.cinema_film_scheduling)
    RecyclerView cinemaFilmScheduling;
    @BindView(R.id.cinema_group)
    RadioGroup cinemaGroup;
    private Unbinder bind;
    private CinemaInfoBean.ResultBean result;
    private CinemaSchedulAdapter cinemaSchedulAdapter;
    private int id;
    private List<CinemaFilmBean.ResultBean> movieList;
    private CinemaFilmAdapter cinemaFilmAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cinema_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);

        cinemaFilm.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                int movieId = movieList.get(position).getId();
                if(cinemaGroup.getChildCount()>0){
                    RadioButton radioButton= (RadioButton) cinemaGroup.getChildAt(position);
                    radioButton.setChecked(true);
                    onGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST,id,movieId),FilmSchedulBean.class);
                }
            }
        });

        LinearLayoutManager schedulLayoutManager=new LinearLayoutManager(this);
        cinemaFilmScheduling.setLayoutManager(schedulLayoutManager);
        cinemaSchedulAdapter = new CinemaSchedulAdapter(this);
        cinemaFilmScheduling.setAdapter(cinemaSchedulAdapter);

        cinemaFilmAdapter = new CinemaFilmAdapter(this);
        cinemaFilm.setAdapter(cinemaFilmAdapter);

        onGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET, id),CinemaInfoBean.class);
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_LIST_BY_CINEMAID_GET, id),CinemaFilmBean.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getFlowId(MessageBean messageBean){
        if(messageBean.getId().equals("onitemclick")){
            cinemaFilm.smoothScrollToPosition((Integer) messageBean.getObject());
            RadioButton radioButton= (RadioButton) cinemaGroup.getChildAt((Integer) messageBean.getObject());
            radioButton.setChecked(true);
        }
    }

    @OnClick(R.id.cinema_film_return)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cinema_film_return:
                finish();
                break;
            default:break;
        }
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof CinemaInfoBean){
            CinemaInfoBean cinemaInfoBean= (CinemaInfoBean) data;
            result = cinemaInfoBean.getResult();
            cinemaLogo.setImageURI(Uri.parse(result.getLogo()));
            cinemaName.setText(result.getName());
            cinemaAddress.setText(result.getAddress());
        }else if(data instanceof CinemaFilmBean){
            CinemaFilmBean filmBean= (CinemaFilmBean) data;
            if(filmBean.getResult().size()>0){
                movieList = filmBean.getResult();
                if(movieList.size()>0){
                    cinemaFilmAdapter.setListBeans(movieList);
                    cinemaGroup.removeAllViews();
                    int width = cinemaGroup.getWidth();
                    int childWidth = width / movieList.size();
                    for (int i=0;i<movieList.size();i++){
                        RadioButton radioButton=new RadioButton(this);
                        radioButton.setWidth(childWidth);
                        radioButton.setBackgroundResource(R.drawable.home_film_divide_selected);
                        radioButton.setChecked(false);
                        cinemaGroup.addView(radioButton);
                    }
                    cinemaFilm.smoothScrollToPosition(movieList.size()/2);
                    RadioButton radioButton= (RadioButton) cinemaGroup.getChildAt(movieList.size()/2);
                    radioButton.setChecked(true);
                }else{
                    ToastUtil.showToast("没有影片信息");
                }
            }
        }else if(data instanceof FilmSchedulBean){
            FilmSchedulBean schedulBean= (FilmSchedulBean) data;
            if(schedulBean.getResult().size()>0){
                cinemaSchedulAdapter.setList(schedulBean.getResult());
            }
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        EventBus.getDefault().unregister(this);
    }
}
