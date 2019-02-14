package com.bw.movie.mine.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.bean.UpdatePassBean;
import com.bw.movie.utils.EncryptUtil;
import com.bw.movie.utils.RegularUtils;
import com.bw.movie.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePassWordActivity extends BaseActivty {

    @BindView(R.id.editloadpwd)
    EditText editloadpwd;
    @BindView(R.id.editnewpwd)
    EditText editnewpwd;
    @BindView(R.id.editnewpwd2)
    EditText editnewpwd2;
    @BindView(R.id.cardview)
    CardView cardview;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.but_return)
    ImageButton butReturn;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_pass_word;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof UpdatePassBean){
            UpdatePassBean updatePassBean = (UpdatePassBean) data;
            ToastUtil.showToast(updatePassBean.getMessage());
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }

    @OnClick({R.id.confirm, R.id.but_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                String loadpwd = editloadpwd.getText().toString().trim();
                String newpwd = editnewpwd.getText().toString().trim();
                String newpwd2 = editnewpwd2.getText().toString().trim();
                if (loadpwd.equals("")||newpwd.equals("")||newpwd2.equals("")){
                    ToastUtil.showToast("输入框不能为空");
                }else{
                    if (RegularUtils.isPassword(newpwd)){
                        if (newpwd.equals(newpwd2)){
                            Map<String, String> map = new HashMap<>();
                            map.put("oldPwd",EncryptUtil.encrypt(loadpwd));
                            map.put("newPwd",EncryptUtil.encrypt(newpwd));
                            map.put("newPwd2",EncryptUtil.encrypt(newpwd2));
                            onPostRequest(Apis.URL_MODIFY_USER_PWD_POST,map,UpdatePassBean.class);
                        }
                    }else{
                        ToastUtil.showToast("密码格式不正确，请重新输入");
                    }
                }
                break;
            case R.id.but_return:
                finish();
                break;
        }
    }
}
