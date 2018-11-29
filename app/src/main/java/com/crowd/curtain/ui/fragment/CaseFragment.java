package com.crowd.curtain.ui.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;
import base.BaseFragment;
import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.util.ToastUtil;
import base.widget.recycler.BaseRefreshLayout;
import base.widget.recycler.ViewOnItemChildClickListener;
import com.crowd.curtain.R;
import com.crowd.curtain.api.CaseApi;
import com.crowd.curtain.common.model.CaseEntity;
import com.crowd.curtain.ui.activity.LoginActivity;
import com.crowd.curtain.ui.activity.UploadCaseActivity;
import com.crowd.curtain.ui.adapter.CaseListAdapter;
import com.crowd.curtain.utils.AppSharePerference;

import static base.widget.recycler.BaseRefreshLayout.LOADMORE;
import static base.widget.recycler.BaseRefreshLayout.REFRESH;


/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */
@Layout(R.layout.fragment_case)
public class CaseFragment extends BaseFragment implements ViewOnItemChildClickListener{
    public static final String ADPOT = "2";
    public static final String UPLOADED = "1";
    public static final String WAITING = "0";

    @Id(R.id.contentLayout)
    ViewPager fragmentPager;
    @Id(R.id.cursor)
    ImageView cursor;
    @Id(R.id.tv_center_title)
    TextView tvCenterTitle;
    @Id(R.id.tv_unadopt)
    TextView tvUnadopt;
    @Id(R.id.tv_waiting)
    TextView tv_waiting;
    @Id(R.id.tv_uploaded)
    TextView tvUploaded;
    @Id(R.id.iv_right)
    ImageView ivUpload;


    private BaseRefreshLayout unadpotLayout;
    private BaseRefreshLayout waitingLayout;
    private BaseRefreshLayout uploadedLayout;
    private RecyclerView recyclerUnadpot;
    private RecyclerView waitingRecyaler;
    private RecyclerView recyclerUploaded;
    private int bmpW;
    private int offset;
    private int currIndex=0;
    private PagerAdapter pagAdapter;
    private List<View> listView = new ArrayList<>();
    private View uploadedView;
    private View unadpotView;

    private CaseListAdapter mUnadpotAdapter;
    private CaseListAdapter mUploadedAdapter;
    private CaseListAdapter mWaitingAdapter;
    private View waitingView;


