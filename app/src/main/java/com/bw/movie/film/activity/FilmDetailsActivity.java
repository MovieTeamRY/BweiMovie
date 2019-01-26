package com.bw.movie.film.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.bw.movie.film.bean.DetailsBean;
import com.bw.movie.film.bean.FilmDetailsBean;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

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

    @Override
    protected int getLayoutResId() {
        return R.layout.film_activity_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //绑定控件
        ButterKnife.bind(this);
        //加载详情的布局
        getDetailsView();
    }
    /**
     *加载详情的布局
     *@author Administrator
     *@time 2019/1/26 0026 16:14
     */
    private void getDetailsView() {
        View view = View.inflate(this,R.layout.film_pop_details_view,null);
        class_name = view.findViewById(R.id.class_name);
        director_name = view.findViewById(R.id.director_name);
        data_name = view.findViewById(R.id.data_name);
        address_name = view.findViewById(R.id.address_name);
        plot_name_text = view.findViewById(R.id.plot_name_text);
        image_detail_three = view.findViewById(R.id.image_detail_three);
        detail_down = view.findViewById(R.id.detail_down);

        mPop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
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
        int movieId = intent.getIntExtra("id", 0);
        //请求查看电影信息的接口
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_BY_ID_GET,movieId),DetailsBean.class);
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
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
            //TODO 设置值详细
            Uri uri = Uri.parse(filmDetailsBean.getResult().getImageUrl());
            image_detail_three.setImageURI(uri);
            class_name.setText(filmDetailsBean.getResult().getMovieTypes());
            director_name.setText(filmDetailsBean.getResult().getDirector());
            data_name.setText(filmDetailsBean.getResult().getDuration());
            address_name.setText(filmDetailsBean.getResult().getPlaceOrigin());
            plot_name_text.setText(filmDetailsBean.getResult().getSummary());
        }
        ToastUtil.showToast(filmDetailsBean.getMessage());
    }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }


    @SuppressLint("NewApi")
    @OnClick({R.id.detail, R.id.notice, R.id.stills, R.id.film_review, R.id.return_image, R.id.but_purchase})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail:
                //改变窗口透明度
                changeWindowAlfa(0.6f);
                mPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                mPop.update();
                mPop.showAsDropDown(view, Gravity.BOTTOM,0 ,0 );
                break;
            case R.id.notice:
                break;
            case R.id.stills:
                break;
            case R.id.film_review:
                break;
            case R.id.return_image:
                break;
            case R.id.but_purchase:
                break;
        }
    }
}
