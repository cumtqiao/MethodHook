package cn.edu.fudan.se.methodhook.core;

import com.saurik.substrate.MS;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public final class MethodHook<CLASS> {
    private final Class<CLASS> clazz;
    private final String methodName;
    private final List<Class> parameterTypes;
    private MethodLogger logger;

    public MethodHook(Class<CLASS> clazz, String methodName) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.parameterTypes = new ArrayList<>();
    }

    public MethodHook logger(MethodLogger logger) {
        this.logger = logger;
        return this;
    }

    public MethodHook addParameterType(Class... parameterType) {
        Collections.addAll(parameterTypes, parameterType);
        return this;
    }

    public void hook() {
        MS.hookClassLoad(clazz.getClass().getName(), new MS.ClassLoadHook() {
            @Override
            public void classLoaded(Class<?> aClass) {
                try {
                    Class<?>[] paraTypeArray = parameterTypes.isEmpty() ? null :
                            parameterTypes.toArray(new Class[parameterTypes.size()]);
                    final Method method = aClass.getMethod(methodName, paraTypeArray);
                    method.setAccessible(true);

                    MS.hookMethod(aClass, method, new MS.MethodAlteration<CLASS, Object>() {
                        @Override
                        public Object invoked(CLASS t, Object... args) throws Throwable {
                            if (logger != null) {
                                //TODO set processName in a ContextWrapper
                                MethodLogEntry logEntry = MethodLogEntry.create(method);
                                //TODO need to log arguments?
                                logger.logMethod(logEntry);
                            }
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
