package com.crowd.curtain.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhihu.matisse.Matisse;

import java.io.File;

import base.http.Callback;
import base.http.ErrorModel;
import base.indicator.CirclePageIndicator;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.Arith;
import base.util.DensityUtils;
import base.util.PicassoImageLoader;
import com.crowd.curtain.R;
import com.crowd.curtain.api.ApplicationApi;
import com.crowd.curtain.api.ProductApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.BuyersShow;
import com.crowd.curtain.common.model.ProImage;
import com.crowd.curtain.common.model.ProductDetail;
import com.crowd.curtain.ui.adapter.ProBannerAdapter;
import com.crowd.curtain.ui.customview.AutoPlayViewPager;
import com.crowd.curtain.ui.customview.CameraDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 *
 * @author zhangpeng
 * @date 2018/3/5
 */
@Layout(R.layout.activity_product)
public class ProductDetailActivity extends AppBaseActivity implements NestedScrollView.OnScrollChangeListener{
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;

    @Id(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @Id(R.id.product_viewpager)
    private AutoPlayViewPager autoPlayViewPage;
    @Id(R.id.pro_indicator)
    CirclePageIndicator indicator;
    @Id(R.id.pro_num)
    TextView tvProNum;
    @Id(R.id.pro_type)
    TextView tvProType;
    @Id(R.id.pro_metrail)
    TextView tvProMeta;
    @Id(R.id.pro_address)
    TextView tvProAddress;
    @Id(R.id.pro_title)
    TextView tvProName;
    @Id(R.id.iv_toTop)
    ImageView ivTop;
    @Id(R.id.pro_imags)
    LinearLayout imageList;
    ProBannerAdapter bannerAdapter;

    ProductDetail product;
    private CameraDialog cameraDialog;
    private int bannerId=-1;
    private String productId="";
    private long startTime;

    public static Intent newIntent(int bannerId,String productId) {
        Intent it = new Intent(App.getContext(), ProductDetailActivity.class);
        it.putExtra("bannerId",bannerId);
        it.putExtra("productId",productId);
        return it;
    }

    public static Intent newIntent(String productId) {
        Intent it = new Intent(App.getContext(), ProductDetailActivity.class);
        it.putExtra("productId",productId);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bannerId = getIntent().getIntExtra("bannerId",-1);
        productId = getIntent().getStringExtra("productId");
        initView();
    }
    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("商品详情");
        cameraDialog = new CameraDialog(mContext);
        initData();
    }


    private void initData() {
        nestedScroll.setOnScrollChangeListener(this);
        nestedScroll.scrollTo(0, 0);
        bannerAdapter = new ProBannerAdapter(mContext, autoPlayViewPage);
        autoPlayViewPage.setAdapter(bannerAdapter);
        indicator.setViewPager(autoPlayViewPage);
        autoPlayViewPage.setDirection(AutoPlayViewPager.Direction.LEFT);
        autoPlayViewPage.setCurrentItem(0);
        autoPlayViewPage.start();
        imageList.removeAllViews();
//        JSONObject jsonpObject = JSON.parseObject(ApplicationApi.readLocalJson(mContext, "contacts.json"));
        getProdetail(bannerId,productId);
    }

