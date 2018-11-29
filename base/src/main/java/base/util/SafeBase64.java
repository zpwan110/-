package base.util;

/**
 * Created by HanTuo on 16/8/4.
 */
public class SafeBase64 {
    public static String decodeString(String str) {
        try {
            str = str.replace(' ', '-');
            str = str.replace('+', '-');
            str = str.replace('/', '_');
            str = str.replace("=", "");
            String result = new String(android.util.Base64.decode(str.getBytes(), android.util.Base64.URL_SAFE));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
