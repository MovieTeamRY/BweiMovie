package com.bw.movie.mine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.adapter.ObligationAdapter;
import com.bw.movie.mine.bean.ObligationBean;
import com.bw.movie.mine.fragment.CompletedFragment;
import com.bw.movie.mine.fragment.CompletedFragment_ViewBinding;
import com.bw.movie.mine.fragment.ObligationFragment;
import com.bw.movie.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RecordActivity extends BaseActivty {
    @BindView(R.id.user_tab)
    TabLayout userTab;
    @BindView(R.id.user_viewpager)
    ViewPager userViewpager;
    private Unbinder bind;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        final String[] menu=new String[]{"待付款","已完成"};
        userViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0){
                    return new ObligationFragment();
                }else{
                    return new CompletedFragment();
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
        userTab.setupWithViewPager(userViewpager);
        for (int i = 0; i < menu.length; i++) {
            TabLayout.Tab tab = userTab.getTabAt(i);
            //获得每一个tab
            tab.setCustomView(R.layout.cinema_tab_layout);
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
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG", error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

}
