package base.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/12/22.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected Context mContext;
    protected ViewOnItemChildClickListener mOnVItemClickListener;
    protected ViewHolderHelper mViewHolderHelper;
    public int itemType;

    public BaseRecyclerViewHolder(View itemView, int itemType, ViewOnItemChildClickListener onRVItemClickListener) {
        super(itemView);
        mContext = itemView.getContext();
        mOnVItemClickListener = onRVItemClickListener;
        itemView.setOnClickListener(this);
        mViewHolderHelper = new ViewHolderHelper(this.itemView);
        mViewHolderHelper.setRecyclerViewHolder(this);
        mViewHolderHelper.setOnItemChildClickListener(onRVItemClickListener);
        this.itemType = itemType;
    }

    public BaseRecyclerViewHolder(View itemView,ViewOnItemChildClickListener onRVItemClickListener) {
        super(itemView);
        mContext = itemView.getContext();
        mOnVItemClickListener = onRVItemClickListener;
        itemView.setOnClickListener(this);
        mViewHolderHelper = new ViewHolderHelper(this.itemView);
        mViewHolderHelper.setRecyclerViewHolder(this);
        mViewHolderHelper.setOnItemChildClickListener(onRVItemClickListener);
    }

    public ViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnVItemClickListener) {
            mOnVItemClickListener.onItemChildClick(v, getAdapterPosition());
        }
    }

}
