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
import com.bw.movie.utils.IntentUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelaeseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RelaeseBean.ResultBean> list;
    private Context context;

    public RelaeseAdapter(List<RelaeseBean.ResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.home_recycler_flow_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RelaeseBean.ResultBean resultBean = list.get(position%list.size());
        ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.simpleDraweeView.setImageURI(Uri.parse(resultBean.getImageUrl()));
        viewHolder.name.setText(resultBean.getName());
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
        return Integer.MAX_VALUE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.film_simple)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.film_name)
        TextView name;
        @BindView(R.id.film_relative)
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
