package com.bw.movie.film.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.film.bean.MovieFilmBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieHotAdapter extends RecyclerView.Adapter<MovieHotAdapter.ViewHolder> {

    private Context mContext;
    private List<MovieFilmBean.ResultBean> mList;
    private final int SUCCESS = 1;
    private final int CANCEL = 2;

    public MovieHotAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<MovieFilmBean.ResultBean> data) {
        mList.clear();
        if (data != null) {
            mList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addmList(List<MovieFilmBean.ResultBean> data) {
        if (data != null) {
            mList.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_movie_hot_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final MovieFilmBean.ResultBean resultBean = mList.get(i);
        Uri uri = Uri.parse(resultBean.getImageUrl());
        viewHolder.image.setImageURI(uri);
        viewHolder.name.setText(resultBean.getName());
        viewHolder.contentText.setText(resultBean.getSummary());
        //取出状态值判断是否已关注
        if(resultBean.getFollowMovie().equals("1")){
            viewHolder.attentionImage.setChecked(true);
        }else{
            viewHolder.attentionImage.setChecked(false);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotCallBack.skipDetails(resultBean.getId());
            }
        });
        viewHolder.attentionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.attentionImage.isChecked()){
                    if(resultBean.getFollowMovie().equals("2")){
                        hotCallBack.hotCallBack(resultBean.getId(),i,true);
                    }
                }else{
                    if(resultBean.getFollowMovie().equals("1")){
                        hotCallBack.hotCallBack(resultBean.getId(),i,false);
                    }
                }
            }
        });
    }
    public void setAttentionScccess(int i){
        mList.get(i).setFollowMovie(String.valueOf(SUCCESS));
        notifyDataSetChanged();
    }
    public void setCancelAttention(int i){
        mList.get(i).setFollowMovie(String.valueOf(CANCEL));
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.attentionImage)
        CheckBox attentionImage;
        @BindView(R.id.contentText)
        TextView contentText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    MovieListHotCallBack hotCallBack;
    public interface MovieListHotCallBack{
        void hotCallBack(int id,int i,boolean b);
        void skipDetails(int id);
    }
    public void setHotCallBack(MovieListHotCallBack hotCallBack) {
        this.hotCallBack = hotCallBack;
    }
}
