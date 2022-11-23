package org.example.Server;

import org.example.Logger.Level;

import java.io.*;
import java.net.ServerSocket;
import java.util.HashMap;

import static org.example.Main.logger;

public class Server {
    public static HashMap<ConnectedClient, String> clients = new HashMap<>();

    public Server(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.log("Сервер запущен", Level.DEBUG);
            while (true) {
                new ConnectedClient(serverSocket.accept());
            }
        } catch (IOException e) {
            System.out.println("кто-то отключился");
            logger.log(e.toString(), Level.ERROR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
