package org.example.Client;

import org.example.Logger.Level;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static org.example.Main.logger;
import static org.example.Main.settings;

public class Client {
    private Socket clientSocket;
    private Scanner scanner;
    private BufferedWriter outMessage;
    private BufferedReader inMessage;
    private static BufferedReader reader;


    public Client(int port, String host) {
        identification();
        try {
            this.clientSocket = new Socket(host, port);
        } catch (IOException e) {
            logger.log("Client: " + "Сокет отсутсвует", Level.ERROR);
        }

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            inMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outMessage = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outMessage.write(settings.getUser() + ": подключился к чату" + "\n");
            outMessage.flush();
            new ReadMsg().start();
            WriteMsg();
            //new WriteMsg().start();
        } catch (IOException e) {
            logger.log("Client: " + e, Level.ERROR);
            Client.this.downService();
        }
    }


    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (clientSocket.isConnected()) {
                    message = inMessage.readLine();
                    if (message != null)
                        logger.log(message, Level.INFO);
                }
            } catch (IOException e) {
                logger.log("saveLog: " + e, Level.ERROR);
                Client.this.downService();
            }
        }
    }

    public void WriteMsg() {
        while (clientSocket.isConnected()) {
            String message;
            try {
                message = reader.readLine();
                if (message.equals("/exit")) {
                    logger.log("Пользователь написал выйти из программы", Level.DEBUG);
                    Client.this.downService();
                    break;
                } else {
                    outMessage.write(settings.getUser() + ": " + message + "\n");
                    outMessage.flush();
                }
            } catch (IOException e) {
                logger.log("saveLog: " + e, Level.ERROR);
                Client.this.downService();
            }
        }
    }

    public void identification() {
        scanner = new Scanner(System.in);
        System.out.print("Укажите свой ник ");
        settings.setUser(scanner.nextLine());
        System.out.println("Для выхода из чата напишите '/exit'");
    }

    private void downService() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                inMessage.close();
                outMessage.close();
                logger.stop();
            }
        } catch (IOException ignored) {
        }
    }
}
