package com.bw.movie.cinema.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.bean.FilmSchedulBean;
import com.bw.movie.cinema.view.SeatTable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeatActivity extends BaseActivty {

    @BindView(R.id.cinema_name)
    TextView cinemaName;
    @BindView(R.id.cinema_address)
    TextView cinemaAddress;
    @BindView(R.id.film_name)
    TextView filmName;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.seattable)
    SeatTable seattable;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.pay_ok)
    ImageView payOk;
    @BindView(R.id.pay_fail)
    ImageView payFail;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        //设置文字样式
        getTextView();
        //选座
        selectView();
    }
    /**
     *设置文字样式
     *@author Administrator
     *@time 2019/1/30 0030 10:28
     */
    private void getTextView() {
        //设置颜色
        SpannableString spannableString = new SpannableString("¥\t\t0.0");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        spannableString.setSpan(colorSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        price.setText(spannableString);

        //设置文字大小
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.5f);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan01, spannableString.length()-1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        price.setText(spannableString);
    }

    /**
     *选座
     *@author Administrator
     *@time 2019/1/30 0030 10:23
     */
    private void selectView() {
        seattable.setScreenName("8号厅荧幕");//设置屏幕名称
        seattable.setMaxSelected(3);//设置最多选中
        seattable.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if (column == 2) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                if (row == 6 && column == 6) {
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seattable.setData(10, 15);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        FilmSchedulBean.ResultBean resultBean = bundle.getParcelable("resultBean");
        ArrayList<String> list =bundle.getStringArrayList("list");
            cinemaName.setText(list.get(0));
            cinemaAddress.setText(list.get(1));
            filmName.setText(list.get(2));

    }

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }

    @OnClick({R.id.pay_ok, R.id.pay_fail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_ok:
                break;
            case R.id.pay_fail:
                break;
        }
    }
}
