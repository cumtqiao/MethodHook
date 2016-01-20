package cn.edu.fudan.se.methodhook.core;

import android.content.Intent;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public abstract class LogServiceIntentHandler {

    private MethodLogEntryProcessor processor;

    public LogServiceIntentHandler(MethodLogEntryProcessor processor) {
        this.processor = processor;
    }

    public void stop() {
    }

    public abstract void handleIntent(Intent intent);

    public void start() {
    }

    protected void processEntry(MethodLogEntry entry) {
        processor.processEntry(entry);
    }
}
