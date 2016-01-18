package cn.edu.fudan.se.methodhook.logger;

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
            try {
                this.client = new Socket("127.0.0.1", LogService.PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                MethodLogEntry logEntry = logEntryQueue.poll();
                try {
                    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                    out.writeObject(logEntry);
                    out.flush();
                } catch (Exception e) {
                    retry(logEntry);
                }
            }
        }

        private void retry(MethodLogEntry logEntry) {
            try {
                sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logMethod(logEntry);
        }
    }
}
