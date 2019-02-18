package com.bw.movie.film.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bw.movie.R;
import com.bw.movie.film.bean.FilmDetailsBean;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<FilmDetailsBean.ResultBean.ShortFilmListBean> list;
    private Context context;

    public NoticeAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<FilmDetailsBean.ResultBean.ShortFilmListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_adapter_notice_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.notice_image.setUp(list.get(position).getVideoUrl(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
        Glide.with(context).load(list.get(position).getImageUrl()).crossFade()
                .into(holder.notice_image.thumbImageView);
        holder.notice_image.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notice_image)
        JCVideoPlayerStandard notice_image;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
