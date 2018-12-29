package com.crowd.curtain.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.util.DensityUtils;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CateEntity;
import base.util.PicassoImageLoader;

/**
 * Created by zhangpeng on 2018/3/2.
 */

public class CateTypeAdapter extends BaseRecyclerAdapter<CateEntity> {
    private List<CateEntity> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    public CateTypeAdapter(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setDatas(mItems);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.cate_item ,parent, false),mOnItemChildClickListener);
        ImageView isImage= new ImageView(mContext);
        ImageView isImageHight= new ImageView(mContext);
        RelativeLayout.LayoutParams parame = new RelativeLayout.LayoutParams(DensityUtils.getDensityWidth()/4, DensityUtils.dip2px(50));
        RelativeLayout.LayoutParams parame2 = new RelativeLayout.LayoutParams(DensityUtils.getDensityWidth()/4, DensityUtils.dip2px(25));
        isImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        isImage.setLayoutParams(parame);
        isImage.setId(R.id.type_img);
        isImageHight.setScaleType(ImageView.ScaleType.FIT_CENTER);
        isImageHight.setLayoutParams(parame);
        isImageHight.setId(R.id.type_hight_img);
        isImageHight.setVisibility(View.INVISIBLE);
        ((ViewGroup)viewHolder.itemView).addView(isImage);
        ((ViewGroup)viewHolder.itemView).addView(isImageHight);
        parame2.addRule(RelativeLayout.BELOW,R.id.type_hight_img);
        TextView mTitle = new TextView(mContext);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setTextSize(DensityUtils.sp2px(4));
        mTitle.setTextColor(R.color.black_text);
        mTitle.setLayoutParams(parame2);
        mTitle.setId(R.id.type_name);
        ((ViewGroup)viewHolder.itemView).addView(mTitle);
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CateEntity model) {
        View convertView = viewHolderHelper.getConvertView();
        ImageView cateGrayImg =  convertView.findViewById(R.id.type_img);
        ImageView cateHightImg = convertView.findViewById(R.id.type_hight_img);
        TextView cateName = convertView.findViewById(R.id.type_name);
        PicassoImageLoader.getInstance().displayImage(mContext,"https://gxqfz.omclml.com/thumb/"+model.active_icon,cateHightImg);
        PicassoImageLoader.getInstance().displayImage(mContext,"https://gxqfz.omclml.com/thumb/"+model.inactive_icon,cateGrayImg);
        cateName.setText(model.name);
        if(model.isSelect()){
            cateHightImg.setVisibility(View.VISIBLE);
        }else {
            cateHightImg.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemPos(CateEntity s) {
        for (int i = 0; i < mDatas.size(); i++) {
            if(s.getId().equals(mDatas.get(i).getId())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
