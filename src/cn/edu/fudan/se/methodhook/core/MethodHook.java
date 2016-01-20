package cn.edu.fudan.se.methodhook.core;

import android.util.Log;
import cn.edu.fudan.se.methodhook.logger.SocketBasedMethodLogger;
import com.saurik.substrate.MS;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public final class MethodHook<CLASS> {
    private final String clazz;
    private final String methodName;
    private final List<Class> parameterTypes;

    public MethodHook(Class<CLASS> clazz, String methodName) {
        this.clazz = clazz.getName();
        this.methodName = methodName;
        this.parameterTypes = new ArrayList<>();
    }

    public MethodHook addParameterType(Class... parameterType) {
        Collections.addAll(parameterTypes, parameterType);
        return this;
    }

    public void hook() {
        MS.hookClassLoad(clazz, new MS.ClassLoadHook() {
            @Override
            public void classLoaded(Class<?> aClass) {
                try {
                    Class[] paraTypeArray = parameterTypes.isEmpty() ? null : parameterTypes.toArray(new Class[parameterTypes.size()]);
                    final Method method = aClass.getMethod(methodName, paraTypeArray);
                    method.setAccessible(true);

                    MS.hookMethod(aClass, method, new MS.MethodAlteration<CLASS, Object>() {
                        @Override
                        public Object invoked(CLASS t, Object... args) throws Throwable {
                            //TODO set processName in a ContextWrapper
                            MethodLogEntry logEntry = MethodLogEntry.create(method);
                            Log.i("Method Hook", "send log entry:" + logEntry);
                            //TODO need to log arguments?
                            SocketBasedMethodLogger.newInstance().logMethod(logEntry);
                            return invoke(t, args);
                        }
                    });
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
