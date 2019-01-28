package com.bw.movie.film.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.film.adapter.FilmCommentAdapter;
import com.bw.movie.film.adapter.NoticeAdapter;
import com.bw.movie.film.adapter.RevirwAdapter;
import com.bw.movie.film.adapter.StillsAdapter;
import com.bw.movie.film.bean.CancalFollowMovieBean;
import com.bw.movie.film.bean.CommentBean;
import com.bw.movie.film.bean.DetailsBean;
import com.bw.movie.film.bean.FilmCommentBean;
import com.bw.movie.film.bean.FilmDetailsBean;
import com.bw.movie.film.bean.FollowMovieBean;
import com.bw.movie.film.bean.PraiseBean;
import com.bw.movie.film.bean.RevirwBean;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PopupWindow nopicePop;
    private PopupWindow stillsPop;
    private PopupWindow reviewPop;
    private TextView class_name;
    private TextView director_name;
    private TextView data_name;
    private TextView address_name;
    private TextView plot_name_text;
    private SimpleDraweeView image_detail_three;
    private ImageView detail_down;
    private int movieId;
    private NoticeAdapter noticeAdapter;
    private View review_view;
    private View stills_view;
    private View notice_view;
    private View detail_view;
    private StillsAdapter stillsAdapter;
    private int mpage;
    private final int COUNT=5;
    private RevirwAdapter revirwAdapter;
    private XRecyclerView film_recyclerview;
    private int i;
    private FilmDetailsBean filmDetailsBean;
    private FilmDetailsBean.ResultBean result;
    private XRecyclerView film_comment_recycler;
    private FilmCommentAdapter filmCommentAdapter;
    private boolean bool=true;
    private ImageButton write;

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
        getNoticeView();
        getStillsView();
        getRevirwView();
    }
    /**
     *评论布局
     *@author Administrator
     *@time 2019/1/27 0027 11:45
     */
    private void getRevirwView() {
        review_view = View.inflate(this,R.layout.film_pop_review_view,null);
        detail_down= review_view.findViewById(R.id.film_down);
        film_recyclerview = review_view.findViewById(R.id.film_recyclerview);
        write = review_view.findViewById(R.id.write);
        final LinearLayout linearLayout = review_view.findViewById(R.id.layout_write);
        final EditText edit_write = review_view.findViewById(R.id.edit_write);
        final TextView but_write = review_view.findViewById(R.id.but_write);
        getReviewPopView(review_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        film_recyclerview.setLayoutManager(linearLayoutManager);
        film_recyclerview.setPullRefreshEnabled(true);
        film_recyclerview.setLoadingMoreEnabled(true);
        film_recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage=1;
                init();
            }
            @Override
            public void onLoadMore() {
                init();
            }
        });
        //TODO 创建适配器
        revirwAdapter = new RevirwAdapter(this);
        film_recyclerview.setAdapter(revirwAdapter);
        //点赞
        revirwAdapter.setLucky(new RevirwAdapter.Lucky() {
            @Override
            public void onLucky(int commentId,int position) {
                i=position;
                Map<String,String> map = new HashMap<>();
                map.put("commentId",String.valueOf(commentId));
                onPostRequest(Apis.URL_MOVIE_COMMENT_GREAT_POST,map,PraiseBean.class);
            }
        });
        //点击查看评论回复
        revirwAdapter.setClick(new RevirwAdapter.Click() {
            @Override
            public void onClick(int commentId,XRecyclerView film_comment_recyclerview) {
                film_comment_recycler=film_comment_recyclerview;
                if (bool){
                    film_comment_recycler.setVisibility(View.VISIBLE);
                    getcommetView(commentId);
                    mpage=1;
                    onGetRequest(String.format(Apis.URL_FIND_COMMENT_REPLY_GET,commentId,mpage,COUNT),FilmCommentBean.class);
                }else{
                    film_comment_recycler.setVisibility(View.GONE);
                }
                bool=!bool;
            }
        });
        //评论影片
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                but_write.setVisibility(View.VISIBLE);
                write.setVisibility(View.GONE);

            }
        });
        //评论
        but_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                but_write.setVisibility(View.GONE);
                write.setVisibility(View.VISIBLE);
                String trim = edit_write.getText().toString().trim();
                if (trim.equals("")){

                }else{
                    Map<String, String> map =new HashMap<>();
                    map.put("movieId",String.valueOf(movieId));
                    map.put("commentContent",trim);
                    onPostRequest(Apis.URL_COMMENT_REPLY_POST,map,CommentBean.class);
                }


            }
        });

    }
    private void getcommetView(final int commentId) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        film_comment_recycler.setLayoutManager(linearLayoutManager);
        //创建评论回复适配器
        filmCommentAdapter = new FilmCommentAdapter(this);
        film_comment_recycler.setAdapter(filmCommentAdapter);
        film_comment_recycler.setPullRefreshEnabled(true);
        film_comment_recycler.setLoadingMoreEnabled(true);
        film_comment_recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mpage=1;
                onGetRequest(String.format(Apis.URL_FIND_COMMENT_REPLY_GET,commentId,mpage,COUNT),FilmCommentBean.class);
            }
            @Override
            public void onLoadMore() {
                onGetRequest(String.format(Apis.URL_FIND_COMMENT_REPLY_GET,commentId,mpage,COUNT),FilmCommentBean.class);
            }
        });
    }
    /**
     *剧照布局
     *@author Administrator
     *@time 2019/1/27 0027 11:45
     */
    private void getStillsView() {
        stills_view = View.inflate(this,R.layout.file_pop_stills_view,null);
        detail_down= stills_view.findViewById(R.id.stills_down);
        getStillsPopView(stills_view);
        RecyclerView stills_recyclerview= stills_view.findViewById(R.id.stills_recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        stills_recyclerview.setLayoutManager(staggeredGridLayoutManager);
        //TODO 创建适配器
        stillsAdapter = new StillsAdapter(this);
        stills_recyclerview.setAdapter(stillsAdapter);

    }
    /**
     *预告片布局
     *@author Administrator
     *@time 2019/1/27 0027 11:45
     */
    private void getNoticeView() {
        notice_view = View.inflate(this,R.layout.film_pop_notice_view,null);
        detail_down= notice_view.findViewById(R.id.notice_down);
        getNoticePopView(notice_view);
        RecyclerView notice_recyclerview= notice_view.findViewById(R.id.notice_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notice_recyclerview.setLayoutManager(linearLayoutManager);
        //TODO 创建适配器
        noticeAdapter = new NoticeAdapter(this);
        notice_recyclerview.setAdapter(noticeAdapter);

    }
    /**
     *详情布局
     *@author Administrator
     *@time 2019/1/27 0027 11:44
     */
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
        getDetailsPopView(detail_view);
    }

    /**
     *加载详情的布局
     *@author Administrator
     *@time 2019/1/26 0026 16:14
     */
    private void getDetailsPopView(View view) {
        mPop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        mPop.setFocusable(true);
        //设置是否可以触摸
        mPop.setTouchable(true);
        //关闭
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
    }
    /**
     *预告片
     *@author Administrator
     *@time 2019/1/27 0027 11:12
     */
    private void getNoticePopView(View view) {
        nopicePop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        nopicePop.setFocusable(true);
        //设置是否可以触摸
        nopicePop.setTouchable(true);
        nopicePop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10ffffff")));
        //关闭
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nopicePop.dismiss();
            }
        });
    }
    /**
     *剧照
     *@author Administrator
     *@time 2019/1/27 0027 11:12
     */
    private void getStillsPopView(View view) {
        stillsPop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        stillsPop.setFocusable(true);
        //设置是否可以触摸
        stillsPop.setTouchable(true);
        //关闭
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stillsPop.dismiss();
            }
        });
    }
    /**
     *剧照
     *@author Administrator
     *@time 2019/1/27 0027 11:12
     */
    private void getReviewPopView(View view) {
        reviewPop = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        //设置焦点
        reviewPop.setFocusable(true);
        //设置是否可以触摸
        reviewPop.setTouchable(true);
        //关闭
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewPop.dismiss();
            }
        });
    }
    @Override
    protected void initData() {
        Intent intent = getIntent();
        movieId = intent.getIntExtra("id", 0);
        //请求查看电影信息的接口
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_BY_ID_GET, movieId),DetailsBean.class);
        //请求电影详情接口
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_DETAIL_GET,movieId),FilmDetailsBean.class);
        //请求电影评论接口
        init();
    }
    /**
     * 请求电影评论接口
     *@author Administrator
     *@time 2019/1/27 0027 13:14
     */
    private void init() {
        mpage=1;
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_COMMENT_GET,movieId,mpage,COUNT),RevirwBean.class);
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
        filmDetailsBean = (FilmDetailsBean) data;
        result = filmDetailsBean.getResult();
        if (filmDetailsBean.isSuccess()&& filmDetailsBean !=null){
                //TODO 设置值详细
                Uri uri = Uri.parse(filmDetailsBean.getResult().getImageUrl());
                image_detail_three.setImageURI(uri);
                class_name.setText("类型：" + filmDetailsBean.getResult().getMovieTypes());
                director_name.setText("导演：" + filmDetailsBean.getResult().getDirector());
                data_name.setText("时长：" + filmDetailsBean.getResult().getDuration());
                address_name.setText("产地：" + filmDetailsBean.getResult().getPlaceOrigin());
                plot_name_text.setText(filmDetailsBean.getResult().getSummary());
                //传值到预告片适配器
                noticeAdapter.setList(filmDetailsBean.getResult().getShortFilmList());
                //传值到剧照适配器
                List<String> posterList = filmDetailsBean.getResult().getPosterList();
                stillsAdapter.setList(posterList);
                //设置是否关注
            if (result.getFollowMovie()==1){
                imageDetailSelect.setBackgroundResource(R.mipmap.com_icon_heart_selected);
            }else if (result.getFollowMovie()==2){
                imageDetailSelect.setBackgroundResource(R.mipmap.com_icon_heart_default);
            }
        }
    }else if (data instanceof RevirwBean){
        RevirwBean revirwBean = (RevirwBean) data;
        if (revirwBean!=null&&revirwBean.isSuccess()){
            //TODO 传值到适配器
            if (mpage==1){
                revirwAdapter.setList(revirwBean.getResult());
            }else{
                revirwAdapter.addList(revirwBean.getResult());
            }
            mpage++;
            film_recyclerview.loadMoreComplete();
            film_recyclerview.refreshComplete();
        }
        ToastUtil.showToast(revirwBean.getMessage());
    }else if (data instanceof PraiseBean){
        PraiseBean praiseBean = (PraiseBean) data;
        if (praiseBean!=null&&praiseBean.isSuccess()){
            revirwAdapter.addWhetherGreat(i);
        }
        ToastUtil.showToast(praiseBean.getMessage());
    }else if (data instanceof FollowMovieBean){
        FollowMovieBean followMovieBean = (FollowMovieBean) data;
        if (followMovieBean!=null&&followMovieBean.isSuccess()){
            result.setFollowMovie(1);
            imageDetailSelect.setBackgroundResource(R.mipmap.com_icon_heart_selected);
        }
        ToastUtil.showToast(followMovieBean.getMessage());
    }else if (data instanceof CancalFollowMovieBean){
        CancalFollowMovieBean cancalFollowMovieBean = (CancalFollowMovieBean) data;
        if (cancalFollowMovieBean!=null&&cancalFollowMovieBean.isSuccess()){
            result.setFollowMovie(2);
            imageDetailSelect.setBackgroundResource(R.mipmap.com_icon_heart_default);
        }
        ToastUtil.showToast(cancalFollowMovieBean.getMessage());
    }else if (data instanceof FilmCommentBean){
        FilmCommentBean filmCommentBean = (FilmCommentBean) data;
        if (filmCommentBean!=null&&filmCommentBean.isSuccess()){

           /* film_comment_recycler.setVisibility(View.VISIBLE);*/
            //TODO 传值到查看评论回复适配器
            if (mpage == 1) {
                filmCommentAdapter.setList(filmCommentBean.getResult());
            } else {
                filmCommentAdapter.addList(filmCommentBean.getResult());
            }
            mpage++;
            film_comment_recycler.loadMoreComplete();
            film_comment_recycler.refreshComplete();
        }else{
           // film_comment_recycler.setVisibility(View.GONE);
            ToastUtil.showToast(filmCommentBean.getMessage());
        }

    }else if (data instanceof CommentBean){
        CommentBean commentBean = (CommentBean) data;
        ToastUtil.showToast(commentBean.getMessage());
    }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
        ToastUtil.showToast(error);
    }
    
    @SuppressLint("NewApi")
    @OnClick({R.id.detail, R.id.notice, R.id.stills, R.id.film_review, R.id.return_image, R.id.but_purchase,R.id.image_detail_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail:
                //显示pop
                mPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                mPop.update();
                break;
            case R.id.notice:
                //显示pop
                nopicePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                nopicePop.update();
                break;
            case R.id.stills:
                //显示pop
                stillsPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                stillsPop.update();
                break;
            case R.id.film_review:
                //显示pop
                reviewPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                reviewPop.update();
                break;
            case R.id.return_image:
                finish();
                break;
            case R.id.image_detail_select:
                //关注
                if ( result.getFollowMovie()==1){
                    onGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_MOVIE_GET, filmDetailsBean.getResult().getId()),CancalFollowMovieBean.class);
                }else if (result.getFollowMovie()==2) {
                    onGetRequest(String.format(Apis.URL_FOLLOW_MOVIE_GET, filmDetailsBean.getResult().getId()), FollowMovieBean.class);
                }
                break;
            case R.id.but_purchase:

                break;

            default:break;
        }
    }
    
}
