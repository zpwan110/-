package base.util;

import android.view.Gravity;
import android.widget.Toast;

import base.BaseUtil;

/**
 * @author zhangpeng
 */
public class ToastUtil extends BaseUtil {


    public static void showToast(String str) {
        if (context == null) {
            throw new NullPointerException("context for BaseUtil  can not  be null");
        }
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    protected static void showToastResources(int id) {
        if (context == null) {
            throw new NullPointerException("context for BaseUtil  can not  be null");
        }
        showToast(context.getResources().getString(id));
    }

    public static void showToastFailPic(String message) {
        if (BaseUtil.context == null) {
            throw new NullPointerException("context for BaseUtil  can not  be null");
        }
        Toast mSuccessToast = null;
        if (mSuccessToast == null) {
            mSuccessToast = Toast.makeText(BaseUtil.context, message,
                    Toast.LENGTH_SHORT);
            mSuccessToast.setGravity(Gravity.CENTER, 0, -0);
        } else {
            mSuccessToast.setText(message);
            mSuccessToast.setDuration(Toast.LENGTH_SHORT);
        }
        mSuccessToast.show();
    }
	/*protected static void showToastFailPic(int message){
		if(context==null){
			throw new NullPointerException("context for BaseUtil  can not  be null");
		}
		Toast mSuccessToast=null;
		if(mSuccessToast==null){
			mSuccessToast = Toast.makeText(context,message,
					Toast.LENGTH_SHORT);
			mSuccessToast.setGravity(Gravity.CENTER, 0, -0);
		}else{
			mSuccessToast.setText(message);
			mSuccessToast.setDuration(Toast.LENGTH_SHORT);
		}
		mSuccessToast.show();
	}*/

    public static void showToastOkPic(String message) {
        if (BaseUtil.context == null) {
            throw new NullPointerException("context for BaseUtil  can not  be null");
        }
        Toast mFaileToast = null;
        if (mFaileToast == null) {
            mFaileToast = Toast.makeText(BaseUtil.context, message, Toast.LENGTH_SHORT);
            mFaileToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mFaileToast.setText(message);
            mFaileToast.setDuration(Toast.LENGTH_SHORT);
        }
        mFaileToast.show();
    }
}
