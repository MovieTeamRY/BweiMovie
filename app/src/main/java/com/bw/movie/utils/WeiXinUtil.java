package com.bw.movie.utils;

import android.content.Context;
import android.util.Log;

import com.bw.movie.base.MyApplication;
import com.bw.movie.wxapi.bean.WXPayBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
     *微信工具类
     *@author Administrator
     *@time 2019/1/28 0028 14:24
     */
    public class WeiXinUtil {
         /**APP_ID 替换为你的应用从官方网站申请到的合法appID*/
        public static  String APP_ID = "wxb3852e6a6b7d9516";

        /**IWXAPI 是第三方app和微信通信的openApi接口*/
        private WeiXinUtil() {

        }
        public  static  boolean success(Context context){
            //判断是否安装过微信
            if (WeiXinUtil.reg(context).isWXAppInstalled()) {
                return  true;
            }else {
                return false;
            }
        }
        public static IWXAPI reg(Context context){
            if (context!=null) {
                //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
                IWXAPI wxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
                //注册到微信
                wxapi.registerApp(APP_ID);
                return wxapi;
            }else {
                return  null;
            }
        }
   //支付
    public static void  weiXinPay(Context context, WXPayBean bean){
//        IWXAPI wxapi = WXAPIFactory.createWXAPI(MyApplication.getApplication(), APP_ID, true);
//        //注册到微信
//        wxapi.registerApp(APP_ID);

        IWXAPI wxapi = reg(context);

        PayReq payReq = new PayReq();
        payReq.appId=bean.getAppId();
        payReq.prepayId=bean.getPrepayId();
        payReq.partnerId=bean.getPartnerId();
        payReq.nonceStr=bean.getNonceStr();
        payReq.timeStamp=bean.getTimeStamp();
        payReq.sign=bean.getSign();
        payReq.packageValue=bean.getPackageValue();
        Log.d("",payReq.toString()+"111111");
        wxapi.sendReq(payReq);
    }
}
