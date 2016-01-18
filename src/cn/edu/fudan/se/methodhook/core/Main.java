package cn.edu.fudan.se.methodhook.core;

import cn.edu.fudan.se.methodhook.logger.SocketBasedMethodLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public class Main {
    static void initialize() {
        MethodLogger logger = SocketBasedMethodLogger.newInstance();
        List<MethodHook> methodHooks = Arrays.asList(
                new MethodHook<>(File.class, "createNewFile"),
                new MethodHook<>(FileInputStream.class, "read"),
                new MethodHook<>(FileInputStream.class, "read").addParameterType(byte[].class, int.class, int.class),
                new MethodHook<>(FileOutputStream.class, "write").addParameterType(byte[].class, int.class, int.class)
        );

        for (MethodHook methodHook : methodHooks) {
            methodHook.logger(logger);
        }

        for (MethodHook methodHook : methodHooks) {
            methodHook.hook();
        }
    }
}
