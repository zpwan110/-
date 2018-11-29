package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.List;

import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.util.ToastUtil;
import base.widget.recycler.EndlessRecyclerOnScrollListener;
import base.widget.recycler.ViewOnItemChildClickListener;
import com.crowd.curtain.R;
import com.crowd.curtain.api.SearchApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.CurtainMaterail;
import com.crowd.curtain.ui.adapter.CurtaiMaterialAdapter;
import com.crowd.curtain.ui.customview.CurtuaiPoint;
import com.crowd.curtain.ui.customview.PolyToPolyView;

/**
 * Created by zhangpeng on 2018/3/8.
 */
@Layout(R.layout.activity_editor_photo)
public class EditorPhotoActivity extends AppBaseActivity implements PolyToPolyView.DoubleClick,ViewOnItemChildClickListener,SeekBar.OnSeekBarChangeListener {

    private static final String BG_URI = "bg_uri";
    private static final String CURTAIN = "curtain";
    private static final String PARENT = "parent";
    @Id(R.id.rl_title)
    RelativeLayout rlTitle;
    @Id(R.id.ic_touch)
    PolyToPolyView polyToPolyView;
    @Id(R.id.iv_setting)
    ImageView iv_setting;
    @Id(R.id.ll_refresh)
    View refreshView;
    @Id(R.id.refreshParent)
    RelativeLayout layoutParent;
    @Id(R.id.rclear_curtain)
    RecyclerView recyclerView;
    @Id(R.id.edt_search)
    EditText edtSearch;
    @Id(R.id.ll_bottom)
    LinearLayout llBottom;
    private Uri currentUri;
    private CurtaiMaterialAdapter mCurtaiMaterail;
    private float brightProgress;
    private float contrastProgress;
    private float grayProgess;
    private SeekBar lightSeek;
    private PopupWindow popupSetting;
    private SeekBar duibiSeek;
    private SeekBar huiduSeek;
    private PopupWindow changgeWind;
    private RadioGroup rdgroup;
    private int catsType=2;
    private String curtainNum;
    private int pageNum=1;
    private String parentId;


