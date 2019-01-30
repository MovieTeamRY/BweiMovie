package com.bw.movie.cinema.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.cinema.activity.SeatActivity;
import com.bw.movie.cinema.bean.FilmSchedulBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CinemaSchedulAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FilmSchedulBean.ResultBean> list;
    private Context context;

    public CinemaSchedulAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<FilmSchedulBean.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cinema_film_scheduling_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final FilmSchedulBean.ResultBean resultBean = list.get(i);
        ViewHolder holder= (ViewHolder) viewHolder;
        if(resultBean.getStatus()==2){
            holder.schedulingLoc.setText("影片已过期");
        }else if(resultBean.getStatus()==1){
            holder.schedulingLoc.setText(resultBean.getScreeningHall());
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click!=null){
                        click.onClick(list.get(i));
                    }
//                    Intent intent=new Intent(context,SeatActivity.class);
//                    intent.putExtra("scheduleId",resultBean.getId());
//                    context.startActivity(intent);
                }
            });
            holder.schedulingNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (click!=null){
                        click.onClick(list.get(i));
                    }
                    /*Intent intent=new Intent(context,SeatActivity.class);
                    intent.putExtra("scheduleId",resultBean.getId());
                    context.startActivity(intent);*/
                }
            });
        }
        holder.schedulingLanguage.setText(resultBean.getDuration());
        holder.startTime.setText(resultBean.getBeginTime());
        holder.endTime.setText(resultBean.getEndTime());
        double price = resultBean.getPrice();
        String prices= String.valueOf(price);
        SpannableString spannableString = new SpannableString(prices);
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.0f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(0.5f);
        spannableString.setSpan(sizeSpan01, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.schedulingPrice.setText(spannableString);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.scheduling_loc)
        TextView schedulingLoc;
        @BindView(R.id.scheduling_language)
        TextView schedulingLanguage;
        @BindView(R.id.start_time)
        TextView startTime;
        @BindView(R.id.end_time)
        TextView endTime;
        @BindView(R.id.scheduling_next)
        ImageView schedulingNext;
        @BindView(R.id.scheduling_price)
        TextView schedulingPrice;
        @BindView(R.id.scheduling_relative)
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    Click click;
    public void setOnClickListener(Click click){
        this.click=click;
    }
    public interface Click{
        void onClick(FilmSchedulBean.ResultBean resultBean);
    }
}
