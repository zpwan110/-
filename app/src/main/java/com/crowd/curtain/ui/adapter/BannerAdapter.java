/*
 * AUTHOR：Yolanda
 * 
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import base.BaseActivity;
import base.util.PicassoImageLoader;
import com.crowd.curtain.common.model.BannerEntity;
import com.crowd.curtain.ui.activity.ProductDetailActivity;

/**
 * Created by Yolanda on 2016/5/5.
 *
 * @author Yolanda; QQ: 757699476
 */
public class BannerAdapter extends PagerAdapter {

    private ViewPager pageView;
    private Context mContext;

    private List<BannerEntity> bannerList;

    public BannerAdapter(Context context, ViewPager pageView) {
        this.mContext = context;
        this.pageView = pageView;
    }

    public void update(List<BannerEntity> resIds) {
        if (this.bannerList != null)
        {this.bannerList.clear();}
        if (resIds != null)
        {this.bannerList = resIds;}
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return bannerList == null ? 0 : bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        PicassoImageLoader.getInstance().displayImage(mContext,bannerList.get(position%bannerList.size()).image,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)mContext).toActivity(ProductDetailActivity.newIntent(bannerList.get(position).bannerId,bannerList.get(position).productId));
            }
        });
        container.addView(imageView);
        return imageView;
    }

}
