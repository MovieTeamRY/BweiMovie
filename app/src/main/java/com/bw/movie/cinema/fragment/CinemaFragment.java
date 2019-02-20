package com.bw.movie.cinema.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
/**
 * 影院页面
 * @author YU
 * @date 2019.01.27
 */
public class CinemaFragment extends BaseFragment implements GeocodeSearch.OnGeocodeSearchListener {
    @BindView(R.id.image_loc)
    ImageButton imageLoc;
    @BindView(R.id.text_loc)
    TextView textLoc;
    @BindView(R.id.image_search)
    ImageButton imageSearch;
    @BindView(R.id.edit_search)
    XEditText editSearch;
    @BindView(R.id.text_search)
    TextView textSearch;
    @BindView(R.id.film_search_linear)
    LinearLayout filmSearchLinear;
    @BindView(R.id.cinema_tab)
    TabLayout cinemaTab;
    @BindView(R.id.cinema_viewpager)
    ViewPager cinemaViewpager;
    Unbinder unbinder;
    private GeocodeSearch geocodeSearch;
    private String dataName;

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
        geocodeSearch = new GeocodeSearch(getContext());
        geocodeSearch.setOnGeocodeSearchListener(this);
        //edittext焦点事件
        editSearch.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为得到焦点时的处理内容
                    //收回软件盘
                    InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    if ( imm.isActive( ) ) {
                        imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

                    }
                }
            }
        });
        imageLoc.setBackgroundResource(R.mipmap.cinema_detail_icon_location_default);
        textLoc.setTextColor(Color.parseColor("#333333"));
        //添加fragment
        final String[] menu=new String[]{getString(R.string.recomm_cinema),getString(R.string.near_cinema)};
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
            String[] str = (String[]) messageBean.getObject();
            textLoc.setText(str[0]);
            AddressUtils.getAddressUtils().StopLocation();
        }else if(messageBean.getId().equals("isChange")){
            editSearch.setVisibility(View.GONE);
            textSearch.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_loc, R.id.image_search, R.id.text_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_loc:
                //收起软键盘
                InputMethodManager imm = ( InputMethodManager ) view.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow( view.getApplicationWindowToken( ) , 0 );
                }
                //AddressUtils.getAddressUtils().getAddressDetail(getActivity());
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
                            dataName = data.getName();
                            GeocodeQuery query = new GeocodeQuery(data.getName(), data.getCode());
                            geocodeSearch.getFromLocationNameAsyn(query);
                        }

                        @Override
                        public void onCancel(){
                            ToastUtil.showToast(getString(R.string.address_cancel_check));
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
        AddressUtils.getAddressUtils().StopLocation();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
        if(geocodeAddressList.size()>0){
            LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
            double latitude = latLonPoint.getLatitude();
            double longitude = latLonPoint.getLongitude();
            textLoc.setText(dataName);
            EventBus.getDefault().postSticky(new MessageBean("address",new String[]{dataName,String.valueOf(latitude),String.valueOf(longitude)}));
        }else{
            ToastUtil.showToast("查无信息");
        }

    }
}
