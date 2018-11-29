package base.eventbus;

/**
 * Created by HanTuo on 16/10/12.
 */

public class Event<T> {
    public int action;
    public boolean stick;//收到就取消监听、
    public T data;


    public Event(@Actions.ActionDef int eventAction) {
        action = eventAction;
    }

    public Event(@Actions.ActionDef int eventAction, T dataObj) {
        action = eventAction;
        data = dataObj;
    }
}
