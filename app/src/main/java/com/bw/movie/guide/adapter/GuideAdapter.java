package com.bw.movie.guide.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.guide.bean.GuideBean;

import java.util.List;

public class GuideAdapter extends PagerAdapter {

    private List<GuideBean> list;
    private Context context;

    public GuideAdapter(List<GuideBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(list!=null) {
            return list.size() + 1;
        }else{
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if(position<list.size()){
            View view=View.inflate(context, R.layout.guide_custom_view,null);
            ImageView imageView=view.findViewById(R.id.guide_custom_image);
            imageView.setImageResource(list.get(position).getImage());
            TextView textView=view.findViewById(R.id.guide_custom_text);
            textView.setText(list.get(position).getText());
            TextView describe=view.findViewById(R.id.guide_custom_descirbe);
            describe.setText(list.get(position).getDescirbe());
            container.addView(view);
            return view;
        }else{
            return null;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
