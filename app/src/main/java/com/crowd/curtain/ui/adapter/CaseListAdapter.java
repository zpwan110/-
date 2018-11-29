package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.imagezoom.util.ImageZoom;
import base.util.DateUtils;
import base.util.PicassoImageLoader;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CaseEntity;
import com.crowd.curtain.ui.fragment.CaseFragment;

/**
 * Created by zhangpeng on 2018/2/28.
 */

public class CaseListAdapter extends BaseRecyclerAdapter<CaseEntity> implements View.OnClickListener{
    private List<CaseEntity> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private String  statue;
    public CaseListAdapter(Context context,String statue) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.statue = statue;
        setDatas(mItems);
    }
//    public List<CaseEntity> mItems = new ArrayList<CaseEntity>(
//            Arrays.asList(
//                    new CaseEntity(),
//                    new CaseEntity(),
//                    new CaseEntity(),
//                    new CaseEntity(),
//                    new CaseEntity(),
//                    new CaseEntity(),
//                    new CaseEntity()
//            ));

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder  viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.case_item, parent, false),
                mOnItemChildClickListener);
        setItemChildListener(viewHolder.getViewHolderHelper());
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CaseEntity model) {
        View convertView = viewHolderHelper.getConvertView();
        TextView topText = convertView.findViewById(R.id.tv_top_text);
        TextView topTimer = convertView.findViewById(R.id.tv_top_timer);
        TextView caseDesc = convertView.findViewById(R.id.tv_desc);
        TextView botoomText = convertView.findViewById(R.id.tv_bottom_text);
        LinearLayout imagesView = convertView.findViewById(R.id.ll_images);
        TextView bottomTimer = convertView.findViewById(R.id.tv_bottom_timer);
        TextView faileDes = convertView.findViewById(R.id.tv_faile_desc);
        TextView tvUpload = convertView.findViewById(R.id.tv_upload);
        TextView tvDelete = convertView.findViewById(R.id.tv_delete);
        LinearLayout llBotoom = convertView.findViewById(R.id.ll_bottom);
        LinearLayout llTop = convertView.findViewById(R.id.ll_top);
        if(model!=null){
            caseDesc.setText(model.desc);
            if(statue.equals(CaseFragment.ADPOT)){
                llBotoom.setVisibility(View.VISIBLE);
                llTop.setVisibility(View.VISIBLE);
                faileDes.setText(model.reason+"");
                topText.setText("编号:"+model.number);
                topTimer.setText(DateUtils.timesTwo(model.time));
                tvUpload.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
            }else{
                llBotoom.setVisibility(View.GONE);
                llTop.setVisibility(View.VISIBLE);
                topText.setText("编号:"+model.number);
                topTimer.setText(DateUtils.timesTwo(model.time));
                tvUpload.setVisibility(View.GONE);
                tvDelete.setVisibility(View.GONE);
            }
            imagesView.removeAllViews();
            for (int i = 0; i < model.image.size(); i++) {
                View image = LayoutInflater.from(mContext).inflate(R.layout.upload_image_item,null);
                ImageView imageView =image.findViewById(R.id.iv_upload_img);
                imageView.setTag(new int[]{position,i});
                imageView.setOnClickListener(CaseListAdapter.this);
                ImageView imageDel =image.findViewById(R.id.iv_upload_del);
                imageDel.setVisibility(View.GONE);
                PicassoImageLoader.getInstance().displayImage(mContext,model.image.get(i).thumb,imageView);
                imagesView.addView(image);
            }
        }
    }

    @Override
    protected void setItemChildListener(ViewHolderHelper viewHolderHelper) {
        super.setItemChildListener(viewHolderHelper);
        if(viewHolderHelper.getConvertView().findViewById(R.id.tv_delete)!=null){
            viewHolderHelper.setItemChildClickListener(R.id.tv_delete);
        }
        if(viewHolderHelper.getConvertView().findViewById(R.id.tv_upload)!=null){
            viewHolderHelper.setItemChildClickListener(R.id.tv_upload);
        }
    }

    @Override
    public int getItemPos(CaseEntity s) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        int[] indexs = (int[]) v.getTag();
        ImageZoom.show(mContext, indexs[1], getDatas().get(indexs[0]).getThumbList());
    }
}
