package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import base.util.DateUtils;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.MessageModel;

/**
 * Created by zhangpeng on 2018/4/6.
 */

public class MessageAdapter extends BaseRecyclerAdapter<MessageModel> {
    private List<MessageModel> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private String  statue;
    public MessageAdapter(Context context) {
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
        BaseRecyclerViewHolder  viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.message_item, parent, false),
                mOnItemChildClickListener);
        setItemChildListener(viewHolder.getViewHolderHelper());
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MessageModel model) {
        View convertView = viewHolderHelper.getConvertView();
        TextView titleText = convertView.findViewById(R.id.msg_title);
        TextView contentText = convertView.findViewById(R.id.msg_content);
        TextView topTimer = convertView.findViewById(R.id.msg_time);
        titleText.setText(model.title+"");
        contentText.setText(model.details+"");
        topTimer.setText(DateUtils.timesTwo(model.time)+"");
    }
    @Override
    public int getItemPos(MessageModel s) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
