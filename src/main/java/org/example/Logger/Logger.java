package org.example.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.example.Main.logger;
import static org.example.Main.settings;

public class Logger {
    private StringBuilder outText = new StringBuilder();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Logger INSTANCE;
    private String fileName = "Logger.txt";

    private int writeTimout = 10000; //10 секунд

    private boolean start = true;

    private Logger() {
    }

    public void log(String message, Level level) {
        message = "[" + formatter.format(new Date()) + "] " + message + "\n";
        if (settings.getRole().equals("Client") && level == Level.INFO) {
            System.out.print(message);
        }
        message = level + ": " + message;
        if (!settings.getRole().equals("Client")) {
            System.out.print(message);
        }
        synchronized (outText) {
            outText.append(message);
        }
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger();
                }
            }
        }
        return INSTANCE;
    }

    public void saveLog() {
        new Thread(() -> {
            while (start) {
                try {
                    File file = new File(fileName);
                    BufferedWriter writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter(file, true));
                        writer.append(outText);
                        synchronized (outText) {
                            outText.setLength(0);
                        }
                    } finally {
                        if (writer != null) writer.close();
                    }
                    Thread.sleep(writeTimout);
                } catch (InterruptedException e) {
                    logger.log("saveLog: " + e, Level.ERROR);
                } catch (IOException e) {
                    logger.log("saveLog: " + e, Level.ERROR);
                }
            }
        }).start();
    }

    public void stop() {
        start = false;
    }
}
