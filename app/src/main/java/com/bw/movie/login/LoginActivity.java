package com.bw.movie.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.home.activity.HomeActivity;
import com.bw.movie.sign.SignActivity;
import com.bw.movie.utils.EncryptUtil;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.RegularUtils;
import com.bw.movie.utils.ToastUtil;
import com.bw.movie.utils.WeiXinUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.xw.repo.XEditText;

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
    XEditText loginTextPhone;
    @BindView(R.id.login_text_pwd)
    XEditText loginTextPwd;
    @BindView(R.id.login_image_eye)
    ImageButton loginImageEye;
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
    private String phone;
    private String pwd;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private Map<String, String> map;

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof LoginBean){
            LoginBean loginBean = (LoginBean) data;
            if (loginBean.isSuccess()&&loginBean!=null){
                //登录成功后将账号和密码存入
                if (loginCheckRem.isChecked()) {
                    edit.putString("phone", phone);
                    edit.putString("pwd", pwd);
                    edit.putBoolean("isCheck",true);
                    edit.commit();
                }else{
                    edit.clear();
                    edit.commit();
                }
                //判断自动登录是否勾选
                if (loginCheckAuto.isChecked()) {
                    edit.putBoolean("auto_isCheck", true);
                    //提交
                    edit.commit();
                }
                //存入状态值
                edit.putString("UserId",String.valueOf(loginBean.getResult().getUserId())).putString("SessionId",loginBean.getResult().getSessionId()).commit();
                //登录成功后跳转首页
                //IntentUtils.getInstence().intent(LoginActivity.this,HomeActivity.class);
                setResult(100);
                //销毁
                finish();
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
        //判断记住密码是否勾选
        boolean isCheck = preferences.getBoolean("isCheck", false);
        if (isCheck){
            loginCheckRem.setChecked(true);
            loginTextPhone.setText(preferences.getString("phone",null));
            loginTextPwd.setText(preferences.getString("pwd",null));
        }
        //取出自动登录的状态值
        boolean auto_isCheck = preferences.getBoolean("auto_isCheck", false);
        if (auto_isCheck) {
           //TODO 自动登录请求登录接口
            map.put("phone", preferences.getString("phone",null));
            map.put("pwd", EncryptUtil.encrypt(preferences.getString("pwd",null)));
            onPostRequest(Apis.URL_LOGIN_POST, map, LoginBean.class);
            //ToastUtil.showToast("自动登录成功");
        }
        //点击自动登录监听
        loginCheckAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loginCheckRem.setChecked(true);
                }else{
                    loginCheckRem.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /**绑定ButterKnife*/
        ButterKnife.bind(this);
        //创建map集合，存放请求参数
        map = new HashMap<>();
        //显示与隐藏密码
        loginImageEye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    loginTextPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    loginTextPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return false;
            }
        });
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.login_text_sign, R.id.login_but, R.id.login_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_text_sign:
                IntentUtils.getInstence().intent(LoginActivity.this,SignActivity.class);
                finish();
                break;
            case R.id.login_but:
                //获取输入框的值
                phone = loginTextPhone.getText().toString().trim();
                pwd = loginTextPwd.getText().toString().trim();
                if (phone.equals("")|| pwd.equals("")){
                    ToastUtil.showToast("账号或密码不能为空");
                }else {
                    if (RegularUtils.isMobile(phone)){
                        if (RegularUtils.isPassword(pwd)){
                            map.put("phone", phone);
                            Log.i("TAG",EncryptUtil.encrypt(pwd));
                            map.put("pwd", EncryptUtil.encrypt(pwd));
                            onPostRequest(Apis.URL_LOGIN_POST, map, LoginBean.class);
                        }else{
                            ToastUtil.showToast("密码格式不正确，请重新输入");
                        }
                    }else{
                        ToastUtil.showToast("手机号格式不正确，请重新输入");
                    }
                }
                break;
            case R.id.login_weixin:
                //微信登录
                if (!WeiXinUtil.success(this)) {
                    ToastUtil.showToast("请先安装应用");
                } else {
                    //  验证
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    WeiXinUtil.reg(LoginActivity.this).sendReq(req);
                    finish();
                }
                break;
            default:break;
        }
    }
}
