package com.bw.movie.guide.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;

public class GuideCustomView extends View {

    private ImageView custom_image;
    private TextView custom_text;
    private TextView custom_describe;

    public GuideCustomView(Context context) {
        super(context);
        initView(context);
    }

    public GuideCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.guide_custom_view, null, false);
        custom_image = view.findViewById(R.id.guide_custom_image);
        custom_text = view.findViewById(R.id.guide_custom_text);
        custom_describe = view.findViewById(R.id.guide_custom_descirbe);
    }

    public void setData(Integer image,String text,String describe){
        custom_image.setImageResource(image);
        custom_text.setText(text);
        custom_describe.setText(describe);
    }
}
