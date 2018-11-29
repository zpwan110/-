package base.http;

/**
 * Created by HanTuo on 16/10/9.
 */

public interface Callback<T> {
    void onSuccess(T t);

    void onFail(ErrorModel httpError);
}
