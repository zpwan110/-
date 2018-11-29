package config;

import android.content.Context;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.Util;
import com.tencent.tauth.Tencent;

/**
 * Created by HanTuo on 16/10/28.
 */

public class Config {
    public static final String WE_CHAT_APP_ID = "wx42e0d87382f22a8a";
    public static final String QQ_APP_ID = "1106744071";
    public static final String WB_APP_ID = "3763885184";
    public static final String REDIRECT = "http://open.weibo.com/apps/"+WB_APP_ID+"/privilege/oauth";
    public static final String WB_SECRET = "ab21db93039b1aa4b66fc78561e5f55c";
    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    public static boolean isWXInstalled(Context context) {
        final IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, Config.WE_CHAT_APP_ID, false);
        return iwxapi.isWXAppInstalled();
    }

    public static boolean isQQInstalled(Context context) {
        Tencent tencent = Tencent.createInstance(Config.QQ_APP_ID, context);
        return Util.isMobileQQSupportShare(context);
    }

}
