package cn.edu.fudan.se.methodhook.intenthandler;

import android.content.Intent;
import android.util.Log;
import cn.edu.fudan.se.methodhook.core.LogServiceIntentHandler;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;
import cn.edu.fudan.se.methodhook.core.MethodLogEntryProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dawnwords on 2016/1/20.
 */
public class SocketBasedIntentHandler extends LogServiceIntentHandler {
    public static final int PORT = 1949;

    private ServerSocket server;

    private volatile boolean started;

    public SocketBasedIntentHandler(MethodLogEntryProcessor processor) {
        super(processor);
    }

    @Override
    public void handleIntent(Intent intent) {
        while (started) {
            try {
                new ClientHandler(server.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        try {
            server = new ServerSocket(PORT);
            Log.e("LogService", "server started");
        } catch (IOException e) {
            Log.e("LogService", "exception occurs during server starting:" + e.getClass().getName() + ":" + e.getMessage());
        }
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    private class ClientHandler extends Thread {
        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                Object methodLogEntry;
                while ((methodLogEntry = in.readObject()) != null) {
                    if (methodLogEntry instanceof MethodLogEntry) {
                        processEntry((MethodLogEntry) methodLogEntry);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
