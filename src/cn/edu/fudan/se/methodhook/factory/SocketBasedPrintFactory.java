package cn.edu.fudan.se.methodhook.factory;

import cn.edu.fudan.se.methodhook.core.ComponentFactory;
import cn.edu.fudan.se.methodhook.core.LogServiceIntentHandler;
import cn.edu.fudan.se.methodhook.core.MethodLogEntryProcessor;
import cn.edu.fudan.se.methodhook.core.MethodLogger;
import cn.edu.fudan.se.methodhook.entryprocessor.PrintEntryProcessor;
import cn.edu.fudan.se.methodhook.intenthandler.SocketBasedIntentHandler;
import cn.edu.fudan.se.methodhook.logger.SocketBasedMethodLogger;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public class SocketBasedPrintFactory extends ComponentFactory {

    @Override
    public LogServiceIntentHandler createIntentHandler() {
        return new SocketBasedIntentHandler(createEntryProcessor());
    }

    @Override
    public MethodLogEntryProcessor createEntryProcessor() {
        return new PrintEntryProcessor(context);
    }

    @Override
    public MethodLogger createMethodLogger() {
        return new SocketBasedMethodLogger();
    }
}
