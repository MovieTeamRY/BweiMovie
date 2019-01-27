package com.bw.movie.film.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.film.bean.RevirwBean;
import com.bw.movie.utils.DateTimeUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RevirwAdapter extends RecyclerView.Adapter<RevirwAdapter.ViewHolder> {

    private List<RevirwBean.ResultBean> list;
    private Context context;

    public RevirwAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<RevirwBean.ResultBean> data) {
        list.clear();
        if (data!=null){
            list.addAll(data);
        }
        notifyDataSetChanged();
    }
    public void addList(List<RevirwBean.ResultBean> data) {
        if (data!=null){
            list.addAll(data);
        }
        notifyDataSetChanged();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_adapter_revirw_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Uri uri =Uri.parse(list.get(position).getCommentHeadPic());
        holder.imageHeader.setImageURI(uri);
        holder.name.setText(list.get(position).getCommentUserName());
        holder.contentText.setText(list.get(position).getCommentContent());
        holder.dateText.setText(DateTimeUtil.changeTime(list.get(position).getCommentTime()));
        holder.pariseNum.setText(list.get(position).getGreatNum()+"");
        holder.commentNum.setText(list.get(position).getReplyNum()+"");
        if (list.get(position).getIsGreat()==0){
            holder.praise.setBackgroundResource(R.mipmap.com_icon_praise_default);
        }else if(list.get(position).getIsGreat()==1){
            holder.praise.setBackgroundResource(R.mipmap.com_icon_praise_selected);
        }
        //点赞
        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLucky!=null) {
                    mLucky.onLucky(list.get(position).getCommentId(),position);
                }
            }
        });
        //查看评论回复
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.film_comment_recyclerview.setVisibility(View.VISIBLE);
                if (click!=null){
                    click.onClick(list.get(position).getCommentId(),holder.film_comment_recyclerview);
                }
            }
        });
    }
    /**
     *设置点赞
     *@author Administrator
     *@time 2019/1/1 0001 18:14
     */
    public void addWhetherGreat(int position) {
        list.get(position).setIsGreat(1);
        list.get(position).setGreatNum(list.get(position).getGreatNum()+1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_header)
        SimpleDraweeView imageHeader;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.content_text)
        TextView contentText;
        @BindView(R.id.date_text)
        TextView dateText;
        @BindView(R.id.praise)
        ImageView praise;
        @BindView(R.id.parise_num)
        TextView pariseNum;
        @BindView(R.id.comment)
        ImageView comment;
        @BindView(R.id.comment_num)
        TextView commentNum;
        @BindView(R.id.film_comment_recyclerview)
        XRecyclerView film_comment_recyclerview;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    /**点赞*/
    private Lucky mLucky;
    public void setLucky(Lucky mLucky) {
        this.mLucky = mLucky;
    }
    public interface Lucky {
        void onLucky(int commentId,int position);
    }
    /**点击查看评论回复*/
    private Click click;
    public void setClick(Click click) {
        this.click = click;
    }
    public interface Click {
        void onClick(int commentId,XRecyclerView film_comment_recyclerview);
    }
}
