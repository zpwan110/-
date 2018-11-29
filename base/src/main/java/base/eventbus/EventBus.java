package base.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by HanTuo on 16/10/12.
 */

public class EventBus {
    private static HashMap<String, Method> actionMethodMap = new HashMap<>();
    private static HashMap<Integer, List<Object>> actionTargetsMap = new HashMap<>();
    private static LinkedList<Event> unhandledEventList = new LinkedList<>();

    public static void postEventToNewThread(final Event event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                postEvent(event);
            }
        }).start();
    }

    public static void postEventToMainThread(final Event event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                postEvent(event);
            }
        });
    }


    public static void postEvent(Event event) {
        try {
            int action = event.action;
            boolean isEventConsumed = false;
            for (Object o : actionTargetsMap.get(action)) {
                Method method = actionMethodMap.get(getMethodKey(action, o));
                if (null == method) {
                    continue;
                }
                method.setAccessible(true);
                isEventConsumed = true;
                try {
                    method.invoke(o, event);
                } catch (IllegalArgumentException e) {
                    method.invoke(o);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(!isEventConsumed) {
                unhandledEventList.add(event);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static String getMethodKey(int action, Object o) {
        return getMethodKey(action, o.getClass());
    }

    private static String getMethodKey(int action, Class clazz) {
        return action + clazz.getName();
    }

    public static void regitstOnBus(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(Action.class)) {
                    int[] actions = method.getAnnotation(Action.class).value();
                    for (int action : actions) {
                        actionMethodMap.put(getMethodKey(action, clazz), method);
                        List list = actionTargetsMap.get(action);
                        if (null == list) {
                            list = new LinkedList();
                            actionTargetsMap.put(action, list);
                        }
                        if (!list.contains(obj)) {
                            list.add(obj);
                        }
                        if(!unhandledEventList.isEmpty()){
                            for(Event event:unhandledEventList){
                                if(event.action==action){
                                    invokeObjMethod(obj, method, event);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private static void invokeObjMethod(Object o, Method m, Object... args){
        try {
            m.setAccessible(true);
            m.invoke(o, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void unregistEventListeners(int action) {
        List<Object> list = actionTargetsMap.get(action);
        for (Object o : list) {
            unregistOnBus(o);
        }
    }

    public static void unregistOnBus(Object obj) {
        for (List list : actionTargetsMap.values()) {
            if (list.contains(obj)) {
                list.remove(obj);
            }
        }
    }


    public static void clear() {
        actionTargetsMap.clear();
        actionTargetsMap.clear();
    }
}
