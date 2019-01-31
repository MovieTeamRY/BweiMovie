package com.bw.movie.cinema.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.cinema.bean.CinemaCommentBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CinemaCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CinemaCommentBean.ResultBean> list;
    private Context context;

    public CinemaCommentAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<CinemaCommentBean.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void addList(List<CinemaCommentBean.ResultBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.cinema_comment_recycler_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        CinemaCommentBean.ResultBean resultBean = list.get(i);
        ViewHolder holder= (ViewHolder) viewHolder;
        holder.simpleDraweeView.setImageURI(Uri.parse(resultBean.getCommentHeadPic()));
        holder.nikeName.setText(resultBean.getCommentUserName());
        holder.num.setText(resultBean.getGreatNum()+"");
        holder.content.setText(resultBean.getCommentContent());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = sDateFormat.format(resultBean.getCommentTime());
        holder.time.setText(time);
        if(resultBean.getIsGreat()==1){
            holder.imageView.setImageResource(R.mipmap.com_icon_praise_selected);
        }else{
            holder.imageView.setImageResource(R.mipmap.com_icon_praise_default);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.comment_navater)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.comment_nikeName)
        TextView nikeName;
        @BindView(R.id.comment_content)
        TextView content;
        @BindView(R.id.comment_time)
        TextView time;
        @BindView(R.id.comment_num)
        TextView num;
        @BindView(R.id.comment_prise)
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