    @Override
    protected void initViews() {
        super.initViews();
        ivUpload.setImageResource(R.mipmap.icon_up);
        ivUpload.setVisibility(View.VISIBLE);
        tvCenterTitle.setVisibility(View.VISIBLE);
        tvCenterTitle.setText("实景案例");

        listView = new ArrayList<View>();
        uploadedView = LayoutInflater.from(mContext).inflate(R.layout.refresh_layout,null);
        uploadedLayout = uploadedView.findViewById(R.id.refreshParent);
        recyclerUploaded = uploadedView.findViewById(R.id.recyclerContent);

        waitingView = LayoutInflater.from(mContext).inflate(R.layout.refresh_layout,null);
        waitingLayout = waitingView.findViewById(R.id.refreshParent);
        waitingRecyaler = waitingView.findViewById(R.id.recyclerContent);

        unadpotView = LayoutInflater.from(mContext).inflate(R.layout.refresh_layout,null);
        unadpotLayout = unadpotView.findViewById(R.id.refreshParent);
        recyclerUnadpot = unadpotView.findViewById(R.id.recyclerContent);
        listView.add(uploadedView);
        listView.add(waitingLayout);
        listView.add(unadpotView);
        pagAdapter = new MyPagerAdapter(listView);
        initImageView();
        fragmentPager.setAdapter(pagAdapter);
        fragmentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changStatue(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initRefrensh();
    }

    private void initImageView() {
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.ico_cours).getWidth();// 获取图片宽度
        int screenW = DensityUtils.getDensityWidth();// 获取分辨率宽度
        offset = (screenW /pagAdapter.getCount() - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }
    @Click(R.id.tv_uploaded)
    private void changeUploaded(){
        fragmentPager.setCurrentItem(0);
    }
    @Click(R.id.tv_waiting)
    private void changeWaiting(){
        fragmentPager.setCurrentItem(1);
    }
    @Click(R.id.tv_unadopt)
    private void changeUnadpot(){
        fragmentPager.setCurrentItem(2);
    }

    private void changStatue(int state) {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * pagAdapter.getCount();// 页卡1 -> 页卡3 偏移量
        Animation animation = null;
        switch (state) {
            case 0:
                tvUploaded.setTextColor(Color.parseColor("#379dfd"));
                tv_waiting.setTextColor(Color.parseColor("#5d5d5d"));
                tvUnadopt.setTextColor(Color.parseColor("#5d5d5d"));
                break;
            case 1:
                tvUploaded.setTextColor(Color.parseColor("#5d5d5d"));
                tv_waiting.setTextColor(Color.parseColor("#379dfd"));
                tvUnadopt.setTextColor(Color.parseColor("#5d5d5d"));
                break;
            case 2:
                tvUploaded.setTextColor(Color.parseColor("#5d5d5d"));
                tv_waiting.setTextColor(Color.parseColor("#5d5d5d"));
                tvUnadopt.setTextColor(Color.parseColor("#379dfd"));
                break;
        }
        animation = new TranslateAnimation(one*currIndex, one*state, 0, 0);
        currIndex = state;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    void initRefrensh(){
        unadpotLayout.setNetworkAnomalyView(null, false);
        uploadedLayout.setNetworkAnomalyView(null, false);
        waitingLayout.setNetworkAnomalyView(null, false);
        mUnadpotAdapter = new CaseListAdapter(mContext,ADPOT);
        mUploadedAdapter = new CaseListAdapter(mContext,UPLOADED);
        mWaitingAdapter = new CaseListAdapter(mContext,WAITING);
        mUnadpotAdapter.setOnItemChildClickListener(this);
        mUploadedAdapter.setOnItemChildClickListener(this);
        mWaitingAdapter.setOnItemChildClickListener(this);
        recyclerUnadpot.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerUploaded.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        waitingRecyaler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerUnadpot.setAdapter(mUnadpotAdapter);
        recyclerUploaded.setAdapter(mUploadedAdapter);
        waitingRecyaler.setAdapter(mWaitingAdapter);

        unadpotLayout.setDelegate(new BaseRefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
                refreshLayout.refereshStatue = REFRESH;
                getCaseList(1,ADPOT,refreshLayout,mUnadpotAdapter);
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
                refreshLayout.hasMoreData(refreshLayout.isHasMore());
                if(refreshLayout.isHasMore()){
                    refreshLayout.refereshStatue = LOADMORE;
                    getCaseList(++refreshLayout.page,ADPOT,refreshLayout,mUnadpotAdapter);
                    return true;
                }

                return false;
            }
        });
        waitingLayout.setDelegate(new BaseRefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
                refreshLayout.refereshStatue = REFRESH;
                getCaseList(1,WAITING,refreshLayout,mWaitingAdapter);
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
                refreshLayout.hasMoreData(refreshLayout.isHasMore());
                if(refreshLayout.isHasMore()){
                    waitingLayout.refereshStatue = LOADMORE;
                    getCaseList(++refreshLayout.page,WAITING,refreshLayout,mWaitingAdapter);
                    return true;
                }
//                ToastUtil.showToast("没有更多了");
                return false;
            }
        });
        uploadedLayout.setDelegate(new BaseRefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
                refreshLayout.refereshStatue = REFRESH;
                getCaseList(1,UPLOADED,refreshLayout,mUploadedAdapter);
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
                refreshLayout.hasMoreData(refreshLayout.isHasMore());
                if(refreshLayout.isHasMore()){
                    uploadedLayout.refereshStatue = LOADMORE;
                    getCaseList(++refreshLayout.page,UPLOADED,refreshLayout,mUploadedAdapter);
                    return true;
                }
//                ToastUtil.showToast("没有更多了");
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(AppSharePerference.getUserToken())){
            getCaseList(1,ADPOT,unadpotLayout,mUnadpotAdapter);
            getCaseList(1,UPLOADED,uploadedLayout,mUploadedAdapter);
            getCaseList(1,WAITING,waitingLayout,mWaitingAdapter);
        }

    }

    private void getCaseList(int pageNum, String isAdpot, BaseRefreshLayout baseRefreshLayout, CaseListAdapter recyclerAdapter){
        CaseApi.getCaseDate(pageNum, BaseRefreshLayout.SIZE, isAdpot, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                    if(jsonObject.containsKey("data")){
                        String dataObj = jsonObject.getString("data");
                        List<CaseEntity>  caseList = JSON.parseArray(dataObj,CaseEntity.class);
                        if(baseRefreshLayout.refereshStatue.equals(REFRESH)){
                            recyclerAdapter.setDatas(caseList);
                            baseRefreshLayout.endRefreshing();
                            baseRefreshLayout.page = 1;
                        }
                        if(baseRefreshLayout.refereshStatue.equals(LOADMORE)){
                            recyclerAdapter.addMoreDatas(caseList);
                            baseRefreshLayout.endLoadingMore();
                            baseRefreshLayout.page = pageNum;
                        }
                    }
            }

            @Override
            public void onFail(ErrorModel httpError) {
                if(baseRefreshLayout.refereshStatue.equals(REFRESH)){
                    baseRefreshLayout.endRefreshing();
                }
                if(baseRefreshLayout.refereshStatue.equals(LOADMORE)){
                    baseRefreshLayout.endLoadingMore();
                }
            }
        });
    }
    @Click(R.id.iv_right)
    private void toUploadCase(){
        if(TextUtils.isEmpty(AppSharePerference.getUserToken())){
            ((BaseActivity)mContext).toActivity(LoginActivity.newIntent());
        }
        ((BaseActivity)mContext).toActivity(UploadCaseActivity.newIntent());
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if(fragmentPager.getCurrentItem()==2){
            CaseEntity caseEntity = mUnadpotAdapter.getDatas().get(position);
            switch (v.getId()){
                case R.id.tv_upload:
                    ((BaseActivity)mContext).toActivity(UploadCaseActivity.newIntent(caseEntity.number));
                    break;
                case R.id.tv_delete:
                    CaseApi.deleteCase(caseEntity.buyersShowId, new Callback<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            ToastUtil.showToast("删除成功");
                            mUnadpotAdapter.getDatas().remove(position);
                            mUnadpotAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(ErrorModel httpError) {

                        }
                    });
                    break;
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        private List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) 	{
            container.removeView(mListViews.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }
        @Override
        public int getCount() {
            return  mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
    };
}
