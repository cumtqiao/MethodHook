package cn.edu.fudan.se.methodhook.core;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public class LogService extends IntentService {

    private LogServiceIntentHandler handler;

    public LogService() {
        super("method log service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = ComponentFactory.getInstance().createIntentHandler();
        handler.start();
    }

    @Override
    public void onDestroy() {
        handler.stop();
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handler.handleIntent(intent);
    }
}
