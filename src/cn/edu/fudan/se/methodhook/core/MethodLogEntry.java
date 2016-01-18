package cn.edu.fudan.se.methodhook.core;

import android.os.Process;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by Dawnwords on 2016/1/17.
 */
public class MethodLogEntry implements Serializable{
    private int pid;
    private String threadName;
    private Method method;
    private long time;
    private String processName;

    private MethodLogEntry(int pid, String threadName, Method method) {
        this.pid = pid;
        this.threadName = threadName;
        this.method = method;
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

    public Method method() {
        return method;
    }

    public long time() {
        return time;
    }

    @Override
    public String toString() {
        return "{" +
                "pid:" + pid +
                ", thread:'" + threadName + '\'' +
                ", method:" + method +
                ", processName:'" + processName + '\'' +
                '}';
    }
}
