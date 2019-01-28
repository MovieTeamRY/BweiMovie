package com.bw.movie.cinema.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.cinema.bean.CinemaFilmBean;
import com.bw.movie.utils.MessageBean;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CinemaFilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CinemaFilmBean.ResultBean> listBeans;
    private Context context;

    public CinemaFilmAdapter(Context context) {
        this.context = context;
        listBeans = new ArrayList<>();
    }

    public void setListBeans(List<CinemaFilmBean.ResultBean> listBeans) {
        this.listBeans = listBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recycler_flow_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        CinemaFilmBean.ResultBean movieListBean = listBeans.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.filmSimple.setImageURI(Uri.parse(movieListBean.getImageUrl()));
        holder.filmName.setText(movieListBean.getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new MessageBean("onitemclick",i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.film_simple)
        SimpleDraweeView filmSimple;
        @BindView(R.id.film_name)
        TextView filmName;
        @BindView(R.id.film_relative)
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
