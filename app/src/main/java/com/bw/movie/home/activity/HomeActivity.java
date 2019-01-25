package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.CinemaFragment;
import com.bw.movie.film.fragment.FilmFragment;
import com.bw.movie.home.view.CustomViewpager;
import com.bw.movie.mine.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends BaseActivty {
    @BindView(R.id.home_viewpager)
    CustomViewpager homeViewpager;
    @BindView(R.id.home_film)
    RadioButton homeFilm;
    @BindView(R.id.home_cinema)
    RadioButton homeCinema;
    @BindView(R.id.home_my)
    RadioButton homeMy;
    @BindView(R.id.home_group)
    RadioGroup homeGroup;
    private Unbinder bind;

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }

    @Override
    protected void initData() {
        homeViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return new FilmFragment();
                    case 1:
                        return new CinemaFragment();
                    case 2:
                        return new MineFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        homeViewpager.setCurrentItem(0);
        homeGroup.check(R.id.home_film);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    //点击放大
    private void setAddAnimator(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.17f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.17f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(0);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    //点击缩小
    private void setCutAnimator(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(0);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick({R.id.home_film, R.id.home_cinema, R.id.home_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_film:
                homeViewpager.setCurrentItem(0);
                setAddAnimator(view);
                setCutAnimator(homeCinema);
                setCutAnimator(homeMy);
                break;
            case R.id.home_cinema:
                homeViewpager.setCurrentItem(1);
                setAddAnimator(view);
                setCutAnimator(homeFilm);
                setCutAnimator(homeMy);
                break;
            case R.id.home_my:
                homeViewpager.setCurrentItem(2);
                setAddAnimator(view);
                setCutAnimator(homeCinema);
                setCutAnimator(homeFilm);
                break;
            default:break;
        }
    }
}
