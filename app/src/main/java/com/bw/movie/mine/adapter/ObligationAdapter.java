package com.bw.movie.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.mine.bean.ObligationBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ObligationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ObligationBean.ResultBean> list;
    private PopupWindow popupWindow;
    private int pay=1;

    public ObligationAdapter(Context context) {
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.obligation_recycler_item_view, viewGroup, false);
        return new ViewHolderObligation(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ObligationBean.ResultBean resultBean = list.get(i);
        ViewHolderObligation holder= (ViewHolderObligation) viewHolder;
        holder.filmName.setText(resultBean.getMovieName());
        holder.filmOrder.setText("订单号："+resultBean.getOrderId());
        holder.filmCinema.setText("影院："+resultBean.getCinemaName());
        holder.filmHall.setText("影厅："+resultBean.getScreeningHall());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createTime = sDateFormat.format(resultBean.getCreateTime());
        holder.filmTime.setText("时间："+createTime+"\t"+resultBean.getBeginTime()+"-"+resultBean.getEndTime());
        holder.filmNum.setText("数量："+resultBean.getAmount()+"张");
        holder.filmTotalPrice.setText("金额："+resultBean.getPrice()*resultBean.getAmount()+"元");
        holder.filmTopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(context,R.layout.pay_pop_view,null);
                ImageView detail_down= view.findViewById(R.id.detail_down);
                RadioGroup radiogroup= view.findViewById(R.id.radiogroup);
                final TextView confirm_pay= view.findViewById(R.id.confirm_pay);
                popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,550);
                //设置焦点
                popupWindow.setFocusable(true);
                //设置是否可以触摸
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                //关闭
                detail_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.pay_wx:
                                pay=1;
                                confirm_pay.setText("微信支付"+resultBean.getPrice()*resultBean.getAmount()+"元");
                                break;
                            case R.id.pay_alipay:
                                pay=2;
                                confirm_pay.setText("支付宝支付"+resultBean.getPrice()*resultBean.getAmount()+"元");
                                break;
                        }
                    }
                });
                if (pay==1){
                    confirm_pay.setText("微信支付"+resultBean.getPrice()*resultBean.getAmount()+"元");
                }else if(pay==2){
                    confirm_pay.setText("支付宝支付"+resultBean.getPrice()*resultBean.getAmount()+"元");
                }

                //TODO  购买下单
                confirm_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(payClick!=null){
                            payClick.onClick(list.get(i),pay);
                        }
                    }
                });
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderObligation extends RecyclerView.ViewHolder {
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
        public ViewHolderObligation(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public PayClick payClick;
    public void setOnClick(PayClick payClick){
        this.payClick=payClick;
    }
    public interface PayClick{
        void onClick(ObligationBean.ResultBean resultBean, int pay);
    }
}
