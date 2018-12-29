package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import base.util.DensityUtils;
import base.util.PicassoImageLoader;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CurtainEntity;
import com.crowd.curtain.ui.activity.ProductDetailActivity;
import com.crowd.curtain.ui.customview.RoundCornerImageView;

/**
 * Created by zhangpeng on 2018/2/28.
 */

public class CurtainHomeAdapter extends BaseRecyclerAdapter<CurtainEntity> {
    private List<CurtainEntity> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    public CurtainHomeAdapter(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setDatas(mItems);
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder  viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.home_curtain_item, parent, false),
                mOnItemChildClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildClickListener(mOnItemChildClickListener);
        setItemChildListener(viewHolder.getViewHolderHelper());
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CurtainEntity model) {
        View convertView = viewHolderHelper.getConvertView();
        RoundCornerImageView curImg =  convertView.findViewById(R.id.iv_curtain);
        TextView curName = (TextView)convertView.findViewById(R.id.tv_name);
        TextView curNumber = (TextView)convertView.findViewById(R.id.tv_number);
        if(model.name==null){
            return;
        }
        curImg.setRadius(DensityUtils.dip2px(7), DensityUtils.dip2px(7),0,0);
        curName.setText(model.name==null?"":model.name);
        curNumber.setText(model.number==null?"":model.number);
        PicassoImageLoader.getInstance().displayImage(mContext,model.image==null?R.mipmap.curtai:model.image,curImg);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)mContext).toActivity(ProductDetailActivity.newIntent(model.productId+""));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public int getItemPos(CurtainEntity s) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
