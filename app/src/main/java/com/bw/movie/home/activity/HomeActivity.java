package com.bw.movie.home.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.fragment.CinemaFragment;
import com.bw.movie.film.fragment.FilmFragment;
import com.bw.movie.home.bean.TokenPushBean;
import com.bw.movie.mine.fragment.MineFragment;
import com.bw.movie.utils.AddressUtils;
import com.bw.movie.utils.AnimatorUtils;
import com.bw.movie.utils.ToastUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import java.util.HashMap;
import java.util.Map;

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
    private CinemaFragment cinemaFragment;
    private MineFragment mineFragment;
    private String token;

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof TokenPushBean){
            TokenPushBean tokenPushBean  = (TokenPushBean) data;
        }
    }
    @Override
    protected void onNetFail(String error) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取token值
        Map<String, String> map=new HashMap<>();
        map.put("token",token);
        map.put("os",String.valueOf(1));
        onPostRequest(Apis.URL_UP_LOAD_PUSH_TOKEN_POST,map,TokenPushBean.class);
    }

    @Override
    protected void initData() {
        //信鸽推送
        XGPushConfig.enableDebug(this,true);
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setHuaweiDebug(true);
        XGPushManager.registerPush(getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        token = (String) data;
                        Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                    }
                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                    }
                });
        // 获取token
        XGPushConfig.getToken(this);
        //创建fragment
        filmFragment = new FilmFragment();
        cinemaFragment = new CinemaFragment();
        mineFragment = new MineFragment();
        //AddressUtils.getAddressUtils().getAddressDetail(this);
        //fragment管理器
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.home_frame, filmFragment, filmFragment.getClass().getName());
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
                FragmentManager managerFilm = getSupportFragmentManager();
                FragmentTransaction transactionFilm = managerFilm.beginTransaction();
                transactionFilm.hide(mineFragment);
                transactionFilm.hide(cinemaFragment);
                transactionFilm.show(filmFragment);
                transactionFilm.commit();
                AnimatorUtils.scaleAnimator(view,"scaleX","scaleY",1f,1.17f,0);
                AnimatorUtils.scaleAnimator(homeCinema,"scaleX","scaleY",1f,0);
                AnimatorUtils.scaleAnimator(homeMy,"scaleX","scaleY",1f,0);
                break;
            case R.id.home_cinema:
                FragmentManager managerCinema = getSupportFragmentManager();
                FragmentTransaction transactionCinema = managerCinema.beginTransaction();
                if (managerCinema.findFragmentByTag(cinemaFragment.getClass().getName()) == null) {
                    transactionCinema.add(R.id.home_frame, cinemaFragment, cinemaFragment.getClass().getName());
                }
                transactionCinema.hide(filmFragment);
                transactionCinema.hide(mineFragment);
                transactionCinema.show(cinemaFragment);
                transactionCinema.commit();
                AnimatorUtils.scaleAnimator(view,"scaleX","scaleY",1f,1.17f,0);
                AnimatorUtils.scaleAnimator(homeFilm,"scaleX","scaleY",1f,0);
                AnimatorUtils.scaleAnimator(homeMy,"scaleX","scaleY",1f,0);
                break;
            case R.id.home_my:
                FragmentManager managerMine = getSupportFragmentManager();
                FragmentTransaction transactionMine = managerMine.beginTransaction();
                if (managerMine.findFragmentByTag(mineFragment.getClass().getName()) == null) {
                    transactionMine.add(R.id.home_frame, mineFragment, mineFragment.getClass().getName());
                }
                transactionMine.hide(filmFragment);
                transactionMine.hide(cinemaFragment);
                transactionMine.show(mineFragment);
                transactionMine.commit();
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
                SharedPreferences sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("SessionId","");
                editor.commit();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
