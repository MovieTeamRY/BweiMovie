package com.bw.movie.film.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.film.activity.FilmDetailsActivity;
import com.bw.movie.film.adapter.MovieHotAdapter;
import com.bw.movie.film.bean.CancalFollowMovieBean;
import com.bw.movie.film.bean.FollowMovieBean;
import com.bw.movie.film.bean.MovieFilmBean;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScreenFilmFragment extends BaseFragment {
    @BindView(R.id.hotXRecycleview)
    XRecyclerView hotXRecycleview;
    Unbinder unbinder;
    private int mapge;
    private MovieHotAdapter movieHotAdapter;
    private int postion;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_screen_film_view;
    }

    @Override
    protected void initData() {
        //请求正在热映的数据
        onGetRequest(String.format(Apis.URL_FIND_COMING_SOON_MOVIE_LIST_GET, mapge), MovieFilmBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        mapge=1;
        LinearLayoutManager hotFilmManager=new LinearLayoutManager(getContext());
        hotFilmManager.setOrientation(OrientationHelper.VERTICAL);
        hotXRecycleview.setLayoutManager(hotFilmManager);
        hotXRecycleview.setLoadingMoreEnabled(true);
        hotXRecycleview.setPullRefreshEnabled(true);
        hotXRecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mapge=1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        //创建适配器
        movieHotAdapter = new MovieHotAdapter(getContext());
        hotXRecycleview.setAdapter(movieHotAdapter);
        //点击条目跳转影片详情
        movieHotAdapter.setHotCallBack(new MovieHotAdapter.MovieListHotCallBack() {
            @Override
            public void hotCallBack(int id, int i, boolean bool) {
                postion = i;
                if (bool) {
                    onGetRequest(String.format(Apis.URL_FOLLOW_MOVIE_GET, id), FollowMovieBean.class);
                } else {
                    onGetRequest(String.format(Apis.URL_CANCEL_FOLLOW_MOVIE_GET, id), CancalFollowMovieBean.class);
                }
                if (listHotCallBack!=null){
                    listHotCallBack.callBack();
                }
            }

            @Override
            public void skipDetails(int id) {
                Intent intent=new Intent(getContext(),FilmDetailsActivity.class);
                intent.putExtra("id",id);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof MovieFilmBean) {
            MovieFilmBean movieFilmBean = (MovieFilmBean) data;
            if (movieFilmBean.isSuccess()&&movieFilmBean!=null) {
                if (mapge==1){
                    movieHotAdapter.setmList(movieFilmBean.getResult());
                }else{
                    movieHotAdapter.addmList(movieFilmBean.getResult());
                }
                mapge++;
                hotXRecycleview.refreshComplete();
                hotXRecycleview.loadMoreComplete();
            }
           /* ToastUtil.showToast(movieFilmBean.getMessage());*/
        }else if (data instanceof FollowMovieBean) {
            FollowMovieBean followMovieBean = (FollowMovieBean) data;
            if (followMovieBean != null && followMovieBean.isSuccess()) {
                movieHotAdapter.setAttentionScccess(postion);
            }
            ToastUtil.showToast(followMovieBean.getMessage());
        } else if (data instanceof CancalFollowMovieBean) {
            CancalFollowMovieBean cancalFollowMovieBean = (CancalFollowMovieBean) data;
            if (cancalFollowMovieBean != null && cancalFollowMovieBean.isSuccess()) {
                movieHotAdapter.setCancelAttention(postion);
            }
            ToastUtil.showToast(cancalFollowMovieBean.getMessage());
        }

    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null) {
            unbinder.unbind();
        }
    }
    ListHotCallBack listHotCallBack;
    public interface ListHotCallBack{
        void callBack();
    }
    public void setListHotCallBack(ListHotCallBack callBack){
        listHotCallBack=callBack;
    }
}