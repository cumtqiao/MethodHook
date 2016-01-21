package cn.edu.fudan.se.methodhook.factory;

import cn.edu.fudan.se.methodhook.core.ComponentFactory;
import cn.edu.fudan.se.methodhook.core.LogServiceIntentHandler;
import cn.edu.fudan.se.methodhook.core.MethodLogEntryProcessor;
import cn.edu.fudan.se.methodhook.core.MethodLogger;
import cn.edu.fudan.se.methodhook.entryprocessor.PrintEntryProcessor;
import cn.edu.fudan.se.methodhook.intenthandler.IPCBasedIntentHandler;
import cn.edu.fudan.se.methodhook.logger.IPCBasedMethodLogger;

/**
 * Created by Dawnwords on 2016/1/21.
 */
public class IPCBasedPrintFactory extends ComponentFactory {
    @Override
    public LogServiceIntentHandler createIntentHandler() {
        return new IPCBasedIntentHandler(createEntryProcessor());
    }

    @Override
    public MethodLogEntryProcessor createEntryProcessor() {
        return new PrintEntryProcessor(context);
    }

    @Override
    public MethodLogger createMethodLogger() {
        return new IPCBasedMethodLogger(context);
    }
}
