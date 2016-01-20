package cn.edu.fudan.se.methodhook.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.edu.fudan.se.methodhook.core.LogService;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent(context, LogService.class);
        context.startService(intent);
        Log.i("Method Hook", "auto started");
    }
}
