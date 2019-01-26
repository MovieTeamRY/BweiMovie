package com.bw.movie.film.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.film.activity.FilmDetailsActivity;
import com.bw.movie.film.bean.RelaeseBean;
import com.bw.movie.film.bean.ScreenFilmBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreenFilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ScreenFilmBean.ResultBean> list;
    private Context context;

    public ScreenFilmAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<ScreenFilmBean.ResultBean> data) {
        list.clear();
        if (data!=null) {
            list=data;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.film_recycler_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        final ScreenFilmBean.ResultBean resultBean = list.get(position);
        viewHolder.name.setText(resultBean.getName());
        viewHolder.simpleDraweeView.setImageURI(Uri.parse(resultBean.getImageUrl()));
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FilmDetailsActivity.class);
                intent.putExtra("id",resultBean.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.film_name)
        TextView name;
        @BindView(R.id.image_simple)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.film_relative)
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
