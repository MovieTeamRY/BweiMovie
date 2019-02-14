package com.bw.movie.mine.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseFragment;
import com.bw.movie.login.LoginActivity;
import com.bw.movie.mine.activity.AttentActivity;
import com.bw.movie.mine.activity.FeedBackActivity;
import com.bw.movie.mine.activity.RecordActivity;
import com.bw.movie.mine.activity.UserInfoActivity;
import com.bw.movie.mine.bean.AttendBean;
import com.bw.movie.mine.bean.UserInfoBean;
import com.bw.movie.mine.bean.VersionBean;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.ToastUtil;
import com.bw.movie.utils.VersionUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String sessionId;
    private String mFilePath;

    @Override
    protected int getLayoutResId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initData() {
        sharedPreferences=getContext().getSharedPreferences("User",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        onGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, UserInfoBean.class);
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof UserInfoBean) {
            UserInfoBean userInfoBean = (UserInfoBean) data;
            UserInfoBean.ResultBean resultBean = userInfoBean.getResult();
            if(userInfoBean.getMessage().equals("请先登陆")){
                editor.putString("SessionId","").commit();
            }else{
                userSimple.setImageURI(Uri.parse(resultBean.getHeadPic()));
                userName.setText(resultBean.getNickName());
            }
            sessionId = sharedPreferences.getString("SessionId", null);
        }else if(data instanceof AttendBean){
            AttendBean attendBean= (AttendBean) data;
            if(attendBean.getMessage().equals("请先登陆")){
                IntentUtils.getInstence().intent(getContext(),LoginActivity.class);
            }
            ToastUtil.showToast(attendBean.getMessage());
        }else if (data instanceof VersionBean){
            VersionBean versionBean  = (VersionBean) data;
            if (versionBean.isSuccess()||versionBean!=null){
                if (versionBean.getFlag()==1){
                    showAlertDialog(versionBean.getDownloadUrl());
                }else if (versionBean.getFlag()==2){
                    ToastUtil.showToast("已经是最新版本");
                }
            }
        }
    }
    /**
     * 显示AlertDialog
     * */
    private void showAlertDialog(final String downloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("版本升级");
        builder.setMessage("软件更新");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDialog(downloadUrl);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    /**
     * 点击确定获取url下载
     * */
    private void startDialog(final String downloadUrl) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    startDownload(downloadUrl, progressDialog);
                    progressDialog.dismiss();
                } catch (Exception e) {

                }
            }
        }.start();
    }
    /**
     *更新新版本
     *@author Administrator
     *@time 2019/2/13 0013 20:07
     */
    private void startDownload(String downloadUrl, ProgressDialog progressDialog) throws Exception {
        URL url = new URL(downloadUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(8000);
        progressDialog.setMax(conn.getContentLength());
        InputStream inputStream = conn.getInputStream();
        mFilePath = VersionUtil.getSaveFilePath(downloadUrl);
        File file = new File(mFilePath);
        writeFile(inputStream, file, progressDialog);
    }
    /**
     * 写入文件
     * */
    public void writeFile(InputStream inputStream, File file, ProgressDialog progressDialog) throws Exception {
//判断下载的文件是否已存在
        if (file.exists()) {
            file.delete();
        }
       /* fos = null;*/
        FileOutputStream fos = new FileOutputStream(file);
        byte[] b = new byte[1024];
        int length;
        int total = 0;
        while ((length = inputStream.read(b)) != -1) {
            fos.write(b, 0, length);
            total += length;
            progressDialog.setProgress(total);
        }
        inputStream.close();
        fos.close();
        progressDialog.dismiss();
        installApk(mFilePath);
    }

    private void installApk(String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本大于等于7.0
            // "com.ansen.checkupdate.fileprovider"即是在清单文件中配置的authorities
            // 通过FileProvider创建一个content类型的Uri
            data = FileProvider.getUriForFile(getActivity(), "com.bw.movie.fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
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

    @OnClick({R.id.user_simple,R.id.user_name,R.id.user_message,R.id.user_text_message,R.id.user_attend,R.id.user_text_attention, R.id.user_attention,
            R.id.user_record,R.id.user_text_record, R.id.user_feedback,R.id.user_text_feedback, R.id.user_version,R.id.user_text_version ,R.id.user_logout,R.id.user_text_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_simple:
                if(sessionId.equals("")){
                    IntentUtils.getInstence().intent(getContext(),LoginActivity.class);
                }
                break;
            case R.id.user_name:
                if(sessionId.equals("")){
                    IntentUtils.getInstence().intent(getContext(),LoginActivity.class);
                }
                break;
            case R.id.user_attend:
                //点击用户签到
                if(sessionId.equals("")){
                    ToastUtil.showToast("请先登陆");
                }else{
                    onGetRequest(Apis.URL_USER_SIGN_IN_GET,AttendBean.class);
                }
                break;
            case R.id.user_text_message:
            case R.id.user_message:
                //获取我的信息
                if(sessionId.equals("")){
                    ToastUtil.showToast("请先登陆");
                }else {
                    IntentUtils.getInstence().intent(getActivity(), UserInfoActivity.class);
                }
                break;
            case R.id.user_text_attention:
            case R.id.user_attention:
                //获取我的关注
                if(sessionId.equals("")){
                    ToastUtil.showToast("请先登陆");
                }else {
                    IntentUtils.getInstence().intent(getActivity(), AttentActivity.class);
                }
                break;
            case R.id.user_text_record:
            case R.id.user_record:
                //获取购票记录
                if(sessionId.equals("")){
                    ToastUtil.showToast("请先登陆");
                }else {
                    IntentUtils.getInstence().intent(getActivity(), RecordActivity.class);
                }
                break;
            case R.id.user_text_feedback:
            case R.id.user_feedback:
                //意见反馈
                if(sessionId.equals("")){
                    ToastUtil.showToast("请先登陆");
                }else {
                    IntentUtils.getInstence().intent(getActivity(), FeedBackActivity.class);
                }
                break;
            case R.id.user_text_version:
            case R.id.user_version:
                //版本更新
                 onGetRequest(Apis.URL_FIND_NEW_VERSION_GET,VersionBean.class);
                break;
            case R.id.user_text_logout:
            case R.id.user_logout:
                if(sessionId.equals("")){
                    ToastUtil.showToast("请先登陆");
                }else{
                    Intent intent=new Intent(getContext(),LoginActivity.class);
                    startActivityForResult(intent,100);
                }
                break;
            default:break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==100){

        }else{
           // getActivity().finish();
        }
    }
}
