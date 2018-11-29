package com.crowd.curtain.ui.customview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import com.crowd.curtain.R;
import com.crowd.curtain.utils.SpatialRelationUtil;

/**
 * Created by zhangpeng on 2018/3/22.
 */

public class CurtuaiPoint {
    private int pointXBan,pointYBan;
    private List<Bitmap> listPoint=new ArrayList<>();
    public String imageUrl;
    public  Bitmap mBitmap;
    public  Bitmap mCopyBitmap;
    private  CanvasWrapper mCanvasWrapper;
    private  Matrix mPolyMatrix;
    private  Paint pointLine;
    private  Paint pointPaint;
    /** 边框内距离180px**/
    private final int framPadding = 35;
    /** 图片上一个坐标集 **/
    private float[] src = new float[8];
    /** 图片当前坐标集 **/
    private float[] dst = new float[8];
    private float[] center = new float[2];
    private float[] delete = new float[2];
    private float[] translate = new float[2];
    private RectF rectF = new RectF();

    private int testPoint = 0;
    float[] pointXY = new float[8];
    public List<SpatialRelationUtil.Point> pointList = new ArrayList<>();
    int currentPoint=-1 ;
    private boolean isTranslate =false;
    private PolyToPolyView mParent;
    float locationX ,locationY ;
    private boolean isMove =false;
    public boolean isDelete = false;
    private float triggerRadius = 50;
    private final int pointRange = 160;
    private final int deleteR = 50;
    private Bitmap bitmaoDelete;
    private boolean isGone = false;

