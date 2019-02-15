package com.bw.movie.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.mine.bean.MassageBean;
import com.bw.movie.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder> {

    private Context context;
    private List<MassageBean.ResultBean> list;

    public PushAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<MassageBean.ResultBean> data) {
        list.clear();
        if (data != null) {
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addList(List<MassageBean.ResultBean> data) {
        if (data != null) {
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_push_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.message.setText(list.get(i).getTitle());
        viewHolder.time.setText(DateTimeUtil.changeTime(list.get(i).getPushTime()));
        viewHolder.textContent.setText(list.get(i).getContent());
       if (list.get(i).getStatus()==0){
           viewHolder.textNum.setVisibility(View.VISIBLE);
       }else if (list.get(i).getStatus()==1){
           viewHolder.textNum.setVisibility(View.GONE);
       }
       viewHolder.adapter_layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (click!=null){
                   if (list.get(i).getStatus()==0) {
                       click.onClick(i, list.get(i).getId());
                   }
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notif(int position) {
        list.get(position).setStatus(1);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.text_content)
        TextView textContent;
        @BindView(R.id.text_num)
        TextView textNum;
        @BindView(R.id.adapter_layout)
        ConstraintLayout adapter_layout;
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
        void onClick(int i, int id);
    }
}
