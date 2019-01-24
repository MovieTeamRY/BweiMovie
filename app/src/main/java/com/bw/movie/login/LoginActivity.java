package com.bw.movie.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.utils.RegularUtils;
import com.bw.movie.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author YU
 * @date 2019.01.23
 * 登录页面
 */
public class LoginActivity extends BaseActivty {

    @BindView(R.id.login_text_phone)
    EditText loginTextPhone;
    @BindView(R.id.login_text_pwd)
    EditText loginTextPwd;
    @BindView(R.id.login_image_eye)
    ImageView loginImageEye;
    @BindView(R.id.login_check_rem)
    CheckBox loginCheckRem;
    @BindView(R.id.login_check_auto)
    CheckBox loginCheckAuto;
    @BindView(R.id.login_text_sign)
    TextView loginTextSign;
    @BindView(R.id.login_but)
    Button loginBut;
    @BindView(R.id.login_weixin)
    ImageView loginWeixin;

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
        /**绑定ButterKnife*/
        ButterKnife.bind(this);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.login_text_sign, R.id.login_but, R.id.login_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_text_sign:
                break;
            case R.id.login_but:
                //获取输入框的值
                String phone = loginTextPhone.getText().toString().trim();
                String pwd = loginTextPwd.getText().toString().trim();
                if (phone.equals("")||pwd.equals("")){
                    ToastUtil.showToast("账号或密码不能为空");
                }else {
                    if (RegularUtils.isMobile(phone)&&RegularUtils.isPassword(pwd)){
                        Map<String, String> map = new HashMap<>();
                        map.put("phone", phone);
                        map.put("pwd", pwd);
                        onPostRequest(Apis.URL_LOGIN_POST, map, LoginBean.class);
                    }else{
                        ToastUtil.showToast("手机号格式不正确或密码格式不正确");
                    }

                }
                break;
            case R.id.login_weixin:
                break;
        }
    }
}
