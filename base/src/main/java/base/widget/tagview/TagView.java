package base.widget.tagview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2015-12-31 11:47
 */
public class TagView extends View {

    /** Border width*/
    private float mBorderWidth;

    /** Border radius*/
    private float mBorderRadius;

    /** Text size*/
    private float mTextSize;

    /** Horizontal padding for this view, include left & right padding(left & right padding are equal*/
    private int mHorizontalPadding;

    /** Vertical padding for this view, include top & bottom padding(top & bottom padding are equal)*/
    private int mVerticalPadding;

    /** TagView border color*/
    private int mBorderColor;

    /** TagView background color*/
    private int mBackgroundColor;

    /** TagView text color*/
    private int mTextColor;

    /** Whether this view clickable*/
    private boolean isViewClickable;

    /** The max length for this tag view*/
    private int mTagMaxLength;

    /** OnTagClickListener for click action*/
    private OnTagClickListener mOnTagClickListener;

    /** Move slop(default 20px)*/
    private int mMoveSlop = 20;

    /** How long trigger long click callback(default 500ms)*/
    private int mLongPressTime = 500;

    private Paint mPaint;

    private RectF mRectF;

    private Rect mTextBound;

    private String mAbstractText, mOriginText;

    private boolean isUp, isMoved, isExecLongClick;

    private int mLastX, mLastY;

    private Runnable mLongClickHandle = new Runnable() {
        @Override
        public void run() {
            int state = ((TagContainerLayout)getParent()).getTagViewState();
            if (!isMoved && !isUp && state == ViewDragHelper.STATE_IDLE){
                isExecLongClick = true;
                mOnTagClickListener.onTagLongClick((int) getTag(), getText());
            }
        }
    };

    public TagView(Context context, String text){
        super(context);
        init(text);
    }

    private void init(String text){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        mTextBound = new Rect();
        mOriginText = text;
    }

    private void onDealText(){
        if(!TextUtils.isEmpty(mOriginText)) {
            mAbstractText = mOriginText.length() <= mTagMaxLength ? mOriginText
                    : mOriginText.substring(0, mTagMaxLength - 3) + "...";
        }
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mAbstractText, 0, mAbstractText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mHorizontalPadding * 2 + mTextBound.width(),
                mVerticalPadding * 2 + mTextBound.height());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(canvas.getClipBounds().left + mBorderWidth,
                canvas.getClipBounds().top + mBorderWidth,
                canvas.getClipBounds().right - mBorderWidth,
                canvas.getClipBounds().bottom - mBorderWidth);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(mAbstractText, getWidth() / 2 - mTextBound.width() / 2,
                getHeight() / 2 + mTextBound.height() / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isViewClickable && mOnTagClickListener != null){
            int x = (int) event.getX();
            int y = (int) event.getY();
            int action = event.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    mLastY = y;
                    mLastX = x;
                    isMoved = false;
                    isUp = false;
                    isExecLongClick = false;
                    postDelayed(mLongClickHandle, mLongPressTime);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (isMoved){
                        break;
                    }
                    if (Math.abs(mLastX - x) > mMoveSlop || Math.abs(mLastY - y) > mMoveSlop){
                        isMoved = true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    isUp = true;
                    if (!isExecLongClick) {
                        mOnTagClickListener.onTagClick((int) getTag(), getText());
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public String getText(){
        return mOriginText;
    }

    public boolean getIsViewClickable(){
        return isViewClickable;
    }

    public void setTagMaxLength(int maxLength){
        this.mTagMaxLength = maxLength;
        onDealText();
    }

    public void setOnTagClickListener(OnTagClickListener listener){
        this.mOnTagClickListener = listener;
    }

    public void setTagBackgroundColor(int color){
        this.mBackgroundColor = color;
    }

    public void setTagBorderColor(int color){
        this.mBorderColor = color;
    }

    public void setTagTextColor(int color){
        this.mTextColor = color;
    }

    public void setBorderWidth(float width) {
        this.mBorderWidth = width;
    }

    public void setBorderRadius(float radius) {
        this.mBorderRadius = radius;
    }

    public void setTextSize(float size) {
        this.mTextSize = size;
        onDealText();
    }

    public void setHorizontalPadding(int padding) {
        this.mHorizontalPadding = padding;
    }

    public void setVerticalPadding(int padding) {
        this.mVerticalPadding = padding;
    }

    public void setIsViewClickable(boolean clickable) {
        this.isViewClickable = clickable;
    }

    public interface OnTagClickListener{
        void onTagClick(int position, String text);
        void onTagLongClick(int position, String text);
    }
}
