package com.bw.movie.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;

public class CircularLoading {
    /**
         * 显示Dialog
         * @param context 上下文对象
         * @param msg 提示内容
         * @param isCancelable 是否可以点击取消
         * @return
         */
        public static Dialog showLoadDialog(Context context, String msg, boolean isCancelable) {
            LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_view, null);
        RelativeLayout layout =v.findViewById(R.id.dialog_bg);

        // main.xml中的ImageView
        LoadingView loadImage =v.findViewById(R.id.load_iv);
        TextView pointTextView =v.findViewById(R.id.point_tv);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotating_animation);
        // 使用ImageView显示动画
        loadImage.startAnimation(hyperspaceJumpAnimation);
        pointTextView.setText(msg);
        Dialog loadingDialog = new Dialog(context, R.style.TransDialogStyle);
        loadingDialog.setContentView(layout);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(false);


        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }
    public static Dialog showFailDialog(Context context, String msg, boolean isCancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.fail_loading_view, null);
        RelativeLayout layout =v.findViewById(R.id.fail_layout);

        // main.xml中的ImageView
        ImageView failImage =v.findViewById(R.id.fail_image);
        TextView fail_text =v.findViewById(R.id.fail_text);
        fail_text.setText(msg);
        failImage.setImageResource(R.mipmap.network_img);
        Dialog loadingDialog = new Dialog(context, R.style.TransDialogStyle);
        loadingDialog.setContentView(layout);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(false);


        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 关闭dialog
     */
    public static void closeDialog(Dialog mCircularLoading) {
        if (mCircularLoading != null && mCircularLoading.isShowing()) {
            mCircularLoading.dismiss();
        }
    }
}
