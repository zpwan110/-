package base.http.net;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by HanTuo on 2016/11/5.
 */

public class CrossDomainTokenCookie implements CookieJar {
    public static final String TOKEN_KEY = "token";
    Map<String, List<Cookie>> cookieMap = new HashMap<String, List<Cookie>>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieMap.put(url.host(), cookies);
//        if (UserConfig.isUnLogin()) {
//            if (ServerConfig.getAccountLoginUrl().equals(url.toString())) {
//                for (Cookie cookie : cookies) {
//                    if (TOKEN_KEY.equals(cookie.name())) {
//                        UserConfig.setToken(cookie.value());
//                        break;
//                    }
//                }
//            }
//        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> list = cookieMap.get(url.host());
        List<Cookie> cookieList = new ArrayList<>(1);
        if (null != list) {
            cookieList.addAll(list);
        }
//        if (UserConfig.isLogined()) {
//            Cookie tokenCookie = null;
//            for (Cookie cookie : cookieList) {
//                if (TOKEN_KEY.equals(cookie.name())) {
//                    tokenCookie = cookie;
//                    break;
//                }
//            }
//            if (tokenCookie == null || !tokenCookie.value().equals(UserConfig.getToken())) {
//                cookieList.remove(tokenCookie);
//                cookieList.add(generateCookie(url));
//            }
//        }
        return cookieList;
    }

    private Cookie generateCookie(HttpUrl url) {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name("token");
//        builder.value(UserConfig.getToken());
        builder.domain(url.host());
        return builder.build();
    }
}
