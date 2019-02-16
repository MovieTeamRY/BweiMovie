package com.bw.movie.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;


import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class MyApplication extends Application {

    private static Context context;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static Context getApplication() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryName("images")
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                .build();
        //设置磁盘缓存的配置生成文件
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();
        Fresco.initialize(this,config);

        //android 7.0调用相机闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        //检测内存险漏
        refWatcher = LeakCanary.install(this);
       /* //信鸽推送
        XGPushConfig.enableDebug(this,true);
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setHuaweiDebug(true);
        XGPushConfig.setMiPushAppId(getApplicationContext(), "d0f47e3bdce83");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "fd195eb6a19f52d6a9e5c77f517a908b");
        XGPushConfig.setMzPushAppId(this, "d0f47e3bdce83");
        XGPushConfig.setMzPushAppKey(this, "fd195eb6a19f52d6a9e5c77f517a908b");
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.i("TPush", "注册成功，设备token为：" + data);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.i("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });*/
    }
/*
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        StubAppUtils.attachBaseContext(context);
    }*/
}
