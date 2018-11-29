package base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;

public class PicassoImageLoader {
    private static PicassoImageLoader instance;
    public static synchronized PicassoImageLoader getInstance() {
        if (instance == null) {
            instance = new PicassoImageLoader();
        }
        return instance;
    }

    public  void displayImage(Context context, Object path, ImageView imageView) {
        if(null==path||TextUtils.isEmpty(path.toString())){
                return;
        }
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        if(path instanceof Uri){
            Picasso.with(context).load((Uri) path).into(imageView);
        }else if(path instanceof File){
            Picasso.with(context).load((File)path).into(imageView);
        }else if(path instanceof String){
            Picasso.with(context).load((String)path).into(imageView);
        }else if(path instanceof Integer){
            Picasso.with(context).load((Integer) path).into(imageView);
        }else{
            try {
                throw new Exception();
            } catch (Exception e) {
                LogUtil.e("cannot find classes by path!");
            }
        }
    }
    public  void displayBitmapImage(Context context, String path,int width,int height, final ImageView imageView) {
        Picasso.with(context)
                .load(path)
                .resize(width,height)
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        imageView.setImageDrawable(errorDrawable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        imageView.setImageDrawable(placeHolderDrawable);
                    }
                });
    }
    public  void displayRoundImage(Context context, Object path, ImageView imageView) {
        if(path!=null&&TextUtils.isEmpty(path.toString())){
            return;
        }
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        if(path instanceof Uri){
            Picasso.with(context).load((Uri) path).transform(new RoundTransform(20)).into(imageView);
        }else if(path instanceof File){
            Picasso.with(context).load((File)path).transform(new RoundTransform(20)).into(imageView);
        }else if(path instanceof String){
            Picasso.with(context).load((String)path).transform(new RoundTransform(20)).into(imageView);
        }else if(path instanceof Integer){
            Picasso.with(context).load((Integer) path).transform(new RoundTransform(20)).into(imageView);
        }else{
            try {
                throw new Exception();
            } catch (Exception e) {
                LogUtil.e("cannot find classes by path!");
            }
        }
    }
    /**
     * 圆角显示图片-Picasso
     */
    class RoundTransform implements Transformation {
        private int radius;//圆角值

        public RoundTransform(int radius) {
            this.radius = radius;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int width = source.getWidth();
            int height = source.getHeight();
            //画板
            Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());
            Paint paint = new Paint();
            Canvas canvas = new Canvas(bitmap);//创建同尺寸的画布
            paint.setAntiAlias(true);//画笔抗锯齿
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            //画圆角背景
            RectF rectF = new RectF(new Rect(0, 0, width, height));//赋值
//            canvas.drawRoundRect(rectF, radius, radius, paint);//画圆角矩形
            paint.setFilterBitmap(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(makeRoundRectFrame(width,height), 0, 0, paint);
            source.recycle();//释放

            return bitmap;
        }
        // 四个角的x,y半径
        private float[] radiusArray = { 5f, 5f, 5f, 5f, 0f, 0f, 0f, 0f };
        private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Bitmap makeRoundRectFrame(int w, int h) {
            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            Path path = new Path();
            path.addRoundRect(new RectF(0, 0, w, h), radiusArray, Path.Direction.CW);
            Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bitmapPaint.setColor(Color.GREEN); // 颜色随意，不要有透明度。
            c.drawPath(path, bitmapPaint);
            return bm;
        }

        @Override
        public String key() {
            return "round : radius = " + radius;
        }
    }
}
