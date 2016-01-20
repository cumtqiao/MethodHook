package cn.edu.fudan.se.methodhook.entryprocessor;

import android.content.Context;
import android.util.Log;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;
import cn.edu.fudan.se.methodhook.core.MethodLogEntryProcessor;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public class PrintEntryProcessor extends MethodLogEntryProcessor {
    public PrintEntryProcessor(Context context) {
        super(context);
    }

    @Override
    protected void process(MethodLogEntry entry) {
        Log.e("Method Hook", "store log entry:" + entry.toString());
    }
}
