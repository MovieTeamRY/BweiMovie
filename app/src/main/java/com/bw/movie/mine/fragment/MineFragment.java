package com.bw.movie.mine.fragment;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.mine.activity.AttentActivity;
import com.bw.movie.mine.activity.FeedBackActivity;
import com.bw.movie.mine.activity.RecordActivity;
import com.bw.movie.mine.activity.UserInfoActivity;
import com.bw.movie.mine.bean.AttendBean;
import com.bw.movie.mine.bean.UserInfoBean;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {
    @BindView(R.id.user_simple)
    SimpleDraweeView userSimple;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_attend)
    Button userAttend;
    @BindView(R.id.user_message)
    ImageView userMessage;
    @BindView(R.id.user_text_message)
    TextView userTextMessage;
    @BindView(R.id.user_attention)
    ImageView userAttention;
    @BindView(R.id.user_text_attention)
    TextView userTextAttention;
    @BindView(R.id.user_record)
    ImageView userRecord;
    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initData() {
        onGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, UserInfoBean.class);
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof UserInfoBean) {
            UserInfoBean userInfoBean = (UserInfoBean) data;
            UserInfoBean.ResultBean resultBean = userInfoBean.getResult();
            userSimple.setImageURI(Uri.parse(resultBean.getHeadPic()));
            userName.setText(resultBean.getNickName());
        }else if(data instanceof AttendBean){
            AttendBean attendBean= (AttendBean) data;
            ToastUtil.showToast(attendBean.getMessage());
        }
    }

    @Override
    protected void onNetFail(String error) {
        Log.i("TAG", error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.user_message,R.id.user_attend, R.id.user_attention, R.id.user_record, R.id.user_feedback, R.id.user_version, R.id.user_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_attend:
                //点击用户签到
                onGetRequest(Apis.URL_USER_SIGN_IN_GET,AttendBean.class);
                break;
            case R.id.user_message:
                //获取我的信息
                IntentUtils.getInstence().intent(getActivity(),UserInfoActivity.class);
                break;
            case R.id.user_attention:
                //获取我的关注
                IntentUtils.getInstence().intent(getActivity(),AttentActivity.class);
                break;
            case R.id.user_record:
                //获取购票记录
                IntentUtils.getInstence().intent(getActivity(),RecordActivity.class);
                break;
            case R.id.user_feedback:
                //意见反馈
                IntentUtils.getInstence().intent(getActivity(),FeedBackActivity.class);
                break;
            case R.id.user_version:
                //版本更新
                ToastUtil.showToast("版本更新没有做");
                break;
            case R.id.user_logout:
                //退出登录
                ToastUtil.showToast("退出登录没有做");
                break;
            default:break;
        }
    }
}
