package org.example;

import org.example.Client.Client;
import org.example.Files.Settings;
import org.example.Logger.*;
import org.example.Server.Server;

public class Main {
    public static Logger logger;
    public static Settings settings=new Settings();

    private static String settingsFileName = "Settings1.json";

    public static void main(String[] args){
        logger = Logger.getInstance();
        logger.saveLog();
        logger.log( "Старт программы",Level.DEBUG);

        settings = Settings.getSettings(settingsFileName);

        if (settings.getRole().equals("Server")) {
            Server server = new Server(settings.getPort());
        } else if (settings.getRole().equals("Client")) {
            Client client = new Client(settings.getPort(), settings.getHost());
        } else {
            logger.log("main: "+"Параметр 'Role' в файле настроек указан неправильно.",Level.ERROR );
        }
    }
}















