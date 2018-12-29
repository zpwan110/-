package com.crowd.curtain.ui.fragment;

import android.app.AlertDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import base.BaseActivity;
import base.BaseFragment;
import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.widget.recycler.BaseRefreshLayout;
import base.widget.recycler.LinearSpacingItemDecoration;
import base.widget.recycler.ViewOnItemChildClickListener;
import base.widget.searchview.RecordSQLiteOpenHelper;

import com.crowd.curtain.R;
import com.crowd.curtain.api.SearchApi;
import com.crowd.curtain.common.model.CurtainInfo;
import com.crowd.curtain.common.model.CurtainSearch;
import com.crowd.curtain.common.model.CurtainType;
import com.crowd.curtain.ui.activity.SearchActivity;
import com.crowd.curtain.ui.adapter.CurtainSearchAdapter;
import com.crowd.curtain.ui.adapter.ExpandableListViewAdapter;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static base.widget.recycler.BaseRefreshLayout.LOADMORE;
import static base.widget.recycler.BaseRefreshLayout.REFRESH;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
@Layout(R.layout.fragment_classify)
public class ClassisFragment extends BaseFragment implements ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener,ViewOnItemChildClickListener,BaseRefreshLayout.RefreshLayoutDelegate{

    @Id(R.id.expandablelist)
    ExpandableListView expandableListView;
    @Id(R.id.refreshCurtain)
    BaseRefreshLayout baseRefreshLayout;
    @Id(R.id.recyclerCurtain)
    RecyclerView curtaiRecycler;
    @Id(R.id.edt_search)
    private EditText etSearch;
    @Id(R.id.unleft)
    private ImageView unleft;
    @Id(R.id.classes_title)
    private TextView classesTitle;
    @Id(R.id.unRight)
    private TextView unRight;
    @Id(R.id.iv_search)
    private ImageView ivSearch;

    ExpandableListViewAdapter expandableListViewAdapter;
    public int groupIndex=0;
    public int childIndex =0;
    private CurtainSearchAdapter curtainSearchAdapter;
    private String currentNum="";
    private String currentCate="";
    private int cateType=-1;


    @Override
    protected void initViews() {
        super.initViews();
        classesTitle.setText("分类");
        curtainSearchAdapter = new CurtainSearchAdapter(mContext);
        curtaiRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        curtaiRecycler.setAdapter(curtainSearchAdapter);

        curtainSearchAdapter.setOnItemChildClickListener(this);
        baseRefreshLayout.setNetworkAnomalyView(null, false);
        baseRefreshLayout.setDelegate(this);
        DividerItemDecoration divider = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.custom_divider));
        curtaiRecycler.addItemDecoration(divider);

        expandableListViewAdapter = new ExpandableListViewAdapter(mContext);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnGroupClickListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setGroupIndicator(null);
        getCurtainType();
        initSearch();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        cateType = args.getInt("cateType");
        getCurtainType();
    }
    @Click(R.id.iv_search)
    public void showSearchView(){
        unleft.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.VISIBLE);
        unRight.setVisibility(View.VISIBLE);
        classesTitle.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);
    }
    @Click(R.id.unRight)
    public void showTitleView(){
        unleft.setVisibility(View.INVISIBLE);
        etSearch.setVisibility(View.GONE);
        unRight.setVisibility(View.INVISIBLE);
        classesTitle.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);
    }
    private void getCurtainType(){
        SearchApi.getCateList(new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    String dataStr = jsonObject.getString("data");
                    List<CurtainType> dataObj = JSON.parseArray(dataStr, CurtainType.class);
                    if(dataObj.size()>0){
                        expandableListViewAdapter.setDatas(dataObj);
                        if(cateType==2&&dataObj.size()>1){
                            dataObj.get(1).cates.get(0).select =true;
                            groupIndex = 1;
                            childIndex = 0;
                            expandableListView.expandGroup(1,true);
                            expandableListView.collapseGroup(0);
                            currentNum = dataObj.get(1).cates.get(0).cateId;
                        }else {
                            dataObj.get(0).cates.get(0).select =true;
                            groupIndex = 0;
                            childIndex = 0;
                            expandableListView.expandGroup(0,true);
                            expandableListView.collapseGroup(1);
                            currentNum = dataObj.get(0).cates.get(0).cateId;
                        }
                        searchCurtain(1,currentNum,"",REFRESH);
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

    private void toSearchView(){
        ((BaseActivity)getActivity()).toActivity(SearchActivity.newIntent());
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if(0 == childIndex&groupIndex==groupPosition){
            return false;
        }
        CurtainInfo curtainInfo = expandableListViewAdapter.getDatas().get(groupPosition).cates.get(0);
        expandableListViewAdapter.getDatas().get(groupIndex).cates.get(childIndex).select =false;
        expandableListViewAdapter.getDatas().get(groupPosition).cates.get(0).select =true;
        expandableListView.collapseGroup(groupIndex);
        expandableListView.expandGroup(groupPosition,true);
        expandableListViewAdapter.notifyDataSetChanged();
        groupIndex = groupPosition;
        childIndex = 0;
        currentNum = curtainInfo.cateId;
        searchCurtain(1,curtainInfo.cateId,"",REFRESH);
        return true;
    }
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if(childPosition == childIndex&groupIndex==groupPosition){
            return false;
        }
        CurtainInfo curtainInfo = expandableListViewAdapter.getDatas().get(groupPosition).cates.get(childPosition);
        curtainInfo.select =true;
        expandableListViewAdapter.getDatas().get(groupIndex).cates.get(childIndex).select =false;
        expandableListViewAdapter.notifyDataSetChanged();
        groupIndex = groupPosition;
        childIndex = childPosition;
        currentNum = curtainInfo.cateId;
        searchCurtain(1,currentNum,"",REFRESH);
        return true;
    }


    private void searchCurtain(int pageNum,String num,String cate,String status) {
        SearchApi.getSearchCurtain(pageNum,BaseRefreshLayout.SIZE,num,cate,new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    String dataObj = jsonObject.getString("data");
                    List<CurtainSearch>  curtainSearchList = JSON.parseArray(dataObj,CurtainSearch.class);
                    if(status.equals(REFRESH)){
                        curtainSearchAdapter.setDatas(curtainSearchList);
                        baseRefreshLayout.endRefreshing();
                        baseRefreshLayout.page = 1;
                        baseRefreshLayout.refereshStatue = REFRESH;
                    }
                    if(status.equals(LOADMORE)){
                        curtainSearchAdapter.addMoreDatas(curtainSearchList);
                        baseRefreshLayout.endLoadingMore();
                        baseRefreshLayout.page = pageNum;
                        baseRefreshLayout.refereshStatue = LOADMORE;
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {
                if(status.equals(REFRESH)){
                    baseRefreshLayout.endRefreshing();
                }
                if(status.equals(LOADMORE)){
                    baseRefreshLayout.endLoadingMore();
                }
            }
        });
    }
    public void initSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            // 输入文本后调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                currentCate = etSearch.getText().toString();
                searchCurtain(1,currentNum,currentCate,REFRESH);
            }
        });
    }
    @Override
    public void onItemChildClick(View v, int position) {

    }

    @Override
    public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
        searchCurtain(1,currentNum,currentCate,REFRESH);
    }

    @Override
    public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
        refreshLayout.hasMoreData(refreshLayout.isHasMore());
        if(refreshLayout.isHasMore()){
            searchCurtain(++refreshLayout.page,currentNum,currentCate,LOADMORE);
            return true;
        }
        return false;
    }
}
