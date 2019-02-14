package com.bw.movie.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.mine.bean.ObligationBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ObligationBean.ResultBean> list;
    private Context context;

    public CompletedAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<ObligationBean.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ObligationBean.ResultBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.completed_recycler_item_view,viewGroup,false);
        return new ViewHolderCompleted(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderCompleted completed= (ViewHolderCompleted) viewHolder;
        ObligationBean.ResultBean resultBean = list.get(i);
        completed.filmName.setText(resultBean.getMovieName());
        completed.filmOrder.setText("订单号："+resultBean.getOrderId());
        completed.filmCinema.setText("影院："+resultBean.getCinemaName());
        completed.filmHall.setText("影厅："+resultBean.getScreeningHall());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String createTime = sDateFormat.format(resultBean.getCreateTime());
        completed.filmOrderTime.setText("下单时间"+createTime);
        completed.filmTime.setText(resultBean.getBeginTime()+"--"+resultBean.getEndTime());
        completed.filmNum.setText("数量："+resultBean.getAmount()+"张");
        completed.filmTotalPrice.setText("金额："+resultBean.getPrice()*resultBean.getAmount()+"元");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderCompleted extends RecyclerView.ViewHolder{
        @BindView(R.id.film_name)
        TextView filmName;
        @BindView(R.id.film_order)
        TextView filmOrder;
        @BindView(R.id.film_cinema)
        TextView filmCinema;
        @BindView(R.id.film_hall)
        TextView filmHall;
        @BindView(R.id.film_order_time)
        TextView filmOrderTime;
        @BindView(R.id.film_time)
        TextView filmTime;
        @BindView(R.id.film_num)
        TextView filmNum;
        @BindView(R.id.film_total_price)
        TextView filmTotalPrice;
        public ViewHolderCompleted(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
