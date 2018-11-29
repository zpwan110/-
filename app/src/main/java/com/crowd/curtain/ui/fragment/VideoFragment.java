package com.crowd.curtain.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import base.BaseActivity;
import base.BaseFragment;
import base.PermissionActivity;
import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.util.ToastUtil;
import base.widget.recycler.BaseRefreshLayout;
import base.widget.recycler.GridSpacingItemDecoration;
import base.widget.recycler.ViewOnItemChildClickListener;
import com.crowd.curtain.R;
import com.crowd.curtain.api.VideoApi;
import com.crowd.curtain.common.model.VideoEntity;
import com.crowd.curtain.ui.activity.VideoPlayerActivity;
import com.crowd.curtain.ui.adapter.VideoListAdapter;

import static base.widget.recycler.BaseRefreshLayout.LOADMORE;
import static base.widget.recycler.BaseRefreshLayout.REFRESH;


/**
 * User:Shine
 * Date:2015-10-20
 * Description:
 */

@Layout(R.layout.fragment_video)
public class VideoFragment extends BaseFragment implements BaseRefreshLayout.RefreshLayoutDelegate,ViewOnItemChildClickListener{

    @Id(R.id.refreshParent)
    BaseRefreshLayout baseRefreshLayout;

    @Id(R.id.recyclerContent)
    RecyclerView videoRecyclear;
    @Id(R.id.tv_center_title)
    TextView titleView;
    private VideoListAdapter videoAdapter;

    @Override
    protected void initViews() {
        super.initViews();
        titleView.setText("安装视频");
        titleView.setVisibility(View.VISIBLE);
        baseRefreshLayout.setNetworkAnomalyView(null, false);
        baseRefreshLayout.setDelegate(this);
        videoRecyclear.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        int spacingInPixels = DensityUtils.dip2px( 5.0f);
        videoRecyclear.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false));
        videoAdapter = new VideoListAdapter(mContext);
        videoAdapter.setOnItemChildClickListener(this);
        PermissionActivity.requestPermission((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, "需要授权读写权限", new PermissionActivity.OnPermissionCallback() {
            @Override
            public void onPermissionAuthenticated() {
                videoRecyclear.setAdapter(videoAdapter);
            }
            @Override
            public void onPermissionDenied() {
                ToastUtil.showToast("授权失败");
            }
        });
        getVideoList(1,baseRefreshLayout,videoAdapter);
    }

    private void getVideoList(int pageNum,BaseRefreshLayout baseRefreshLayout,VideoListAdapter recyclerAdapter){
        VideoApi.getVideoDate(pageNum, BaseRefreshLayout.SIZE, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    String dataObj = jsonObject.getString("data");
                    List<VideoEntity> caseList = JSON.parseArray(dataObj,VideoEntity.class);
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
    @Override
    public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
            refreshLayout.refereshStatue = REFRESH;
        getVideoList(1,refreshLayout,videoAdapter);
    }

    @Override
    public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
        refreshLayout.hasMoreData(refreshLayout.isHasMore());
        if(refreshLayout.isHasMore()){
            refreshLayout.refereshStatue = LOADMORE;
            getVideoList(++refreshLayout.page,refreshLayout,videoAdapter);
            return true;
        }

        return false;
    }

    @Override
    public void onItemChildClick(View v, int position) {
        String videoUrl = videoAdapter.getDatas().get(position).videoUrl;
        ((BaseActivity)getActivity()).toActivity(VideoPlayerActivity.newInstant(videoUrl));
    }
}
