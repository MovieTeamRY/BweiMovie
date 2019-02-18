package com.bw.movie.sign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.home.activity.HomeActivity;
import com.bw.movie.login.LoginBean;
import com.bw.movie.utils.EncryptUtil;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.RegularUtils;
import com.bw.movie.utils.ToastUtil;
import com.xw.repo.XEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author YU
 * @date 2019.01.23
 * 注册页面
 */
public class SignActivity extends BaseActivty {
    @BindView(R.id.sign_text_nick)
    XEditText signTextNick;
    @BindView(R.id.sign_group_sex)
    RadioGroup signGroupSex;
    @BindView(R.id.sign_text_date)
    TextView signTextDate;
    @BindView(R.id.sign_text_phone)
    XEditText signTextPhone;
    @BindView(R.id.sign_text_email)
    XEditText signTextEmail;
    @BindView(R.id.sign_text_pwd)
    XEditText signTextPwd;
    @BindView(R.id.sign_but)
    Button signBut;
    private String phone;
    private String pwd;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    @Override
    protected void onNetSuccess(Object data) {
    if (data instanceof SignBean){
        SignBean signBean = (SignBean) data;
        if (signBean.isSuccess()&&signBean!=null){
            //注册成功后登录
            Map<String,String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("pwd", EncryptUtil.encrypt(pwd));
            onPostRequest(Apis.URL_LOGIN_POST, map, LoginBean.class);

            edit.putString("phone", phone);
            edit.putString("pwd", pwd);
            edit.putBoolean("isCheck",true);
            edit.commit();
            //注册成功吐司
            ToastUtil.showToast(signBean.getMessage());
        }
        ToastUtil.showToast(signBean.getMessage());
    }else if (data instanceof LoginBean){
        LoginBean loginBean = (LoginBean) data;
        if (loginBean.isSuccess()&&loginBean!=null){
            IntentUtils.getInstence().intent(SignActivity.this,HomeActivity.class);
            finish();
            //存入状态值
            edit.putString("UserId",String.valueOf(loginBean.getResult().getUserId())).putString("SessionId",loginBean.getResult().getSessionId()).commit();

            ToastUtil.showToast(loginBean.getMessage());
        }
        ToastUtil.showToast(loginBean.getMessage());
    }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }

    @Override
    protected void initData() {
        preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        edit = preferences.edit();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sign;
    }

    @OnClick({R.id.sign_but,R.id.sign_text_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_text_date:
                //收回软件盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(SignActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //获取系统时间
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                calendar.set(year - 10, month, day);
                startDate.set(year - 100, 0, 1);
                endDate.set(year, month, day);
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sDateFormat.format(date);
                        signTextDate.setText(time + "");
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})
                        // 默认全部显示
                        .setCancelText(getString(R.string.alter_pop_cancel))
                        //取消按钮文字
                        .setSubmitText(getString(R.string.alter_pop_sure))
                        //确认按钮文字
                        .setOutSideCancelable(true)
                        //点击屏幕，点在控件外部范围时，是否取消显示
                        .setRangDate(startDate, endDate)
                        //起始终止年月日设定
                        .setDate(calendar)
                        //设置默认时间
                        .isCenterLabel(false)
                        //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .build();
                pvTime.show();
                break;
            case R.id.sign_but:
                String name = signTextNick.getText().toString().trim();
                int radioButtonId = signGroupSex.getCheckedRadioButtonId();
                int sex = 0;
                if (radioButtonId == R.id.sign_radio_man) {
                    sex = 1;
                } else if (radioButtonId == R.id.sign_radio_woman) {
                    sex = 2;
                }
                String data = signTextDate.getText().toString().trim();
                phone = signTextPhone.getText().toString().trim();
                String email = signTextEmail.getText().toString().trim();
                pwd = signTextPwd.getText().toString().trim();
                if (name.equals("") || data.equals("") || phone.equals("") || email.equals("") || pwd.equals("")) {
                    ToastUtil.showToast(getString(R.string.edit_not_can_empty));
                } else {
                    if (RegularUtils.isMobile(phone)) {
                        if (RegularUtils.isEmail(email)) {
                            if (RegularUtils.isPassword(pwd)) {
                                //请求注册接口
                                Map<String, String> map = new HashMap<>();
                                map.put("nickName", name);
                                map.put("phone", phone);
                                map.put("pwd", EncryptUtil.encrypt(pwd));
                                map.put("pwd2", EncryptUtil.encrypt(pwd));
                                map.put("sex", sex + "");
                                map.put("birthday", data);
                                map.put("email", email);
                                onPostRequest(Apis.URL_REGISTER_USER_POST, map, SignBean.class);
                            } else {
                                ToastUtil.showToast(getString(R.string.pwd_rule));
                            }
                        } else {
                            ToastUtil.showToast(getString(R.string.email_rule));
                        }
                    } else {
                        ToastUtil.showToast(getString(R.string.phone_rule));
                    }
                }
                break;
            default:break;
        }

    }
}
