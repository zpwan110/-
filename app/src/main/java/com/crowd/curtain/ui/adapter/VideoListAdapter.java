package com.crowd.curtain.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import base.util.DensityUtils;
import base.util.PicassoImageLoader;
import base.widget.recycler.BaseRecyclerAdapter;
import base.widget.recycler.BaseRecyclerViewHolder;
import base.widget.recycler.ViewHolderHelper;
import com.crowd.curtain.R;
import com.crowd.curtain.common.model.VideoEntity;
import com.crowd.curtain.ui.customview.RoundImageView;

/**
 * Created by zhangpeng on 2018/2/28.
 */

public class VideoListAdapter extends BaseRecyclerAdapter<VideoEntity>{
    private List<VideoEntity> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private String  statue;
    public VideoListAdapter(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setDatas(mItems);
    }
//    public List<VideoEntity> mItems = new ArrayList<VideoEntity>(
//            Arrays.asList(
//                    new VideoEntity(),
//                    new VideoEntity(),
//                    new VideoEntity(),
//                    new VideoEntity(),
//                    new VideoEntity(),
//                    new VideoEntity(),
//                    new VideoEntity()
//            ));

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder  viewHolder = new BaseRecyclerViewHolder(mInflater.inflate(R.layout.video_item, parent, false),
                mOnItemChildClickListener);
        View contentview = viewHolder.getViewHolderHelper().getConvertView();
        if(contentview.findViewById(R.id.video_thumb)!=null){
            viewHolder.getViewHolderHelper().setItemChildClickListener(R.id.video_thumb);
        }
        return viewHolder;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, VideoEntity model) {
        View convertView = viewHolderHelper.getConvertView();
        RoundImageView thumbImage = convertView.findViewById(R.id.video_thumb);
        thumbImage.setRadius(DensityUtils.dip2px(7), DensityUtils.dip2px(7),DensityUtils.dip2px(7),DensityUtils.dip2px(7));
        TextView videoName = convertView.findViewById(R.id.video_name);
        TextView videoDesc = convertView.findViewById(R.id.video_desc);
        TextView videoTime = convertView.findViewById(R.id.video_time);
        videoName.setText(model.videoName);
        videoDesc.setText(model.videoDesc);
        PicassoImageLoader.getInstance().displayImage(mContext,model.videoImage,thumbImage);
    }


    @Override
    public int getItemPos(VideoEntity s) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url,new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