    public static Intent newIntent(File file,String image) {
        Intent it = new Intent(App.getContext(), EditorPhotoActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           Uri uri = FileProvider.getUriForFile(App.getContext(), "curtain.photograph.com.fileprovider", file);
            it.putExtra(BG_URI,uri);
        }else {
            it.putExtra(BG_URI,Uri.fromFile(file));
        }
        it.putExtra(CURTAIN,image);
        return it;
    }
    public static Intent newIntent(String filePath,String parentId,String proId) {
        Intent it = new Intent(App.getContext(), EditorPhotoActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           Uri uri = FileProvider.getUriForFile(App.getContext(), "curtain.photograph.com.fileprovider", new File(filePath));
            it.putExtra(BG_URI,uri);
        }else {
            it.putExtra(BG_URI,Uri.fromFile(new File(filePath)));
        }
        it.putExtra(PARENT,parentId);
        it.putExtra(CURTAIN,proId);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUri = getIntent().getParcelableExtra(BG_URI);
        curtainNum = getIntent().getStringExtra(CURTAIN);
        parentId = getIntent().getStringExtra(PARENT);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        rlTitle.setAlpha(0.7f);
        if(currentUri!=null){
            polyToPolyView.setBgBitmap(currentUri);
            polyToPolyView.invalidate();
        }
        polyToPolyView.setFullScreem(false);
        edtSearch.setText(curtainNum);
        edtSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                String searchText = edtSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(searchText)) {
                    searchList(pageNum,searchText,catsType);
                }
            }
            return false;
        });
        polyToPolyView.setDoubleClick(this);
        LinearLayoutManager linearLayoutManagerHead=new LinearLayoutManager(mContext);
        linearLayoutManagerHead.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManagerHead);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int offX) {
                searchList(pageNum+1,"",catsType);
            }

            @Override
            public void onRefreshing(int offX) {
                searchList(pageNum=1,"",catsType);
            }
        });
        mCurtaiMaterail= new CurtaiMaterialAdapter(mContext);
        mCurtaiMaterail.setOnItemChildClickListener(this);
        recyclerView.setAdapter(mCurtaiMaterail);
        popupSetting = new PopupWindow(this);
        popupSetting.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSetting.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupSetting.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_pop_setting, null));
        popupSetting.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupSetting.setOutsideTouchable(false);
        popupSetting.setFocusable(true);
        lightSeek = popupSetting.getContentView().findViewById(R.id.lightline);
        duibiSeek = popupSetting.getContentView().findViewById(R.id.duibiline);
        huiduSeek = popupSetting.getContentView().findViewById(R.id.huiduline);
        rdgroup = popupSetting.getContentView().findViewById(R.id.rd_group);
        lightSeek.setOnSeekBarChangeListener(this);
        duibiSeek.setOnSeekBarChangeListener(this);
        huiduSeek.setOnSeekBarChangeListener(this);
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rd_single:
                        polyToPolyView.setSingle(true);
                        break;
                    case R.id.rd_multiple:
                        polyToPolyView.setSingle(false);
                        break;
                }
            }
        });
        brightProgress = (duibiSeek.getProgress())/1000;
        contrastProgress = duibiSeek.getProgress()/1000;
        grayProgess = huiduSeek.getProgress()/1000;

        changgeWind= new PopupWindow(this);
        changgeWind.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        changgeWind.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        changgeWind.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_pop_change, null));
        TextView rdBody =changgeWind.getContentView().findViewById(R.id.rd_body);
        TextView rdHead =changgeWind.getContentView().findViewById(R.id.rd_head);
        rdBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polyToPolyView.setBodyOrHead(true);
                rdBody.setTextColor(mContext.getResources().getColor(R.color.btn_radio_off));
                rdHead.setTextColor(mContext.getResources().getColor(R.color.btn_radio_on));
                catsType = 2;
                searchList(pageNum,"",catsType);
            }
        });
        rdHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polyToPolyView.setBodyOrHead(false);
                rdHead.setTextColor(mContext.getResources().getColor(R.color.btn_radio_off));
                rdBody.setTextColor(mContext.getResources().getColor(R.color.btn_radio_on));
                catsType = 1;
                searchList(pageNum,"",catsType);
            }
        });
        changgeWind.setBackgroundDrawable(new ColorDrawable(0x00000000));
        changgeWind.setOutsideTouchable(false);
        changgeWind.setFocusable(true);
        catsType = TextUtils.isEmpty(parentId)?catsType:Integer.parseInt(parentId);
        searchList(pageNum,TextUtils.isEmpty(curtainNum)?"":curtainNum,catsType);
    }

    private void searchList(int page,String num,int cateType) {
        SearchApi.getMaterial(page,cateType, num, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    String dataObj = jsonObject.getString("data");
                    List<CurtainMaterail> mItems = JSON.parseArray(dataObj,CurtainMaterail.class);
                    if(page==1){
                        mCurtaiMaterail.setDatas(mItems);
                    }
                    if(page>1){
                        mCurtaiMaterail.addMoreDatas(mItems);
                        pageNum = page;
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
    }

    @Click(R.id.iv_setting)
    private void showSetting(){
        if(polyToPolyView.currentPoint==null){
            ToastUtil.showToast("请选择窗帘素材");
            return;
        }
        popupSetting.showAtLocation(iv_setting, Gravity.NO_GRAVITY,DensityUtils.dip2px(55), DensityUtils.dip2px(65));
    }
    @Click(R.id.iv_image)
    private void ivImage(){
        changgeWind.showAtLocation(iv_setting, Gravity.NO_GRAVITY,DensityUtils.dip2px(85), DensityUtils.dip2px(65));
    }
    @Click(R.id.iv_cancel)
    private void backUp(){
        finish();
    }
    @Click(R.id.iv_select)
    private void saveImage(){
        polyToPolyView.saveViewImage();
    }

    @Override
    public void onDoubleClick() {
        if(rlTitle.getVisibility()==View.GONE){
            rlTitle.setVisibility(View.VISIBLE);
        }else{
            rlTitle.setVisibility(View.GONE);
        }
        if(llBottom.getVisibility()==View.GONE){
            llBottom.setVisibility(View.VISIBLE);
        }else{
            llBottom.setVisibility(View.GONE);
        }
        polyToPolyView.fullScreem();
    }

    @Override
    public void onItemChildClick(View v, int position) {
        String imagUrl = mCurtaiMaterail.getDatas().get(position).materialImage;
        Picasso.with(mContext).load(mCurtaiMaterail.getDatas().get(position).materialImage).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(catsType==2){
                    CurtuaiPoint curtuaiPoint =new CurtuaiPoint(bitmap,polyToPolyView,imagUrl);
                    polyToPolyView.addBody(curtuaiPoint);
                }else{
                    CurtuaiPoint curtuaiPoint =new CurtuaiPoint(bitmap,polyToPolyView,imagUrl);
                    polyToPolyView.addHead(curtuaiPoint);
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        polyToPolyView.invalidate();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.lightline:
                brightProgress = (progress)/1000f;
                polyToPolyView.changeBitmap(brightProgress,contrastProgress,grayProgess);
                lightSeek.setProgress(progress);
                Log.e("progress", "progress"+progress+"brightProgress"+brightProgress);
                break;
            case R.id.duibiline:
                contrastProgress = (float) ((progress) / 1000f);
                polyToPolyView.changeBitmap(brightProgress,contrastProgress,grayProgess);
//                polyToPolyView.changeImg(contrastProgress);
                duibiSeek.setProgress(progress);
                Log.e("progress", "progress"+progress+"contrastProgress"+contrastProgress);
                break;
            case R.id.huiduline:
                grayProgess = progress/1000f;
                polyToPolyView.changeBitmap(brightProgress,contrastProgress,grayProgess);
                huiduSeek.setProgress(progress);
                break;
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
