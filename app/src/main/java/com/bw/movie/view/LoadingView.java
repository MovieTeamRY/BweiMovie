package com.bw.movie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.R;

public class LoadingView extends LinearLayout {
    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.drawable.loadingview_shap);

        LoadView loading = new LoadView(getContext());
        loading.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        addView(loading);

        TextView tv = new TextView(getContext());
        tv.setText("正在加载中");
        LinearLayout.LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.setMargins(0, 10, 0, 0);
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        tv.setTextColor(Color.WHITE);
        addView(tv);
    }


    class LoadView extends View {

        Paint mPaint;

        private int mWidth;
        private int mHeight;

        private int mCurrentIndex = 0;

        private int count = 12;

        public LoadView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
            mPaint.setColor(Color.WHITE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (mCurrentIndex >= count) {
                mCurrentIndex = 0;
            }

            int endAlpha = 255 / count;
            for (int i = 0; i < count; i++) {
                int alpha;
                if (mCurrentIndex - i > 0) {
                    alpha = endAlpha * (mCurrentIndex - i);
                } else {
                    alpha = 255 - 255 / count * (i - mCurrentIndex);
                }

                mPaint.setColor(Color.argb(alpha, 255, 255, 255));
                canvas.drawLine(mWidth / 2, 0, mWidth / 2, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()), mPaint);
                canvas.rotate(360 / count, mWidth / 2, mHeight / 2);
            }
            mCurrentIndex++;
            postInvalidateDelayed(100);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mWidth = getWidth();
            mHeight = getHeight();
        }
    }
}
