package cn.edu.fudan.se.methodhook.core;

import android.os.Process;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Dawnwords on 2016/1/17.
 */
public class MethodLogEntry implements Serializable {
    private int pid;
    private String threadName;
    private String method;
    private long time;
    private String processName;

    private MethodLogEntry(int pid, String threadName, Method method) {
        this.pid = pid;
        this.threadName = threadName;
        this.method = method.toString();
        this.time = System.currentTimeMillis();
    }

    public static MethodLogEntry create(Method method) {
        return new MethodLogEntry(Process.myPid(), Thread.currentThread().getName(), method);
    }

    public MethodLogEntry processName(String processName) {
        this.processName = processName;
        return this;
    }

    public int pid() {
        return pid;
    }

    public String threadName() {
        return threadName;
    }

    public String processName() {
        if (processName == null) {
            throw new IllegalStateException("ProcessName has not be set in a ContextWrapper");
        }
        return processName;
    }

    public String method() {
        return method;
    }

    public long time() {
        return time;
    }

    @Override
    public String toString() {
        return "{" +
                "pid:" + pid +
                ", threadName:'" + threadName + '\'' +
                ", method:'" + method + '\'' +
                ", time:" + new Date(time).toString() +
                ", processName:'" + processName + '\'' +
                '}';
    }
}
