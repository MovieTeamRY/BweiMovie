package com.bw.movie.cinema.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.utils.AnimatorUtils;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocatedCity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
/**
 * 影院页面
 * @author YU
 * @date 2019.01.27
 */
public class CinemaFragment extends BaseFragment {
    @BindView(R.id.image_loc)
    ImageButton imageLoc;
    @BindView(R.id.text_loc)
    TextView textLoc;
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
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void initData() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        imageLoc.setBackgroundResource(R.mipmap.cinema_detail_icon_location_default);
        textLoc.setTextColor(Color.parseColor("#333333"));
        //添加fragment
        final String[] menu=new String[]{"推荐影院","附近影院"};
        cinemaViewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0){
                    return new RecommFragment();
                }else{
                    return new NearFragment();
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
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void getAddress(MessageBean messageBean){
        if(messageBean.getId().equals("address")){
            textLoc.setText(String.valueOf(messageBean.getObject()));
        }else if(messageBean.getId().equals("isChange")){
            editSearch.setVisibility(View.GONE);
            textSearch.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_loc, R.id.image_search, R.id.text_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_loc:
                //点击搜索地址
                CityPicker.from(getActivity())
                    //activity或者fragment
                    .enableAnimation(true)
                    //自定义动画
                    .setLocatedCity(new LocatedCity(textLoc.getText().toString(), "", ""))
                    //APP自身已定位的城市，传null会自动定位（默认）
                    .setOnPickListener(new OnPickListener() {
                        @Override
                        public void onPick(int position, City data) {
                            textLoc.setText(data.getName());
                            EventBus.getDefault().postSticky(new MessageBean("address",data.getName()));
                            ToastUtil.showToast(data.getName());
                        }

                        @Override
                        public void onCancel(){
                            ToastUtil.showToast("取消选择");
                        }

                        @Override
                        public void onLocate() {
                        }
                    })
                    .show();
                break;
            case R.id.image_search:
                //点击弹出搜索框和搜索文字
                editSearch.setVisibility(View.VISIBLE);
                textSearch.setVisibility(View.VISIBLE);
                AnimatorUtils.translationAnimator(filmSearchLinear,"translationX",-470f,2000,false);
                break;
            case R.id.text_search:
                //收回搜索框和搜索文字 判断文字内容 并搜索
                editSearch.setVisibility(View.VISIBLE);
                textSearch.setVisibility(View.VISIBLE);
                AnimatorUtils.translationAnimator(filmSearchLinear,"translationX",0f,2000,true);
                break;
            default:break;
        }
    }

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
