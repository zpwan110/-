package base.util;
import android.os.Build;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by zhangpeng on 2017/9/20.
 */

public class FlymeUtils {
    /*作者：Mariotaku
    链接：https://www.zhihu.com/question/22102139/answer/24834510
    来源：知乎
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    */

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
