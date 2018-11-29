package base.eventbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by HanTuo on 16/10/12.
 */

public class Actions {
    public static final int QUERY_CITY_SELECTED = 1;//支付时候选了E卡
    public static final int ID_CARD_FRONT = 0;//身份证正面
    public static final int ID_CARD_BACK = 1;//身份证反面

    @IntDef({QUERY_CITY_SELECTED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionDef {
    }

    @IntDef({ID_CARD_FRONT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionFront {
    }
    @IntDef({ID_CARD_BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionBack {
    }

}
