package com.bw.movie.film.fragment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import com.bw.movie.greendao.HotFilmDaoBean;
import com.bw.movie.greendao.RelaeseFilmDaoBean;
import com.bw.movie.greendao.ScreenFilmDaoBean;
import com.bw.movie.greendao.greendao.DaoMaster;
import com.bw.movie.greendao.greendao.DaoSession;
import com.bw.movie.greendao.greendao.HotFilmDaoBeanDao;
import com.bw.movie.greendao.greendao.RelaeseFilmDaoBeanDao;
import com.bw.movie.greendao.greendao.ScreenFilmDaoBeanDao;
import com.bw.movie.utils.AddressUtils;
import com.bw.movie.utils.AnimatorUtils;
import com.bw.movie.utils.IntentUtils;
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
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Unbinder;
import recycler.coverflow.CoverFlowLayoutManger;
import recycler.coverflow.RecyclerCoverFlow;

public class FilmFragment extends BaseFragment {
    private static final int COUNT_ZERO =0 ;
    private static final int COUNT_ONE =1 ;
    private static final int COUNT_TWO =2 ;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
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
    XEditText editSearch;
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
    private List<HotFilmBean.ResultBean> hotResult;
    private List<ScreenFilmBean.ResultBean> screenResult;
    private Bundle bundle;
    private HotFilmDaoBeanDao hotFilmDaoBeanDao;
    private RelaeseFilmDaoBeanDao relaeseFilmDaoBeanDao;
    private ScreenFilmDaoBeanDao screenFilmDaoBeanDao;

