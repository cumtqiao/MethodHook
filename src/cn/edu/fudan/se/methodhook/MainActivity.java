package cn.edu.fudan.se.methodhook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import cn.edu.fudan.se.methodhook.logger.LogService;

import java.io.*;

public class MainActivity extends Activity {

    private static final String STRING_TO_WRITE = "Test File IO Hook";
    private String filePath;

    private static void close(Closeable out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Hook" + File.separator + "FileIOHook";
        startService(new Intent(this, LogService.class));
    }

    public void readFile(View v) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);

            int read, i = 0;
            byte[] data = new byte[1024];
            while ((read = in.read()) != -1) {
                data[i++] = (byte) read;
            }

            Toast.makeText(this, "Read:" + new String(data), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
    }

    public void writeFileInt(View v) {
        FileOutputStream out = null;
        try {
            out = fileOutputStream();
            byte[] data = STRING_TO_WRITE.getBytes();

            for (byte b : data) {
                out.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(out);
        }

    }

    public void writeFileBuffer(View v) {
        FileOutputStream out = null;
        try {
            out = fileOutputStream();
            byte[] data = STRING_TO_WRITE.getBytes();

            out.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
    }

    private FileOutputStream fileOutputStream() throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            File parent = file.getParentFile();
            if (!parent.exists() || !parent.isDirectory()) {
                parent.mkdirs();
            }
            file.createNewFile();
        }
        return new FileOutputStream(file);
    }
}
