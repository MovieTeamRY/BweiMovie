package com.bw.movie.sign;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.utils.EncryptUtil;
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
    if (data instanceof SignBean){
        SignBean signBean = (SignBean) data;
        if (signBean.isSuccess()&&signBean!=null){
            ToastUtil.showToast(signBean.getMessage());
        }
        ToastUtil.showToast(signBean.getMessage());
    }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
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

    @OnClick(R.id.sign_but)
    public void onViewClicked() {
        String name = signTextNick.getText().toString().trim();
        String sex = signTextSex.getText().toString().trim();
        String data = signTextDate.getText().toString().trim();
        String phone = signTextPhone.getText().toString().trim();
        String email = signTextEmail.getText().toString().trim();
        String pwd = signTextPwd.getText().toString().trim();
        if (name.equals("")||sex.equals("")||data.equals("")||phone.equals("")||email.equals("")||pwd.equals("")){
            ToastUtil.showToast("请确认输入框是否为空");
        }else{
            if (sex.equals("男")||sex.equals("女")){
                if (RegularUtils.isMobile(phone)){
                    if (RegularUtils.isEmail(email)){
                        if (RegularUtils.isPassword(pwd)){
                            int sexx=1;
                            if (sex.equals("女")){
                                sexx=2;
                            }
                            //请求注册接口
                            Map<String,String> map = new HashMap<>();
                            map.put("nickName",name);
                            map.put("phone",phone);
                            map.put("pwd",EncryptUtil.encrypt(pwd));
                            map.put("pwd2",EncryptUtil.encrypt(pwd));
                            map.put("sex",String.valueOf(sexx));
                            map.put("birthday",data);
                            map.put("email",email);
                            onPostRequest(Apis.URL_REGISTER_USER_POST,map,SignBean.class);
                        } else{
                            ToastUtil.showToast("密码长度为6-20的数字或字母");
                        }
                    } else{
                        ToastUtil.showToast("请输入合法的邮箱号");
                    }
                } else{
                    ToastUtil.showToast("请输入合法的手机号");
                }
            } else{
                ToastUtil.showToast("请确认性别");
            }
        }

    }
}
