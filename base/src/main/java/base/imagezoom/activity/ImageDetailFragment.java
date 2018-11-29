package base.imagezoom.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import base.BaseFragment;
import base.util.PicassoImageLoader;
import jingshu.com.base.R;

public class ImageDetailFragment extends BaseFragment {
    private String mImageUrl;
    private ImageView mImageView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preview, container, false);
        mImageView = (ImageView) v.findViewById(R.id.pre_image);
        initView();
        return v;
    }

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }
    protected void initView() {
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        if(mImageUrl.contains("http://")||mImageUrl.contains("https://")){
            PicassoImageLoader.getInstance().displayImage(mContext, mImageUrl, mImageView);
        }else{
            File file = new File(mImageUrl);
            PicassoImageLoader.getInstance().displayImage(mContext, Uri.fromFile(file), mImageView);
        }
    }
}
