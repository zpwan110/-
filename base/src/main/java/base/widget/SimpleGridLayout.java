package base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import jingshu.com.base.R;


/**
 * Created by Administrator on 2016/1/27.
 */
public class SimpleGridLayout extends ViewGroup {
    private int column;
    private int dividerWidth;
    private int rowHeight;
    private int childHeight;
    private int childWidth;
    private float childHeightWidthRatio;


    public SimpleGridLayout(Context context) {
        this(context, null);
    }

    public SimpleGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridLayout, defStyleAttr, 0);
        column = typedArray.getInteger(R.styleable.SimpleGridLayout_grid_column, 3);
        dividerWidth = (int) typedArray.getDimension(R.styleable.SimpleGridLayout_grid_divider_width, 0);
        childHeightWidthRatio = typedArray.getFloat(R.styleable.SimpleGridLayout_grid_item_height_width_ratio, 0);
        rowHeight = (int) typedArray.getDimension(R.styleable.SimpleGridLayout_grid_row_height, 1);
        typedArray.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        childWidth = getChildSize(width);
        if (childHeightWidthRatio == 0) {
            childHeight = rowHeight;
        } else {
            childHeight = (int) (childWidth * childHeightWidthRatio);
        }
        width = getPaddingLeft() + getPaddingRight() + column * (childWidth + dividerWidth) - dividerWidth;
        int height = getPaddingTop() + getPaddingBottom() + ((getNotGoneChildCount() - 1) / column + 1) * (childHeight + dividerWidth) - dividerWidth;
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private int getNotGoneChildCount() {
        int num = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                num++;
            }
        }
        return num;
    }

    private int getChildSize(int width) {
        int childSpan = ((width - (column - 1) * dividerWidth) - getPaddingLeft() - getPaddingRight()) / column;
        return childSpan;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            int left = (getPaddingLeft() + index % column * childWidth + index % column * dividerWidth);
            int top = (getPaddingTop() + index / column * childHeight + index / column * dividerWidth);
            child.layout(left, top, (left + childWidth), (top + childHeight));
            index++;
        }
    }
}
