package com.bw.movie.film.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.film.activity.FilmMoreActivity;
import com.bw.movie.film.adapter.HotFilmAdapter;
import com.bw.movie.film.adapter.RelaeseAdapter;
import com.bw.movie.film.adapter.RelaeseFilmAdapter;
import com.bw.movie.film.adapter.ScreenFilmAdapter;
import com.bw.movie.film.bean.HotFilmBean;
import com.bw.movie.film.bean.RelaeseBean;
import com.bw.movie.film.bean.ScreenFilmBean;
import com.bw.movie.utils.AddressUtils;
import com.bw.movie.utils.AnimatorUtils;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.MessageBean;
import com.bw.movie.utils.ToastUtil;
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
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

public class FilmFragment extends BaseFragment {
    private static final int COUNT_ZERO =0 ;
    private static final int COUNT_ONE =1 ;
    private static final int COUNT_TWO =2 ;
    @BindView(R.id.film_group)
    RadioGroup filmGroup;

    @BindView(R.id.recycler_flow)
    RecyclerCoverFlow recyclerFlow;
    Unbinder unbinder;
    @BindView(R.id.image_loc)
    ImageButton filmLoc;
    @BindView(R.id.text_loc)
    TextView textLoc;
    @BindView(R.id.image_search)
    ImageButton imageSearch;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.text_search)
    TextView textSearch;
    @BindView(R.id.film_search_linear)
    LinearLayout searchLinear;

    //热门电影
    @BindView(R.id.hot_film_more)
    ImageButton hotFilmMore;
    @BindView(R.id.hot_film_recycler)
    RecyclerView hotFilmRecycler;
    //正在上映
    @BindView(R.id.relaese_film_more)
    ImageButton relaeseFilmMore;
    @BindView(R.id.relaese_film_recycler)
    RecyclerView relaeseFilmRecycler;
    //即将上映
    @BindView(R.id.screen_film_more)
    ImageButton screenFilmMore;
    @BindView(R.id.screen_film_recycler)
    RecyclerView screenFilmRecycler;
    private int current;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            recyclerFlow.smoothScrollToPosition(current);
            current++;
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    };
    private HotFilmAdapter filmAdapter;
    private RelaeseFilmAdapter relaeseFilmAdapter;
    private ScreenFilmAdapter screenFilmAdapter;
    private List<RelaeseBean.ResultBean> result;
    private Bundle bundle;

    @Override
    protected int getLayoutResId() {
        return R.layout.file_fragment;
    }

    @Override
    protected void initData() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        LinearLayoutManager hotFilmManager=new LinearLayoutManager(getContext());
        hotFilmManager.setOrientation(OrientationHelper.HORIZONTAL);
        hotFilmRecycler.setLayoutManager(hotFilmManager);
        filmAdapter = new HotFilmAdapter(getContext());
        hotFilmRecycler.setAdapter(filmAdapter);

        LinearLayoutManager relaeseFilmManager=new LinearLayoutManager(getContext());
        relaeseFilmManager.setOrientation(OrientationHelper.HORIZONTAL);
        relaeseFilmRecycler.setLayoutManager(relaeseFilmManager);
        relaeseFilmAdapter = new RelaeseFilmAdapter(getContext());
        relaeseFilmRecycler.setAdapter(relaeseFilmAdapter);

        LinearLayoutManager screenFilmManager=new LinearLayoutManager(getContext());
        screenFilmManager.setOrientation(OrientationHelper.HORIZONTAL);
        screenFilmRecycler.setLayoutManager(screenFilmManager);
        screenFilmAdapter = new ScreenFilmAdapter(getContext());
        screenFilmRecycler.setAdapter(screenFilmAdapter);

        onGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET,1),HotFilmBean.class);
        onGetRequest(String.format(Apis.URL_FIND_RELEASE_MOVIE_LIST_GET, 1), RelaeseBean.class);
        onGetRequest(String.format(Apis.URL_FIND_COMING_SOON_MOVIE_LIST_GET,1),ScreenFilmBean.class);
        recyclerFlow.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            //滑动监听
            @Override
            public void onItemSelected(int position) {
                try{
                    RadioButton childAt = (RadioButton) filmGroup.getChildAt(position % result.size());
                    childAt.setChecked(true);
                    current = position;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void getAddress(MessageBean messageBean){
        if(messageBean.getId().equals("address")){
            textLoc.setText(String.valueOf(messageBean.getObject()));
        }else
        if(messageBean.getId().equals("isChange")){
            editSearch.setVisibility(View.GONE);
            textSearch.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_loc, R.id.image_search, R.id.text_search,R.id.hot_film_more,R.id.relaese_film_more,R.id.screen_film_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_loc:
                //TODO 点击定位
                CityPicker.from(getActivity())
                    //activity或者fragment
                    .enableAnimation(true)
                    //自定义动画
                    .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101"))
                    //APP自身已定位的城市，传null会自动定位（默认）
                    .setOnPickListener(new OnPickListener() {
                        @Override
                        public void onPick(int position, City data) {
                            EventBus.getDefault().postSticky(new MessageBean("address",data.getName()));
                            textLoc.setText(data.getName());
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
                //点击搜索框实现动画
                editSearch.setVisibility(View.VISIBLE);
                textSearch.setVisibility(View.VISIBLE);
                AnimatorUtils.translationAnimator(searchLinear,"translationX",-470f,2000,false);
                break;
            case R.id.text_search:
                //点击搜索影片 判断输入框的内容不能为空
                AnimatorUtils.translationAnimator(searchLinear,"translationX",0f,2000,true);
                break;
            case R.id.hot_film_more:
                bundle.putInt("zero",COUNT_ZERO);
                IntentUtils.getInstence().intent(getActivity(),FilmMoreActivity.class,bundle);
                break;
            case R.id.relaese_film_more:
                bundle.putInt("one",COUNT_ONE);
                IntentUtils.getInstence().intent(getActivity(),FilmMoreActivity.class,bundle);
                break;
            case R.id.screen_film_more:
                bundle.putInt("two",COUNT_TWO);
                IntentUtils.getInstence().intent(getActivity(),FilmMoreActivity.class,bundle);
                break;
            default:break;
        }
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        bundle = new Bundle();
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof RelaeseBean) {
            RelaeseBean relaeseBean = (RelaeseBean) data;
            if (relaeseBean.getMessage().equals("查询成功")) {
                if (relaeseBean.getResult().size() > 0) {
                    relaeseFilmAdapter.setList(relaeseBean.getResult());
                    result = relaeseBean.getResult();
                    recyclerFlow.setAdapter(new RelaeseAdapter(result, getContext()));
                    filmGroup.removeAllViews();
                    int width = filmGroup.getWidth();
                    int childWidth = width / result.size();
                    for (int i=0;i<result.size();i++){
                        RadioButton radioButton=new RadioButton(getContext());
                        radioButton.setWidth(childWidth);
                        radioButton.setBackgroundResource(R.drawable.home_film_divide_selected);
                        radioButton.setChecked(false);
                        filmGroup.addView(radioButton);
                    }
                    current = result.size()/2;
                    RadioButton radioButton= (RadioButton) filmGroup.getChildAt(current);
                    radioButton.setChecked(true);
                    handler.sendEmptyMessage(0);
                }
            }
        }else if(data instanceof HotFilmBean){
            HotFilmBean hotFilmBean= (HotFilmBean) data;
            if(hotFilmBean.getMessage().equals("查询成功")){
                if(hotFilmBean.getResult().size()>0){
                    filmAdapter.setList(hotFilmBean.getResult());
                }
            }
        }else if(data instanceof ScreenFilmBean){
            ScreenFilmBean screenFilmBean= (ScreenFilmBean) data;
            if(screenFilmBean.getMessage().equals("查询成功")){
                if(screenFilmBean.getResult().size()>0){
                    screenFilmAdapter.setList(screenFilmBean.getResult());
                }
            }
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG",error);
        ToastUtil.showToast(error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        handler.removeMessages(0);
    }
}
