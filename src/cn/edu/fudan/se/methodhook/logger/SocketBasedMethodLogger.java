package cn.edu.fudan.se.methodhook.logger;

import android.util.Log;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;
import cn.edu.fudan.se.methodhook.core.MethodLogger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public class SocketBasedMethodLogger implements MethodLogger {
    private static SocketBasedMethodLogger instance;
    private BlockingQueue<MethodLogEntry> logEntryQueue;

    private SocketBasedMethodLogger() {
        this.logEntryQueue = new LinkedBlockingQueue<>();

        new LoggerDaemon().start();
    }

    public static SocketBasedMethodLogger newInstance() {
        if (instance == null) {
            instance = new SocketBasedMethodLogger();
        }
        return instance;
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
                this.client = new Socket("127.0.0.1", LogService.PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void reconnect() {
            try {
                sleep(5 * 1000);
                logEntryQueue.clear();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connect();
        }
    }
}
