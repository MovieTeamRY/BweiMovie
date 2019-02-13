package com.bw.movie.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.mine.bean.AttentFilmBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttentFilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AttentFilmBean.ResultBean> resultBeanList;
    private Context context;

    public AttentFilmAdapter(Context context) {
        this.context = context;
        resultBeanList = new ArrayList<>();
    }

    public void setResultBeanList(List<AttentFilmBean.ResultBean> resultBeanList) {
        this.resultBeanList = resultBeanList;
        notifyDataSetChanged();
    }

    public void addResultBeanList(List<AttentFilmBean.ResultBean> resultBeanList) {
        if (resultBeanList != null) {
            this.resultBeanList.addAll(resultBeanList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.attent_film_recycler_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder= (ViewHolder) viewHolder;
        AttentFilmBean.ResultBean resultBean = resultBeanList.get(i);
        holder.filmSimple.setImageURI(Uri.parse(resultBean.getImageUrl()));
        holder.filmContent.setText("简介："+resultBean.getSummary());
        holder.filmName.setText(resultBean.getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(resultBean.getReleaseTime());
        holder.filmTime.setText(format);
    }

    @Override
    public int getItemCount() {
        return resultBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.film_simple)
        SimpleDraweeView filmSimple;
        @BindView(R.id.film_name)
        TextView filmName;
        @BindView(R.id.film_content)
        TextView filmContent;
        @BindView(R.id.film_time)
        TextView filmTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
