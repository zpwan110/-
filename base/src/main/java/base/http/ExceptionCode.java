package base.http;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by HanTuo on 16/10/27.
 */

public class ExceptionCode {
    public static final int CANCEL = 1;
    public static final int CONNECTION_OFF = 2;
    public static final int TIME_OUT = 3;
    public static final int UNKNOWN = 4;
    public static final int AUTHORIZE_FAIL = 5;


    @IntDef({CONNECTION_OFF, TIME_OUT, UNKNOWN, CANCEL, AUTHORIZE_FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HttpCodeDef {

    }
}
