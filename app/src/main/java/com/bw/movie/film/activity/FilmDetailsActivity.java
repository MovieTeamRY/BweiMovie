package com.bw.movie.film.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.film.adapter.NoticeAdapter;
import com.bw.movie.film.adapter.StillsAdapter;
import com.bw.movie.film.bean.DetailsBean;
import com.bw.movie.film.bean.FilmDetailsBean;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilmDetailsActivity extends BaseActivty {
    @BindView(R.id.bg_image_detail)
    SimpleDraweeView bgImageDetail;
    @BindView(R.id.image_detail_select)
    ImageView imageDetailSelect;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.bg_image_detail_name)
    SimpleDraweeView bgImageDetailName;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.notice)
    TextView notice;
    @BindView(R.id.stills)
    TextView stills;
    @BindView(R.id.film_review)
    TextView filmReview;
    @BindView(R.id.return_image)
    ImageView returnImage;
    @BindView(R.id.but_purchase)
    TextView butPurchase;
    private PopupWindow mPop;
    private TextView class_name;
    private TextView director_name;
    private TextView data_name;
    private TextView address_name;
    private TextView plot_name_text;
    private SimpleDraweeView image_detail_three;
    private ImageView detail_down;
    private int num=0;
    private int movieId;
    private NoticeAdapter noticeAdapter;
    private View review_view;
    private View stills_view;
    private View notice_view;
    private View detail_view;
    private StillsAdapter stillsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.film_activity_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //绑定控件
        ButterKnife.bind(this);
        //加载详情的布局

    }

    private void getRevirwView() {
        review_view = View.inflate(this,R.layout.film_pop_review_view,null);
        detail_down= review_view.findViewById(R.id.film_down);
        RecyclerView film_recyclerview= review_view.findViewById(R.id.film_recyclerview);
        //TODO 创建适配器
        getPopView(review_view);
    }

    private void getStillsView() {
        stills_view = View.inflate(this,R.layout.file_pop_stills_view,null);
        detail_down= stills_view.findViewById(R.id.stills_down);
        RecyclerView stills_recyclerview= stills_view.findViewById(R.id.stills_recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        stills_recyclerview.setLayoutManager(staggeredGridLayoutManager);
        //TODO 创建适配器
        stillsAdapter = new StillsAdapter(this);
        stills_recyclerview.setAdapter(stillsAdapter);
        getPopView(stills_view);
    }

    private void getNoticeView() {
        notice_view = View.inflate(this,R.layout.film_pop_notice_view,null);
        detail_down= notice_view.findViewById(R.id.notice_down);
        RecyclerView notice_recyclerview= notice_view.findViewById(R.id.notice_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notice_recyclerview.setLayoutManager(linearLayoutManager);
        //TODO 创建适配器
        noticeAdapter = new NoticeAdapter(this);
        notice_recyclerview.setAdapter(noticeAdapter);
        getPopView(notice_view);
    }

    private void getDetailsView() {
        detail_view = View.inflate(this,R.layout.film_pop_details_view,null);
        //获取控件id
        class_name = detail_view.findViewById(R.id.class_name);
        director_name = detail_view.findViewById(R.id.director_name);
        data_name = detail_view.findViewById(R.id.data_name);
        address_name = detail_view.findViewById(R.id.address_name);
        plot_name_text = detail_view.findViewById(R.id.plot_name_text);
        image_detail_three = detail_view.findViewById(R.id.image_detail_three);
        detail_down = detail_view.findViewById(R.id.detail_down);
        getPopView(detail_view);
    }

    /**
     *加载详情的布局
     *@author Administrator
     *@time 2019/1/26 0026 16:14
     */
    private void getPopView(View view) {

        mPop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        mPop.setFocusable(true);
        //设置是否可以触摸
        mPop.setTouchable(true);
        mPop.setBackgroundDrawable(new BitmapDrawable());

        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlfa(1f);//pop消失，透明度恢复
            }
        });
        //关闭
        mPopclose();
    }
    /**
     *关闭pop
     *@author Administrator
     *@time 2019/1/26 0026 16:27
     */
    private void mPopclose() {
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
    }
    /**
     *改变窗口透明度
     *@author Administrator
     *@time 2019/1/26 0026 16:26
     */
    private void changeWindowAlfa(float v) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = v;
        getWindow().setAttributes(params);
    }



    @Override
    protected void initData() {
        Intent intent = getIntent();
        movieId = intent.getIntExtra("id", 0);
        //请求查看电影信息的接口
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_BY_ID_GET, movieId),DetailsBean.class);


    }

    @Override
    protected void onNetSuccess(Object data) {
    if (data instanceof DetailsBean){
        DetailsBean detailsBean = (DetailsBean) data;
        if (detailsBean.isSuccess()&&detailsBean!=null){
            //展示数据
            Uri uri = Uri.parse(detailsBean.getResult().getImageUrl());
            bgImageDetail.setImageURI(uri);
            bgImageDetailName.setImageURI(uri);
            textName.setText(detailsBean.getResult().getName());
        }
        ToastUtil.showToast(detailsBean.getMessage());
    }else if (data instanceof FilmDetailsBean){
        FilmDetailsBean filmDetailsBean = (FilmDetailsBean) data;
        if (filmDetailsBean.isSuccess()&&filmDetailsBean!=null){
            if (detail_view!=null) {
                //TODO 设置值详细
                Uri uri = Uri.parse(filmDetailsBean.getResult().getImageUrl());
                image_detail_three.setImageURI(uri);
                class_name.setText("类型：" + filmDetailsBean.getResult().getMovieTypes());
                director_name.setText("导演：" + filmDetailsBean.getResult().getDirector());
                data_name.setText("时长：" + filmDetailsBean.getResult().getDuration());
                address_name.setText("产地：" + filmDetailsBean.getResult().getPlaceOrigin());
                plot_name_text.setText(filmDetailsBean.getResult().getSummary());

            }else if (notice_view!=null){
                //产值到预告片适配器
                noticeAdapter.setList(filmDetailsBean.getResult().getShortFilmList());
            }else if (stills_view!=null){
                //产值到预告片适配器
                List<String> posterList = filmDetailsBean.getResult().getPosterList();
                stillsAdapter.setList(posterList);
            }
        }
        ToastUtil.showToast(filmDetailsBean.getMessage());
    }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
        ToastUtil.showToast(error);
    }


    @SuppressLint("NewApi")
    @OnClick({R.id.detail, R.id.notice, R.id.stills, R.id.film_review, R.id.return_image, R.id.but_purchase})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail:
                notice_view=null;
                stills_view=null;
                review_view=null;
                getDetailsView();
                //显示pop
                showPopView(view);
                onGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
                break;
            case R.id.notice:
                detail_view=null;
                stills_view=null;
                review_view=null;
                getNoticeView();
                //显示pop
                showPopView(view);
                onGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
                break;
            case R.id.stills:
                notice_view=null;
                detail_view=null;
                review_view=null;
                getStillsView();
                //显示pop
                showPopView(view);
                onGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
                break;
            case R.id.film_review:
                detail_view=null;
                notice_view=null;
                stills_view=null;
                getRevirwView();
                //显示pop
                showPopView(view);
                break;
            case R.id.return_image:
                finish();
                break;
            case R.id.but_purchase:
                break;
        }
    }

    /**
     *显示pop
     *@author Administrator
     *@time 2019/1/26 0026 21:47
     */
    private void showPopView(View view) {
        //改变窗口透明度
        changeWindowAlfa(0.6f);
        mPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        mPop.update();
        //mPop.showAsDropDown(view, Gravity.BOTTOM,0 ,0 );
    }
}
