package com.bw.movie.film.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.film.adapter.RelaeseAdapter;
import com.bw.movie.film.bean.RelaeseBean;
import com.bw.movie.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

public class FilmFragment extends BaseFragment {
    @BindView(R.id.recycler_flow)
    RecyclerCoverFlow recyclerFlow;
    Unbinder unbinder;
    @BindView(R.id.image_loc)
    ImageButton filmLoc;
    @BindView(R.id.text_loc)
    TextView textLoc;
    @BindView(R.id.image_search)
    ImageButton imageSearch;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.text_search)
    TextView textSearch;
    @BindView(R.id.film_search_linear)
    LinearLayout searchLinear;
    @BindView(R.id.film_group)
    RadioGroup filmGroup;
    //热门电影
    @BindView(R.id.hot_film_more)
    ImageButton hotFilmMore;
    @BindView(R.id.hot_film_recycler)
    RecyclerView hotFilmRecycler;
    //正在上映
    @BindView(R.id.relaese_film_more)
    ImageButton relaeseFilmMore;
    @BindView(R.id.relaese_film_recycler)
    RecyclerView relaeseFilmRecycler;
    //即将上映
    @BindView(R.id.screen_film_more)
    ImageButton screenFilmMore;
    @BindView(R.id.screen_film_recycler)
    RecyclerView screenFilmRecycler;
    private int current;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            recyclerFlow.smoothScrollToPosition(current);
            current++;
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.file_fragment;
    }

    @Override
    protected View getLayoutView() {
        return null;
    }

    @Override
    protected void initData() {
        onGetRequest(String.format(Apis.URL_FIND_RELEASE_MOVIE_LIST_GET, 1), RelaeseBean.class);
        recyclerFlow.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            //滑动监听
            @Override
            public void onItemSelected(int position) {
                RadioButton childAt = (RadioButton) filmGroup.getChildAt(position % 10);
                childAt.setChecked(true);
                current = position;
            }
        });
    }

    @OnClick({R.id.image_loc, R.id.image_search, R.id.text_search,R.id.hot_film_more,R.id.relaese_film_more,R.id.screen_film_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_loc:
                //点击定位
                break;
            case R.id.image_search:
                //点击搜索框实现动画
                editSearch.setVisibility(View.VISIBLE);
                textSearch.setVisibility(View.VISIBLE);
                setAddAnimator(searchLinear);
                break;
            case R.id.text_search:
                //点击搜索影片 判断输入框的内容不能为空
                setCutAnimator(searchLinear);
                break;
        }
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    private void setAddAnimator(View view) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -470f);
        translationX.setInterpolator(new AccelerateInterpolator());
        translationX.setDuration(2000);
        translationX.start();
    }

    private void setCutAnimator(View view) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", 0f);
        translationX.setInterpolator(new AccelerateInterpolator());
        translationX.setDuration(2000);
        translationX.start();
        translationX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                editSearch.setVisibility(View.GONE);
                textSearch.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof RelaeseBean) {
            RelaeseBean relaeseBean = (RelaeseBean) data;
            if (relaeseBean.getMessage().equals("查询成功")) {
                if (relaeseBean.getResult().size() > 0) {
                    recyclerFlow.setAdapter(new RelaeseAdapter(relaeseBean.getResult(), getContext()));
                    current = 5;
                    handler.sendEmptyMessage(0);
                }
            }
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
        handler.removeMessages(0);
    }
}
