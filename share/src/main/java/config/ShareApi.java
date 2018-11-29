package config;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import pay.tairan.com.share.R;

;

/**
 * Created by Jack on 2016/6/27.
 */
public class ShareApi {

    private static final double MAX_IMAGE_SIZE = 28.00;//KB
    private Oauth2AccessToken mAccessToken;


    public enum ShareType {
        WX_FRIEND(0),//微信好友
        WX_MOMENTS(1),//微信朋友圈
        QQ(3),//QQ
        WEIBO(2),//微博
        QZON(4);
        private int code;

        ShareType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static ShareType fromInt(int code) {
            switch (code) {
                case 0:
                    return WX_FRIEND;
                case 1:
                    return WX_MOMENTS;
                case 2:
                    return QQ;
                case 3:
                    return WEIBO;
                case 4:
                    return QZON;
                default:
                    return WX_MOMENTS;
            }
        }
    }


    /**
     * 分享
     *
     * @param activity
     * @param shareTitle 分享的标题
     * @param shareDesc  分享的描述
     * @param iconUrl    分享的图标
     * @param webUrl     分享的url
     * @param shareType
     */
    public static void share(Activity activity, String shareTitle, String shareDesc, String iconUrl, String webUrl, ShareType shareType) {
        if (shareType.equals(ShareType.QQ)) {
            shareToQQ(activity, shareTitle, shareDesc, iconUrl, webUrl, new IUiListener() {
                @Override
                public void onComplete(Object o) {

                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {

                }
            });
        } else if (shareType.equals(ShareType.QZON)) {
            shareToQZone(activity, shareTitle, shareDesc, iconUrl, webUrl, new IUiListener() {
                @Override
                public void onComplete(Object o) {

                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            downloadBitmap(activity, shareTitle, shareDesc, iconUrl, webUrl, shareType);
        }
    }
    public static void share(Activity activity, String shareTitle, String shareDesc, File iconFile, String webUrl, ShareType shareType) {
        if (shareType.equals(ShareType.QQ)) {
            shareToQQ(activity, shareTitle, shareDesc, iconFile, webUrl, new IUiListener() {
                @Override
                public void onComplete(Object o) {

                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {

                }
            });
        } else if (shareType.equals(ShareType.QZON)) {
            shareToQZone(activity, shareTitle, shareDesc, iconFile, webUrl, new IUiListener() {
                @Override
                public void onComplete(Object o) {

                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            downloadBitmap(activity, shareTitle, shareDesc, iconFile, webUrl, shareType);
        }
    }

    private static void downloadBitmap(final Activity activity, final String shareTitle, final String shareDesc, String iconUrl, final String webUrl, final ShareType shareType) {
        if (TextUtils.isEmpty(iconUrl)) {
            shareWithDefaultIcon(activity, shareTitle, shareDesc, webUrl, shareType);
        } else {
            Picasso.with(activity.getApplicationContext()).load(iconUrl)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap thumb, Picasso.LoadedFrom from) {
                            switch (shareType) {
                                case WX_FRIEND:
                                    shareToWeChat(activity, shareTitle, shareDesc, thumb, webUrl, true);
                                    break;
                                case WX_MOMENTS:
                                    shareToWeChat(activity, shareTitle, shareDesc, thumb, webUrl, false);
                                    break;
                                case WEIBO:
                                    shareToWeibo(activity, shareTitle, shareDesc, thumb, webUrl);
                                    break;
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            shareWithDefaultIcon(activity, shareTitle, shareDesc, webUrl, shareType);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }
    private static void downloadBitmap(final Activity activity, final String shareTitle, final String shareDesc, File iconUrl, final String webUrl, final ShareType shareType) {
        if (iconUrl==null) {
            shareWithDefaultIcon(activity, shareTitle, shareDesc, webUrl, shareType);
        } else {
            Picasso.with(activity.getApplicationContext()).load(iconUrl)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap thumb, Picasso.LoadedFrom from) {
                            switch (shareType) {
                                case WX_FRIEND:
                                    shareToWeChat(activity, shareTitle, shareDesc, thumb, webUrl, true);
                                    break;
                                case WX_MOMENTS:
                                    shareToWeChat(activity, shareTitle, shareDesc, thumb, webUrl, false);
                                    break;
                                case WEIBO:
                                    shareToWeibo(activity, shareTitle, shareDesc, thumb, webUrl);
                                    break;
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            shareWithDefaultIcon(activity, shareTitle, shareDesc, webUrl, shareType);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    private static void shareWithDefaultIcon(Activity activity, String shareTitle, String shareDesc, String webUrl, ShareType shareType) {
        Bitmap bitmap = compressBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.image_default_holder));
        switch (shareType) {
            case WX_FRIEND:
                shareToWeChat(activity, shareTitle, shareDesc, bitmap, webUrl, true);
                break;
            case WX_MOMENTS:
                shareToWeChat(activity, shareTitle, shareDesc, bitmap, webUrl, false);
                break;
            case WEIBO:
                shareToWeibo(activity, shareTitle, shareDesc, bitmap, webUrl);
                break;
        }
    }


    /**
     * 分享到微信
     *
     * @param activity
     * @param shareTitle 分享的标题
     * @param shareDesc  分享的描述
     * @param bitmap     分享的图标
     * @param webUrl     分享的url
     * @param toFriend   true 分享到微信好友 false 分享到朋友圈
     */
    private static void shareToWeChat(Activity activity, String shareTitle, String shareDesc, Bitmap bitmap, String webUrl, final boolean toFriend) {
        final IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity.getApplicationContext(), Config.WE_CHAT_APP_ID, true);
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(activity, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        iwxapi.registerApp(Config.WE_CHAT_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = shareDesc;
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        System.out.println("size of bitmap in memery : "+bitmap1.getRowBytes()*bitmap1.getHeight());
        msg.thumbData = bmpToByteArray(bitmap1, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf("webpage" + System.currentTimeMillis());
        req.message = msg;
        req.scene = toFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        boolean b = iwxapi.sendReq(req);
        LogUtil.e("share","result of weixin share "+b);
//        Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), iconRes);
//        msg.thumbData = bmpToByteArray(thumb, true);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = String.valueOf(System.currentTimeMillis());
//        req.message = msg;
//        req.scene = toFriend ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//        boolean b = iwxapi.sendReq(req);
//        Log.e("boolean", b + "");
    }


    /**
     * 分享到QQ好友
     *
     * @param activity
     * @param shareTitle
     * @param shareDesc
     * @param iconUrl
     * @param webUrl
     * @param listener
     */
    private static void shareToQQ(Activity activity, String shareTitle, String shareDesc, String iconUrl, String webUrl, IUiListener listener) {
        if (TextUtils.isEmpty(iconUrl)) {
            return;
        }
        Tencent tencent = Tencent.createInstance(Config.QQ_APP_ID, activity.getApplicationContext());
        if (!Util.isMobileQQSupportShare(activity.getApplicationContext())) {
            Toast.makeText(activity, "您还未安装QQ客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDesc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, iconUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "欧曼臣");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        tencent.shareToQQ(activity, params, listener);
    }
    private static void shareToQQ(Activity activity, String shareTitle, String shareDesc, File iconUrl, String webUrl, IUiListener listener) {
        if (iconUrl==null) {
            return;
        }
        Tencent tencent = Tencent.createInstance(Config.QQ_APP_ID, activity.getApplicationContext());
        if (!Util.isMobileQQSupportShare(activity.getApplicationContext())) {
            Toast.makeText(activity, "您还未安装QQ客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDesc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, iconUrl.getAbsolutePath());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "欧曼臣");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        tencent.shareToQQ(activity, params, listener);
    }

    /**
     * 分享到QQ好友
     *
     * @param activity
     * @param shareTitle
     * @param shareDesc
     * @param iconUrl
     * @param webUrl
     * @param listener
     */
    private static void shareToQZone(Activity activity, String shareTitle, String shareDesc, String iconUrl, String webUrl, IUiListener listener) {
        if (TextUtils.isEmpty(iconUrl)) {
            return;
        }
        Tencent tencent = Tencent.createInstance(Config.QQ_APP_ID, activity.getApplicationContext());
        if (!Util.isMobileQQSupportShare(activity.getApplicationContext())) {
            Toast.makeText(activity, "您还未安装QQ客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add(iconUrl);
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareDesc);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
        tencent.shareToQzone(activity, params, listener);
    }

    private static void shareToQZone(Activity activity, String shareTitle, String shareDesc, File iconUrl, String webUrl, IUiListener listener) {
        if (iconUrl==null) {
            return;
        }
        Tencent tencent = Tencent.createInstance(Config.QQ_APP_ID, activity.getApplicationContext());
        if (!Util.isMobileQQSupportShare(activity.getApplicationContext())) {
            Toast.makeText(activity, "您还未安装QQ客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> imgs = new ArrayList<>();
        imgs.add(iconUrl.getAbsolutePath());
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareDesc);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl);//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
        tencent.shareToQzone(activity, params, listener);
    }


    /**
     * 分享到微博
     *
     * @param activity
     * @param shareTitle
     * @param shareDesc
     * @param iconBitmap
     * @param webUrl
     */
    private static void shareToWeibo(final Activity activity, final String shareTitle, final String shareDesc, final Bitmap iconBitmap, final String webUrl) {
        IWeiboShareAPI iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity.getApplicationContext(), Config.WB_APP_ID);
        if (!iWeiboShareAPI.isWeiboAppInstalled()) {
            Toast.makeText(activity, "您还未安装微博客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean b = iWeiboShareAPI.registerApp();
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareTitle;
        mediaObject.description = shareDesc;

        Bitmap bitmap = Bitmap.createScaledBitmap(iconBitmap, 50, 50, true);
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = webUrl;
        mediaObject.defaultText = shareDesc;


        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = mediaObject;
        TextObject textObject = new TextObject();
        textObject.title = shareTitle;
        textObject.text = shareDesc;
        weiboMessage.textObject = textObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        boolean result = iWeiboShareAPI.sendRequest(activity, request);
    }

    /**
     * QQ分享回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param listener
     */
    public static void onQQShareActivityResultData(int requestCode, int resultCode, Intent data, IUiListener listener) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }


    public static Bitmap compressBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() * bitmap.getHeight() > 40000) {
            Matrix matrix = new Matrix();
            float scale = (float) Math.sqrt(40000d / (bitmap.getWidth() * bitmap.getHeight()));
            matrix.setScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            if (bitmap.getWidth() != bitmap.getHeight()) {
                int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
                bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - size) / 2, (bitmap.getHeight() - size) / 2, size, size);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        double size = byteArrayOutputStream.toByteArray().length / 1024;
        if (size > MAX_IMAGE_SIZE) {
            int quality = 100;
            while ((byteArrayOutputStream.toByteArray().length / 1024) > MAX_IMAGE_SIZE) {
                System.out.println("size ________  " + byteArrayOutputStream.toByteArray().length / 1024);
                byteArrayOutputStream.reset();
                quality = quality - 5;
                System.out.println("quality--------------------- " + quality);
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            }
            bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), null, null);
        }
        return bitmap;
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
