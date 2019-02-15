package com.bw.movie.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.movie.R;

public class CircularLoading {

    private static CircularLoading circularLoading;

    public  static synchronized CircularLoading getInstance() {
        if(circularLoading==null){
            circularLoading=new CircularLoading();
        }
        return circularLoading;
    }
    /**
         * 显示Dialog
         * @param context 上下文对象
         * @param isCancelable 是否可以点击取消
         * @return
         */
        public Dialog showLoadDialog(Context context, boolean isCancelable) {
            LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_view, null);
        RelativeLayout layout =v.findViewById(R.id.dialog_bg);
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
    public Dialog showFailDialog(Context context, String msg, boolean isCancelable) {
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

        final Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //window.location.reload();
                return true;
            }
        });
        return loadingDialog;
    }

    /**
     * 关闭dialog
     */
    public static void closeDialog(Dialog mCircularLoading) {
        if (mCircularLoading != null && mCircularLoading.isShowing()) {
            mCircularLoading.dismiss();
        }
        if(circularLoading!=null){
            circularLoading=null;
        }
    }
}
