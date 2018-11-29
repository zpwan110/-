package com.crowd.curtain.ui.customview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by zhangpeng on 2018/2/2.
 */

public class CanvasWrapper {
    private int mWidth;
    private int mHeight;
    private final Canvas mCanvas = new Canvas();
    private final Rect mDstRect = new Rect();
    private Bitmap mDrawBitmap;

    public CanvasWrapper() {
    }

    public void setCanvasSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        mWidth = width;
        mHeight = height;
        mDstRect.set(0, 0, width, height);
        createDrawBitmap();
    }

    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint mPaint) {
        if (null != bitmap && null != matrix) {
            mCanvas.drawBitmap(bitmap, matrix, mPaint);
        }
    }

    public void draw(Canvas canvas) {
        if (mDrawBitmap != null) {
            canvas.drawBitmap(mDrawBitmap, null, mDstRect, null);
        }
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    private void createDrawBitmap() {
        final int width = mWidth;
        final int height = mHeight;
        if (null != mDrawBitmap) {
            if (mDrawBitmap.getWidth() != width || mDrawBitmap.getHeight() != height) {
                mDrawBitmap = null;
            }
        }

        if (mDrawBitmap == null) {
            try {
                mDrawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        if (mDrawBitmap != null) {
            reset();
            mCanvas.setBitmap(mDrawBitmap);
        }
    }

    private void reset() {
        if (mDrawBitmap != null) {
            mDrawBitmap.eraseColor(Color.TRANSPARENT);
        }
    }
}
