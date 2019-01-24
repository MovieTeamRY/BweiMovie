package com.bw.movie.mvp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.bw.movie.base.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitManager {
    private final String BASE_URL = "http://mobile.bwstudent.com/movieApi/";
    private static RetrofitManager mRetrofitManager;
    private BaseApis mBaseApis;
    public static synchronized RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            mRetrofitManager = new RetrofitManager();
        }
        return mRetrofitManager;
    }

    private RetrofitManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                String userId = preferences.getString("UserId", "");
                String sessionId = preferences.getString("SessionId", "");
                Request.Builder builder1 = request.newBuilder();
                builder1.method(request.method(),request.body());
                builder1.addHeader("ak","0110010010000");
                builder1.addHeader("Content-Type","application/x-www-form-urlencoded");
                if(!TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(sessionId)){
                    builder1.addHeader("userId",userId);
                    builder1.addHeader("sessionId",sessionId);
                }
                Request build = builder1.build();
                return chain.proceed(build);
            }
        });
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build();
        mBaseApis = retrofit.create(BaseApis.class);
    }

    /**
     * get请求
     */
    public void get(String url, HttpListener listener) {

        mBaseApis.get(url)
                //后台执行在哪个线程中
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置我们的rxJava
                .subscribe(getObserver(listener));
    }

    /**
     * 普通post请求
     */
    public void post(String url, Map<String, String> map, HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        mBaseApis.post(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }
    /**
     * 上传头像 文件
     */
    public void postFile(String url, Map<String, String> map,HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        MultipartBody multipartBody = filesToMultipartBody(map);

        mBaseApis.postFile(url, multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }
    /**
     * 把string类型 转换为multpartBody
     */
    public static MultipartBody filesToMultipartBody(Map<String,String> map) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            File file = new File(entry.getValue());
            builder.addFormDataPart(entry.getKey(), file.getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }


    private Observer getObserver(final HttpListener listener){
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    Log.i("TAG",data);
                    if (listener != null) {
                        listener.onSuccess(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onCompleted() {

            }
        };

        return observer;
    }


    public interface HttpListener {
        void onSuccess(String data);

        void onFail(String error);
    }
}
