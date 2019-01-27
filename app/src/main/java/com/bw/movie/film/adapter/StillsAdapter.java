package com.bw.movie.film.adapter;

import android.content.Context;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.movie.R;
import com.bw.movie.film.bean.FilmDetailsBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StillsAdapter extends RecyclerView.Adapter<StillsAdapter.ViewHolder> {

    private List<String> list;
    private Context context;

    public StillsAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<String> data) {
        list.clear();
        if (data!=null) {
            list = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_adapter_stills_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //动态设置宽高
        ViewGroup.LayoutParams layoutParams = holder.stillsimage.getLayoutParams();
        layoutParams.height = dip2px(context, (float) (100 + (Math.random() * 150)));
        holder.stillsimage.setLayoutParams(layoutParams);
        holder.stillsimage.setImageURI(list.get(position));


    }
    //设置宽高比
    public static int dip2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stills_image)
        SimpleDraweeView stillsimage;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
