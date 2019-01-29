package com.bw.movie.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import org.greenrobot.eventbus.EventBus;

public class AnimatorUtils {
    /**
     * 位移 属性动画
     * @param view  视图
     * @param animatrType  动画类型
     * @param values  参数值
     * @param duration  动画时间
     * @param isChange  判断 执行完动画后  是否有要执行的后续操作
     */
    public static void translationAnimator(View view, String animatrType, float values, long duration, final boolean isChange){
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, animatrType, values);
        translationX.setInterpolator(new AccelerateInterpolator());
        translationX.setDuration(duration);
        translationX.start();
        translationX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(isChange){
                    EventBus.getDefault().postSticky(new MessageBean("isChange",isChange));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 放大属性动画
     * @param view  视图
     * @param animatrType01  动画类型1
     * @param animatrType02  动画类型2
     * @param valuesStart 开始参数值
     * @param valuesEnd  结束参数值
     * @param duration  动画时长
     */
    public static void scaleAnimator(View view,String animatrType01,String animatrType02,float valuesStart,float valuesEnd,long duration){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, animatrType01, valuesStart, valuesEnd);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, animatrType02, valuesStart, valuesEnd);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

    /**
     * 缩小属性动画
     * @param view 视图
     * @param animatrType01 动画类型1
     * @param animatrType02 动画类型2
     * @param valuesStart  参数值
     * @param duration  动画时长
     */
    public static void scaleAnimator(View view,String animatrType01,String animatrType02,float valuesStart,long duration){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, animatrType01, valuesStart);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, animatrType02, valuesStart);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }
}
