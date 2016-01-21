package cn.edu.fudan.se.methodhook.core;

import android.app.Application;
import android.content.Context;
import cn.edu.fudan.se.methodhook.factory.IPCBasedPrintFactory;

import java.lang.reflect.Method;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public abstract class ComponentFactory {
    private static ComponentFactory instance;

    protected Context context;

    protected ComponentFactory() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            final Method method = activityThreadClass.getMethod("currentApplication");
            this.context = (Application) method.invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ComponentFactory getInstance() {
        if (instance == null) {
            instance = new IPCBasedPrintFactory();
        }
        return instance;
    }

    public abstract LogServiceIntentHandler createIntentHandler();

    public abstract MethodLogEntryProcessor createEntryProcessor();

    public abstract MethodLogger createMethodLogger();
}
