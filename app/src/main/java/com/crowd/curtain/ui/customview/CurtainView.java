package com.crowd.curtain.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.crowd.curtain.R;

/**
 * Created by zhangpeng on 2018/1/26.
 */

public class CurtainView extends android.support.v7.widget.AppCompatImageView {
    private Camera mCamera;
    //翻转用的图片
    private Bitmap face;
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint();

    private int mLastMotionX, mLastMotionY;

    //图片的中心点坐标
    private int centerX, centerY;
    //转动的总距离，跟度数比例1:1
    private int deltaX, deltaY;
    //图片宽度高度
    private int bWidth, bHeight;
    public CurtainView(Context context) {
        super(context);
        initCamare();
    }

    public CurtainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCamare();
    }

    public CurtainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCamare();
    }
    private void initCamare(){
        setWillNotDraw(false);
        mCamera = new Camera();
        mPaint.setAntiAlias(true);
        face = BitmapFactory.decodeResource(getResources(), R.drawable.small_curtain);
        bWidth = face.getWidth();
        bHeight = face.getHeight();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.centerX = w/2;
        this.centerY =h/2;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initLocation();
    }

    private void initLocation(){
//        mMatrix.postTranslate(Math.abs(centerX-bWidth/2),Math.abs(centerY-bHeight/2));
        mCamera.translate(Math.abs(centerX-bWidth/2),-Math.abs(centerY-bHeight/2),0);
        mCamera.getMatrix(mMatrix);
//        mCamera.setLocation(Math.abs(centerX-bWidth/2),Math.abs(centerY-bHeight/2),0);
        //以图片的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
//        mCamera.setLocation(0,0,0);
//        mMatrix.preTranslate(-centerX, -centerX);
//        mMatrix.preTranslate(centerX, centerX);
        mCamera.save();
    }


    /**
     * 转动
     * @param degreeX
     * @param degreeY
     */
    void rotate(int degreeX, int degreeY) {
        deltaX += degreeX;
        deltaY += degreeY;

        mCamera.save();
        mCamera.rotateY(deltaX);
        mCamera.rotateX(-deltaY);
        mCamera.translate(0, 0, -centerX);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        //以图片的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
//        mMatrix.preTranslate(-centerX, -centerY);
//        mMatrix.postTranslate(centerX, centerY);
        mCamera.save();

        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastMotionX;
                int dy = y - mLastMotionY;
//                rotate(dx, dy);
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(face, mMatrix, null);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
}
