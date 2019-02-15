package com.bw.movie.purchase.adapter;

import android.content.Context;
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
import com.bw.movie.purchase.bean.FilmSchedulingBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchseAdapter extends RecyclerView.Adapter<PurchseAdapter.ViewHolder> {

    private Context context;
    private List<FilmSchedulingBean.ResultBean> list;

    public PurchseAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<FilmSchedulingBean.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_purchse_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Uri uri = Uri.parse(list.get(i).getLogo());
        viewHolder.cinemaSimple.setImageURI(uri);
        viewHolder.cinemaName.setText(list.get(i).getName());
        viewHolder.cinemaAddress.setText(list.get(i).getAddress());
        viewHolder.cinemaLength.setText(list.get(i).getDistance()+"km");
       if (list.get(i).getFollowCinema()==0){
           viewHolder.cinema_prise.setBackgroundResource(R.mipmap.com_icon_heart_default);
       }else if (list.get(i).getFollowCinema()==1){
           viewHolder.cinema_prise.setBackgroundResource(R.mipmap.com_icon_heart_selected);
       }
       //条目点击事件
        viewHolder.cinemaRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (click!=null){
                   click.onClick(list.get(i));
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cinema_simple)
        SimpleDraweeView cinemaSimple;
        @BindView(R.id.cinema_name)
        TextView cinemaName;
        @BindView(R.id.cinema_address)
        TextView cinemaAddress;
        @BindView(R.id.cinema_length)
        TextView cinemaLength;
        @BindView(R.id.cinema_relative)
        RelativeLayout cinemaRelative;
        @BindView(R.id.cinema_prise)
        ImageView cinema_prise;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    Click click;
    public void setOnClickListener(Click click){
        this.click=click;
    }
    public interface Click {
       void onClick(FilmSchedulingBean.ResultBean resultBean);
    }
}
