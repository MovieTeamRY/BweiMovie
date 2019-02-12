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

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.cinema.activity.SeatActivity;
import com.bw.movie.cinema.bean.PayBean;
import com.bw.movie.mine.bean.ObligationBean;
import com.bw.movie.utils.Md5Utils;
import com.bw.movie.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ObligationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ObligationBean.ResultBean> list;
    private final int TYPEONE=1;
    private final int TYPETWO=2;
    private final int status;
    private PopupWindow popupWindow;
    private int pay;
    private double totalPrice=0.0;

    public ObligationAdapter(int status,Context context) {
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

    @Override
    public int getItemViewType(int position) {
        int status = list.get(position).getStatus();
        if(this.status==TYPEONE&&status==TYPEONE){
            return TYPEONE;
        }else if(this.status==TYPETWO&&status==TYPETWO){
            return TYPETWO;
        }else{
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int itemViewType = getItemViewType(i);
        View view;
        if(itemViewType==TYPEONE){
            view = LayoutInflater.from(context).inflate(R.layout.obligation_recycler_item_view, viewGroup, false);
            return new ViewHolderObligation(view);
        }else if(itemViewType==TYPETWO){
            view=LayoutInflater.from(context).inflate(R.layout.completed_recycler_item_view,viewGroup,false);
            return new ViewHolderCompleted(view);
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ObligationBean.ResultBean resultBean = list.get(i);
        if(getItemViewType(i)==1){
            ViewHolderObligation holder= (ViewHolderObligation) viewHolder;
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
                                    pay=0;
                                    confirm_pay.setText("微信支付"+totalPrice+"元");
                                    break;
                                case R.id.pay_alipay:
                                    pay=1;
                                    confirm_pay.setText("支付宝支付"+totalPrice+"元");
                                    break;
                            }
                        }
                    });
                    if (pay==0){
                        confirm_pay.setText("微信支付"+totalPrice+"元");
                    }else if(pay==1){
                        confirm_pay.setText("支付宝支付"+totalPrice+"元");
                    }

                    //TODO  购买下单
                    confirm_pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(payClick!=null){
                                payClick.onClick(list.get(i));
                            }
                        }
                    });
                    popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }
            });
        }else if(getItemViewType(i)==2){
            ViewHolderCompleted completed= (ViewHolderCompleted) viewHolder;
            completed.filmName.setText(resultBean.getMovieName());
            completed.filmOrder.setText("订单号："+resultBean.getOrderId());
            completed.filmCinema.setText("影院："+resultBean.getCinemaName());
            completed.filmHall.setText("影厅："+resultBean.getScreeningHall());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String createTime = sDateFormat.format(resultBean.getCreateTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            String beginTime = timeFormat.format(resultBean.getBeginTime());
            String endTime = timeFormat.format(resultBean.getEndTime());
            completed.filmOrderTime.setText("下单时间"+createTime);
            completed.filmTime.setText(beginTime+"--"+endTime);
            completed.filmNum.setText("数量："+resultBean.getAmount()+"张");
            completed.filmTotalPrice.setText("金额："+resultBean.getPrice()*resultBean.getAmount()+"元");
        }
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
        }
    }

    public PayClick payClick;

    public void setOnClick(PayClick payClick){
        this.payClick=payClick;
    }

    public interface PayClick{
        void onClick(ObligationBean.ResultBean resultBean);
    }
}
