package cn.edu.fudan.se.methodhook.logger;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import cn.edu.fudan.se.methodhook.core.MethodLogEntry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dawnwords on 2016/1/18.
 */
public class LogService extends IntentService {
    static final int PORT = 1949;

    private ServerSocket server;
    private volatile boolean started;

    public LogService() {
        super("method log service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            server = new ServerSocket(PORT);
            Log.e("LogService", "server started");
        } catch (IOException e) {
            Log.e("LogService", "exception occurs during server starting:" + e.getClass().getName() + ":" + e.getMessage());
        }
        started = true;
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (started) {
            try {
                new ClientHandler(server.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                Object read;
                while ((read = in.readObject()) != null) {
                    if (read instanceof MethodLogEntry) {
                        MethodLogEntry entry = (MethodLogEntry) read;
                        //TODO store into SQLite
                        Log.e("Method Hook", entry.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
