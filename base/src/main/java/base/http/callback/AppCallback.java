package base.http.callback;

import base.http.Callback;
import base.http.ErrorModel;

/**
 * Created by zpwan110 on 2017/4/8.
 */

public abstract class AppCallback implements Callback {

    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onFail(ErrorModel httpError) {

    }
}
