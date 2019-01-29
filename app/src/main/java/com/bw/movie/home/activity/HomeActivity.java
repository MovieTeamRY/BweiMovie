package com.bw.movie.home.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.fragment.CinemaFragment;
import com.bw.movie.film.fragment.FilmFragment;
import com.bw.movie.mine.fragment.MineFragment;
import com.bw.movie.utils.AddressUtils;
import com.bw.movie.utils.AnimatorUtils;
import com.bw.movie.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends BaseActivty {
    @BindView(R.id.home_frame)
    FrameLayout homeFrame;
    @BindView(R.id.home_film)
    RadioButton homeFilm;
    @BindView(R.id.home_cinema)
    RadioButton homeCinema;
    @BindView(R.id.home_my)
    RadioButton homeMy;
    @BindView(R.id.home_group)
    RadioGroup homeGroup;
    private Unbinder bind;
    private FilmFragment filmFragment;

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }

    @Override
    protected void initData() {
        filmFragment = new FilmFragment();
        AddressUtils.getAddressUtils().getAddressDetail(this);
        //fragment管理器
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.home_frame, filmFragment);
        transaction.commit();
        homeFilm.setChecked(true);
        AnimatorUtils.scaleAnimator(homeFilm,"scaleX","scaleY",1f,1.17f,0);
        AnimatorUtils.scaleAnimator(homeCinema,"scaleX","scaleY",1f,0);
        AnimatorUtils.scaleAnimator(homeMy,"scaleX","scaleY",1f,0);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
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
                FragmentManager manager1 = getSupportFragmentManager();
                FragmentTransaction transaction1 = manager1.beginTransaction();
                transaction1.replace(R.id.home_frame, filmFragment);
                transaction1.commit();
                AnimatorUtils.scaleAnimator(view,"scaleX","scaleY",1f,1.17f,0);
                AnimatorUtils.scaleAnimator(homeCinema,"scaleX","scaleY",1f,0);
                AnimatorUtils.scaleAnimator(homeMy,"scaleX","scaleY",1f,0);
                break;
            case R.id.home_cinema:
                CinemaFragment cinemaFragment = new CinemaFragment();
                FragmentManager manager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = manager2.beginTransaction();
                transaction2.replace(R.id.home_frame, cinemaFragment);
                transaction2.commit();
                AnimatorUtils.scaleAnimator(view,"scaleX","scaleY",1f,1.17f,0);
                AnimatorUtils.scaleAnimator(homeFilm,"scaleX","scaleY",1f,0);
                AnimatorUtils.scaleAnimator(homeMy,"scaleX","scaleY",1f,0);
                break;
            case R.id.home_my:
                MineFragment mineFragment = new MineFragment();
                FragmentManager manager3 = getSupportFragmentManager();
                FragmentTransaction transaction3 = manager3.beginTransaction();
                transaction3.replace(R.id.home_frame, mineFragment);
                transaction3.commit();
                AnimatorUtils.scaleAnimator(view,"scaleX","scaleY",1f,1.17f,0);
                AnimatorUtils.scaleAnimator(homeCinema,"scaleX","scaleY",1f,0);
                AnimatorUtils.scaleAnimator(homeFilm,"scaleX","scaleY",1f,0);
                break;
            default:break;
        }
    }
    //监听返回键
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                ToastUtil.showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
