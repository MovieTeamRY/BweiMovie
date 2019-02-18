package com.bw.movie.mine.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bw.movie.Apis;
import com.bw.movie.R;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.mine.bean.LoadHeadPicBean;
import com.bw.movie.mine.bean.UpdateUserInfoBean;
import com.bw.movie.mine.bean.UserInfoBean;
import com.bw.movie.utils.ImageFileUtil;
import com.bw.movie.utils.IntentUtils;
import com.bw.movie.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserInfoActivity extends BaseActivty {

    @BindView(R.id.user_simple)
    SimpleDraweeView userSimple;
    @BindView(R.id.user_nikeName)
    TextView userNikeName;
    @BindView(R.id.user_sex)
    TextView userSex;
    @BindView(R.id.user_brith)
    TextView userBrith;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.user_email)
    TextView userEmail;
    @BindView(R.id.user_pwd)
    ImageButton userPwd;
    private Unbinder bind;
    private View camera;
    private View pick;
    private View cancel;
    private PopupWindow window;
    private String path = Environment.getExternalStorageDirectory()
            + "/image.png";
    private String file = Environment.getExternalStorageDirectory()
            + "/file.png";
    private final int REQUESTCODE_CAMERA = 100;
    private final int REQUESTCODE_PICK = 300;
    private final int REQUESTCODE_SUCCESS = 200;
    private AlertDialog dialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bind = ButterKnife.bind(this);
        getInitPopupwindow();
    }

    /***
     *初始化popupwindow
     */
    public void getInitPopupwindow() {
        //加载视图
        View view_p = View.inflate(this, R.layout.mine_cmaera_popupwindow_item, null);
        camera = view_p.findViewById(R.id.text_camera);
        pick = view_p.findViewById(R.id.text_pick);
        cancel = view_p.findViewById(R.id.text_cancel);
        //创建PopupWindow
        window = new PopupWindow(view_p, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置焦点
        window.setFocusable(true);
        //设置背景
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置可触摸
        window.setTouchable(true);
        //点击打开相机
        //打开相机
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调取系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 存到sdcard中
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(path)));
                //执行
                startActivityForResult(intent, REQUESTCODE_CAMERA);
                window.dismiss();
            }
        });
        //点击打开相册
        //打开相册
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                //设置图片格式
                intent.setType("image/*");
                //执行
                startActivityForResult(intent, REQUESTCODE_PICK);
                window.dismiss();

            }
        });
        //点击取消
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }
    @Override
    protected void initData() {
        onGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, UserInfoBean.class);
    }
    public void getUpdateNickName() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        View viewname = View.inflate(this, R.layout.alertdialog_nickname_item, null);
        builder.setView(viewname);
        Button update = viewname.findViewById(R.id.updata_btn);
        Button cencal = viewname.findViewById(R.id.cancal_btn);
        final EditText updateName = viewname.findViewById(R.id.updata_edix);
        final RadioButton man = viewname.findViewById(R.id.manbutton);
        final RadioButton woman = viewname.findViewById(R.id.womanbutton);
        final EditText updateEmail = viewname.findViewById(R.id.updata_edix_email);
        //修改
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateName.getText().toString().trim();
                String email = updateEmail.getText().toString().trim();
                if (name.equals("") || email.equals("")) {
                    ToastUtil.showToast("输入不能为空");
                } else {
                    Map<String, String> map = new HashMap<>();
                    boolean checked = man.isChecked();
                    map.put("sex", String.valueOf(2));
                    userSex.setText("女");
                    if (checked) {
                        map.put("sex", String.valueOf(1));
                        userSex.setText("男");
                    }
                    map.put("nickName", name);
                    map.put("email", email);
                    onPostRequest(Apis.URL_MODIFY_USER_INFO_POST, map, UpdateUserInfoBean.class);
                    userNikeName.setText(name);
                    userEmail.setText(email);
                }
                dialog.dismiss();
            }
        });
        //取消
        cencal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onNetSuccess(Object data) {
        if (data instanceof UserInfoBean) {
            UserInfoBean userInfoBean = (UserInfoBean) data;
            UserInfoBean.ResultBean resultBean = userInfoBean.getResult();
            if(!userInfoBean.getMessage().equals("请先登陆")){
                userSimple.setImageURI(Uri.parse(resultBean.getHeadPic()));
                userNikeName.setText(resultBean.getNickName());
                if(resultBean.getSex()==1){
                    userSex.setText("男");
                }else{
                    userSex.setText("女");
                }
                userPhone.setText(resultBean.getPhone());
                userEmail.setText(resultBean.getEmail());
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String time = sDateFormat.format(resultBean.getBirthday());
                userBrith.setText(time);
            }
        }else if (data instanceof LoadHeadPicBean) {
            //上传头像
            LoadHeadPicBean headPicBean = (LoadHeadPicBean) data;
            ToastUtil.showToast(headPicBean.getMessage());
            onGetRequest(Apis.URL_GET_USER_INFO_BY_USERID_GET, UserInfoBean.class);
        }else if (data instanceof UpdateUserInfoBean) {
            //上传头像
            UpdateUserInfoBean updateUserInfoBean = (UpdateUserInfoBean) data;
            ToastUtil.showToast(updateUserInfoBean.getMessage());
        }
    }

    @Override
    protected void onNetFail(String error) {
        ToastUtil.showToast(error);
        Log.i("TAG",error);
    }

    @OnClick({R.id.user_simple,R.id.user_pwd,R.id.user_return,R.id.update_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_simple:
                window.showAtLocation(View.inflate(UserInfoActivity.this, R.layout.activity_user_info, null),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.user_pwd:
                IntentUtils.getInstence().intent(UserInfoActivity.this,UpdatePassWordActivity.class);
                break;
            case R.id.user_return:
                finish();
                break;
            case R.id.update_info:
                getUpdateNickName();
                break;
            default:break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相机
        if (requestCode == REQUESTCODE_CAMERA && resultCode == RESULT_OK) {
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置宽高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUESTCODE_SUCCESS);
        }
        //相册
        if (requestCode == REQUESTCODE_PICK && resultCode == RESULT_OK) {
            //获取相册路径
            Uri uri = data.getData();
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(uri, "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置框高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUESTCODE_SUCCESS);
        }
        if (requestCode == REQUESTCODE_SUCCESS && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            try {
                ImageFileUtil.setBitmap(bitmap, file, 50);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToast(e.getMessage());
            }
            //上传头像
            Map<String, String> map = new HashMap<>();
            map.put("image", file);
            onpostFileRequest(Apis.URL_UPLOAD_HEADPIC_POST, map, LoadHeadPicBean.class);
        }
    }
}
