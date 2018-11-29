package com.crowd.curtain.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import base.util.DensityUtils;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CateEntity;
import com.crowd.curtain.ui.customview.XHorizontalListView;
import base.util.PicassoImageLoader;

/**
 * Created by zhangpeng on 2018/3/1.
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    private List<CateEntity> lists;

    public HorizontalListViewAdapter(Context context,List<CateEntity> lists){
        this.mContext = context;
        this.lists=lists;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    public void updateData(List<CateEntity> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return lists.size();
    }
    @Override
    public CateEntity getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.cate_item, null);
//            holder.isImage=(ImageView)convertView.findViewById(R.id.type_img);
            holder.isImage= new ImageView(mContext);
            holder.isImageHight= new ImageView(mContext);
            RelativeLayout.LayoutParams parame = new RelativeLayout.LayoutParams(DensityUtils.getDensityWidth()/4, DensityUtils.dip2px(50));
            RelativeLayout.LayoutParams parame2 = new RelativeLayout.LayoutParams(DensityUtils.getDensityWidth()/4, DensityUtils.dip2px(25));
            holder.isImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.isImage.setLayoutParams(parame);
            holder.isImage.setId(R.id.type_img);
            holder.isImageHight.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.isImageHight.setLayoutParams(parame);
            holder.isImageHight.setId(R.id.type_hight_img);
            holder.isImageHight.setVisibility(View.INVISIBLE);
            ((ViewGroup)convertView).addView(holder.isImage);
            ((ViewGroup)convertView).addView(holder.isImageHight);
            parame2.addRule(RelativeLayout.BELOW,R.id.type_hight_img);
            holder.mTitle = new TextView(mContext);
            holder.mTitle.setGravity(Gravity.CENTER);
            holder.mTitle.setTextSize(DensityUtils.sp2px(4));
            holder.mTitle.setTextColor(R.color.black_text);
            holder.mTitle.setLayoutParams(parame2);
            ((ViewGroup)convertView).addView(holder.mTitle);

//            holder.mTitle=(TextView)convertView.findViewById(R.id.type_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(!lists.get(position).isAll){
            PicassoImageLoader.getInstance().displayImage(mContext,lists.get(position).activeIcon,holder.isImageHight);
            PicassoImageLoader.getInstance().displayImage(mContext,lists.get(position).inactiveIcon,holder.isImage);
            holder.mTitle.setText(lists.get(position).name);
            if(lists.get(position).isSelected()){
                holder.isImageHight.setVisibility(View.VISIBLE);
            }else {
                holder.isImageHight.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.mTitle.setText("全部");
            PicassoImageLoader.getInstance().displayImage(mContext,R.mipmap.quanbu,holder.isImage);
        }


        return convertView;
    }
    public void updateSingleRow(XHorizontalListView xHorizontalListView, CateEntity cateEntity) {

        if (xHorizontalListView != null) {
            int start = xHorizontalListView.getFirstVisiblePosition();
            for (int i = start, j = xHorizontalListView.getLastVisiblePosition(); i <= j; i++) {
                if (cateEntity.productCateId == ((CateEntity) xHorizontalListView.getItemAtPosition(i)).productCateId) {
                    View view = xHorizontalListView.getChildAt(i - start);
                    getView(i, view, xHorizontalListView);
                    break;
                }
            }
        }
    }

    private static class ViewHolder {
        private TextView mTitle ;
        private ImageView isImage;
        private ImageView isImageHight;
    }


}
