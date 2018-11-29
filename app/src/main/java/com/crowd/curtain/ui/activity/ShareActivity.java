package com.crowd.curtain.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import config.ShareApi;
import com.crowd.curtain.R;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.config.ServerConfig;

@Layout(R.layout.activity_share)
public class ShareActivity extends AppBaseActivity implements View.OnClickListener{
    private static final String SHARE_QQ = "share_qq";
    private static final String SHARE_QQ_ZONE = "share_qq_zone";
    private static final String SHARE_WX_FRIEND = "share_weixin";
    private static final String SHARE_WX_MOMENTS = "share_wx_moments";
    private static final String SHARE_WEIBO = "share_weibo";

    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvTitle;
    @Id(R.id.iv_right)
    ImageView tvRight;
    @Id(R.id.share_img)
    ImageView shareImg;
    private String title="欧曼臣";
    private String content="";
//    private String imageUrl="http://file.juzimi.com/category_pictures/201312/kasabulankajingdianyulu7352.jpg";
    private String url= ServerConfig.BASE_URL;
    private String imagePath;

    public static Intent newIntent(String img) {
        Intent it = new Intent(App.getContext(), ShareActivity.class);
        it.putExtra("imagePath",img);
        return it;
    }
    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), ShareActivity.class);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePath = getIntent().getStringExtra("imagePath");
        initView();
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("保存与分享");
        tvRight.setImageResource(R.mipmap.shouye);
        content = getString(R.string.share_content);
        if(!TextUtils.isEmpty(imagePath)){
            Picasso.with(mContext).load(new File(imagePath)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    shareImg.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }
    @Click(R.id.backIcon)
    private void backUp(){
        finish();
    }
    @Click(R.id.iv_right)
    private void toShouYe(){
        toActivity(MainActivity.newIntent());
        finish();
    }

    @Click(R.id.share_moment)
    private void toMoment(){
        //TODO 分享到朋友圈
        ShareApi.share((Activity) mContext,title,content,new File(imagePath),url, ShareApi.ShareType.WX_MOMENTS);
    }
    @Click(R.id.share_weixin)
    private void toWeixin(){
        //TODO 分享到微信
        ShareApi.share((Activity) mContext,title,content,new File(imagePath),url, ShareApi.ShareType.WX_FRIEND);
    }
    @Click(R.id.share_weibo)
    private void toWeibo(){
        //TODO 分享到微博
        ShareApi.share((Activity) mContext,title,content,new File(imagePath),url, ShareApi.ShareType.WEIBO);
    }
    @Click(R.id.share_zone)
    private void toZone(){
        //TODO 分享到QQ空间
        ShareApi.share((Activity) mContext,title,content,new File(imagePath),url, ShareApi.ShareType.QZON);
    }
    @Click(R.id.share_qq)
    private void toQQ(){
        //TODO 分享到QQ
        ShareApi.share((Activity) mContext,title,content,new File(imagePath),url, ShareApi.ShareType.QQ);
    }

}
