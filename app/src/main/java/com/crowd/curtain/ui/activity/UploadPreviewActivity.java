/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.zhihu.matisse.internal.entity.SelectionSpec;
import com.zhihu.matisse.internal.utils.Platform;

import java.util.ArrayList;

import base.injectionview.Id;
import base.injectionview.Layout;
import com.crowd.curtain.R;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.ui.adapter.PreviewAdapter;

@Layout(R.layout.activity_preview)
public class UploadPreviewActivity extends AppBaseActivity implements  ViewPager.OnPageChangeListener{
public static final String STATE_SELECTION="state_SELECTION";

    @Id(R.id.pager)
    private ViewPager mPager;
    private PreviewAdapter mAdapter;
    protected int mPreviousPos = -1;

    public static Intent newIntent(ArrayList<String> mItems) {
        Intent intent = new Intent(App.getContext(), UploadPreviewActivity.class);
        intent.putStringArrayListExtra(UploadPreviewActivity.STATE_SELECTION, mItems);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(SelectionSpec.getInstance().themeId);
        super.onCreate(savedInstanceState);
        if (Platform.hasKitKat()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ArrayList<String> mPaths = getIntent().getStringArrayListExtra(STATE_SELECTION);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.addOnPageChangeListener(this);
        mAdapter = new PreviewAdapter(getSupportFragmentManager(), mPaths);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
