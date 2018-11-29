package base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import jingshu.com.base.R;
/**
 * Created by Administrator on 2015/12/14.
 */
public class FixRatioImageView extends android.support.v7.widget.AppCompatImageView {

    private boolean baseOnWith = true;
    private boolean matchDrawableRatio = false;

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    private float ratio = 1;

    public FixRatioImageView(Context context) {
        super(context);
    }

    public FixRatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
        ratio = typedArray.getFloat(R.styleable.Ratio_ratio, 1f);
        baseOnWith = typedArray.getBoolean(R.styleable.Ratio_baseOnWidth, true);
        matchDrawableRatio = typedArray.getBoolean(R.styleable.Ratio_matchDrawableRatio, false);
        typedArray.recycle();
        Drawable drawable = getDrawable();
        setRatioByDrawableRatio(drawable);
    }

    public void setSizeConfig(boolean baseOnWith, float ratio) {
        this.baseOnWith = baseOnWith;
        this.ratio = ratio;
        this.matchDrawableRatio = false;
        if (getVisibility() == View.VISIBLE) {
            requestLayout();
        }
    }

    private void setRatioByDrawableRatio(Drawable drawable) {
        if (matchDrawableRatio) {
            if (null != drawable && matchDrawableRatio) {
                if (baseOnWith) {
                    ratio = drawable.getIntrinsicHeight() * 1f / drawable.getIntrinsicWidth();
                } else {
                    ratio = drawable.getIntrinsicWidth() * 1f / drawable.getIntrinsicHeight();
                }
            }
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (baseOnWith) {
            height = (int) (width * ratio);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            width = (int) (height * ratio);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setRatioByDrawableRatio(drawable);
    }
}
