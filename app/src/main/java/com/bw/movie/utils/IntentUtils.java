package com.bw.movie.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 *intent的跳转工具类
 *@author Administrator
 *@time 2019/1/24 0024 18:54
 */
public class IntentUtils {
    public static IntentUtils instence;

    public static IntentUtils getInstence() {
        if (null == instence) {
            synchronized (IntentUtils.class) {
                instence = new IntentUtils();
            }
        }
        return instence;
    }

    /**
     * 不带参数的跳转
     *
     * @param fromContext
     * @param cls
     *  泛型
     */
    public void intent(Context fromContext, Class<?> cls) {
        Intent intent = new Intent(fromContext, cls);
        fromContext.startActivity(intent);
    }

    /**
     * 带参数的跳转
     *
     * @param fromContext
     * @param cls
     *  泛型
     */
    public void intent(Context fromContext, Class<?> cls, Bundle bb) {
        Intent intent = new Intent(fromContext, cls);
        intent.putExtra("bundle",bb);
        fromContext.startActivity(intent);
    }
}