    @Override
    protected int getLayoutResId() {
        return R.layout.file_fragment;
    }
    private void initDB() {
        DaoMaster.DevOpenHelper hotHelper = new DaoMaster.DevOpenHelper(getContext(), "hotFilm");
        SQLiteDatabase hotHelperWritableDatabase = hotHelper.getWritableDatabase();
        DaoMaster hotDaoMaster = new DaoMaster(hotHelperWritableDatabase);
        DaoSession hotDaoSession = hotDaoMaster.newSession();
        hotFilmDaoBeanDao = hotDaoSession.getHotFilmDaoBeanDao();

        DaoMaster.DevOpenHelper relaeseHelper = new DaoMaster.DevOpenHelper(getContext(), "relaeseFilm");
        SQLiteDatabase relaeseHelperWritableDatabase = relaeseHelper.getWritableDatabase();
        DaoMaster relaeseDaoMaster = new DaoMaster(relaeseHelperWritableDatabase);
        DaoSession relaeseDaoSession = relaeseDaoMaster.newSession();
        relaeseFilmDaoBeanDao = relaeseDaoSession.getRelaeseFilmDaoBeanDao();

        DaoMaster.DevOpenHelper screenHelper = new DaoMaster.DevOpenHelper(getContext(), "screenFilm");
        SQLiteDatabase screenHelperWritableDatabase = screenHelper.getWritableDatabase();
        DaoMaster screenDaoMaster = new DaoMaster(screenHelperWritableDatabase);
        DaoSession screenDaoSession = screenDaoMaster.newSession();
        screenFilmDaoBeanDao = screenDaoSession.getScreenFilmDaoBeanDao();
    }
    @Override
    protected void initData() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        //定位
        AddressUtils.getAddressUtils().getAddressDetail(getActivity());
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
        //创建表数据库
        initDB();
        //加载热门电影布局
        LinearLayoutManager hotFilmManager=new LinearLayoutManager(getContext());
        hotFilmManager.setOrientation(OrientationHelper.HORIZONTAL);
        hotFilmRecycler.setLayoutManager(hotFilmManager);
        filmAdapter = new HotFilmAdapter(getContext());
        hotFilmRecycler.setAdapter(filmAdapter);
        //加载正在热映布局
        LinearLayoutManager relaeseFilmManager=new LinearLayoutManager(getContext());
        relaeseFilmManager.setOrientation(OrientationHelper.HORIZONTAL);
        relaeseFilmRecycler.setLayoutManager(relaeseFilmManager);
        relaeseFilmAdapter = new RelaeseFilmAdapter(getContext());
        relaeseFilmRecycler.setAdapter(relaeseFilmAdapter);
        //加载即将上映布局
        LinearLayoutManager screenFilmManager=new LinearLayoutManager(getContext());
        screenFilmManager.setOrientation(OrientationHelper.HORIZONTAL);
        screenFilmRecycler.setLayoutManager(screenFilmManager);
        screenFilmAdapter = new ScreenFilmAdapter(getContext());
        screenFilmRecycler.setAdapter(screenFilmAdapter);
        //读取数据库里热门电影的数据
        QueryBuilder<HotFilmDaoBean> hotFilmDaoBeanQueryBuilder = hotFilmDaoBeanDao.queryBuilder();
        List<HotFilmDaoBean> hotList = hotFilmDaoBeanQueryBuilder.list();
        int hotSize = hotList.size();
        if(hotSize>0){
            hotResult=new ArrayList<>();
            for (int i=0;i<hotSize;i++){
                HotFilmDaoBean hotFilmDaoBean = hotList.get(i);
                hotResult.add(new HotFilmBean.ResultBean(hotFilmDaoBean.getFollowMovie(),(int)hotFilmDaoBean.getId(),hotFilmDaoBean.getImageUrl(),hotFilmDaoBean.getName(),hotFilmDaoBean.getRank(),hotFilmDaoBean.getSummary()));
            }
            filmAdapter.setList(hotResult);
        }else{
            onGetRequest(String.format(Apis.URL_FIND_HOT_MOVIE_LIST_GET,1),HotFilmBean.class);
        }
        //读取数据库里正在热映的数据
        QueryBuilder<RelaeseFilmDaoBean> relaeseFilmDaoBeanQueryBuilder = relaeseFilmDaoBeanDao.queryBuilder();
        List<RelaeseFilmDaoBean> relaeseList = relaeseFilmDaoBeanQueryBuilder.list();
        int relaeseSize = relaeseList.size();
        if(relaeseSize>0){
            result=new ArrayList<>();
            filmGroup.removeAllViews();
            int childWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.dp_312) / relaeseSize;
            for (int i=0;i<relaeseSize;i++){
                RelaeseFilmDaoBean relaeseFilmDaoBean = relaeseList.get(i);
                result.add(new RelaeseBean.ResultBean(relaeseFilmDaoBean.getFollowMovie(),(int)relaeseFilmDaoBean.getId(),relaeseFilmDaoBean.getImageUrl(),relaeseFilmDaoBean.getName(),relaeseFilmDaoBean.getRank(),relaeseFilmDaoBean.getSummary(),relaeseFilmDaoBean.getReleaseTimeShow()));
                RadioButton radioButton=new RadioButton(getContext());
                radioButton.setWidth(childWidth);
                Bitmap a=null;
                radioButton.setButtonDrawable(new BitmapDrawable(a));
                radioButton.setBackgroundResource(R.drawable.home_film_divide_selected);
                radioButton.setChecked(false);
                filmGroup.addView(radioButton);
            }
            recyclerFlow.setVisibility(View.VISIBLE);
            relaeseFilmAdapter.setList(result);
            recyclerFlow.setAdapter(new RelaeseAdapter(result, getContext()));
            current = relaeseSize/2;
            RadioButton radioButton= (RadioButton) filmGroup.getChildAt(current);
            radioButton.setChecked(true);
            handler.sendEmptyMessage(0);
        }else{
            onGetRequest(String.format(Apis.URL_FIND_RELEASE_MOVIE_LIST_GET, 1), RelaeseBean.class);
        }
        //读取数据库里即将上映的数据
        QueryBuilder<ScreenFilmDaoBean> screenFilmDaoBeanQueryBuilder = screenFilmDaoBeanDao.queryBuilder();
        List<ScreenFilmDaoBean> screenList = screenFilmDaoBeanQueryBuilder.list();
        int screenSize = screenList.size();
        if(screenSize>0){
            screenResult=new ArrayList<>();
            for (int i=0;i<screenSize;i++){
                ScreenFilmDaoBean screenFilmDaoBean = screenList.get(i);
                screenResult.add(new ScreenFilmBean.ResultBean(screenFilmDaoBean.getFollowMovie(),(int)screenFilmDaoBean.getId(),screenFilmDaoBean.getImageUrl(),screenFilmDaoBean.getName(),screenFilmDaoBean.getRank(),screenFilmDaoBean.getSummary(),screenFilmDaoBean.getReleaseTimeShow()));
            }
            screenFilmAdapter.setList(screenResult);
        }else{
            onGetRequest(String.format(Apis.URL_FIND_COMING_SOON_MOVIE_LIST_GET,1),ScreenFilmBean.class);
        }
        //轮播图的滑动监听
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
    //定位发送的地址信息
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    public void getAddress(MessageBean messageBean){
        if(messageBean.getId().equals("address")){
            String[] str = (String[]) messageBean.getObject();
            textLoc.setText(str[0]);
            AddressUtils.getAddressUtils().StopLocation();
        }else
        if(messageBean.getId().equals("isChange")){
            //搜索框的来回变换
            editSearch.setVisibility(View.GONE);
            textSearch.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_loc, R.id.image_search, R.id.text_search,R.id.hot_film_more,R.id.relaese_film_more,R.id.screen_film_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_loc:
                //收起软键盘
                InputMethodManager imm = ( InputMethodManager ) view.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow( view.getApplicationWindowToken( ) , 0 );

                }
                //AddressUtils.getAddressUtils().getAddressDetail(getActivity());
                //TODO 点击定位
                CityPicker.from(getActivity())
                    //activity或者fragment
                    .enableAnimation(true)
                    //自定义动画
                        .setLocatedCity(new LocatedCity(textLoc.getText().toString(), "", ""))
                    //APP自身已定位的城市，传null会自动定位（默认）
                    .setOnPickListener(new OnPickListener() {
                        @Override
                        public void onPick(int position, City data) {
                            EventBus.getDefault().postSticky(new MessageBean("address",new String[]{data.getName()}));
                            textLoc.setText(data.getName());
                            //ToastUtil.showToast(data.getName());
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
                bundle.putInt("type",COUNT_ZERO);
                IntentUtils.getInstence().intent(getActivity(),FilmMoreActivity.class,bundle);
                break;
            case R.id.relaese_film_more:
                bundle.putInt("type",COUNT_ONE);
                IntentUtils.getInstence().intent(getActivity(),FilmMoreActivity.class,bundle);
                break;
            case R.id.screen_film_more:
                bundle.putInt("type",COUNT_TWO);
                IntentUtils.getInstence().intent(getActivity(),FilmMoreActivity.class,bundle);
                break;
            default:break;
        }
    }

    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;

    @OnTouch(R.id.recycler_flow)
    public boolean OnTouchListener(MotionEvent event,View view){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getRawX();
            y1 = event.getRawY();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指离开的时候
            x2 = event.getRawX();
            y2 = event.getRawY();
            if(Math.abs(x1-x2)>50){
                return false;
            }
            scrollView.scrollTo(0, scrollView.getScrollY() + (int) (y1-y2));
            y1 = y2;
        }
        return true;
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
            if (relaeseBean.getMessage().equals(getString(R.string.selected_success))) {
                result = relaeseBean.getResult();
                int size = result.size();
                if (size > 0) {
                    recyclerFlow.setVisibility(View.VISIBLE);
                    relaeseFilmAdapter.setList(relaeseBean.getResult());
                    recyclerFlow.setAdapter(new RelaeseAdapter(result, getContext()));
                    filmGroup.removeAllViews();
                    int width = filmGroup.getWidth();
                    int childWidth = width / size;
                    for (int i = 0; i<size; i++){
                        //存入数据库
                        RelaeseFilmDaoBean relaeseFilmDaoBean=new RelaeseFilmDaoBean();
                        RelaeseBean.ResultBean resultBean = result.get(i);
                        relaeseFilmDaoBean.setFollowMovie(resultBean.isFollowMovie());
                        relaeseFilmDaoBean.setId(resultBean.getId());
                        relaeseFilmDaoBean.setImageUrl(resultBean.getImageUrl());
                        relaeseFilmDaoBean.setName(resultBean.getName());
                        relaeseFilmDaoBean.setRank(resultBean.getRank());
                        relaeseFilmDaoBean.setReleaseTimeShow(resultBean.getReleaseTimeShow());
                        relaeseFilmDaoBean.setSummary(resultBean.getSummary());
                        relaeseFilmDaoBeanDao.insertOrReplace(relaeseFilmDaoBean);
                        //创建radiogroup中的radiobutton 实现条目的滚动切换
                        RadioButton radioButton=new RadioButton(getContext());
                        radioButton.setWidth(childWidth);
                        Bitmap a=null;
                        radioButton.setButtonDrawable(new BitmapDrawable(a));
                        radioButton.setBackgroundResource(R.drawable.home_film_divide_selected);
                        radioButton.setChecked(false);
                        filmGroup.addView(radioButton);
                    }
                    current = size/2;
                    RadioButton radioButton= (RadioButton) filmGroup.getChildAt(current);
                    radioButton.setChecked(true);
                    handler.sendEmptyMessage(0);
                }
            }
        }else if(data instanceof HotFilmBean){
            HotFilmBean hotFilmBean= (HotFilmBean) data;
            if(hotFilmBean.getMessage().equals(getResources().getString(R.string.selected_success))){
                List<HotFilmBean.ResultBean> result = hotFilmBean.getResult();
                int size = result.size();
                if(size>0){
                    for (int i=0;i<size;i++){
                        //存入数据库
                        HotFilmBean.ResultBean resultBean = result.get(i);
                        HotFilmDaoBean hotFilmDaoBean=new HotFilmDaoBean();
                        hotFilmDaoBean.setFollowMovie(resultBean.isFollowMovie());
                        hotFilmDaoBean.setId(resultBean.getId());
                        hotFilmDaoBean.setImageUrl(resultBean.getImageUrl());
                        hotFilmDaoBean.setName(resultBean.getName());
                        hotFilmDaoBean.setRank(resultBean.getRank());
                        hotFilmDaoBean.setSummary(resultBean.getSummary());
                        hotFilmDaoBeanDao.insertOrReplace(hotFilmDaoBean);
                    }
                    filmAdapter.setList(hotFilmBean.getResult());
                }
            }
        }else if(data instanceof ScreenFilmBean){
            ScreenFilmBean screenFilmBean= (ScreenFilmBean) data;
            if(screenFilmBean.getMessage().equals(getResources().getString(R.string.selected_success))){
                List<ScreenFilmBean.ResultBean> result = screenFilmBean.getResult();
                int size = result.size();
                if(size>0){
                    for (int i=0;i<size;i++){
                        //存入数据库
                        ScreenFilmDaoBean screenFilmDaoBean=new ScreenFilmDaoBean();
                        ScreenFilmBean.ResultBean resultBean = result.get(i);
                        screenFilmDaoBean.setFollowMovie(resultBean.isFollowMovie());
                        screenFilmDaoBean.setId(resultBean.getId());
                        screenFilmDaoBean.setImageUrl(resultBean.getImageUrl());
                        screenFilmDaoBean.setName(resultBean.getName());
                        screenFilmDaoBean.setRank(resultBean.getRank());
                        screenFilmDaoBean.setReleaseTimeShow(resultBean.getReleaseTimeShow());
                        screenFilmDaoBean.setSummary(resultBean.getSummary());
                        screenFilmDaoBeanDao.insertOrReplace(screenFilmDaoBean);
                    }
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
        AddressUtils.getAddressUtils().StopLocation();
    }
}
