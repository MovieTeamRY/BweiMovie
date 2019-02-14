package com.bw.movie.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.bean.FeedBackBean;
import com.bw.movie.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivty {

    @BindView(R.id.edit_feedback)
    EditText editFeedback;
    @BindView(R.id.submission)
    TextView submission;
    @BindView(R.id.return_image)
    ImageView returnImage;
    @BindView(R.id.layout_success)
    LinearLayout layoutSuccess;
    @BindView(R.id.layout)
    LinearLayout layout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof FeedBackBean) {
            FeedBackBean feedBackBean = (FeedBackBean) data;
            if (feedBackBean.isSuccess() || feedBackBean != null) {
                layoutSuccess.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                submission.setVisibility(View.GONE);
            }else {
                ToastUtil.showToast(feedBackBean.getMessage());
            }
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
    }


    @OnClick({R.id.submission, R.id.return_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submission:
                String trim = editFeedback.getText().toString().trim();
                if (trim.equals("")) {
                    ToastUtil.showToast("请输入意见反馈内容");
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("content", trim);
                    onPostRequest(Apis.URL_RECORD_FEED_BACK_POST, map, FeedBackBean.class);
                }
                break;
            case R.id.return_image:
                finish();
                break;
        }
    }

}
