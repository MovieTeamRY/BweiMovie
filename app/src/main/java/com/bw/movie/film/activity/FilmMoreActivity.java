package com.bw.movie.film.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.film.fragment.HotFilmFragment;
import com.bw.movie.film.fragment.RelaeseFilmFragment;
import com.bw.movie.film.fragment.ScreenFilmFragment;
import com.bw.movie.utils.AddressUtils;
import com.bw.movie.utils.AnimatorUtils;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;
import com.xw.repo.XEditText;
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

public class FilmMoreActivity extends BaseActivty {

    @BindView(R.id.film_tab)
    TabLayout filmTab;
    @BindView(R.id.film_viewpager)
    ViewPager filmViewpager;
    @BindView(R.id.image_loc)
    ImageButton imageLoc;
    @BindView(R.id.return_image)
    ImageView return_image;
    @BindView(R.id.text_loc)
    TextView textLoc;
    @BindView(R.id.edit_search)
    XEditText edit_search;
    @BindView(R.id.image_search)
    ImageButton image_search;
    @BindView(R.id.text_search)
    TextView text_search;
    @BindView(R.id.film_search_linear)
    LinearLayout film_search_linear;
    private Unbinder bind;
    private static final int COUNT_ZERO = 0;
    private static final int COUNT_ONE = 1;
    private static final int COUNT_TWO = 2;
    private HotFilmFragment hotFilmFragment;
    private RelaeseFilmFragment relaeseFilmFragment;
    private ScreenFilmFragment screenFilmFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_film_more;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {
        //edittext焦点事件
        edit_search.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为得到焦点时的处理内容
                    //收回软件盘
                    Log.i("TAG","失去焦点");
                    //收起软键盘
                    InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    if ( imm.isActive( ) ) {
                        imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

                    }
                }
            }
        });
        imageLoc.setBackgroundResource(R.mipmap.cinema_detail_icon_location_default);
        textLoc.setTextColor(Color.parseColor("#333333"));
        final String[] menu = new String[]{"热门电影", "正在热映", "即将上映"};
        //创建fragment
        hotFilmFragment = new HotFilmFragment();
        relaeseFilmFragment = new RelaeseFilmFragment();
        screenFilmFragment = new ScreenFilmFragment();
        //设置热映和正在上映关注联动
        hotFilmFragment.setListHotCallBack(new HotFilmFragment.ListHotCallBack() {
            @Override
            public void callBack() {
                relaeseFilmFragment.onResume();
            }
        });
        relaeseFilmFragment.setListHotCallBack(new RelaeseFilmFragment.ListHotCallBack() {
            @Override
            public void callBack() {
                hotFilmFragment.onResume();
            }
        });
        filmViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == COUNT_ZERO) {
                    return hotFilmFragment;
                } else if (i == COUNT_ONE) {
                    return relaeseFilmFragment;
                } else if (i == COUNT_TWO) {
                    return screenFilmFragment;
                }
                return null;
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
        filmTab.setupWithViewPager(filmViewpager);
        for (int i = 0; i < menu.length; i++) {
            TabLayout.Tab tab = filmTab.getTabAt(i);
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
        //接收Ienten传来的值
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        int type = bundle.getInt("type");
        Log.i("TAG", type + "");
        //判断type值，切换对应的fragment
        if (type == COUNT_ZERO) {
            filmViewpager.setCurrentItem(COUNT_ZERO);
        } else if (type == COUNT_ONE) {
            filmViewpager.setCurrentItem(COUNT_ONE);
        } else if (type == COUNT_TWO) {
            filmViewpager.setCurrentItem(COUNT_TWO);
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
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void getAddress(MessageBean messageBean){
        if(messageBean.getId().equals("address")){
            textLoc.setText(String.valueOf(messageBean.getObject()));
            AddressUtils.getAddressUtils().StopLocation();
        }else if(messageBean.getId().equals("isChange")){
            edit_search.setVisibility(View.GONE);
            text_search.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.return_image, R.id.image_loc,R.id.image_search,R.id.text_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.return_image:
                finish();
                break;
            case R.id.image_loc:
                //收起软键盘
                InputMethodManager imm = ( InputMethodManager ) view.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow( view.getApplicationWindowToken( ) , 0 );

                }
                //TODO 点击定位
                CityPicker.from(FilmMoreActivity.this)
                        //activity或者fragment
                        .enableAnimation(true)
                        //自定义动画
                        .setLocatedCity(new LocatedCity(textLoc.getText().toString(), "", ""))
                        //APP自身已定位的城市，传null会自动定位（默认）
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, City data) {
                                EventBus.getDefault().postSticky(new MessageBean("address", data.getName()));
                                textLoc.setText(data.getName());
                                ToastUtil.showToast(data.getName());
                            }
                            @Override
                            public void onCancel() {
                                ToastUtil.showToast("取消选择");
                            }

                            @Override
                            public void onLocate() {
                            }
                        })
                        .show();
                break;
            case R.id.text_search:
                AnimatorUtils.translationAnimator(film_search_linear,"translationX",0f,2000,true);
                break;
            case R.id.image_search:
                edit_search.setVisibility(View.VISIBLE);
                text_search.setVisibility(View.VISIBLE);
                AnimatorUtils.translationAnimator(film_search_linear,"translationX",-470f,2000,false);
                break;
            default:break;
        }

    }

}
