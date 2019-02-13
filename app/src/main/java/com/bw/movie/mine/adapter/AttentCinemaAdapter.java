package com.bw.movie.mine.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.mine.bean.AttentCinemaBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttentCinemaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AttentCinemaBean.ResultBean> resultBeanList;
    private Context context;

    public AttentCinemaAdapter(Context context) {
        this.context = context;
        resultBeanList = new ArrayList<>();
    }

    public void setResultBeanList(List<AttentCinemaBean.ResultBean> resultBeanList) {
        this.resultBeanList = resultBeanList;
        notifyDataSetChanged();
    }

    public void addResultBeanList(List<AttentCinemaBean.ResultBean> resultBeanList) {
        if (resultBeanList != null) {
            this.resultBeanList.addAll(resultBeanList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.attent_cinema_recycler_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder= (ViewHolder) viewHolder;
        AttentCinemaBean.ResultBean resultBean = resultBeanList.get(i);
        holder.cinemaSimple.setImageURI(Uri.parse(resultBean.getLogo()));
        holder.cinemaName.setText(resultBean.getName());
        holder.cinemaAddress.setText(resultBean.getAddress());
    }

    @Override
    public int getItemCount() {
        return resultBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cinema_simple)
        SimpleDraweeView cinemaSimple;
        @BindView(R.id.cinema_name)
        TextView cinemaName;
        @BindView(R.id.cinema_address)
        TextView cinemaAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
