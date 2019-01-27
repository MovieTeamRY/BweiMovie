package com.bw.movie.cinema.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CinemaFragment extends BaseFragment {
    @BindView(R.id.image_loc)
    ImageButton imageLoc;
    @BindView(R.id.image_search)
    ImageButton imageSearch;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.text_search)
    TextView textSearch;
    @BindView(R.id.film_search_linear)
    LinearLayout filmSearchLinear;
    @BindView(R.id.cinema_tab)
    TabLayout cinemaTab;
    @BindView(R.id.cinema_viewpager)
    ViewPager cinemaViewpager;
    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.cinema_fragment;
    }

    @Override
    protected View getLayoutView() {
        return null;
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void initData() {
        final String[] menu=new String[]{"推荐影院","附近影院"};
        cinemaViewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0){
                    return new NearFragment();
                }else{
                    return new RecommFragment();
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
        cinemaTab.setupWithViewPager(cinemaViewpager);
        for (int i = 0; i < menu.length; i++) {
            TabLayout.Tab tab = cinemaTab.getTabAt(i);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image_loc, R.id.image_search, R.id.text_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_loc:
                //点击搜索地址
                break;
            case R.id.image_search:
                //点击弹出搜索框和搜索文字
                break;
            case R.id.text_search:
                //收回搜索框和搜索文字 判断文字内容 并搜索
                break;
            default:break;
        }
    }
}
