package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.widget.edittext.ClearEditText;
import base.widget.recycler.BaseRefreshLayout;
import base.widget.recycler.LinearSpacingItemDecoration;
import com.crowd.curtain.R;
import com.crowd.curtain.api.MessageApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.MessageModel;
import com.crowd.curtain.ui.adapter.MessageAdapter;

import static base.widget.recycler.BaseRefreshLayout.LOADMORE;
import static base.widget.recycler.BaseRefreshLayout.REFRESH;

/**
 * Created by zhangpeng on 2018/4/6.
 */
@Layout(R.layout.activity_message)
public class MessageActivity extends AppBaseActivity implements BaseRefreshLayout.RefreshLayoutDelegate{
    @Id(R.id.backIcon)
    ImageView btnBack;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.et_login_mobile)
    ClearEditText tvAccount;
    @Id(R.id.et_login_password)
    ClearEditText tvPwd;
    @Id(R.id.messageParent)
    BaseRefreshLayout messageLayout;
    @Id(R.id.recyclerMessage)
    RecyclerView messageList;
    private MessageAdapter recyclerAdapter;

    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), MessageActivity.class);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("消息中心");
        messageLayout.setNetworkAnomalyView(null, false);
        recyclerAdapter= new MessageAdapter(mContext);
        messageLayout.setDelegate(this);
        messageList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        messageList.addItemDecoration(new LinearSpacingItemDecoration(DensityUtils.dip2px(15)));
        messageList.setAdapter(recyclerAdapter);
        getMessage(1);
    }

    private void getMessage(int page) {
        MessageApi.getMessageDate(page, BaseRefreshLayout.SIZE, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    String dataObj = jsonObject.getString("data");
                    List<MessageModel> messageList = JSON.parseArray(dataObj,MessageModel.class);
                    if(messageLayout.refereshStatue.equals(REFRESH)){
                        recyclerAdapter.setDatas(messageList);
                        messageLayout.endRefreshing();
                        messageLayout.page = 1;
                    }
                    if(messageLayout.refereshStatue.equals(LOADMORE)){
                        recyclerAdapter.addMoreDatas(messageList);
                        messageLayout.endLoadingMore();
                        messageLayout.page = page;
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {
                if(messageLayout.refereshStatue.equals(REFRESH)){
                    messageLayout.endRefreshing();
                }
                if(messageLayout.refereshStatue.equals(LOADMORE)){
                    messageLayout.endLoadingMore();
                }
            }
        });
    }

    @Click(R.id.backIcon)
    private void backUp(){
        finish();
    }


    @Override
    public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
        refreshLayout.refereshStatue = REFRESH;
        getMessage(1);
    }

    @Override
    public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
        refreshLayout.hasMoreData(refreshLayout.isHasMore());
        if(refreshLayout.isHasMore()){
            refreshLayout.refereshStatue = LOADMORE;
            getMessage(++refreshLayout.page);
            return true;
        }

        return false;
    }
}