    @Click(R.id.backIcon)
    private void backUp(){
        finish();
    }
    @Click(R.id.btnBuyNow)
    private void toEditPhoto(){
        cameraDialog.show();
    }
    @Click(R.id.iv_toTop)
    public void scrollToTop(){
        nestedScroll.scrollTo(0,0);
    }
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY&&scrollY>50) {
            // 向下滑动
            ivTop.setVisibility(View.VISIBLE);
        }
        if (scrollY < oldScrollY) {
            // 向上滑动
        }
        if (scrollY == 0) {
            // 顶部
            ivTop.setVisibility(View.GONE);
        }
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            // 底部
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            compressPic(new File(Matisse.obtainPathResult(data).get(0)));
        }
        if (requestCode == MainActivity.CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            File picture = new File(getExternalCacheDir() + "/img.jpg");
            compressPic(picture);
            // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
        }
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
                        toActivity(EditorPhotoActivity.newIntent(file.getAbsolutePath(),product.parentCateId,product.productNumber));
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    public void getProdetail(int bannerId,String productId) {
        ProductApi.getProductById(bannerId,productId,new Callback<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    product = JSON.parseObject(jsonObject.getString("data"),ProductDetail.class);
                    tvProName.setText(product.productName);
                    tvProNum.setText(product.productNumber);
                    tvProType.setText(product.parentCateName);
                    tvProMeta.setText(product.cloth);
                    tvProAddress.setText(product.origin);
                    bannerAdapter.update(product.productImage);
                    LinearLayout.LayoutParams params;
                    LinearLayout.LayoutParams paramsTitle =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(65));
                    if(product.display!=null){
                        ImageView proImgTitle = new ImageView(mContext);
                        proImgTitle.setLayoutParams(paramsTitle);
                        imageList.addView(proImgTitle);
                        PicassoImageLoader.getInstance().displayImage(mContext,R.mipmap.pro_diaplay,proImgTitle);
                        for (int i = 0; i < product.display.size(); i++) {
                            ProImage proImage = product.display.get(i);
                            ImageView proImg = new ImageView(mContext);
                            proImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            double div = Arith.div(proImage.height,proImage.width,3);
                            int width = DensityUtils.getDensityWidth()-DensityUtils.dip2px(40);
                            int height = (int) (width*div);
                            params =new LinearLayout.LayoutParams(width,height);
                            params.setMargins(0,0,0,DensityUtils.dip2px(20));
                            proImg.setLayoutParams(new LinearLayout.LayoutParams(params.width,params.height));
                            proImg.setBackgroundColor(Color.parseColor("#ccceee"));
                            PicassoImageLoader.getInstance().displayImage(mContext,proImage.src,proImg);
                            imageList.addView(proImg,params);
                        }
                    }
                    if(product.details!=null){
                        ImageView proImgTitle = new ImageView(mContext);
                        proImgTitle.setLayoutParams(paramsTitle);
                        imageList.addView(proImgTitle);
                        PicassoImageLoader.getInstance().displayImage(mContext,R.mipmap.pro_detail,proImgTitle);
                        for (int i = 0; i < product.details.size(); i++) {
                            ProImage proImage = product.details.get(i);
                            ImageView proImg = new ImageView(mContext);
                            proImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            double div = Arith.div(proImage.height,proImage.width,3);
                            int width = DensityUtils.getDensityWidth()-DensityUtils.dip2px(40);
                            int height = (int) (width*div);
                            params =new LinearLayout.LayoutParams(width,height);
                            params.setMargins(0,0,0,DensityUtils.dip2px(20));
                            proImg.setLayoutParams(params);
                            PicassoImageLoader.getInstance().displayImage(mContext,product.details.get(i).src,proImg);
                            imageList.addView(proImg,params);
                        }
                       /* if(!TextUtils.isEmpty(product.materialImage.src)){
                            ProImage proImage = product.productParameter;
                            ImageView proImg = new ImageView(mContext);
                            proImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            double div = Arith.div(proImage.height,proImage.width,3);
                            int width = DensityUtils.getDensityWidth()-DensityUtils.dip2px(40);
                            int height = (int) (width*div);
                            params =new LinearLayout.LayoutParams(width,height);
                            params.setMargins(0,0,0,DensityUtils.dip2px(20));
                            proImg.setLayoutParams(new LinearLayout.LayoutParams(params.width,params.height));
                            PicassoImageLoader.getInstance().displayImage(mContext,product.materialImage.src,proImg);
                            imageList.addView(proImg,params);
                        }*/
                    }
                    if(!TextUtils.isEmpty(product.productParameter.src)){
                        ImageView proImgTitle = new ImageView(mContext);
                        proImgTitle.setLayoutParams(paramsTitle);
                        imageList.addView(proImgTitle);
                        PicassoImageLoader.getInstance().displayImage(mContext,R.mipmap.pro_param,proImgTitle);
                        ProImage proImage = product.productParameter;
                        ImageView proImg = new ImageView(mContext);
                        proImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        double div = Arith.div(proImage.height,proImage.width,3);
                        int width = DensityUtils.getDensityWidth()-DensityUtils.dip2px(40);
                        int height = (int) (width*div);
                        params =new LinearLayout.LayoutParams(width,height);
                        params.setMargins(0,0,0,DensityUtils.dip2px(20));
                        PicassoImageLoader.getInstance().displayImage(mContext,product.productParameter.src,proImg);
                        imageList.addView(proImg,params);
                    }

                    if(product.brandStory!=null){
                        ImageView proImgTitle = new ImageView(mContext);
                        proImgTitle.setLayoutParams(paramsTitle);
                        imageList.addView(proImgTitle);
                        PicassoImageLoader.getInstance().displayImage(mContext,R.mipmap.pro_store,proImgTitle);
                        params =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        TextView storyText= new TextView(mContext);
                        storyText.setText(product.brandStory);
                        storyText.setPadding(5,0,5,0);
                        storyText.setTextSize(DensityUtils.sp2px(4));
                        storyText.setTextColor(R.color.black_text);
                        imageList.addView(storyText,params);
                    }
                    ImageView proTitle = new ImageView(mContext);
                    proTitle.setLayoutParams(paramsTitle);
                    imageList.addView(proTitle);
                    PicassoImageLoader.getInstance().displayImage(mContext,R.mipmap.pro_case,proTitle);
                    for (int i = 0; i < product.buyersShow.size(); i++) {
                        BuyersShow buyersShow = product.buyersShow.get(i);
                        View image = LayoutInflater.from(mContext).inflate(R.layout.pro_case_item
                                ,null);
                        LinearLayout tvCaseImages = image.findViewById(R.id.ll_images);
                        TextView tvCaseName = image.findViewById(R.id.tv_desc);
                        TextView tvCaseTime = image.findViewById(R.id.tv_bottom_timer);
                        tvCaseName.setText(buyersShow.showsDesc);
                        tvCaseTime.setText(buyersShow.showsTime);
                        for (int j = 0; j <buyersShow.showsImage.size(); j++) {
                            BuyersShow.Images showImages = buyersShow.showsImage.get(j);
                            View imageItem = LayoutInflater.from(mContext).inflate(R.layout.upload_image_item,null);
                            ImageView imageView =imageItem.findViewById(R.id.iv_upload_img);
                            imageItem.findViewById(R.id.iv_upload_del).setVisibility(View.GONE);
                            PicassoImageLoader.getInstance().displayImage(mContext,showImages.thumb,imageView);
                            tvCaseImages.addView(imageItem);
                        }
                        imageList.addView(image);
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long diffTime =(System.currentTimeMillis()-startTime)/1000;
        if(product==null){return;}
        ApplicationApi.postProductTime(product.productId, diffTime,new Callback<org.json.JSONObject>() {
            @Override
            public void onSuccess(org.json.JSONObject jsonObject) {

            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }
}
