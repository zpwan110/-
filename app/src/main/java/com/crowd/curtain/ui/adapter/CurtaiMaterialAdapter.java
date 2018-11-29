package com.crowd.curtain.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.util.PicassoImageLoader;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CurtainMaterail;

/**
 * Created by zhangpeng on 2018/3/2.
 */

public class CurtaiMaterialAdapter extends BaseRecyclerAdapter<CurtainMaterail> {
    private List<CurtainMaterail> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    public CurtaiMaterialAdapter(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setDatas(mItems);
    }
//        public List<CurtainMaterail> mItems = new ArrayList<CurtainMaterail>(
//            Arrays.asList(
//                    new CurtainMaterail(),
//                    new CurtainMaterail(),
//                    new CurtainMaterail(),
//                    new CurtainMaterail(),
//                    new CurtainMaterail(),
//                    new CurtainMaterail(),
//                    new CurtainMaterail()));
    @SuppressLint("ResourceAsColor")
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.material_item ,parent, false),mOnItemChildClickListener);
        View contentview = viewHolder.getViewHolderHelper().getConvertView();
        if(contentview.findViewById(R.id.iv_materail_img)!=null){
            viewHolder.getViewHolderHelper().setItemChildClickListener(R.id.iv_materail_img);
        }
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CurtainMaterail model) {
        View convertView = viewHolderHelper.getConvertView();
        ImageView cateGrayImg =  convertView.findViewById(R.id.iv_materail_img);
        TextView cateHightImg = convertView.findViewById(R.id.tv_materail_num);
        PicassoImageLoader.getInstance().displayImage(mContext,model.materialImage,cateGrayImg);
        cateHightImg.setText(model.number);
    }

    @Override
    public int getItemPos(CurtainMaterail s) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
