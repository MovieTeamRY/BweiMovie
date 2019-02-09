package com.bw.movie.mine.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.bean.UserInfoBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserInfoActivity extends BaseActivty {

    @BindView(R.id.user_simple)
    SimpleDraweeView userSimple;
    @BindView(R.id.user_nikeName)
    TextView userNikeName;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.user_brith)
    TextView userBrith;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.user_email)
    TextView userEmail;
    @BindView(R.id.user_pwd)
    ImageButton userPwd;
    private Unbinder bind;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        onGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, UserInfoBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof UserInfoBean) {
            UserInfoBean userInfoBean = (UserInfoBean) data;
            UserInfoBean.ResultBean resultBean = userInfoBean.getResult();
            userSimple.setImageURI(Uri.parse(resultBean.getHeadPic()));
            userNikeName.setText(resultBean.getNickName());
            if(resultBean.getSex()==1){
                userSex.setText("男");
            }else{
                userSex.setText("女");
            }
            userPhone.setText(resultBean.getPhone());
            userEmail.setText(resultBean.getEmail());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = sDateFormat.format(resultBean.getBirthday());
            userBrith.setText(time);
        }
    }

    @Override
    protected void onNetFail(String error) {

    }

    @OnClick({R.id.user_simple, R.id.user_nikeName, R.id.user_sex, R.id.user_brith, R.id.user_phone, R.id.user_email, R.id.user_pwd,R.id.user_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_simple:
                break;
            case R.id.user_nikeName:
                break;
            case R.id.user_sex:
                break;
            case R.id.user_brith:
                break;
            case R.id.user_phone:
                break;
            case R.id.user_email:
                break;
            case R.id.user_pwd:
                break;
            case R.id.user_return:
                finish();
                break;
            default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
