package com.crowd.curtain.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.injectionview.Id;
import base.injectionview.Layout;
import com.crowd.curtain.R;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.CurtainMaterail;
import com.crowd.curtain.ui.adapter.CurtaiMaterialAdapter;
import com.crowd.curtain.ui.customview.HorizontalRefreshLayout;

@Layout(R.layout.activity_test)
public class TestActivity extends AppBaseActivity {
    @Id(R.id.refreshParent)
    HorizontalRefreshLayout refreshParent;
    @Id(R.id.dataRecyclear)
    RecyclerView dataRecyclear;
    private CurtaiMaterialAdapter mCurtaiMaterail;
    public List<CurtainMaterail> mItems = new ArrayList<CurtainMaterail>(
            Arrays.asList(
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail(),
                    new CurtainMaterail()));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManagerHead=new LinearLayoutManager(mContext);
        linearLayoutManagerHead.setOrientation(LinearLayoutManager.HORIZONTAL);
        dataRecyclear.setLayoutManager(linearLayoutManagerHead);
        mCurtaiMaterail= new CurtaiMaterialAdapter(mContext);
        dataRecyclear.setAdapter(mCurtaiMaterail);
        mCurtaiMaterail.setDatas(mItems);
    }


}
