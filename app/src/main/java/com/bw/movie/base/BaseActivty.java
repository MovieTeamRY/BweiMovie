package com.bw.movie.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bw.movie.mvp.presenter.IpresenterImpl;
import com.bw.movie.mvp.view.Iview;
import com.bw.movie.utils.ToastUtil;
import com.bw.movie.view.CircularLoading;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseActivty extends AppCompatActivity implements Iview {

    private IpresenterImpl ipresenter;
    private Dialog loadDialog,failDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ipresenter=new IpresenterImpl(this);
        //页面增加一个判断，因为4.4版本之前没有沉浸式可言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //动态网络权限
        stateNetWork();
        //初始化
        initView(savedInstanceState);
        //添加数据
        initData();
    }
    /**
     * 注销页面 解绑
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ipresenter.onDetach();
        CircularLoading.closeDialog(loadDialog);
        CircularLoading.closeDialog(failDialog);
        //检测activity内存泄漏
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }


    /**
     * post请求数据
     * @param url  请求数据的路径
     * @param map  请求的参数
     * @param clas  转换数据的类
     */
    protected void onPostRequest(String url, Map<String,String> map, Class clas){
        if(map==null){
            map=new HashMap<>();
        }
        if(loadDialog==null){
            loadDialog = CircularLoading.getInstance().showLoadDialog(this, true);
        }
        ipresenter.onPostStart(url,map,clas);
    }

    /**
     * get请求数据
     * @param url  请求数据的路径
     * @param clas 转换数据的类
     */
    protected void onGetRequest(String url,Class clas){
        if(loadDialog==null){
            loadDialog = CircularLoading.getInstance().showLoadDialog(this, true);
        }
        ipresenter.onGetStart(url,clas);
    }
    /**
     *上传头像
     *@author Administrator
     *@time 2019/2/9 0009 18:23
     */
    protected void onpostFileRequest(String url, Map<String,String> map, Class clas){
        if(map==null){
            map=new HashMap<>();
        }
        if(loadDialog==null){
            loadDialog = CircularLoading.getInstance().showLoadDialog(this, true);
        }
        ipresenter.onpostFileRequest(url,map,clas);
    }
    /**
     * 请求数据成功
     */
    @Override
    public void onSuccess(Object data) {
        CircularLoading.closeDialog(loadDialog);
        CircularLoading.closeDialog(failDialog);
        onNetSuccess(data);
    }

    /**
     * 加载数据失败  或是没有网络
     * @param error
     */
    @Override
    public void onFail(String error) {
        if(loadDialog!=null){
            CircularLoading.closeDialog(loadDialog);
        }

        if(error.equals("当前网络不可用，请检查网络状态")){
            if(failDialog==null){
                failDialog = CircularLoading.getInstance().showFailDialog(this, "糟糕，网络不给力呀！", true);
            }
        }else{
            if(failDialog!=null){
                CircularLoading.closeDialog(failDialog);
            }
            onNetFail(error);
        }
    }
    /**
     * 加载视图
     * @return
     */
    protected abstract int getLayoutResId();
    /**
     * 加载视图
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 加载数据
     */
    protected abstract void initData();

    protected abstract void onNetSuccess(Object data);
    /**
     * 请求网络失败
     * @param error  失败信息
     */
    protected abstract void onNetFail(String error);


    /**
     *添加动态网络权限
     *@author Administrator
     *@time 2018/12/29 0029 16:12
     */
    private void stateNetWork() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            String[] mStatenetwork = new String[]{
                    //写的权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    //读的权限
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //入网权限
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    //WIFI权限
                    Manifest.permission.ACCESS_WIFI_STATE,
                    //读手机权限
                    Manifest.permission.READ_PHONE_STATE,
                    //网络权限
                    Manifest.permission.INTERNET,
                    //打开相机
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS

            };
            ActivityCompat.requestPermissions(this,mStatenetwork,100);
        }
    }
    /**动态注册的回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission  = false;
        if(requestCode == 100){
            for (int i = 0;i<grantResults.length;i++){
                if(grantResults[i] == -1){
                    hasPermission = true;
                }
            }
        }
    }
    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (BaseActivty.this.getCurrentFocus() != null) {
                if (BaseActivty.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(BaseActivty.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }


}
