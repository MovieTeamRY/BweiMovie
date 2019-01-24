package com.bw.movie.guide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.guide.bean.GuideBean;
import com.bw.movie.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideActivity extends BaseActivty {

    @BindView(R.id.guide_image)
    ImageView guideImage;
    @BindView(R.id.guide_viewpager)
    ViewPager guideViewpager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<GuideBean> list;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            guideViewpager.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }

    @Override
    protected void initData() {
        list=new ArrayList<>();
        list.add(new GuideBean(R.mipmap.guide_first,"看遍人生百态"));
        list.add(new GuideBean(R.mipmap.guide_first,"荡涤你的心灵"));
        list.add(new GuideBean(R.mipmap.guide_first,"净化你的灵魂"));
        list.add(new GuideBean(R.mipmap.guide_first,"八维移动通信学院作品","带您开启一段美好的电影之旅"));
        sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
        boolean is_guide = sharedPreferences.getBoolean("is_guide", false);
        if(is_guide){
            Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{

        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guide;
    }
}
