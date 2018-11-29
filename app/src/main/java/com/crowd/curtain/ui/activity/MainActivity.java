package com.crowd.curtain.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhihu.matisse.Matisse;

import org.json.JSONObject;

import java.io.File;

import com.crowd.curtain.R;
import com.crowd.curtain.api.ApplicationApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.ui.customview.CameraDialog;
import com.crowd.curtain.ui.customview.MainNavigateTabBar;
import com.crowd.curtain.ui.fragment.CaseFragment;
import com.crowd.curtain.ui.fragment.ClassisFragment;
import com.crowd.curtain.ui.fragment.HomeFragment;
import com.crowd.curtain.ui.fragment.VideoFragment;
import com.crowd.curtain.utils.AppSharePerference;

import base.BroadcastConstants;
import base.PermissionActivity;
import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.ToastUtil;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static base.util.BroadCastReceiverUtil.registerBroadcastReceiver;

@Layout(R.layout.activity_main)
public class MainActivity extends AppBaseActivity {
    public static final String TAG_PAGE_HOME = "首页";
    public static final String TAG_PAGE_CLASSIES = "分类";
    public static final String TAG_PAGE_EDITER = "拍照编辑";
    public static final String TAG_PAGE_VIDEO = "安装视频";
    public static final String TAG_PAGE_CASE = "实景案例";

    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int HOME = 0;
    private String mCurrentPhotoPath;

    public static Intent newIntent( int pageIndex) {
        Intent it = new Intent(App.getContext(), MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return it;
    }
    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return it;
    }
    @Id(R.id.takePhoto)
    TextView takePhoto;
    @Id(R.id.mainTabBar)
    MainNavigateTabBar mNavigateTabBar;
    File photoFile = null;
    CameraDialog cameraDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraDialog = new CameraDialog(mContext);
        registerBroadcastReceiver(activity, new String[]{BroadcastConstants.BROADCAST_REFRESH_TOKEN_TIME_OUT}, this);
        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);
        mNavigateTabBar.addTab(HomeFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.home_normal, R.mipmap.home_highlight, TAG_PAGE_HOME));
        mNavigateTabBar.addTab(ClassisFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.class_normal, R.mipmap.class_highlight, TAG_PAGE_CLASSIES));
        mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, TAG_PAGE_EDITER));
        mNavigateTabBar.addTab(VideoFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.video_normal, R.mipmap.video_highlight, TAG_PAGE_VIDEO));
        mNavigateTabBar.addTab(CaseFragment.class, new MainNavigateTabBar.TabParam(R.mipmap.buyershow_normal, R.mipmap.buyershow_highlight, TAG_PAGE_CASE));
        if(TextUtils.isEmpty(AppSharePerference.getDeviceId())){
            PermissionActivity.requestPermission((Activity) activity, new String[]{Manifest.permission.READ_PHONE_STATE}, "需要授权获取手机唯一码权限", new PermissionActivity.OnPermissionCallback() {
                @Override
                public void onPermissionAuthenticated() {
                    initDeviceCode();
                }
                @Override
                public void onPermissionDenied() {
                    ToastUtil.showToast("授权失败");
                }
            });
        }else{
            if(TextUtils.isEmpty(AppSharePerference.getDeviceId())){
                ApplicationApi.postAppData(AppSharePerference.getDeviceId(), new Callback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                    }

                    @Override
                    public void onFail(ErrorModel httpError) {

                    }
                });
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }

    public void onClickPublish(View v) {
        cameraDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            compressPic(new File(Matisse.obtainPathResult(data).get(0)));
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            File picture = new File(getExternalCacheDir() + "/img.jpg");
            compressPic(picture);
            // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
        }

    }
    public void toFragment(String tageName,int cateType){
        mNavigateTabBar.toFragment(tageName,cateType);
    }

    public  void compressPic(File photoFile) {
        Luban.with(this)
                .load(photoFile)                                   // 传人要压缩的图片列表
                .ignoreBy(150)                                  // 忽略不压缩图片的大小
                .setTargetDir(getExternalCacheDir().getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        toActivity(EditorPhotoActivity.newIntent(file,""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

}
