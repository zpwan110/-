package base.http;

/**
 * Created by HanTuo on 16/10/24.
 */

public class ErrorModel {
    /**
     * 0 网络异常；1 连接超时；
     */
    public int code;
    public int serverCode;
    public String msg;
    public Object extra;

    public ErrorModel(int code, int serverCode, String msg, Object extra) {
        this.code = code;
        this.msg = msg;
        this.serverCode = serverCode;
        this.extra = extra;
    }

    public ErrorModel(int code, int serverCode, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
