package com.crowd.curtain.ui.fragment;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import base.BaseFragment;
import base.http.Callback;
import base.http.ErrorModel;
import base.indicator.CirclePageIndicator;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.widget.recycler.GridSpacingItemDecoration;
import base.widget.recycler.ViewOnItemChildClickListener;
import com.crowd.curtain.R;
import com.crowd.curtain.api.HomeApi;
import com.crowd.curtain.common.model.BannerEntity;
import com.crowd.curtain.common.model.CateEntity;
import com.crowd.curtain.common.model.CurtainEntity;
import com.crowd.curtain.ui.activity.MainActivity;
import com.crowd.curtain.ui.activity.SearchActivity;
import com.crowd.curtain.ui.activity.SettingActivity;
import com.crowd.curtain.ui.adapter.BannerAdapter;
import com.crowd.curtain.ui.adapter.CateTypeAdapter;
import com.crowd.curtain.ui.adapter.CurtainHomeAdapter;
import com.crowd.curtain.ui.customview.AutoPlayViewPager;
import com.crowd.curtain.utils.AppToast;

/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
@Layout(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements NestedScrollView.OnScrollChangeListener{

    @Id(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @Id(R.id.recyclerView)
    RecyclerView mCurtainBodylist;
    @Id(R.id.curtainHeadlist)
    RecyclerView mCurtainHeadlist;
    @Id(R.id.leftIcon)
    ImageView leftIcon;
    @Id(R.id.iv_search)
    ImageView ivSearch;
    @Id(R.id.iv_toTop)
    ImageView ivTotop;
    @Id(R.id.cate_body_name)
    TextView tvBodyName;
    @Id(R.id.cate_head_name)
    TextView tvHeadName;
    @Id(R.id.main_viewpager)
    private AutoPlayViewPager autoPlayViewPage;
    @Id(R.id.indicator)
    CirclePageIndicator indicator;
    @Id(R.id.cate_body_type)
    RecyclerView cateBodyType;
    @Id(R.id.cate_head_type)
    RecyclerView cateHeadType;

    @Id(R.id.loadBodyMore)
    TextView btnBodyMore;

    @Id(R.id.loadHeadMore)
    TextView btnHeadMore;

    CurtainHomeAdapter curtainBodyAdapter;
    CurtainHomeAdapter curtainHeadAdapter;
    BannerAdapter bannerAdapter;
    private CateTypeAdapter typeBodyAdapter;
    private CateTypeAdapter typeHeadAdapter;
    private CateEntity bodyCante;
    private CateEntity headCante;

    private  final int page_size=6;
    private int currentBodyPage = 1;
    private int currentHeadPage = 1;

    @Override
    protected void initViews() {
        super.initViews();
        leftIcon.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);


        nestedScroll.setOnScrollChangeListener(this);
        nestedScroll.scrollTo(0,0);
        bannerAdapter = new BannerAdapter(mContext,autoPlayViewPage);
        autoPlayViewPage.setAdapter(bannerAdapter);
        indicator.setViewPager(autoPlayViewPage);
        autoPlayViewPage.setDirection(AutoPlayViewPager.Direction.LEFT);
        autoPlayViewPage.setCurrentItem(0);
        autoPlayViewPage.start();

        curtainBodyAdapter = new CurtainHomeAdapter(mContext);
        curtainHeadAdapter = new CurtainHomeAdapter(mContext);
        mCurtainBodylist.setNestedScrollingEnabled(false);
        mCurtainHeadlist.setNestedScrollingEnabled(false);
        mCurtainBodylist.setAdapter(curtainBodyAdapter);
        mCurtainHeadlist.setAdapter(curtainHeadAdapter);
        mCurtainBodylist.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        mCurtainHeadlist.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        LinearLayoutManager linearLayoutManagerHead = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManagerHead.setOrientation(LinearLayoutManager.HORIZONTAL);
        cateBodyType.setLayoutManager(linearLayoutManager);
        cateHeadType.setLayoutManager(linearLayoutManagerHead);
        int spacingInPixels = DensityUtils.dip2px( 5.0f);
        mCurtainBodylist.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false));

        typeBodyAdapter = new CateTypeAdapter(mContext);
        typeHeadAdapter = new CateTypeAdapter(mContext);
        cateBodyType.setAdapter(typeBodyAdapter);
        cateHeadType.setAdapter(typeHeadAdapter);
        typeBodyAdapter.setOnItemChildClickListener(new ViewOnItemChildClickListener() {
            @Override
            public void onItemChildClick(View v, int position) {
                if(position!=0) {
                    if (bodyCante == null) {
                        return;
                    }
                    if (typeBodyAdapter.getItem(position).name.equals(bodyCante.name)) {
                        return;
                    } else {
                        bodyCante.setSelected(false);
                        int oldPos = typeBodyAdapter.getItemPos(bodyCante);
                        typeBodyAdapter.notifyItemChanged(oldPos == -1 ? 0 : oldPos);
                        bodyCante = typeBodyAdapter.getItem(position);
                        bodyCante.setSelected(true);
                        int newPos = typeBodyAdapter.getItemPos(bodyCante);
                        typeBodyAdapter.notifyItemChanged(newPos == -1 ? 0 : newPos);
                        loadCurtainList(bodyCante.productCateId,1,curtainBodyAdapter,true);
                    }
                }else{
                    ((MainActivity)mContext).toFragment(MainActivity.TAG_PAGE_CLASSIES,1);
                }
            }
        });

        typeHeadAdapter.setOnItemChildClickListener(new ViewOnItemChildClickListener() {
            @Override
            public void onItemChildClick(View v, int position) {
                if(position!=0){
                    if(headCante==null){
                        return;
                    }
                    if(typeHeadAdapter.getItem(position).name.equals(headCante.name)){
                        return;
                    }else{
                        headCante.setSelected(false);
                        int oldPos = typeHeadAdapter.getItemPos(headCante);
                        typeBodyAdapter.notifyItemChanged(oldPos==-1?0:oldPos);
                        headCante = typeHeadAdapter.getItem(position);
                        headCante.setSelected(true);
                        int newPos = typeHeadAdapter.getItemPos(headCante);
                        typeBodyAdapter.notifyItemChanged(newPos==-1?0:newPos);
                        loadCurtainList(headCante.productCateId,1,curtainHeadAdapter,false);
                    }
                }else{
                    ((MainActivity)mContext).toFragment(MainActivity.TAG_PAGE_CLASSIES,2);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        HomeApi.getHomeDate(new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonpObject) {
                if(jsonpObject.containsKey("data")){
                    JSONObject dataObj = jsonpObject.getJSONObject("data");
                    List<BannerEntity> bannerList = JSON.parseArray(dataObj.getString("banner"),BannerEntity.class);
                    bannerAdapter.update(bannerList);
                    JSONArray productObj = dataObj.getJSONArray("product");
                    if(productObj.size()>0){
                        JSONObject cateBody = productObj.getJSONObject(0);
                        List<CateEntity> cateBodyList = JSON.parseArray(cateBody.getString("cate"),CateEntity.class);
                        cateBodyList.add(0,new CateEntity("","全部",true));
                        tvBodyName.setText(cateBody.getString("cateName"));
                        bodyCante = cateBodyList.size()>1?cateBodyList.get(1):null;
                        bodyCante.setSelected(true);
                        typeBodyAdapter.setDatas(cateBodyList);
                        loadCurtainList(bodyCante.productCateId,1,curtainBodyAdapter,true);
                    }
                    if(productObj.size()>1){
                        JSONObject cateHead = productObj.getJSONObject(1);
                        if(null!=cateHead){
                            List<CateEntity> cateHeadList = JSON.parseArray(cateHead.getString("cate"),CateEntity.class);
                            cateHeadList.add(0,new CateEntity("","全部",true));
                            tvHeadName.setText(cateHead.getString("cateName"));
                            headCante = cateHeadList.size()>1?cateHeadList.get(1):null;
                            headCante.setSelected(true);
                            typeHeadAdapter.setDatas(cateHeadList);
                            loadCurtainList(headCante.productCateId,1,curtainHeadAdapter,false);
                        }
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

    private void loadCurtainList(String productId,int page,CurtainHomeAdapter curtaiAdapter,boolean isBodyOrHead) {
        HomeApi.getTypeCurtain(productId,page,page_size,new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    List<CurtainEntity> curtainList = JSON.parseArray(jsonObject.getString("data"),CurtainEntity.class);
                    if(curtainList.size()>0){
                        if(isBodyOrHead){
                            currentBodyPage = page;
                            btnBodyMore.setVisibility(View.VISIBLE);
                            if(currentBodyPage==1){
                                curtaiAdapter.setDatas(curtainList);
                            }else{
                                curtaiAdapter.addMoreDatas(curtainList);
                            }
                        }else{
                            currentHeadPage = page;
                            btnHeadMore.setVisibility(View.VISIBLE);
                            if(currentHeadPage==1){
                                curtaiAdapter.setDatas(curtainList);
                            }else{
                                curtaiAdapter.addMoreDatas(curtainList);
                            }
                        }
                    }else{
                        if(currentBodyPage==1){
                            btnBodyMore.setVisibility(View.GONE);
                        }
                        if(currentHeadPage==1){
                            btnHeadMore.setVisibility(View.GONE);
                        }
                        AppToast.showFail("暂时没有更多了");
                    }

                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

    @Click(R.id.leftIcon)
    public void showMiny(){
        ((MainActivity)getActivity()).toActivity(SettingActivity.newIntent());
    }
    @Click(R.id.iv_search)
    public void toSearch(){
        ((MainActivity)getActivity()).toActivity(SearchActivity.newIntent());
    }
    @Click(R.id.iv_toTop)
    public void scrollToTop(){
        nestedScroll.scrollTo(0,0);
    }
    @Click(R.id.loadBodyMore)
    public void loadBodyMore(){
        loadCurtainList(bodyCante.productCateId,currentBodyPage+1,curtainBodyAdapter,true);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY&&scrollY>50) {
            // 向下滑动
            ivTotop.setVisibility(View.VISIBLE);
        }
        if (scrollY < oldScrollY) {
            // 向上滑动
        }
        if (scrollY == 0) {
            // 顶部
            ivTotop.setVisibility(View.GONE);
        }
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            // 底部
        }
    }
    }
