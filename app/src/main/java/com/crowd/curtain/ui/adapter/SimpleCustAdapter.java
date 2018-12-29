package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crowd.curtain.R;
import com.crowd.curtain.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.eventbus.EventBus;

public class SimpleCustAdapter extends BaseAdapter{

    private DelCall delCall;
    private Context context;
    List<String> historyList;
    public SimpleCustAdapter(Context mContext, List<String> historyList,DelCall delCall) {
        this.context = mContext;
        this.historyList = historyList;
        this.delCall = delCall;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_clear,null);
            viewHolder=new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.clear_text);
            viewHolder.imageView = view.findViewById(R.id.clear_del);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(historyList.get(i));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delCall.del(historyList.get(i),i);
            }
        });
        return view;
    }
}
class ViewHolder{
   TextView textView;
   ImageView imageView;

}