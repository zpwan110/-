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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Id(R.id.hotCurtain)
    RecyclerView mCurtainHotlist;
    @Id(R.id.commandCurtain)
    RecyclerView mCurtainComlist;
    @Id(R.id.tv_center_title)
    TextView centerText;
    @Id(R.id.leftIcon)
    ImageView leftIcon;
    @Id(R.id.iv_search)
    ImageView ivSearch;
    @Id(R.id.iv_right)
    ImageView ivRight;
    @Id(R.id.iv_toTop)
    ImageView ivTotop;
    @Id(R.id.main_viewpager)
    private AutoPlayViewPager autoPlayViewPage;
    @Id(R.id.indicator)
    CirclePageIndicator indicator;
    @Id(R.id.cate_type)
    RecyclerView cateType;
    @Id(R.id.hotCurtain)
    RecyclerView cateHotType;
    @Id(R.id.commandCurtain)
    RecyclerView cateComType;

    CurtainHomeAdapter curtainHotAdapter;
    CurtainHomeAdapter curtainComAdapter;
    BannerAdapter bannerAdapter;
    private CateTypeAdapter typeAdapter;
    private int selectPos=0;

    @Override
    protected void initViews() {
        super.initViews();
        leftIcon.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        centerText.setText(R.string.app_name);

        nestedScroll.setOnScrollChangeListener(this);
        nestedScroll.scrollTo(0,0);
        bannerAdapter = new BannerAdapter(mContext,autoPlayViewPage);
        autoPlayViewPage.setAdapter(bannerAdapter);
        indicator.setViewPager(autoPlayViewPage);
        autoPlayViewPage.setDirection(AutoPlayViewPager.Direction.LEFT);
        autoPlayViewPage.setCurrentItem(0);
        autoPlayViewPage.start();

        curtainHotAdapter = new CurtainHomeAdapter(mContext);
        curtainComAdapter = new CurtainHomeAdapter(mContext);
        mCurtainHotlist.setNestedScrollingEnabled(false);
        mCurtainComlist.setNestedScrollingEnabled(false);
        mCurtainHotlist.setAdapter(curtainHotAdapter);
        mCurtainComlist.setAdapter(curtainComAdapter);
        mCurtainHotlist.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        mCurtainComlist.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        LinearLayoutManager linearLayoutManagerHead = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManagerHead.setOrientation(LinearLayoutManager.HORIZONTAL);
        cateType.setLayoutManager(linearLayoutManager);

        int spacingInPixels = DensityUtils.dip2px( 5.0f);
        mCurtainHotlist.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false));
        mCurtainComlist.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false));

        cateHotType.setAdapter(curtainHotAdapter);
        cateComType.setAdapter(curtainComAdapter);
        typeAdapter = new CateTypeAdapter(mContext);
        cateType.setAdapter(typeAdapter);
        typeAdapter.setOnItemChildClickListener(new ViewOnItemChildClickListener() {
            @Override
            public void onItemChildClick(View v, int position) {
                if (typeAdapter.getItem(position).name.equals(typeAdapter.getItem(selectPos).name)) {
                    return;
                } else {
                    typeAdapter.getItem(selectPos).setSelect(false);
                    typeAdapter.getItem(position).setSelect(true);
                    loadCurtainList(typeAdapter.getItem(position).id);
                    typeAdapter.notifyDataSetChanged();
                    selectPos = position;
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
                    JSONArray cates = dataObj.getJSONArray("product");
                    JSONObject cate = cates.getJSONObject(0);
                    List<CateEntity> cateList = JSON.parseArray(cate.getString("cate"),CateEntity.class);
                    if(cateList!=null&&cateList.size()>0){
                        cateList.get(0).setSelect(true);
                        typeAdapter.setDatas(cateList);
                        loadCurtainList(cateList.get(0).id);
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

    private void loadCurtainList(String productId) {
        curtainHotAdapter.clear();
        curtainComAdapter.clear();
        HomeApi.getTypeCurtain(productId,new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    List<CurtainEntity> hotList = JSON.parseArray(dataObj.getString("hot"),CurtainEntity.class);
                    List<CurtainEntity> comList = JSON.parseArray(dataObj.getString("special"),CurtainEntity.class);
                    if(hotList.size()>0){
                        curtainHotAdapter.setDatas(hotList);
                    }
                    if(comList.size()>0){
                        curtainComAdapter.setDatas(comList);
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
