package base.http;

import android.support.annotation.Nullable;

import java.lang.reflect.Type;

/**
 * Created by HanTuo on 16/10/24.
 */

public interface DataConverter {
    Object convert(String data, Type clazz);

    /**
     * @param data
     * @return
     */
    ErrorModel convertError(@ExceptionCode.HttpCodeDef int code, @Nullable String data);
}
