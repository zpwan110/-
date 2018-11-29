package base.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import base.BaseUtil;

/**
 * app缓存类
 * @author HanTuo
 * @date 16/7/27
 */
public class ObjCacheUtil extends BaseUtil{

    public static void saveAsync(final Callback<Boolean> callback, @NonNull final File file, @NonNull final Object obj) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean success = save(file, obj);
                if (null != callback) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(success);
                        }
                    });
                }
            }
        }).start();
    }

    public static void saveAsync(final Callback<Boolean> callback, @NonNull final String name, @NonNull final Object obj) {
        saveAsync(callback, getFileFromObj(obj, name), obj);
    }

    public static void saveAsync(final Callback<Boolean> callback, @NonNull final Object obj) {
        saveAsync(callback, getFileFromObj(obj), obj);
    }

    public static void saveAsync(@NonNull File file, @NonNull Object obj) {
        saveAsync(null, file, obj);
    }

    public static void saveAsync(@NonNull String name, @NonNull Object obj) {
        saveAsync(null, name, obj);
    }

    public static void saveAsync(@NonNull Object obj) {
        saveAsync((Callback<Boolean>) null, obj);
    }

    public static boolean save(@NonNull File file, @NonNull Object obj) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            File tmpFile = new File(file.getPath() + ".tmp");
            FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
            String string = JSON.toJSONString(obj);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(string.getBytes());
            bufferedOutputStream.flush();
            fileOutputStream.close();
            return tmpFile.renameTo(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean save(@NonNull String name, @NonNull Object obj) {
        return save(getFileFromObj(obj, name), obj);
    }

    public static boolean save(@NonNull Object obj) {
        return save(getFileFromObj(obj), obj);
    }

    public static <T> void getAsync(final Callback<T> callback, @NonNull final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Type type = callback.getClass().getGenericInterfaces()[0];
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
                final T t = (T) get(file, type);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult(t);
                    }
                });
            }
        }).start();
    }

    public static <T> void getAsync(final Callback<T> callback, String name) {
        getAsync(callback, getFile(callback, name));
    }

    public static <T> void getAsync(final Callback<T> callback) {
        getAsync(callback, getFile(callback));
    }


    private static <T> File getFile(Callback<T> callback) {
        return getFile(callback, null);
    }

    private static <T> File getFile(Callback<T> callback, String name) {
        Type type = callback.getClass().getGenericInterfaces()[0];
        type = ((ParameterizedType) type).getActualTypeArguments()[0];
        String fileName = null;
        if (type instanceof Class) {
            fileName = ((Class) type).getName();
        } else {
            fileName = "Collection-" + ((Class)((ParameterizedType) type).getActualTypeArguments()[0]).getName();
        }
        if (name != null) {
            fileName += name;
        }
        return new File(DEFAULT_DIR, fileName);
    }

    public static File getFileFromObj(Object object) {
        return getFileFromObj(object, null);
    }

    public static File getFileFromObj(Object object, String name) {
        String fileName = null;
        if (object instanceof Iterable) {
            Iterable iterable = (Iterable) object;
            if(iterable.iterator().hasNext()){
                fileName = "Collection-" + iterable.iterator().next().getClass().getName();
            }else{
                fileName = "EmptyList";
            }
        } else if (object instanceof Class) {
            fileName = ((Class) object).getName();
        } else {
            fileName = object.getClass().getName();
        }
        if (null != name) {
            fileName += name;
        }
        return new File(DEFAULT_DIR, fileName);
    }

    public static
    @Nullable
    <T extends Object> T get(File file, Type clazz) {
        try {
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                byte[] bytes = new byte[bufferedInputStream.available()];
                bufferedInputStream.read(bytes);
                String string = new String(bytes);
                T t = JSON.parseObject(string, clazz);
                return t;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static
    @Nullable
    <T extends Object> T get(String name, Class<T> clazz) {
        return get(getFileFromObj(clazz, name), clazz);
    }

    public static
    @Nullable
    <T extends Object> T get(Class<T> clazz) {
        return get(getFileFromObj(clazz), clazz);
    }

    public interface Callback<T> {
        void onResult(T t);
    }


}
