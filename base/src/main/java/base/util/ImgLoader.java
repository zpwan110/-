package base.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by HanTuo on 16/10/28.
 */

public class ImgLoader {
    Picasso picasso;
    RequestCreator requestCreator;


    public static ImgLoader with(Context context) {
        ImgLoader imgLoader = new ImgLoader();
        imgLoader.picasso = Picasso.with(context);
        return imgLoader;
    }

    public ImgLoader load(String url) {
        requestCreator = picasso.load(url);
        return this;
    }

    public ImgLoader load(File file) {
        requestCreator = picasso.load(file);
        return this;
    }

    public ImgLoader load(int resId) {
        requestCreator = picasso.load(resId);
        return this;
    }

    public void into(ImageView target) {
        requestCreator.into(target);
    }

}
