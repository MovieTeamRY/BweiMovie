package com.bw.movie.cinema.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.ali.AliPayBean;
import com.bw.movie.ali.Result;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.cinema.bean.FilmSchedulBean;
import com.bw.movie.cinema.bean.PayBean;
import com.bw.movie.cinema.view.SeatTable;
import com.bw.movie.login.LoginActivity;
import com.bw.movie.mine.activity.RecordActivity;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.Md5Utils;
import com.bw.movie.utils.ToastUtil;
import com.bw.movie.utils.WeiXinUtil;
import com.bw.movie.wxapi.bean.WXPayBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private FilmSchedulBean.ResultBean resultBean;
    private ArrayList<String> list;
    private int num;
    private int num1;
    private int seatsUseCount;
    private double totalPrice;
    private int numCount = 0;
    private PopupWindow popupWindow;
    private int pay = 1;
    private SharedPreferences.Editor edit;
    private SharedPreferences preferences;
    private PayBean payBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_seat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        edit = preferences.edit();
        //设置文字样式
        getTextView();

    }

    /**
     * 选择支付方式的布局
     *
     * @author Administrator
     * @time 2019/1/31 0031 8:59
     */
    private void getPayPopvView() {
        View view = View.inflate(SeatActivity.this, R.layout.pay_pop_view, null);
        ImageView detail_down = view.findViewById(R.id.detail_down);
        RadioGroup radiogroup = view.findViewById(R.id.radiogroup);
        final TextView confirm_pay = view.findViewById(R.id.confirm_pay);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, 550);
        //设置焦点
        popupWindow.setFocusable(true);
        //设置是否可以触摸
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        //关闭
        detail_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.pay_wx:
                        pay = 1;
                        confirm_pay.setText("微信支付" + totalPrice + "元");
                        break;
                    case R.id.pay_alipay:
                        pay = 2;
                        confirm_pay.setText("支付宝支付" + totalPrice + "元");
                        break;
                }
            }
        });
        if (pay == 1) {
            confirm_pay.setText("微信支付" + totalPrice + "元");
        } else if (pay == 2) {
            confirm_pay.setText("支付宝支付" + totalPrice + "元");
            pay=1;
        }

        //TODO  购买下单
        confirm_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pay == 1) {
                    //微信支付
                    Map<String, String> map = new HashMap<>();
                    map.put("payType", String.valueOf(pay));
                    map.put("orderId", payBean.getOrderId());
                    onPostRequest(Apis.URL_PAY_POST, map, WXPayBean.class);
                } else if (pay == 2) {
                    //支付宝支付
                    Map<String, String> map=new HashMap<>();
                    map.put("payType",String.valueOf(pay));
                    map.put("orderId",payBean.getOrderId());
                    onPostRequest(Apis.URL_PAY_POST,map,AliPayBean.class);
                }

            }
        });
    }

    /**
     * 设置文字样式
     *
     * @author Administrator
     * @time 2019/1/30 0030 10:28
     */
    private void getTextView() {
        //设置颜色
        SpannableString spannableString = new SpannableString("¥\t\t0.0");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        spannableString.setSpan(colorSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置文字大小
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.5f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(0.5f);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        price.setText(spannableString);
    }

    /**
     * 选座
     *
     * @author Administrator
     * @time 2019/1/30 0030 10:23
     */
    private void selectView() {
        //影厅座位总数
        int seatsTotal = resultBean.getSeatsTotal();
        //已售座位数
        seatsUseCount = resultBean.getSeatsUseCount();
        Random rand = new Random();
        num = rand.nextInt(9);
        num1 = rand.nextInt(9);
        Log.i("TAG", num + "========================" + num1);
        //设置屏幕名称
        seattable.setScreenName(resultBean.getScreeningHall() + "荧幕");
        //设置最多选中
        seattable.setMaxSelected(3);
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
                for (int i = 0; i < seatsUseCount; i++) {
                    if (isValidSeat(num, num1)) {
                        if (row == num + i && column == num1 + i) {
                            return true;
                        }
                    } else {
                        num++;
                    }

                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                totalPrice += resultBean.getPrice();
                String totalprice = String.format("%.2f", totalPrice);
                price.setText(totalprice + "");
                numCount++;
            }

            @Override
            public void unCheck(int row, int column) {
                totalPrice -= resultBean.getPrice();
                String totalprice = String.format("%.2f", totalPrice);
                price.setText(totalprice + "");
                numCount--;

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        //设置影厅总座位数
        if (seatsTotal <= 10) {
            seattable.setData(1, seatsTotal + 1);
        } else if (seatsTotal > 10 || seatsTotal <= 100) {
            seattable.setData(5, seatsTotal / 10 + 1);
        } else if (seatsTotal > 100) {
            seattable.setData(10, seatsTotal / 10 + 1);
        }
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        resultBean = bundle.getParcelable("resultBean");
        list = bundle.getStringArrayList("list");
        cinemaName.setText(list.get(0));
        cinemaAddress.setText(list.get(1));
        filmName.setText(list.get(2));
        tvText.setText("\t" + resultBean.getBeginTime() + "-" + resultBean.getEndTime() + "\t\t\t" + resultBean.getScreeningHall());

        //选座
        selectView();


    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof PayBean) {
            payBean = (PayBean) data;
            if (payBean.getMessage().equals(getString(R.string.please_login))) {
                IntentUtils.getInstence().intent(this, LoginActivity.class);
            } else if (payBean.isSuccess() && payBean != null) {
                ToastUtil.showToast(payBean.getMessage());
                //调用支付pop弹框
                getPayPopvView();
                //支付
                popupWindow.showAtLocation(View.inflate(this, R.layout.activity_seat, null), Gravity.BOTTOM, 0, 0);
            }
            ToastUtil.showToast(payBean.getMessage());
        } else if (data instanceof WXPayBean) {
            //微信支付
            WXPayBean wxPayBean = (WXPayBean) data;
            WeiXinUtil.weiXinPay(this, wxPayBean);
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }else if (data instanceof AliPayBean) {
            //支付宝支付
            AliPayBean aliPayBean = (AliPayBean) data;
            if (aliPayBean != null || aliPayBean.isSuccess()) {
                pay=1;
                final String orderInfo = aliPayBean.getResult();
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(SeatActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);

                        Message msg = new Message();
                        // msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        }
    }
    //通过handler发送支付结果
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Result result = new Result((Map<String, String>) msg.obj);
            ToastUtil.showToast(result.getMemo());
            String resultStatus = result.getResultStatus();
            //根据支付结果码跳转到相应的页面
            Intent intent=new Intent(SeatActivity.this,RecordActivity.class);
            if (resultStatus.equals("6001")){
                intent.putExtra("status","obligation");
            }else if (resultStatus.equals("9000")){
                intent.putExtra("status","compeleted");
            }else if (resultStatus.equals("4000")){
                intent.putExtra("status","obligation");
            }
            startActivity(intent);
        }
    };
    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }

    @OnClick({R.id.pay_ok, R.id.pay_fail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_ok:
                //下订单
                Map<String, String> map = new HashMap<>();
                map.put("scheduleId", String.valueOf(resultBean.getId()));
                map.put("amount", String.valueOf(numCount));
                map.put("sign", Md5Utils.MD5(preferences.getString("UserId", null) + resultBean.getId() + numCount + "movie"));
                onPostRequest(Apis.URL_BUY_MOVIE_TICKET_POST, map, PayBean.class);
                break;
            case R.id.pay_fail:
                //取消
                finish();
                break;
        }
    }
}
