package com.crowd.curtain.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.PermissionActivity;
import base.http.Callback;
import base.http.ErrorModel;
import base.imagezoom.util.ImageZoom;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.ToastUtil;
import com.crowd.curtain.R;
import com.crowd.curtain.api.CaseApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.ui.customview.LoadingDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 *
 * @author zhangpeng
 * @date 2018/3/5
 */
@Layout(R.layout.activity_upload_case)
public class UploadCaseActivity extends AppBaseActivity implements View.OnClickListener{
    private static final int IMAGE_REQUEST_CODE = 0;
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;

    @Id(R.id.edt_case_num)
    EditText edtCaseNum;
    @Id(R.id.edt_case_introduce)
    EditText edtIntroduce;
    @Id(R.id.ll_images)
    LinearLayout llImages;
    @Id(R.id.tv_img_count)
    TextView tvImageCount;
    @Id(R.id.tv_submit_upload)
    TextView tvSubmit;
    private List<File> listFiles= new ArrayList<>();
    private int selectCount=5;
    private LoadingDialog progressDialog;
    private String caseNum;

    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), UploadCaseActivity.class);
        return it;
    }
    public static Intent newIntent(String caseNum) {
        Intent it = new Intent(App.getContext(), UploadCaseActivity.class);
        it.putExtra("case_num",caseNum);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        caseNum = getIntent().getStringExtra("case_num");
        initView();
    }
    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("实景案例上传");
        progressDialog = LoadingDialog.createDialog(mContext, "上传中");
        if(!TextUtils.isEmpty(caseNum)){
            edtCaseNum.setText(caseNum);
            edtCaseNum.setFocusable(false);
            edtCaseNum.setFocusableInTouchMode(false);
            edtCaseNum.setInputType(InputType.TYPE_NULL);
        }
    }

    @Click(R.id.rl_camera)
    private void takePic(){

        selectCount = (5-llImages.getChildCount());
        PermissionActivity.requestPermission((Activity) activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, "需要授权读写权限", new PermissionActivity.OnPermissionCallback() {
            @Override
            public void onPermissionAuthenticated() {
                Matisse.from(UploadCaseActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Dracula)
                        .countable(true)
                        .capture(true)
                        .captureStrategy(
                                new CaptureStrategy(false, "com.crowd.curtain.fileprovider"))
                        .maxSelectable(selectCount)
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(IMAGE_REQUEST_CODE);
            }
            @Override
            public void onPermissionDenied() {
                ToastUtil.showToast("授权失败");
            }
        });
    }
    public  String getRealFilePath(final Uri uri ) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null,null);
        String path="";
        if (cursor != null && cursor.moveToFirst()) {
             path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        }
        return path;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            addImages(Matisse.obtainPathResult(data));
        }
    }
    @Click(R.id.backIcon)
    private void toback(){
        finish();
    }
    @Override
    public void onClick(View v) {
        String path = (String) v.getTag();
        int index=listFiles.indexOf(new File(path));
        if(index<0||index>=llImages.getChildCount()){
            return;
        }
        if(v.getId()==R.id.iv_upload_img){
            ArrayList<String> mItems=new ArrayList<>();
            for (int j = 0; j <listFiles.size() ; j++) {
                mItems.add(listFiles.get(j).getAbsolutePath());
            }
            ImageZoom.show(UploadCaseActivity.this, index, mItems);
            return;
        }
        if(v.getId()==R.id.iv_upload_del){
            llImages.removeView(llImages.getChildAt(index));
            listFiles.get(index).delete();
            listFiles.remove(index);
            tvImageCount.setText(llImages.getChildCount()+"/5");
            llImages.invalidate();
            return;
        }
    }

    private void addImages(List<String> mFiles) {
        if(mFiles!=null&&mFiles.size()>0){
            for (int i = 0; i < mFiles.size(); i++) {
                View image = LayoutInflater.from(mContext).inflate(R.layout.upload_image_item,null);
                ImageView imageView =image.findViewById(R.id.iv_upload_img);
                ImageView imageDel =image.findViewById(R.id.iv_upload_del);
//                   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUrilist.get(i));
//                    NativeUtil.qualityCompress(bitmap,saveFile);、
                Luban.with(this)
                        .load(mFiles.get(i))                                   // 传人要压缩的图片列表
                        .ignoreBy(120)                                  // 忽略不压缩图片的大小
                        .setTargetDir(getExternalCacheDir().getAbsolutePath())                        // 设置压缩后文件存储位置
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                                listFiles.add(file);
                                imageDel.setOnClickListener(UploadCaseActivity.this);
                                imageView.setOnClickListener(UploadCaseActivity.this);
                                imageView.setTag(file.getAbsolutePath());
                                imageDel.setTag(file.getAbsolutePath());
                                llImages.addView(image);
                                tvImageCount.setText(llImages.getChildCount()+"/5");
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过程出现问题时调用
                            }
                        }).launch();
            }
        }
    }
    @Click(R.id.tv_submit_upload)
    private void uploadCase(){
        String caseNum = edtCaseNum.getText().toString().replace(" ","");
        String caseIntroduce = edtIntroduce.getText().toString().replace(" ","");
        if(TextUtils.isEmpty(caseNum)){
            ToastUtil.showToast("编号不能为空");
            return;
        }
        if(TextUtils.isEmpty(caseIntroduce)){
            ToastUtil.showToast("文字介绍不能为空");
            return;
        }
        Map<String,String> prames = new HashMap<>();
        prames.put("number",caseNum);
        prames.put("desc",caseIntroduce);
        String[] fileKeys =new String[listFiles.size()];
        File[] mFiles= new File[listFiles.size()];
        for (int i = 0; i < listFiles.size(); i++) {
            mFiles[i] = listFiles.get(i);
            fileKeys[i] = "image"+(i+1);
        }
        progressDialog.show();
        if(mFiles.length>0){
            CaseApi.postUploadCase(fileKeys,mFiles,prames, new Callback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonpObject) {
                    if(jsonpObject.containsKey("message")){
                        ToastUtil.showToast(jsonpObject.getString("message"));
                        progressDialog.dismiss();
                        deleteFile(listFiles);
                        finish();
                    }
                }

                @Override
                public void onFail(ErrorModel httpError) {
                    progressDialog.dismiss();
                }
            });
        }else{
            ToastUtil.showToast("请上传图片");
        }

    }

    private void deleteFile(List<File> mFiles) {
            for (int i = 0; i < mFiles.size(); i++) {
                File f = mFiles.get(i);
                f.delete();
            }
    }
}
