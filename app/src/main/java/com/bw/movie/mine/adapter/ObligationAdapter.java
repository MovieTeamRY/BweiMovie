package com.bw.movie.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.mine.bean.ObligationBean;
import com.bw.movie.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ObligationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ObligationBean.ResultBean> list;
    private int status;

    public ObligationAdapter(int status, Context context) {
        this.context = context;
        this.status=status;
        list = new ArrayList<>();
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
        if(status==1){
            View view = LayoutInflater.from(context).inflate(R.layout.obligation_recycler_item_view, viewGroup, false);
            return new ViewHolder(view);
        }else if(status==2){
            return null;
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder= (ViewHolder) viewHolder;
        ObligationBean.ResultBean resultBean = list.get(i);
        if(status==1){
            holder.filmName.setText(resultBean.getMovieName());
            holder.filmOrder.setText("订单号："+resultBean.getOrderId());
            holder.filmCinema.setText("影院："+resultBean.getCinemaName());
            holder.filmHall.setText("影厅："+resultBean.getScreeningHall());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String createTime = sDateFormat.format(resultBean.getCreateTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            String beginTime = timeFormat.format(resultBean.getBeginTime());
            String endTime = timeFormat.format(resultBean.getEndTime());
            holder.filmTime.setText("时间："+createTime+"\t"+beginTime+"-"+endTime);
            holder.filmNum.setText("数量："+resultBean.getAmount()+"张");
            holder.filmTotalPrice.setText("金额："+resultBean.getPrice()*resultBean.getAmount()+"元");
            holder.filmTopay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast("去付款");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.film_name)
        TextView filmName;
        @BindView(R.id.film_order)
        TextView filmOrder;
        @BindView(R.id.film_cinema)
        TextView filmCinema;
        @BindView(R.id.film_hall)
        TextView filmHall;
        @BindView(R.id.film_time)
        TextView filmTime;
        @BindView(R.id.film_num)
        TextView filmNum;
        @BindView(R.id.film_total_price)
        TextView filmTotalPrice;
        @BindView(R.id.film_topay)
        Button filmTopay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