    public CurtuaiPoint() {
    }
    public CurtuaiPoint(Bitmap bitmap,PolyToPolyView view,String url) {
        mParent = view;
        bitmaoDelete = BitmapFactory.decodeResource(mParent.getContext().getResources(), R.mipmap.ico_delete_img);
        listPoint.add(BitmapFactory.decodeResource(mParent.getContext().getResources(), R.mipmap.pointa));
        listPoint.add(BitmapFactory.decodeResource(mParent.getContext().getResources(), R.mipmap.pointb));
        listPoint.add(BitmapFactory.decodeResource(mParent.getContext().getResources(), R.mipmap.pointc));
        listPoint.add(BitmapFactory.decodeResource(mParent.getContext().getResources(), R.mipmap.pointd));
        this.mBitmap = bitmap;
        this.pointXBan = listPoint.get(0).getWidth()/2;
        this.pointYBan = listPoint.get(0).getHeight()/2;
        this.mCopyBitmap = bitmap;
        src = new float[]{0-framPadding, 0-framPadding,
                bitmap.getWidth()+framPadding, 0-framPadding,
                bitmap.getWidth()+framPadding, bitmap.getHeight()+framPadding,
                0-framPadding, bitmap.getHeight()+framPadding
        };
        this.imageUrl = url;
        dst = src.clone();
        pointPaint = new Paint();
        pointLine = new Paint();
        pointPaint.setAntiAlias(true);
        pointLine.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointLine.setStrokeWidth(8);
        pointPaint.setColor(Color.parseColor("#fefe00"));
        pointLine.setColor(Color.parseColor("#fefe00"));
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPolyMatrix = new Matrix();
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, 4);
        mCanvasWrapper = new CanvasWrapper();
        initPrame(view.centerX,view.centerY);
    }
    public void initPrame(int centerX,int centerY){
        translate[0] = Math.abs(centerX-mBitmap.getWidth()/2);
        translate[1] = Math.abs(centerY-mBitmap.getHeight()/2);
        delete[0] = centerX-bitmaoDelete.getWidth()/2;
        delete[1] =  translate[1]-framPadding*3-bitmaoDelete.getHeight()/2;
        center[0] = centerX;
        center[1] = centerY;
        setTestPoint(4);
    }
    public void setTestPoint(int testPoint) {
        this.testPoint = testPoint > 4 || testPoint < 0 ? 4 : testPoint;
        dst = src.clone();
        resetPolyMatrix(testPoint,0,0);
    }
    public void resetPolyMatrix(int pointCount,float moveX,float moveY) {
        mPolyMatrix.reset();
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
        mPolyMatrix.postTranslate(translate[0],translate[1]);
        mPolyMatrix.mapPoints(pointXY, src);
        if(isTranslate){
            center[0] += moveX;
            center[1] += moveY;
            delete[0] += moveX;
            delete[1] += moveY;
        }else if(currentPoint!=-1){
            switch (currentPoint){
                case 0:
                    center[0] += moveX/2;
                    center[1] += moveY/2;
                    delete[0] += moveX/2;
                    delete[1] += moveY/2;
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
                case 2:
                    center[0] += moveX/2;
                    delete[0] += moveX/2;
                    delete[1] += moveY/2;
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
                case 4:
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
                case 6:
                    center[1] += moveY/2;
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
            }
        }

        mParent.invalidate();
    }

    public void onTouch(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationX = event.getX();
                locationY = event.getY();
                currentPoint =-1;
                isMove = false;
                isTranslate =false;
                if(Math.abs(locationX - center[0]) <= pointRange && Math.abs(locationY - center[1]) <= pointRange){
                    isTranslate =true;
                    break;
                }
                // 根据触控位置改变dst
                for (int i = 0; i < testPoint * 2; i += 2) {
                    float shortX = locationX - pointXY[i];
                    float shortY = locationY - pointXY[i+1];
                    if (Math.abs(shortX) <= triggerRadius && Math.abs(shortY) <= triggerRadius&&!isMove) {
                        currentPoint = i;
                        isMove =true;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float tempX = event.getX();
                float tempY = event.getY();
                if(isTranslate){
                    translate[0] +=(tempX-locationX);
                    translate[1] +=(tempY-locationY);
                }
                resetPolyMatrix(4,(tempX-locationX),(tempY-locationY));
                locationX = event.getX();
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float shortX = locationX - delete[0];
                float shortY = locationY - delete[1];
                if(Math.abs(shortX) <= deleteR && Math.abs(shortY) <= deleteR){
                    isDelete = true;
                    mParent.delete();
                    mParent.invalidate();
                }
                break;
        }
    }

    public Canvas dispathDraw(Canvas canvas,boolean isDrawFram){
        if(isDelete){
            return null;
        }
            mCanvasWrapper.setCanvasSize(canvas.getWidth(), canvas.getHeight());
            mCanvasWrapper.drawBitmap(mBitmap, mPolyMatrix, pointPaint);
            mCanvasWrapper.draw(canvas);
            if(!isDrawFram){
                // 绘制触控点
                pointList.clear();
                for (int i = 0; i < dst.length; i += 2) {
                    canvas.drawBitmap(listPoint.get(i/2),pointXY[i]-pointXBan, pointXY[i + 1]-pointYBan, pointPaint);
                    pointList.add(new SpatialRelationUtil.Point(pointXY[i],pointXY[i + 1]));
                    switch (i){
                        case 0:
                            canvas.drawLine(pointXY[i]+pointXBan,pointXY[i + 1],pointXY[i + 2]-pointXBan,pointXY[i + 3],pointLine);
                            break;
                        case 2:
                            canvas.drawLine(pointXY[i],pointXY[i + 1]+pointYBan,pointXY[i + 2],pointXY[i + 3]-pointYBan,pointLine);
                            break;
                        case 4:
                            canvas.drawLine(pointXY[i]-pointXBan,pointXY[i + 1],pointXY[i + 2]+pointXBan,pointXY[i + 3],pointLine);
                            break;
                        case 6:
                            canvas.drawLine(pointXY[i],pointXY[i + 1]-pointYBan,pointXY[0],pointXY[1]+pointYBan,pointLine);
                            break;
                    }
                }
                canvas.drawBitmap(bitmaoDelete,delete[0], delete[1], pointPaint);
                canvas.drawPoint(center[0], center[1], pointPaint);
            }
        return mCanvasWrapper.getCanvas();
    }
    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }
}
