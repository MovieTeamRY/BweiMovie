package com.bw.movie.cinema.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.cinema.activity.CinemaDetailActivity;
import com.bw.movie.cinema.bean.RecommCinemaBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecommCinemaBean.Result> list;
    private Context context;

    public RecommAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<RecommCinemaBean.Result> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.cinema_recycler_item_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RecommCinemaBean.Result result = list.get(i);
        ViewHolder holder= (ViewHolder) viewHolder;
        holder.simpleDraweeView.setImageURI(Uri.parse(result.getLogo()));
        holder.address.setText(result.getAddress());
        holder.length.setText(result.getDistance()+"km");
        holder.name.setText(result.getName());
        if(result.getFollowCinema()==1){
            holder.prise.setImageResource(R.mipmap.com_icon_heart_selected);
        }else{
            holder.prise.setImageResource(R.mipmap.com_icon_heart_default);
        }
        Intent intent=new Intent(context,CinemaDetailActivity.class);
        intent.putExtra("id",result.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cinema_simple)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.cinema_name)
        TextView name;
        @BindView(R.id.cinema_address)
        TextView address;
        @BindView(R.id.cinema_length)
        TextView length;
        @BindView(R.id.cinema_prise)
        ImageView prise;
        @BindView(R.id.cinema_relative)
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
