package com.bw.movie.sign;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author YU
 * @date 2019.01.23
 * 注册页面
 */
public class SignActivity extends BaseActivty {
    @BindView(R.id.sign_text_nick)
    EditText signTextNick;
    @BindView(R.id.sign_text_sex)
    EditText signTextSex;
    @BindView(R.id.sign_text_date)
    EditText signTextDate;
    @BindView(R.id.sign_text_phone)
    EditText signTextPhone;
    @BindView(R.id.sign_text_email)
    EditText signTextEmail;
    @BindView(R.id.sign_text_pwd)
    EditText signTextPwd;
    @BindView(R.id.sign_but)
    Button signBut;

    @Override
    protected void onNetSuccess(Object data) {

    }

    @Override
    protected void onNetFail(String error) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sign;
    }
}
