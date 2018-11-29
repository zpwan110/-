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

import java.util.ArrayList;
import java.util.List;

import base.imagezoom.util.ImageZoom;
import base.util.PicassoImageLoader;
import com.crowd.curtain.common.model.ProImage;

/**
 * Created by Yolanda on 2016/5/5.
 *
 * @author Yolanda; QQ: 757699476
 */
public class ProBannerAdapter extends PagerAdapter{

    private ViewPager pageView;
    private Context mContext;

    private List<ProImage> bannerList;
    List<String> pathList = new ArrayList<>();

    public ProBannerAdapter(Context context, ViewPager pageView) {
        this.mContext = context;
        this.pageView = pageView;
    }

    public void update(List<ProImage> resIds) {
        if (this.bannerList != null)
        {this.bannerList.clear();}
        if (resIds != null)
        {this.bannerList = resIds;}
        this.pathList.clear();
        for (int i = 0; i < bannerList.size(); i++) {
            pathList.add(bannerList.get(i).src);
        }
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
        PicassoImageLoader.getInstance().displayImage(mContext,bannerList.get(position%bannerList.size()).src,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setOnClickListener(v -> ImageZoom.show(mContext, position, pathList));
        container.addView(imageView);
        return imageView;
    }

}
