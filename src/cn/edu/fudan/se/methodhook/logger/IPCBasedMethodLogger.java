package cn.edu.fudan.se.methodhook.logger;

import android.content.Context;
import android.content.Intent;
import cn.edu.fudan.se.methodhook.core.LogService;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;
import cn.edu.fudan.se.methodhook.core.MethodLogger;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public class IPCBasedMethodLogger implements MethodLogger {
    private Context context;

    public IPCBasedMethodLogger(Context context) {
        this.context = context;
    }

    @Override
    public void logMethod(MethodLogEntry methodLogEntry) {
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, LogService.class);
        intent.putExtra("Method Hook", methodLogEntry);
        context.startService(intent);
    }
}
