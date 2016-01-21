package cn.edu.fudan.se.methodhook.intenthandler;

import android.content.Intent;
import android.util.Log;
import cn.edu.fudan.se.methodhook.core.LogServiceIntentHandler;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;
import cn.edu.fudan.se.methodhook.core.MethodLogEntryProcessor;

import java.io.Serializable;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public class IPCBasedIntentHandler extends LogServiceIntentHandler {
    public IPCBasedIntentHandler(MethodLogEntryProcessor processor) {
        super(processor);
    }

    @Override
    public void handleIntent(Intent intent) {
        Serializable entry = intent.getSerializableExtra("Method Hook");
        if (entry == null || !(entry instanceof MethodLogEntry)) {
            Log.e("Method Hook", "Log Service Receive Error");
            return;
        }
        processEntry((MethodLogEntry) entry);
    }
}
