package com.bw.movie.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *事件转换类 //long ；类型转化
 *@author Administrator
 *@time 2019/1/27 0027 13:53
 */
public class DateTimeUtil {

    public static String changeTime(Long time) {
        //创建日期
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String StringTime = format.format(date);
        return StringTime;
    }
}
