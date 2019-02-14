package com.bw.movie.wxapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.login.WXBean;
import com.bw.movie.utils.ToastUtil;
import com.bw.movie.utils.WeiXinUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;
import java.util.Map;

public class WXEntryActivity extends BaseActivty implements IWXAPIEventHandler {
    private String mCode;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_weixin_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        WeiXinUtil.reg(WXEntryActivity.this).handleIntent(getIntent(),this);
    }

    @Override
    protected void initData() {
        preferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        edit = preferences.edit();
        //请求微信登录接口
        Map<String, String> map=new HashMap<>();
        map.put("code",mCode);
        onPostRequest(Apis.URL_WE_CHAT_BINDING_LOGIN_POST,map,WXBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof WXBean){
            WXBean wxBean = (WXBean) data;
            if (wxBean.isSuccess()&&wxBean!=null){
                //IntentUtils.getInstence().intent(WXEntryActivity.this,HomeActivity.class);
                finish();
                //存入状态值
                edit.putString("UserId",String.valueOf(wxBean.getResult().getUserId())).putString("SessionId",wxBean.getResult().getSessionId()).commit();

            }
            ToastUtil.showToast(wxBean.getMessage());
        }
    }

    @Override
    protected void onNetFail(String error) {

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(final BaseResp baseResp) {
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCode=((SendAuth.Resp)baseResp).code;
                        Log.i("TAG",mCode);
                    }
                });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
        }
    }
}
