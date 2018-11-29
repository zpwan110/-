package base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import jingshu.com.base.R;

/**
 * Created by Administrator on 2015/12/14.
 */
public class FixRatioFrameLayout extends FrameLayout {

    private boolean baseOnWidth = true;
    private float ratio = 1;
    private boolean isSetManuly;

    public FixRatioFrameLayout(Context context) {
        super(context);
    }

    public FixRatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isSetManuly) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
            ratio = typedArray.getFloat(R.styleable.Ratio_ratio, 1f);
            baseOnWidth = typedArray.getBoolean(R.styleable.Ratio_baseOnWidth, true);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (baseOnWidth) {
            height = (int) (width * ratio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            width = (int) (height * ratio);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setRatio(boolean baseOnWidth, float ratio) {
        isSetManuly = true;
        boolean needRequestLayout = baseOnWidth != this.baseOnWidth && ratio != this.ratio;
        this.baseOnWidth = baseOnWidth;
        this.ratio = ratio;
        if (needRequestLayout) {
            requestLayout();
        }
    }
}
