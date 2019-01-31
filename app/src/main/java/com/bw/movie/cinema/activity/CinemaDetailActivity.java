package com.bw.movie.cinema.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.adapter.CinemaFilmAdapter;
import com.bw.movie.cinema.adapter.CinemaSchedulAdapter;
import com.bw.movie.cinema.bean.CinemaFilmBean;
import com.bw.movie.cinema.bean.CinemaInfoBean;
import com.bw.movie.cinema.bean.FilmSchedulBean;
import com.bw.movie.cinema.fragment.CinemaCommentFragment;
import com.bw.movie.cinema.fragment.CinemaDetailFragment;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

public class CinemaDetailActivity extends BaseActivty {

    @BindView(R.id.cinema_logo)
    SimpleDraweeView cinemaLogo;
    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.cinema_navigation)
    ImageView cinemaNavigation;
    @BindView(R.id.cinema_film)
    RecyclerCoverFlow cinemaFilm;
    @BindView(R.id.cinema_film_scheduling)
    RecyclerView cinemaFilmScheduling;
    @BindView(R.id.cinema_group)
    RadioGroup cinemaGroup;
    private Unbinder bind;
    private CinemaInfoBean.ResultBean result;
    private CinemaSchedulAdapter cinemaSchedulAdapter;
    private int id;
    private List<CinemaFilmBean.ResultBean> movieList;
    private CinemaFilmAdapter cinemaFilmAdapter;
    private PopupWindow detailWindow;
    private View popView;
    private Bundle bundle;
    private int i;
    private ArrayList<String> list;
    private CinemaDetailFragment cinemaDetailFragment;
    private CinemaCommentFragment cinemaCommentFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cinema_detail;
    }
    /**
     *寻找pop中viewpager的id
     *@author YU
     *@time 2019/1/30 0030 9:45
     */
    @Override
    public <T extends View> T findViewById(int id) {
        if (id == R.id.detail_viewpager && popView !=null){
            return popView.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        popView = LayoutInflater.from(this).inflate(R.layout.cinema_detail_pop_view,null,false);
        detailWindow = new PopupWindow(popView,ViewGroup.LayoutParams.MATCH_PARENT,1300);
        detailWindow.setFocusable(true);
        detailWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        TabLayout tabLayout=popView.findViewById(R.id.detail_tab);

        ViewPager viewPager=popView.findViewById(R.id.detail_viewpager);
        ImageView downImage=popView.findViewById(R.id.detail_down);
        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailWindow.isShowing()){
                    detailWindow.dismiss();
                }
            }
        });
        final String[] menu=new String[]{"详情","评论"};
        cinemaDetailFragment = new CinemaDetailFragment();
        cinemaCommentFragment = new CinemaCommentFragment();
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0){
                    return cinemaDetailFragment;
                }else{
                    return cinemaCommentFragment;
                }
            }

            @Override
            public int getCount() {
                return menu.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return menu[position];
            }
        });
        //tablayout和viewpager关联
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < menu.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            //获得每一个tab
            tab.setCustomView(R.layout.cinema_detail_pop_tab_text);
            //给每一个tab设置view
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.cinema_tab_text).setSelected(true);
                //第一个tab被选中
            }
            TextView textView = tab.getCustomView().findViewById(R.id.cinema_tab_text);
            textView.setText(menu[i]);
            //设置tab上的文字
        }
    }

    @Override
    protected void initData() {
        bundle = new Bundle();
        list = new ArrayList<>();
        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);

        cinemaFilm.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                i=position;
                int movieId = movieList.get(position).getId();
                if(cinemaGroup.getChildCount()>0){
                    RadioButton radioButton= (RadioButton) cinemaGroup.getChildAt(position);
                    radioButton.setChecked(true);
                    onGetRequest(String.format(Apis.URL_FIND_MOVIE_SCHEDULE_LIST,id,movieId),FilmSchedulBean.class);
                }
            }
        });

        LinearLayoutManager schedulLayoutManager=new LinearLayoutManager(this);
        cinemaFilmScheduling.setLayoutManager(schedulLayoutManager);
        cinemaSchedulAdapter = new CinemaSchedulAdapter(this);
        cinemaFilmScheduling.setAdapter(cinemaSchedulAdapter);
        //点击事件
        cinemaSchedulAdapter.setOnClickListener(new CinemaSchedulAdapter.Click() {
            @Override
            public void onClick(FilmSchedulBean.ResultBean resultBean) {
                //如果list有第三位数据，则每次请求删除第三位
                if (list.size()==3){
                    list.remove(2);
                }
                list.add(movieList.get(i).getName());
                bundle.putParcelable("resultBean",resultBean);
                bundle.putStringArrayList("list",list);
                IntentUtils.getInstence().intent(CinemaDetailActivity.this,SeatActivity.class,bundle);
            }
        });

        cinemaFilmAdapter = new CinemaFilmAdapter(this);
        cinemaFilm.setAdapter(cinemaFilmAdapter);

        onGetRequest(String.format(Apis.URL_FIND_CINEMA_INFO_GET, id),CinemaInfoBean.class);
        onGetRequest(String.format(Apis.URL_FIND_MOVIE_LIST_BY_CINEMAID_GET, id),CinemaFilmBean.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getFlowId(MessageBean messageBean){
        if(messageBean.getId().equals("onitemclick")){
            i=(Integer) messageBean.getObject();
            cinemaFilm.smoothScrollToPosition((Integer) messageBean.getObject());
            RadioButton radioButton= (RadioButton) cinemaGroup.getChildAt((Integer) messageBean.getObject());
            radioButton.setChecked(true);
        }
    }

    @OnClick({R.id.cinema_film_return,R.id.cinema_name,R.id.cinema_logo,R.id.cinema_address})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cinema_film_return:
                finish();
                break;
            case R.id.cinema_name:
                if(!detailWindow.isShowing()){
                    /*cinemaDetailFragment.setCinemaId(id);
                    cinemaCommentFragment.setCinemaId(id);*/
                    detailWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
                }
                break;
            case R.id.cinema_address:
                if(!detailWindow.isShowing()){

                    detailWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
                }
                break;
            case R.id.cinema_logo:
                if(!detailWindow.isShowing()){
                   /* cinemaDetailFragment.setCinemaId(id);
                    cinemaCommentFragment.setCinemaId(id);*/
                    detailWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
                }
                break;
            case R.id.cinema_navigation:
                if(!detailWindow.isShowing()){
                   /* cinemaDetailFragment.setCinemaId(id);
                    cinemaCommentFragment.setCinemaId(id);*/
                    detailWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
                }
                break;
            default:break;
        }
    }

    @Override
    protected void onNetSuccess(Object data) {
        if(data instanceof CinemaInfoBean){
            CinemaInfoBean cinemaInfoBean= (CinemaInfoBean) data;
            result = cinemaInfoBean.getResult();
            cinemaLogo.setImageURI(Uri.parse(result.getLogo()));
            cinemaName.setText(result.getName());
            cinemaAddress.setText(result.getAddress());
            //存入集合传递到选座页面
            list.add(result.getName());
            list.add(result.getAddress());
            list.add("");
        }else if(data instanceof CinemaFilmBean){
            CinemaFilmBean filmBean= (CinemaFilmBean) data;
            if(filmBean.getResult()!=null&&filmBean.getResult().size()>0){
                movieList = filmBean.getResult();
                if(movieList.size()>0){
                    cinemaFilm.setVisibility(View.VISIBLE);
                    cinemaFilmAdapter.setListBeans(movieList);
                    cinemaGroup.removeAllViews();
                    int width = cinemaGroup.getWidth();
                    int childWidth = width / movieList.size();
                    for (int i=0;i<movieList.size();i++){
                        RadioButton radioButton=new RadioButton(this);
                        radioButton.setWidth(childWidth);
                        radioButton.setBackgroundResource(R.drawable.home_film_divide_selected);
                        Bitmap a=null;
                        radioButton.setButtonDrawable(new BitmapDrawable(a));
                        radioButton.setChecked(false);
                        cinemaGroup.addView(radioButton);
                    }
                    cinemaFilm.smoothScrollToPosition(movieList.size()/2);
                    RadioButton radioButton= (RadioButton) cinemaGroup.getChildAt(movieList.size()/2);
                    radioButton.setChecked(true);
                }else{
                    ToastUtil.showToast("没有影片信息");
                }
            }
        }else if(data instanceof FilmSchedulBean){
            FilmSchedulBean schedulBean= (FilmSchedulBean) data;
            if(schedulBean.getResult().size()>0){
                cinemaSchedulAdapter.setList(schedulBean.getResult());
            }
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        EventBus.getDefault().unregister(this);
    }
}
