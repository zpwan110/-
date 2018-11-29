package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anet.channel.util.StringUtils;
import base.BaseActivity;
import base.util.DensityUtils;
import base.util.PicassoImageLoader;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CurtainSearch;
import com.crowd.curtain.ui.activity.ProductDetailActivity;
import com.crowd.curtain.ui.customview.RoundCornerImageView;

/**
 * Created by zhangpeng on 2018/3/19.
 */

public class CurtainSearchAdapter extends BaseRecyclerAdapter<CurtainSearch> {
    private List<CurtainSearch> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    public CurtainSearchAdapter(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setDatas(mItems);
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder  viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.curtai_search, parent, false),
                mOnItemChildClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildClickListener(mOnItemChildClickListener);
        setItemChildListener(viewHolder.getViewHolderHelper());
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CurtainSearch model) {
        View convertView = viewHolderHelper.getConvertView();
        RoundCornerImageView curtainThumb =  convertView.findViewById(R.id.iv_curtain_thumb);
        curtainThumb.setRadius(DensityUtils.dip2px(7), DensityUtils.dip2px(7),DensityUtils.dip2px(7),DensityUtils.dip2px(7));
        TextView curtainName = (TextView) convertView.findViewById(R.id.tv_curtain_name);
        TextView curtainNum = (TextView)convertView.findViewById(R.id.tv_curtain_num);
        if(model!=null){
            curtainName.setText(model.productName);
            curtainNum.setText("编号:"+model.number);
            convertView.setOnClickListener(v -> ((BaseActivity)mContext).toActivity(ProductDetailActivity.newIntent(model.productId)));
        }
        if(StringUtils.isNotBlank(model.thumb)){
            PicassoImageLoader.getInstance().displayImage(mContext,model.thumb,curtainThumb);
        }

    }

    @Override
    public int getItemPos(CurtainSearch s) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
