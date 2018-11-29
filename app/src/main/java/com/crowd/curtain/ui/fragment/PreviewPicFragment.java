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
package com.crowd.curtain.ui.fragment;

import android.os.Bundle;
import android.widget.ImageView;

import com.zhihu.matisse.R;
import com.zhihu.matisse.internal.entity.Item;

import base.BaseFragment;
import base.injectionview.Id;
import base.injectionview.Layout;

@Layout(R.layout.fragment_preview)
public class PreviewPicFragment extends BaseFragment {

    private static final String ARGS_ITEM = "args_item";
    @Id(R.id.image_view)
    private ImageView image;
    public static PreviewPicFragment newInstance(String item) {
        PreviewPicFragment fragment = new PreviewPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_ITEM, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        final Item item = getArguments().getParcelable(ARGS_ITEM);
        if (item == null) {
            return;
        }
//        PicassoImageLoader.getInstance().displayImage(getActivity(),new File(item.path),image);
    }

}
