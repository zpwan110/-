package com.crowd.curtain.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import base.eventbus.Actions;
import base.eventbus.EventBus;
import base.http.Callback;
import base.http.ErrorModel;
import base.injectionview.Click;
import base.injectionview.Id;
import base.injectionview.Layout;
import base.util.DensityUtils;
import base.widget.recycler.BaseRefreshLayout;
import base.widget.recycler.BaseRefreshLayout.RefreshLayoutDelegate;
import base.widget.recycler.LinearSpacingItemDecoration;
import base.widget.searchview.RecordSQLiteOpenHelper;
import base.widget.searchview.SearchEditText;
import base.widget.searchview.SearchListView;
import base.widget.tagview.TagContainerLayout;
import base.widget.tagview.TagView;

import com.crowd.curtain.api.SearchApi;
import com.crowd.curtain.base.App;
import com.crowd.curtain.base.AppBaseActivity;
import com.crowd.curtain.common.model.CurtainSearch;
import com.crowd.curtain.ui.adapter.CurtainSearchAdapter;
import com.crowd.curtain.R;
import com.crowd.curtain.ui.adapter.DelCall;
import com.crowd.curtain.ui.adapter.SimpleCustAdapter;

import static base.widget.recycler.BaseRefreshLayout.LOADMORE;
import static base.widget.recycler.BaseRefreshLayout.REFRESH;

@Layout(R.layout.activity_search)
public class SearchActivity extends AppBaseActivity implements RefreshLayoutDelegate,DelCall{
    private final String sql="select id as _id,name from records where name = ?";
    @Id(R.id.clear_search)
    LinearLayout ll_clear;
    @Id(R.id.tv_backLeft)
    ImageView toBack;
    @Id(R.id.edt_search)
    SearchEditText searchEditText;
    @Id(R.id.rightText)
    TextView rightText;
    @Id(R.id.hot_search)
    TagContainerLayout hotSearchList;
    @Id(R.id.edt_search)
    EditText et_search; // 搜索按键
    @Id(R.id.history_list)
    private SearchListView listView;
    @Id(R.id.searchParent)
    private BaseRefreshLayout refreshLayout;
    @Id(R.id.searchRecyclear)
    private RecyclerView searchRecyclear;
    @Id(R.id.nestedScroll)
    private ScrollView scrollView;
    CurtainSearchAdapter recyclerAdapter;
    private BaseAdapter adapter;
    // 数据库变量
    // 用于存放历史搜索记录
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;
    private String searchContent;

