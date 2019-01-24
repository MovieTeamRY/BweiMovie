package com.bw.movie.guide.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.guide.bean.GuideBean;
import com.bw.movie.guide.view.GuideCustomView;
import com.bw.movie.login.LoginActivity;

import java.util.List;

public class GuideAdapter extends PagerAdapter {

    private List<GuideBean> list;
    private Context context;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        GuideCustomView guideCustomView=new GuideCustomView(context);
        guideCustomView.setData(list.get(position).getImage(),list.get(position).getText(),list.get(position).getDescirbe());
        container.addView(guideCustomView);
        if(position==list.size()-1){
            Intent intent=new Intent(context,LoginActivity.class);
            context.startActivity(intent);
        }
        return guideCustomView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
