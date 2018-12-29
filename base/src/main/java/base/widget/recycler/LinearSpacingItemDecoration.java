package base.widget.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2015/11/23.
 */
public class LinearSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;

    public LinearSpacingItemDecoration(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.contains(0,0,0,spanCount);
    }


}