package com.bw.movie.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.base.MyApplication;
import com.bw.movie.guide.adapter.GuideAdapter;
import com.bw.movie.guide.bean.GuideBean;
import com.bw.movie.home.activity.HomeActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GuideActivity extends BaseActivty {

    @BindView(R.id.guide_image)
    ImageView guideImage;
    @BindView(R.id.guide_viewpager)
    ViewPager guideViewpager;
    @BindView(R.id.guide_group)
    LinearLayout guideGroup;
    @BindView(R.id.guide_text)
    TextView guideText;
    @BindView(R.id.guide_title)
    TextView guideTitle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<GuideBean> list;
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            GuideActivity activity = (GuideActivity) reference.get();
            if(activity != null){
                boolean is_guide = activity.sharedPreferences.getBoolean("is_guide", false);
                activity.isGuide(is_guide);
            }
        }
    }
    private Unbinder bind;
    private MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initData() {
        sharedPreferences=getSharedPreferences("User",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        myHandler.sendEmptyMessageDelayed(0, 2000);
    }
    //判断是否是第一次下载
    private void isGuide(boolean is_guide){
        if(is_guide){
            Intent intent=new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            guideImage.setVisibility(View.GONE);
            guideText.setVisibility(View.GONE);
            guideTitle.setVisibility(View.GONE);
            guideViewpager.setVisibility(View.VISIBLE);
            guideGroup.setVisibility(View.VISIBLE);
            list=new ArrayList<>();
            list.add(new GuideBean(R.mipmap.guide_first,"看遍人生百态"));
            list.add(new GuideBean(R.mipmap.guide_second,"荡涤你的心灵"));
            list.add(new GuideBean(R.mipmap.guide_third,"净化你的灵魂"));
            list.add(new GuideBean(R.mipmap.icon,"八维移动通信学院作品","带您开启一段美好的电影之旅"));
            GuideAdapter guideAdapter=new GuideAdapter(list,this);
            guideViewpager.setAdapter(guideAdapter);
            initDot(list.size());
            guideGroup.getChildAt(0).setSelected(true);
            guideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                private int cacheIndex = -1;
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    if(i==list.size()){
                        Intent intent=new Intent(GuideActivity.this,HomeActivity.class);
                        startActivity(intent);
                        guideGroup.removeAllViews();
                        finish();
                    }else{
                        if(i<list.size()){
                            guideGroup.getChildAt(0).setSelected(false);
                            guideGroup.getChildAt(i).setSelected(true);
                            if(cacheIndex>=0){
                                guideGroup.getChildAt(cacheIndex).setSelected(false);
                            }
                            cacheIndex=i;
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

            editor.putBoolean("is_guide",true);
            editor.commit();
        }
    }

    private void initDot(int size) {
        guideGroup.removeAllViews();
        for (int i=0;i<size;i++){
            ImageView imageView=new ImageView(this);
            imageView.setBackgroundResource(R.drawable.guide_selector);
            LinearLayout.LayoutParams params=
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            int dimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
            params.leftMargin=dimension;
            params.rightMargin=dimension;
            guideGroup.addView(imageView,params);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onPause() {
        super.onPause();
        guideGroup.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        myHandler.removeCallbacksAndMessages(null);
    }
}
