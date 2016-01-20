package cn.edu.fudan.se.methodhook.core;

import android.app.ActivityManager;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public abstract class MethodLogEntryProcessor {

    private HashMap<Integer, String> pidPNameMap;
    private ActivityManager activityManager;

    public MethodLogEntryProcessor(Context context) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        pidPNameMap = new HashMap<>();
    }

    public void processEntry(MethodLogEntry entry) {
        completeEntry(entry);
        process(entry);
    }

    private void completeEntry(MethodLogEntry entry) {
        int pid = entry.pid();
        String processName = pidPNameMap.get(pid);
        if (processName == null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    processName = processInfo.processName;
                    pidPNameMap.put(pid, processName);
                    break;
                }
            }
        }
        entry.processName(processName);
    }


    protected abstract void process(MethodLogEntry entry);
}
