package com.crowd.curtain.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.crowd.curtain.R;
import com.crowd.curtain.common.model.CurtainInfo;
import com.crowd.curtain.common.model.CurtainType;

/**
 * Created by zhangpeng on 2018/3/19.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<CurtainType> curtainTypeList = new ArrayList();


    public ExpandableListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setDatas(List<CurtainType> curtainTypeList){
        if(curtainTypeList!=null&&curtainTypeList.size()>0){
            this.curtainTypeList = curtainTypeList;
            notifyDataSetChanged();
        }
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int parentPos, int childPos) {
        return curtainTypeList.get(parentPos).cates.get(childPos);
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return curtainTypeList.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int parentPos) {
        return curtainTypeList.get(parentPos).cates.size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int parentPos) {
        return curtainTypeList.get(parentPos);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int parentPos, int childPos) {
        return childPos;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int parentPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_view, null);
        }
        view.setTag(R.layout.expandable_list_view, parentPos);
        view.setTag(R.layout.expandable_list_view_child, -1);
        TextView text = (TextView) view.findViewById(R.id.parent_title);
        text.setText(curtainTypeList.get(parentPos).parentCateName);
        return view;
    }

    //  获得子项显示的view
    @SuppressLint("ResourceAsColor")
    @Override
    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_view_child, null);
        }
        view.setTag(R.layout.expandable_list_view, parentPos);
        view.setTag(R.layout.expandable_list_view_child, childPos);
        TextView text = (TextView) view.findViewById(R.id.child_title);
        CurtainInfo curtainInfo = (CurtainInfo) getChild(parentPos,childPos);
        text.setText(curtainInfo.cateName);
        Resources resources= mContext.getResources();
        if(curtainInfo.select){
            view.setBackgroundResource(R.color.text_bg_color);
            text.setTextColor(resources.getColor(R.color.text_color));
        }else{
            view.setBackgroundResource(R.color.text_color);
            text.setTextColor(resources.getColor(R.color.text_selecte_color));
        }
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public List<CurtainType> getDatas() {
        return curtainTypeList;
    }
}