    public static Intent newIntent() {
        Intent it = new Intent(App.getContext(), SearchActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return it;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSearch();
        initHotSearch();
    }
    private void initHotSearch() {
        refreshLayout.setNetworkAnomalyView(null, false);
        recyclerAdapter= new CurtainSearchAdapter(mContext);
        refreshLayout.setDelegate(this);
        searchRecyclear.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        searchRecyclear.addItemDecoration(new LinearSpacingItemDecoration(DensityUtils.dip2px(15)));
        searchRecyclear.setAdapter(recyclerAdapter);
        SearchApi.getHotSearch(new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    if(dataObj.containsKey("hots")){
                        List<String> hotsSearch= JSON.parseArray(dataObj.getString("hots"),String.class);
                        hotSearchList.setTags(hotsSearch);
                    }
                }
            }

            @Override
            public void onFail(ErrorModel httpError) {

            }
        });
        hotSearchList.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                et_search.setText(text);
                searchContent = text;
                insertData(searchContent);
                searchCurtain(1,searchContent,recyclerAdapter);
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }
        });
        scrollView.invalidate();
    }


    public void initSearch(){
        // 1. 初始化UI组件->>关注c
        // 2. 实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(mContext);
        // 3. 第1次进入时查询所有的历史搜索记录
        queryData("");
        /**
         * 监听输入键盘更换后的搜索按键
         * 调用时刻：点击键盘上的搜索键时
         */
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 1. 点击搜索按键后，根据输入的搜索字段进行查询
                    // 注：由于此处需求会根据自身情况不同而不同，所以具体逻辑由开发者自己实现，此处仅留出接口
                    // 2. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
                    boolean hasData = hasData(et_search.getText().toString().trim());
                    // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
                    if (!hasData) {
                        searchContent = et_search.getText().toString().trim();
                        insertData(searchContent);
                        searchCurtain(1,searchContent,recyclerAdapter);
                    }
                }
                return false;
            }
        });



        /**
         * 搜索框的文本变化实时监听
         */
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            // 输入文本后调用该方法
            @Override
            public void afterTextChanged(Editable s) {
                // 每次输入后，模糊查询数据库 & 显示
                // 注：若搜索框为空,则模糊搜索空字符 = 显示所有的搜索历史
                String tempName = et_search.getText().toString();
                queryData(tempName); // ->>关注1
                scrollView.setVisibility(View.VISIBLE);

            }
        });
        /**
         * 搜索记录列表（ListView）监听
         * 即当用户点击搜索历史里的字段后,会直接将结果当作搜索字段进行搜索
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取用户点击列表里的文字,并自动填充到搜索框内
                TextView textView = (TextView) view.findViewById(R.id.clear_text);
                searchContent = textView.getText().toString();
                searchEditText.setText(searchContent);
                searchCurtain(1,searchContent,recyclerAdapter);

            }
        });
    }
    private void searchCurtain(int pageNum,String searchText,CurtainSearchAdapter recyclerAdapter) {
        scrollView.setVisibility(View.GONE);
        SearchApi.getSearchCurtain(pageNum, BaseRefreshLayout.SIZE, searchText, new Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if(jsonObject.containsKey("data")){
                    String dataObj = jsonObject.getString("data");
                    List<CurtainSearch>  curtainList = JSON.parseArray(dataObj,CurtainSearch.class);
                    if(refreshLayout.refereshStatue.equals(REFRESH)){
                        recyclerAdapter.setDatas(curtainList);
                        refreshLayout.endRefreshing();
                        refreshLayout.page = 1;
                    }
                    if(refreshLayout.refereshStatue.equals(LOADMORE)){
                        recyclerAdapter.addMoreDatas(curtainList);
                        refreshLayout.endLoadingMore();
                        refreshLayout.page = pageNum;
                    }
                }

            }

            @Override
            public void onFail(ErrorModel httpError) {
                if(refreshLayout.refereshStatue.equals(REFRESH)){
                    refreshLayout.endRefreshing();
                    refreshLayout.page = 1;
                }
                if(refreshLayout.refereshStatue.equals(LOADMORE)){
                    refreshLayout.endLoadingMore();
                    refreshLayout.page = pageNum;
                }
            }
        });
    }
    @Click(R.id.tv_backLeft)
    private void backUp(){
        finish();
    }
    @Click(R.id.rightText)
    private void clearSearch(){
        et_search.setText("");
    }
    /**
     * 清除历史记录
     */
    @Click(R.id.clear_search)
    private void clearHistory(){
        // 清空数据库->>关注2
        deleteData();
        // 模糊搜索空字符 = 显示所有的搜索历史（此时是没有搜索记录的）
        queryData("");
    }
    /**
     * 关注1
     * 模糊查询数据 & 显示到ListView列表上
     */
    List<String> list = new ArrayList<>();
    private void queryData(String tempName) {
        list.clear();
        // 1. 模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records  order by id desc ", null);
        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex("name");
            list.add(cursor.getString(index));
        }
        // 2. 创建adapter适配器对象 & 装入模糊搜索的结果
        adapter = new SimpleCustAdapter(mContext,list,this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // 当输入框为空 & 数据库中有搜索记录时，显示 "删除搜索记录"按钮
        if (cursor.getCount() != 0){
            ll_clear.setVisibility(View.VISIBLE);
        }else {
            ll_clear.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 关注2：清空数据库
     */
    private void deleteData() {

        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
        ll_clear.setVisibility(View.INVISIBLE);
    }
    /**
     * 删除数据
     */
    private void deleteOneData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records where name = '" + tempName + "' ");
        db.close();

    }

    /**
     * 关注3
     * 检查数据库中是否已经有该搜索记录
     */
    private boolean hasData(String tempName) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(sql, new String[]{tempName});
        //  判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 关注4
     * 插入数据到数据库，即写入搜索字段到历史搜索记录
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    @Override
    public void onRefreshLayoutBeginRefreshing(BaseRefreshLayout refreshLayout) {
        refreshLayout.refereshStatue = REFRESH;
        searchCurtain(1,searchContent,recyclerAdapter);
    }

    @Override
    public boolean onRefreshLayoutBeginLoadingMore(BaseRefreshLayout refreshLayout) {
        refreshLayout.hasMoreData(refreshLayout.isHasMore());
        if(refreshLayout.isHasMore()){
            refreshLayout.refereshStatue = LOADMORE;
            searchCurtain(++refreshLayout.page,searchContent,recyclerAdapter);
            return true;
        }

        return false;
    }

    @Override
    public void del(String value, int i) {
        list.remove(i);
        adapter.notifyDataSetChanged();
        deleteOneData(value);
        if(listView.getCount()==0){
            ll_clear.setVisibility(View.GONE);
        }
    }
}
