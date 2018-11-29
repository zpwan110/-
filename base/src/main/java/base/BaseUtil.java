package base;

import android.content.Context;

import java.io.File;

/**
 * Created by zhangpeng on 2018/2/2.
 */

public class BaseUtil {
    protected static Context context;
    protected static File DEFAULT_DIR;
    public static void initContext(Context mApplication) {
        BaseUtil.context = mApplication;
        DEFAULT_DIR = context.getExternalFilesDir("ObjectCache");

    }
}
