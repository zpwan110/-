package com.crowd.curtain.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import base.BaseActivity;
import com.crowd.curtain.ui.activity.ShareActivity;
import com.crowd.curtain.utils.SpatialRelationUtil;


/**
 * <请描述这个类是干什么的>
 *
 * @author guobin
 * @data: 2016/9/20 09:30
 * @Email: 702250823@qq.com
 * @version: V1.0
 */
public class PolyToPolyView extends View{
    private static final String TAG = "SetPolyToPoly";

    private Bitmap bgBitmap;
    private Rect bgSrcRect;
    private Rect bgDesRect;
    DoubleClick doubleClick;
    public int centerX;
    public int centerY;
    private List<CurtuaiPoint> bodyList = new ArrayList<>();
    private List<CurtuaiPoint> headList = new ArrayList<>();
    public CurtuaiPoint currentPoint;
    private boolean single =true;
    private int COUNT_MAX =8;
//    private List<String> curtaiUrl = new ArrayList<>();

    public void setFullScreem(boolean fullScreem) {
        isFullScreem = fullScreem;
    }

    public boolean isFullScreem =false;

    public void setBodyOrHead(boolean bodyUp) {
        isBodyOrHead = bodyUp;
    }

    private boolean isBodyOrHead = true;

    public DoubleClick getDoubleClick() {
        return doubleClick;
    }

    public void setDoubleClick(DoubleClick doubleClick) {
        this.doubleClick = doubleClick;
    }

    public PolyToPolyView(Context context) {
        this(context, null);
    }

    public PolyToPolyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolyToPolyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bgSrcRect = new Rect(0, 0, w, h);
        bgDesRect = new Rect(0, 0, w, h);
        this.centerX = w/2;
        this.centerY =h/2;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        if(currentPoint!=null){
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                SpatialRelationUtil.Point point =new SpatialRelationUtil.Point(event.getX(),event.getY());
                if(isBodyOrHead){
                    for (CurtuaiPoint curtain:bodyList) {
                        if(SpatialRelationUtil.isPolygonContainsPoint(curtain.pointList,point)){
                            currentPoint = curtain;
                            setFullScreem(false);
                            break;
                        }
                    }
                }else{
                    for (CurtuaiPoint curtain:headList) {
                        if(SpatialRelationUtil.isPolygonContainsPoint(curtain.pointList,point)){
                            currentPoint = curtain;
                            setFullScreem(false);
                            break;
                        }
                    }
                }
            }
            currentPoint.onTouch(event);
        }
        return true;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        for (CurtuaiPoint curtain:headList) {
            super.dispatchDraw(curtain.dispathDraw(canvas,true));
        }
        for (CurtuaiPoint curtain:bodyList) {
            super.dispatchDraw(curtain.dispathDraw(canvas,true));
        }
        if(currentPoint!=null){
            super.dispatchDraw(currentPoint.dispathDraw(canvas,isFullScreem));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bgBitmap!=null){
            canvas.drawBitmap(bgBitmap, null,bgDesRect, null);
        }
    }
    public void setBgBitmap(Uri uri){
        try {
            bgBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveViewImage() {
        setFullScreem(true);
        invalidate();
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir  = getContext().getExternalCacheDir();
        File tempFile = new File(storageDir, imageFileName);
        try {
            this.setDrawingCacheEnabled(true);
            this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            this.setDrawingCacheBackgroundColor(Color.WHITE);
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            c.drawColor(Color.WHITE);
            /** 如果不设置canvas画布为白色,则生成透明 */
//            this.layout(0, 0, w, h);
            this.draw(c);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            ((BaseActivity)getContext()).toActivity(ShareActivity.newIntent(tempFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        /**
         * 双击发生时的通知
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击事件
                if(getDoubleClick()!=null){
                    getDoubleClick().onDoubleClick();
                }
            return super.onDoubleTap(e);
        }
    });

    /**
     *
     * @param light 亮度度
     * @param contrast 对比度
     * @param gray 灰度
     */
    public void changeBitmap(float light, float contrast, float gray) {
        if(currentPoint==null||currentPoint.mBitmap==null){
            return;
        }
        Bitmap bmp = currentPoint.mCopyBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        ColorMatrix contrastMatrix = new ColorMatrix();
        ColorMatrix lightMatrix = new ColorMatrix();
        ColorMatrix grayMatrix=new ColorMatrix();
        if(contrast<0.3){
            contrast =0.3f;
        }else if(contrast>1.5){
            contrast =1.5f;
        }
        if(light<0.3){
            light =0.3f;
        }else if(light>1.5){
            light =1.5f;
        }
        contrastMatrix.set(new float[] {
                contrast, 0, 0, 0, 0,
                0, contrast, 0, 0, 0,
                0, 0, contrast, 0, 0,
                0, 0, 0, 1, 0
        });
        grayMatrix.setSaturation(gray);
        lightMatrix.setScale(light, light, light, 1);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.postConcat(lightMatrix);
        colorMatrix.postConcat(contrastMatrix);
        colorMatrix.postConcat(grayMatrix);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bmp, 0, 0, paint);
        currentPoint.setBitmap(bmp);
        invalidate();
    }
    public void addHead(CurtuaiPoint curtuaiPoint) {
        if(single){
            headList.clear();
            headList.add(curtuaiPoint);
        }else{
                headList.add(curtuaiPoint);
        }
        currentPoint = curtuaiPoint;

    }

    public void addBody(CurtuaiPoint curtuaiPoint) {
        if(single){
            bodyList.clear();
            bodyList.add(curtuaiPoint);
        }else{
                bodyList.add(curtuaiPoint);
        }
        currentPoint = curtuaiPoint;
    }

    public void setSingle(boolean single) {
        this.single = single;
        bodyList.clear();
        headList.clear();
        currentPoint =null;
        invalidate();
    }

    public void delete() {
        if(isBodyOrHead){
            for (int i = 0; i < bodyList.size(); i++) {
                if(bodyList.get(i).isDelete){
                    headList.remove(i);
                    break;
                }
            }
        }else{
            for (int i = 0; i < headList.size(); i++) {
                if(headList.get(i).isDelete){
                    headList.remove(i);
                    break;
                }
            }
        }
    }

    public void fullScreem() {
        if(isFullScreem){
            isFullScreem = false;
        }else{
            isFullScreem = true;
        }
    }

    public interface DoubleClick{
        void onDoubleClick();
    }
}