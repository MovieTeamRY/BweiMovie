package com.bw.movie.cinema.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.view.SeatTable;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        //选座
        selectView();
    }

    private void selectView() {
        seattable.setScreenName("8号厅荧幕");//设置屏幕名称
        seattable.setMaxSelected(3);//设置最多选中
        seattable.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if(column==2) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                if(row==6&&column==6){
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
        seattable.setData(10,15);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }


}
