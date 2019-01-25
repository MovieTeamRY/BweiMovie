package com.bw.movie.utils;

import android.widget.Toast;

import com.bw.movie.base.MyApplication;


/**
 * 吐司提示工具类
 *@author Administrator
 *@time 2019/1/24 0024 18:54
 */
public class ToastUtil {
    private static Toast toast;
    /**
     * 强大的吐司，能够连续弹的吐司
     * @param text
     */
    public static void showToast(String text){
        if(toast==null){
        //如果等于null，则创建
            toast = Toast.makeText(MyApplication.getApplication(), text,Toast.LENGTH_SHORT);
        }else {
            //如果不等于空，则直接将text设置给toast
            toast.setText(text);
        }
        toast.show();
    }
}
