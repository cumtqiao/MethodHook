package cn.edu.fudan.se.methodhook.logger;

import android.util.Log;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;
import cn.edu.fudan.se.methodhook.core.MethodLogger;
import cn.edu.fudan.se.methodhook.intenthandler.SocketBasedIntentHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public class SocketBasedMethodLogger implements MethodLogger {

    private BlockingQueue<MethodLogEntry> logEntryQueue;

    public SocketBasedMethodLogger() {
        this.logEntryQueue = new LinkedBlockingQueue<>();

        new LoggerDaemon().start();
    }

    @Override
    public void logMethod(MethodLogEntry methodLogEntry) {
        try {
            logEntryQueue.put(methodLogEntry);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class LoggerDaemon extends Thread {
        private Socket client;

        public LoggerDaemon() {
            connect();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                MethodLogEntry logEntry = null;
                try {
                    logEntry = logEntryQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (logEntry == null) continue;
                try {
                    if (client == null) {
                        reconnect();
                    }
                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    out.writeObject(logEntry);
                    out.flush();
                } catch (Exception e) {
                    Log.e("Method Hook", "socket error:" + e.getClass().getSimpleName() + ":" + e.getMessage());
                }
            }
        }

        private void connect() {
            try {
                this.client = new Socket("127.0.0.1", SocketBasedIntentHandler.PORT);
                Log.e("Method Hook", "reconnect completed");
            } catch (IOException e) {
                if (e.getMessage().contains("EACCES")) {
                    Log.e("Method Hook", "No Internet Permission");
                } else {
                    e.printStackTrace();
                }
            }
        }

        private void reconnect() {
            try {
                Log.e("Method Hook", "reconnect after 5s");
                sleep(5 * 1000);
                logEntryQueue.clear();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connect();
        }
    }
}
